package szymonwolyniec.activrun;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalendar extends Fragment {

    @Bind(R.id.calendarMonth)
    TextView calendarMonth;
    @Bind(R.id.calendarAddEditBtn)
    Button calendarAddEditBtn;
    @Bind(R.id.calendarTitleTV)
    TextView calendarTitleTV;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    CompactCalendarView compactCalendar;
    String dateEpoch;
    View view;


    public FragmentCalendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));


        calendarMonth.setText(null);


        compactCalendar = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.calendarGiveAll();
        int colorEvent;
        long timeMilis;


        if (kurs.getCount() != 0) {

            kurs.moveToFirst();
            do {
                timeMilis = Long.valueOf(kurs.getString(0));
                colorEvent = kurs.getInt(3);
                Event event = new Event(colorEvent, timeMilis);
                compactCalendar.addEvent(event);

            } while (kurs.moveToNext());
        }


        calendarMonth.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {


            @Override
            public void onDayClick(Date dateClicked) {

                dateToEpoch(dateClicked.toString());

                boolean eventOnThisDay = dayInDB(dateEpoch);

                if (!eventOnThisDay)
                    calendarTitleTV.setText(getResources().getString(R.string.noEventText));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                calendarMonth.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        gotoToday();
        return view;
    }

    private boolean dayInDB(String dateEpoch) {

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.calendarGiveAll();
        boolean falseTrue;

        if (kurs.getCount() != 0) {
            kurs.moveToFirst();
            do {


                if (dateEpoch.equals(kurs.getString(0))) {

                    calendarTitleTV.setText(kurs.getString(1));

                    falseTrue = true;
                    break;
                } else falseTrue = false;

            } while (kurs.moveToNext());

            if (falseTrue == true) return true;
            else return false;

        } else {
            return false;
        }

    }


    private void dateToEpoch(String myDateClick) {
        String dayClick = myDateClick.substring(8, 10);
        String monthClick = myDateClick.substring(4, 7);
        String yearClick = myDateClick.substring(30, 34);

        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.UK);
        String myDate = dayClick + "/" + monthClick + "/" + yearClick;

        try {
            Date date = df.parse(myDate);
            long epoch = date.getTime();
            dateEpoch = String.valueOf(epoch);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public void gotoToday() {
        compactCalendar.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
        dateToEpoch(Calendar.getInstance(Locale.getDefault()).getTime().toString());

        boolean eventOnThisDay = dayInDB(dateEpoch);


        if (!eventOnThisDay) {
            calendarTitleTV.setText(getResources().getString(R.string.noEventText));
        }
    }


    @OnClick(R.id.calendarAddEditBtn)
    public void addEditEvent() {
        AppCompatActivity activity = (MainActivity) view.getContext();
        Fragment myFragment = new FragmentCalendarAddEdit();

        Bundle bundle = new Bundle();
        bundle.putString("dateEpoch", dateEpoch);
        myFragment.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
