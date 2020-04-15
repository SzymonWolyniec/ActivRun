package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUsersEdit extends Fragment {


    @Bind(R.id.usersEditLogin)
    EditText usersEditLogin;
    @Bind(R.id.usersEditName)
    EditText usersEditName;
    @Bind(R.id.usersEditSurname)
    EditText usersEditSurname;
    @Bind(R.id.usersEditDate)
    TextView usersEditDate;
    @Bind(R.id.userEditBirthdayImageBtn)
    ImageButton userEditBirthdayImageBtn;
    @Bind(R.id.usersEditHeight)
    EditText usersEditHeight;
    @Bind(R.id.usersEditWeight)
    EditText usersEditWeight;
    @Bind(R.id.usersEditEmail)
    EditText usersEditEmail;
    @Bind(R.id.usersEditSaveButton)
    Button usersEditSaveButton;
    @Bind(R.id.usersEditResetPasswordButton)
    Button usersEditResetPasswordButton;

    Calendar calendar;
    DatePickerDialog calendarPickerDialog;
    int positionInListUser, day, saveDay, month, saveMonth, year, saveYear, userID;
    String userLogin;
    @Bind(R.id.usersEditDeleteUserButton)
    Button usersEditDeleteUserButton;

    public FragmentUsersEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_edit, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        positionInListUser = this.getArguments().getInt("positionInListUser");
        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveUserByListPosition();

        if (kurs.getCount() != 0) {
            kurs.moveToPosition(positionInListUser);


            userID = kurs.getInt(0);
            userLogin = kurs.getString(10);
            usersEditLogin.setText(userLogin);

            if (userLogin.equals("Admin")) {
                usersEditLogin.setEnabled(false);
            }

            usersEditName.setText(kurs.getString(1));
            usersEditSurname.setText(kurs.getString(2));

            day = kurs.getInt(3);
            saveDay = day;

            month = kurs.getInt(4);
            saveMonth = month;
            month--;

            year = kurs.getInt(5);
            saveYear = year;

            usersEditDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
            usersEditHeight.setText(kurs.getString(7));
            usersEditWeight.setText(kurs.getString(8));
            usersEditEmail.setText(kurs.getString(9));


        }


        return view;
    }

    @OnClick(R.id.userEditBirthdayImageBtn)
    public void userEditSetBirthday() {
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


                usersEditDate.setText(String.format("%02d/%02d/%4d", saveDay, saveMonth, saveYear));

            }
        }, year, month, day);

        calendarPickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        calendarPickerDialog.show();
    }


    @OnClick(R.id.usersEditSaveButton)
    public void userEditSave() {

        DataBaseMain bd = new DataBaseMain(getActivity());

        String saveLogin = usersEditLogin.getText().toString();
        String saveName = usersEditName.getText().toString();
        String saveSurname = usersEditSurname.getText().toString();
        int saveHeight = Integer.valueOf(usersEditHeight.getText().toString());
        int saveWeight = Integer.valueOf(usersEditWeight.getText().toString());
        String saveEmail = usersEditEmail.getText().toString();

        bd.editUserDataByAdmin(userID, saveLogin, saveName, saveSurname, saveDay, saveMonth, saveYear, saveHeight, saveWeight, saveEmail);

        Toast.makeText(getActivity(), getResources().getString(R.string.dataSaved), Toast.LENGTH_SHORT).show();

        FragmentUsers fragmentUsers = new FragmentUsers();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerLayout, fragmentUsers);

        fragmentTransaction.commit();

        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.usersEditResetPasswordButton)
    public void userEditResetPassword() {

        AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(getActivity());

        alertDialogDeleteUser.setTitle(R.string.resetPassword);
        alertDialogDeleteUser.setMessage(R.string.resetPasswordMessage);
        alertDialogDeleteUser.setIcon(R.drawable.ic_logo);


        alertDialogDeleteUser.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String resetPasswordSet = "123";

                        Sha1Hash hashPassword = new Sha1Hash();
                        resetPasswordSet = hashPassword.hashString(resetPasswordSet);

                        DataBaseMain bd = new DataBaseMain(getActivity());
                        bd.resetPassword(userID, resetPasswordSet);

                        Toast.makeText(getActivity(), getResources().getString(R.string.passwordResetSuccessfully), Toast.LENGTH_LONG).show();

                        if(userLogin.equals("Admin"))
                        {
                            removeBackStack();
                            signOut();
                        }

                        dialog.cancel();
                    }
                });


        alertDialogDeleteUser.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        alertDialogDeleteUser.show();

    }

    @OnClick(R.id.usersEditDeleteUserButton)
    public void userEditDeleteUser() {

        AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(getActivity());

        alertDialogDeleteUser.setTitle(R.string.deleteUser);
        alertDialogDeleteUser.setMessage(R.string.deleteUserMessage);
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

                                FragmentUsers fragmentUsers = new FragmentUsers();

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.containerLayout, fragmentUsers);

                                fragmentTransaction.commit();
                                getFragmentManager().popBackStack();
                            }
                            else
                            {
                                dialog.cancel();
                                deleteAdminDialog();
                            }


                        dialog.cancel();

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



    public void deleteAdminDialog()
    {

        AlertDialog.Builder alertDialogDeleteAdmin = new AlertDialog.Builder(getActivity());

        alertDialogDeleteAdmin.setTitle(R.string.deleteUser);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
