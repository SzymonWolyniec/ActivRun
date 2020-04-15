package szymonwolyniec.activrun;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;
import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;


public class SignUpActivity extends AppCompatActivity {

    //region ButterKnife Bind
    @Bind(R.id.nameEditText)
    EditText nameET;
    @Bind(R.id.surnameEditText)
    EditText surnameET;
    @Bind(R.id.heightdEditText)
    EditText heightdET;
    @Bind(R.id.weightEditText)
    EditText weightET;
    @Bind(R.id.maleRadioButton)
    RadioButton maleRadioBtn;
    @Bind(R.id.femaleRadioButton)
    RadioButton femaleRadioBtn;
    @Bind(R.id.sexRadioGroup)
    RadioGroup sexRadioGrp;
    @Bind(R.id.emailEditText)
    EditText emailET;
    @Bind(R.id.email2EditText)
    EditText email2ET;
    @Bind(R.id.passwordEditTextREG)
    EditText passwordET;
    @Bind(R.id.password2EditText)
    EditText password2ET;
    @Bind(R.id.loginEditTextREG)
    EditText loginEditTextREG;
    @Bind(R.id.signUpButtonREG)
    Button signUpBtn;
    @Bind(R.id.birthdayTextView)
    TextView birthdayTV;
    @Bind(R.id.birthdayImageButton)
    ImageButton birthdayImageBtn;
    //endregion

    //region My Data Save
    String saveName, saveSurname, saveBirthday, saveSex, saveEmail, saveLogin, savePassword;
    int saveHeight, saveWeight, saveDay, saveMonth, saveYear;

    //endregion

    Calendar calendar;
    DatePickerDialog calendarPickerDialog;
    int day = 1, month = 1, year = 1980;


    private AwesomeValidation awasomeValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_actv_layout);
        ButterKnife.bind(this);
        Locale.setDefault(new Locale("en", "US"));
        getSupportActionBar().hide();


    }


    @OnClick(R.id.birthdayImageButton)
    public void takeBithday() {

        calendar = Calendar.getInstance();

        calendarPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                String birthdayString = birthdayTV.getText().toString();
                birthdayString = birthdayString.substring(0, birthdayString.length() - 10);
                birthdayTV.setText(birthdayString);

                birthdayTV.setText(birthdayTV.getText().toString() + String.format("%02d/%02d/%4d", mDay, mMonth + 1, mYear));

            }
        }, 1990, 0, 1);
        calendarPickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        calendarPickerDialog.show();


    }


    @OnClick(R.id.signUpButtonREG)
    public void onClick() {

        final AwesomeValidation awasomeValid = new AwesomeValidation(COLORATION);
        awasomeValid.setColor(Color.YELLOW);


        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,24}";


        awasomeValid.addValidation(this, R.id.nameEditText, "[a-zA-ZąęćżźńłóśĄĆĘŁŃÓŚŹŻ\\s]{1,25}", R.string.err_name);
        awasomeValid.addValidation(this, R.id.surnameEditText, "[a-zA-ZąęćżźńłóśĄĆĘŁŃÓŚŹŻ\\s]{1,25}", R.string.err_surname);
        awasomeValid.addValidation(this, R.id.heightdEditText, "[0-9]{2,3}", R.string.err_height);
        awasomeValid.addValidation(this, R.id.weightEditText, "[0-9]{2,3}", R.string.err_weight);
        awasomeValid.addValidation(this, R.id.emailEditText, Patterns.EMAIL_ADDRESS, R.string.err_email);
        awasomeValid.addValidation(this, R.id.email2EditText, R.id.emailEditText, R.string.err_email2);
        awasomeValid.addValidation(this, R.id.loginEditTextREG, "[a-zA-Z0-9]{3,25}", R.string.err_login);
        awasomeValid.addValidation(this, R.id.passwordEditTextREG, regexPassword, R.string.err_password);
        awasomeValid.addValidation(this, R.id.password2EditText, R.id.passwordEditTextREG, R.string.err_password2);


        if (awasomeValid.validate()) {

            DataBaseMain bd = new DataBaseMain(this);

            if (!bd.loginExist(loginEditTextREG.getText().toString()))
            {

                if(!bd.emailExist(emailET.getText().toString())) {



                    getDataToAddUser();

                    bd.addUser(saveName, saveSurname, saveDay, saveMonth, saveYear, saveSex, saveHeight, saveWeight, saveEmail, saveLogin, savePassword);

                    // Sprawdzenie czy istnieje tablica z treningami dla danego użytkownika (o danym loginie)
                    // Teoretycznie nie możliwe, bo użytkownik nie zarejestruje się jeśli taki login wystepuje już w BD


                    boolean existTable = bd.isTableExistForThisUser(bd.getReadableDatabase(), saveLogin);
                    bd.setUserLogin(saveLogin);



                    if(!existTable)// nie istnieje tablica dla tego użytkownika, zmienienie wersji BD, dodanie tablicy dla użytkownika w DataBaseMain.class
                    {
                        // Pobranie wersji BD

                        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.MY_PREFERENCES),Context.MODE_PRIVATE);
                        int dbVersion = sharedPref.getInt("dbVersion",1);


                        // Zwiększenie o 1 wersji BD
                        dbVersion++;
                        bd.setVersion(dbVersion);

                        // Zapis do SharedPreferences wersji BD
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("dbVersion", dbVersion);
                        editor.apply();

                        // Wywołanie metody .getWritableDatabase w celu odświeżenia bazdy danych (wywołana onUpgrade)
                        DataBaseMain DB = new DataBaseMain(this);
                        DB.getWritableDatabase();

                        Intent backToLogin = new Intent(this, LoginActivity.class);
                        startActivity(backToLogin);

                    }


                    Toast.makeText(this, getResources().getString(R.string.signUpSuccessfully), Toast.LENGTH_LONG).show();
                }

                else
                {
                    String emailExist = getResources().getString(R.string.emailExist);
                    Toast.makeText(this, emailExist, Toast.LENGTH_LONG).show();
                }
            }

            else
            {
                String loginExist = getResources().getString(R.string.loginExist);
                Toast.makeText(this, loginExist, Toast.LENGTH_LONG).show();
            }

        }

    }


    public void getDataToAddUser()
    {
        saveName = nameET.getText().toString();
        saveSurname = surnameET.getText().toString();

        saveBirthday = birthdayTV.getText().toString();
        saveYear = Integer.parseInt(saveBirthday.substring(saveBirthday.length() - 4));

        saveBirthday = saveBirthday.substring(0, saveBirthday.length() - 5);
        saveMonth = Integer.parseInt(saveBirthday.substring(saveBirthday.length() - 2));

        saveBirthday = saveBirthday.substring(0, saveBirthday.length() - 3);
        saveDay = Integer.parseInt(saveBirthday.substring(saveBirthday.length() - 2));


        if (maleRadioBtn.isChecked()) saveSex = "male";
        else saveSex = "female";

        saveHeight = Integer.parseInt(heightdET.getText().toString());
        saveWeight = Integer.parseInt(weightET.getText().toString());

        saveEmail = emailET.getText().toString();
        saveLogin = loginEditTextREG.getText().toString();

        Sha1Hash hashPassword = new Sha1Hash();
        savePassword = hashPassword.hashString(passwordET.getText().toString());
    }
}


