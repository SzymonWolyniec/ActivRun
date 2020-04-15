package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {


    @Bind(R.id.signInButton)
    Button signInBtn;
    @Bind(R.id.signUpButton)
    Button signUpBtn;
    @Bind(R.id.loginEditText)
    EditText loginET;
    @Bind(R.id.passwordEditText)
    EditText passwordET;
    @Bind(R.id.rememberPasswordCheckBox)
    CheckBox rememberPasswordCheckBox;

    int dbVersion;
    boolean rememberPasswordBoolean;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actv_layout);
        ButterKnife.bind(this);
        Locale.setDefault(new Locale("en", "US"));
        getSupportActionBar().hide();


        // Pobranie jaka była wersja BD przed zamknięciem APK

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.MY_PREFERENCES), Context.MODE_PRIVATE);
        dbVersion = sharedPref.getInt("dbVersion", 1);
        rememberPasswordBoolean = sharedPref.getBoolean("rememberPasswordBoolean", false);


        // Ustawienie tej wersji BD
        DataBaseMain bd = new DataBaseMain(this);
        bd.setVersion(dbVersion);


        if (dbVersion == 1) {
            if (!bd.adminExist()) {
                Intent setAdmin = new Intent(this, SetAdminPassword.class);
                startActivity(setAdmin);
            }
        }




        if (rememberPasswordBoolean == true) {
            String loginRemember = sharedPref.getString("rememberLogin", "");
            String passwordRemember = sharedPref.getString("rememberPassword", "");

            loginET.setText(loginRemember);
            passwordET.setText(passwordRemember);

            signInRememberTrue();
        }

    }

    public void signInRememberTrue()
    {
        DataBaseMain bd = new DataBaseMain(this);

        String login = loginET.getText().toString();
        String password = passwordET.getText().toString();

        if (bd.loginDataCorrect(login, password)) {
            bd.setUserLogin(login);

            passwordET.setText("");

            Intent toMain = new Intent(this, MainActivity.class);
            toMain.putExtra("itemToChecked", "nav_history");
            startActivity(toMain);
        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.incorrectLoginOrPassword), Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.signInButton)
    public void signIn() {

        DataBaseMain bd = new DataBaseMain(this);
        Sha1Hash hashPassword = new Sha1Hash();

        String login = loginET.getText().toString();
        String password = hashPassword.hashString(passwordET.getText().toString());

        if (bd.loginDataCorrect(login, password)) {
            bd.setUserLogin(login);

            if (rememberPasswordCheckBox.isChecked())
            {
                SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.MY_PREFERENCES), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("rememberPasswordBoolean", true);
                editor.putString("rememberLogin", login);
                editor.putString("rememberPassword", password);
                editor.apply();
            }

            passwordET.setText("");

            Intent toMain = new Intent(this, MainActivity.class);
            toMain.putExtra("itemToChecked", "nav_history");
            startActivity(toMain);
        } else {
            Toast.makeText(this, getResources().getString(R.string.incorrectLoginOrPassword), Toast.LENGTH_LONG).show();
        }

    }


    @OnClick(R.id.signUpButton)
    public void toSignUp() {

        Intent toSignUp = new Intent(this, SignUpActivity.class);
        startActivity(toSignUp);
    }


    public void refresh() {
        DataBaseMain bd = new DataBaseMain(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


        // refresh();
        // Pobranie jaka była wersja BD przed zamknięciem APK
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.MY_PREFERENCES), Context.MODE_PRIVATE);
        dbVersion = sharedPref.getInt("dbVersion", 1);

        // Ustawienie tej wersji BD
        DataBaseMain bd = new DataBaseMain(this);
        bd.setVersion(dbVersion);

        if (dbVersion == 1) {
            if (!bd.adminExist()) {
                Intent setAdmin = new Intent(this, SetAdminPassword.class);
                startActivity(setAdmin);
            }
        }

    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder alertDialogExit = new AlertDialog.Builder(LoginActivity.this);

        alertDialogExit.setTitle(R.string.exitTitle);
        alertDialogExit.setMessage(R.string.exitMessage);
        alertDialogExit.setIcon(R.drawable.ic_logo);


        alertDialogExit.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.finishAffinity(LoginActivity.this);

                        dialog.cancel();
                    }
                });


        alertDialogExit.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        alertDialogExit.show();

    }

}

