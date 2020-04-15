package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalendarAddEdit extends Fragment {


    @Bind(R.id.calendarShowDate)
    TextView calendarShowDate;
    @Bind(R.id.calendarAddEditNotesET)
    EditText calendarAddEditNotesET;
    @Bind(R.id.calendarRed)
    RadioButton calendarRed;
    @Bind(R.id.calendarYellow)
    RadioButton calendarYellow;
    @Bind(R.id.calendarGreen)
    RadioButton calendarGreen;
    @Bind(R.id.calendarBlue)
    RadioButton calendarBlue;
    @Bind(R.id.calendarMagenta)
    RadioButton calendarMagenta;
    @Bind(R.id.calendarAddEditBackBtn)
    Button calendarAddEditBackBtn;
    @Bind(R.id.calendarAddEditSaveBtn)
    Button calendarAddEditSaveBtn;
    @Bind(R.id.calendarAddEditTitleET)
    EditText calendarAddEditTitleET;

    View view;
    String dateEpoch;
    boolean isInDatabase;
    @Bind(R.id.calendarAddEditRemoveBtn)
    Button calendarAddEditRemoveBtn;


    public FragmentCalendarAddEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar_add_edit, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        dateEpoch = this.getArguments().getString("dateEpoch");
        getDateFromEpoch();


        isInDatabase = checkIfIsInDataBase(dateEpoch);

        if (!isInDatabase) {
            calendarRed.setChecked(true);
            calendarAddEditRemoveBtn.setVisibility(View.GONE);
        }

        return view;
    }

    private boolean checkIfIsInDataBase(String dateEpoch) {
        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.calendarGiveAll();
        boolean falseTrue;

        if (kurs.getCount() != 0) {
            kurs.moveToFirst();
            do {
                if (dateEpoch.equals(kurs.getString(0))) {
                    falseTrue = true;
                    calendarAddEditTitleET.setText(kurs.getString(1));
                    calendarAddEditNotesET.setText(kurs.getString(2));
                    int color = kurs.getInt(3);

                    if (color == -65536) calendarRed.setChecked(true);
                    else if (color == -2304) calendarYellow.setChecked(true);
                    else if (color == -16449792) calendarGreen.setChecked(true);
                    else if (color == -16772609) calendarBlue.setChecked(true);
                    else calendarMagenta.setChecked(true);

                    calendarAddEditSaveBtn.setText(getResources().getString(R.string.save));

                    break;
                } else falseTrue = false;

            } while (kurs.moveToNext());

            if (falseTrue == true) return true;
            else return false;

        } else {
            return false;
        }

    }

    public void getDateFromEpoch() {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String myDate = myFormat.format(new Date(Long.valueOf(dateEpoch)));

        calendarShowDate.setText(myDate);
    }

    @OnClick(R.id.calendarAddEditRemoveBtn)
    public void removeEvent() {


        AlertDialog.Builder alertDialogDeleteWorkout = new AlertDialog.Builder(getActivity());

        alertDialogDeleteWorkout.setTitle(R.string.removeNote);
        alertDialogDeleteWorkout.setMessage(R.string.removeNoteMessage);
        alertDialogDeleteWorkout.setIcon(R.drawable.ic_logo);


        alertDialogDeleteWorkout.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        DataBaseMain bd = new DataBaseMain(getActivity());
                        bd.removeEvent(dateEpoch);

                        AppCompatActivity activity = (MainActivity) view.getContext();
                        Fragment myFragment = new FragmentCalendar();

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).commit();

                        dialog.cancel();

                    }
                });


        alertDialogDeleteWorkout.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        alertDialogDeleteWorkout.show();

    }


    @OnClick(R.id.calendarAddEditBackBtn)
    public void addEditBack() {

        AppCompatActivity activity = (MainActivity) view.getContext();
        Fragment myFragment = new FragmentCalendar();

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).commit();
    }

    @OnClick(R.id.calendarAddEditSaveBtn)
    public void addEditSave() {


        if ((TextUtils.isEmpty(calendarAddEditTitleET.getText())))
            Toast.makeText(getActivity(), getResources().getString(R.string.emptyTitleNotes), Toast.LENGTH_SHORT).show();
        else if ((TextUtils.isEmpty(calendarAddEditNotesET.getText())))
            Toast.makeText(getActivity(), getResources().getString(R.string.emptyTextNotes), Toast.LENGTH_SHORT).show();
        else {

            if (isInDatabase) {
                DataBaseMain bd = new DataBaseMain(getActivity());
                String title = calendarAddEditTitleET.getText().toString();
                String notes = calendarAddEditNotesET.getText().toString();
                int color;

                if (calendarRed.isChecked())
                    color = Color.parseColor("#FFFF0000");
                else if (calendarYellow.isChecked())
                    color = Color.parseColor("#FFFFF700");
                else if (calendarGreen.isChecked())
                    color = Color.parseColor("#FF04FF00");
                else if (calendarBlue.isChecked())
                    color = Color.parseColor("#FF0011FF");
                else
                    color = Color.parseColor("#FFFF00EA");

                bd.editEvent(dateEpoch, title, notes, color);
            } else {
                DataBaseMain bd = new DataBaseMain(getActivity());
                String title = calendarAddEditTitleET.getText().toString();
                String notes = calendarAddEditNotesET.getText().toString();
                int color;

                if (calendarRed.isChecked())
                    color = Color.parseColor("#FFFF0000");
                else if (calendarYellow.isChecked())
                    color = Color.parseColor("#FFFFF700");
                else if (calendarGreen.isChecked())
                    color = Color.parseColor("#FF04FF00");
                else if (calendarBlue.isChecked())
                    color = Color.parseColor("#FF0011FF");
                else
                    color = Color.parseColor("#FFFF00EA");


                bd.addToCalendar(dateEpoch, title, notes, color);
            }

            AppCompatActivity activity = (MainActivity) view.getContext();
            Fragment myFragment = new FragmentCalendar();

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).commit();

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void allRadioButtonFalse()
    {
        calendarRed.setChecked(false);
        calendarYellow.setChecked(false);
        calendarGreen.setChecked(false);
        calendarBlue.setChecked(false);
        calendarMagenta.setChecked(false);
    }

    @OnClick({R.id.calendarRed, R.id.calendarYellow, R.id.calendarGreen, R.id.calendarBlue, R.id.calendarMagenta})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.calendarRed:
                allRadioButtonFalse();
                calendarRed.setChecked(true);
                break;
            case R.id.calendarYellow:
                allRadioButtonFalse();
                calendarYellow.setChecked(true);
                break;
            case R.id.calendarGreen:
                allRadioButtonFalse();
                calendarGreen.setChecked(true);
                break;
            case R.id.calendarBlue:
                allRadioButtonFalse();
                calendarBlue.setChecked(true);
                break;
            case R.id.calendarMagenta:
                allRadioButtonFalse();
                calendarMagenta.setChecked(true);
                break;

        }
    }
}
