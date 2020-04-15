package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment {


    @Bind(R.id.accountName)
    EditText accountName;
    @Bind(R.id.accountSurname)
    EditText accountSurname;
    @Bind(R.id.accountHeight)
    EditText accountHeight;
    @Bind(R.id.accountWeight)
    EditText accountWeight;
    @Bind(R.id.accountEmail)
    EditText accountEmail;
    @Bind(R.id.accountCurrentPassword)
    EditText accountCurrentPassword;
    @Bind(R.id.accountNewPassword)
    EditText accountNewPassword;
    @Bind(R.id.accountConfirmPassword)
    EditText accountConfirmPassword;
    @Bind(R.id.accountSaveButton)
    Button accountSaveButton;
    @Bind(R.id.accountDate)
    TextView accountDate;
    @Bind(R.id.accountBirthdayImageBtn)
    ImageButton accountBirthdayImageBtn;
    @Bind(R.id.accountEmail2)
    EditText accountEmail2;
    @Bind(R.id.accountCheckBox)
    CheckBox accountCheckBox;
    @Bind(R.id.newPasswordTV)
    TextView newPasswordTV;
    @Bind(R.id.confrimNewPasswordTV)
    TextView confrimNewPasswordTV;
    @Bind(R.id.accountDeleteAccountButton)
    Button accountDeleteUserButton;

    Calendar calendar;
    DatePickerDialog calendarPickerDialog;
    int day, month, year;
    int saveDay, saveMonth, saveYear;
    int userID;
    String userLogin;


    private AwesomeValidation awasomeValid;

    public FragmentAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveUser();

        if (kurs.getCount() != 0) {

            kurs.moveToFirst();

            accountName.setText(kurs.getString(1));
            accountSurname.setText(kurs.getString(2));

            day = Integer.valueOf(kurs.getString(3));
            saveDay = day;

            month = Integer.valueOf(kurs.getString(4));
            saveMonth = month;
            month--;


            year = Integer.valueOf(kurs.getString(5));
            saveYear = year;

            accountDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
            accountHeight.setText(kurs.getString(7));
            accountWeight.setText(kurs.getString(8));
            accountEmail.setText(kurs.getString(9));


            userID = kurs.getInt(0);
            userLogin = kurs.getString(10);
        }


        return view;
    }


    @OnClick(R.id.accountSaveButton)
    public void accountSave() {

        final AwesomeValidation awasomeValid = new AwesomeValidation(COLORATION);
        awasomeValid.setColor(Color.YELLOW);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,24}";


        awasomeValid.addValidation(getActivity(), R.id.accountName, "[a-zA-ZąęćżźńłóśĄĆĘŁŃÓŚŹŻ\\s]{1,25}", R.string.err_name);
        awasomeValid.addValidation(getActivity(), R.id.accountSurname, "[a-zA-ZąęćżźńłóśĄĆĘŁŃÓŚŹŻ\\s]{1,25}", R.string.err_surname);
        awasomeValid.addValidation(getActivity(), R.id.accountHeight, "[0-9]{2,3}", R.string.err_height);
        awasomeValid.addValidation(getActivity(), R.id.accountWeight, "[0-9]{2,3}", R.string.err_weight);
        awasomeValid.addValidation(getActivity(), R.id.accountEmail, Patterns.EMAIL_ADDRESS, R.string.err_email);
        awasomeValid.addValidation(getActivity(), R.id.accountEmail2, R.id.accountEmail, R.string.err_email2);

        if (accountCheckBox.isChecked()) {
            awasomeValid.addValidation(getActivity(), R.id.accountNewPassword, regexPassword, R.string.err_password);
            awasomeValid.addValidation(getActivity(), R.id.accountConfirmPassword, R.id.accountNewPassword, R.string.err_password2);
        }


        if (awasomeValid.validate()) {
            DataBaseMain bd = new DataBaseMain(getActivity());
            Sha1Hash hashPassword = new Sha1Hash();

            String password = hashPassword.hashString(accountCurrentPassword.getText().toString());

            if (bd.passwordCorrect(password)) {
                String name = accountName.getText().toString();
                String surname = accountSurname.getText().toString();
                String height = accountHeight.getText().toString();
                String weight = accountWeight.getText().toString();
                String mail = accountEmail.getText().toString();

                if (accountCheckBox.isChecked()) {
                    String newPassword = hashPassword.hashString(accountNewPassword.getText().toString());
                    bd.editUserData(name, surname, saveDay, saveMonth, saveYear, Integer.valueOf(height), Integer.valueOf(weight), mail, newPassword);
                    Toast.makeText(getActivity(), getResources().getString(R.string.dataSaved), Toast.LENGTH_SHORT).show();
                } else {
                    bd.editUserData(name, surname, saveDay, saveMonth, saveYear, Integer.valueOf(height), Integer.valueOf(weight), mail, password);
                    Toast.makeText(getActivity(), getResources().getString(R.string.dataSaved), Toast.LENGTH_SHORT).show();
                }

                FragmentAccount fragmentAccount = new FragmentAccount();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerLayout, fragmentAccount);

                fragmentTransaction.commit();
            } else
                Toast.makeText(getActivity(), getResources().getString(R.string.incorrectPassword), Toast.LENGTH_LONG).show();


        }


    }

    @OnClick(R.id.accountBirthdayImageBtn)
    public void setBirthday() {
        calendar = Calendar.getInstance();

        calendarPickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                saveDay = mDay;
                day = mDay;

                saveMonth = mMonth + 1;
                month = mMonth;

                saveYear = mYear;
                year = mYear;


                accountDate.setText(String.format("%02d/%02d/%4d", saveDay, saveMonth, saveYear));

            }
        }, year, month, day);

        calendarPickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        calendarPickerDialog.show();
    }


    @OnClick(R.id.accountCheckBox)
    public void changePassword() {

        if (accountCheckBox.isChecked()) {
            newPasswordTV.setVisibility(View.VISIBLE);
            confrimNewPasswordTV.setVisibility(View.VISIBLE);
            accountNewPassword.setVisibility(View.VISIBLE);
            accountConfirmPassword.setVisibility(View.VISIBLE);
        } else {
            newPasswordTV.setVisibility(View.GONE);
            confrimNewPasswordTV.setVisibility(View.GONE);
            accountNewPassword.setVisibility(View.GONE);
            accountConfirmPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.accountDeleteAccountButton)
    public void accountDeleteUser()
    {

        DataBaseMain bd = new DataBaseMain(getActivity());
        Sha1Hash hashPassword = new Sha1Hash();
        String password = hashPassword.hashString(accountCurrentPassword.getText().toString());

        if (bd.passwordCorrect(password)) {

        AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(getActivity());

        alertDialogDeleteUser.setTitle(R.string.deleteAccount);
        alertDialogDeleteUser.setMessage(R.string.deleteAccountMessage);
        alertDialogDeleteUser.setIcon(R.drawable.ic_logo);


        alertDialogDeleteUser.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {


                        DataBaseMain bd = new DataBaseMain(getActivity());


                        if(!userLogin.equals("Admin"))
                        {
                            bd.deleteUser(userID, userLogin);

                            dialog.cancel();
                            removeBackStack();
                            signOut();
                        }
                        else
                        {
                            dialog.cancel();
                            deleteAdminDialog();
                        }

                    }
                });


        alertDialogDeleteUser.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });


        alertDialogDeleteUser.show();
        }

        else
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.incorrectPassword), Toast.LENGTH_LONG).show();
        }
    }



    public void deleteAdminDialog()
    {

        AlertDialog.Builder alertDialogDeleteAdmin = new AlertDialog.Builder(getActivity());

        alertDialogDeleteAdmin.setTitle(R.string.deleteAccount);
        alertDialogDeleteAdmin.setMessage(R.string.removeAdminMessage);
        alertDialogDeleteAdmin.setIcon(R.drawable.ic_logo);


        alertDialogDeleteAdmin.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });



        alertDialogDeleteAdmin.show();
    }

    public void removeBackStack()
    {
        int count = getFragmentManager().getBackStackEntryCount();

        for (int i = 0; i < count ; i++) {
            getFragmentManager().popBackStack();
        }
    }

    private void signOut()
    {

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.MY_PREFERENCES), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("rememberPasswordBoolean", false);
        editor.putString("rememberLogin", "");
        editor.putString("rememberPassword", "");
        editor.apply();
        getActivity().finish();


    }
}


