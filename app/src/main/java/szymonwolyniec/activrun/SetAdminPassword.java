package szymonwolyniec.activrun;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetAdminPassword extends AppCompatActivity {

    @Bind(R.id.adminLogin)
    EditText adminLogin;
    @Bind(R.id.adminPassword)
    EditText adminPassword;
    @Bind(R.id.adminPassword2)
    EditText adminPassword2;
    @Bind(R.id.adminBtn)
    Button adminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_admin_password);
        ButterKnife.bind(this);
        Locale.setDefault(new Locale("en", "US"));


    }

    @OnClick(R.id.adminBtn)
    public void adminSetPassword()
    {
        if (adminPassword.getText().toString().equals(adminPassword2.getText().toString()))
        {
            DataBaseMain bd = new DataBaseMain(this);


            boolean existTable = bd.isTableExistForThisUser(bd.getReadableDatabase(), "Admin");
            bd.setUserLogin("Admin");

            String adminPasswordStr = adminPassword.getText().toString();

            if (adminPasswordStr.length() >= 3)
            {

                if (!existTable) {
                    Sha1Hash hashPassword = new Sha1Hash();


                    adminPasswordStr = hashPassword.hashString(adminPasswordStr);


                    bd.addAdmin(adminPasswordStr);


                    // Pobranie wersji BD
                    SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.MY_PREFERENCES), Context.MODE_PRIVATE);
                    int dbVersion = sharedPref.getInt("dbVersion", 1);


                    // Zwiększenie o 1 wersji BD
                    dbVersion++;
                    bd.setVersion(dbVersion);

                    // Zapis do SharedPreferences wersji BD
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("dbVersion", dbVersion);
                    editor.commit();

                    // Wywołanie metody .getWritableDatabase w celu odświeżenia bazdy danych (wywołana onUpgrade)
                    DataBaseMain DB = new DataBaseMain(this);
                    DB.getWritableDatabase();
                }


                Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
            }
            else Toast.makeText(this, getResources().getString(R.string.passwordMin),Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, getResources().getString(R.string.err_password2),Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder alertDialogExit = new AlertDialog.Builder(SetAdminPassword.this);

        alertDialogExit.setTitle(R.string.backPressedAdminTitle);
        alertDialogExit.setMessage(R.string.backPressedAdminMessage);
        alertDialogExit.setIcon(R.drawable.ic_logo);


        alertDialogExit.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {


                        dialog.cancel();
                    }
                });

        alertDialogExit.show();

    }
}
