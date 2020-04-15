package szymonwolyniec.activrun;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String lastItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        lastItem = intent.getStringExtra("itemToChecked");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem nav_nameSurnameShow = menu.findItem(R.id.nav_nameSurnameShow);

        DataBaseMain bd = new DataBaseMain(this);
        Cursor cursor = bd.giveUser();
        cursor.moveToFirst();
        String nameAndSurname = cursor.getString(1) + " " + cursor.getString(2);
        String userLogin = cursor.getString(10);

        nav_nameSurnameShow.setTitle(nameAndSurname);


        if(!userLogin.equals("Admin"))
        {
            MenuItem nav_users = menu.findItem(R.id.nav_users);
            nav_users.setVisible(false);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        lastItem = intent.getStringExtra("itemToChecked");
    }

    @Override
    protected void onResume() {
        super.onResume();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


            if (lastItem.equals("nav_history") || lastItem.equals("nav_historyFromWorkout"))
            {
                // Zaznaczenie plus wywołanie akcji z tego pola
                navigationView.setCheckedItem(R.id.nav_history);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_history));
            }
            else if (lastItem.equals("nav_calendar"))
            {
                navigationView.setCheckedItem(R.id.nav_calendar);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_calendar));
            }
            else if (lastItem.equals("nav_statistics"))
            {
                navigationView.setCheckedItem(R.id.nav_statistics);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_statistics));
            }
            else if (lastItem.equals("nav_calculators"))
            {
                navigationView.setCheckedItem(R.id.nav_calculators);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_calculators));
            }
            else if (lastItem.equals("nav_account"))
            {
                navigationView.setCheckedItem(R.id.nav_account);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_account));
            }
            else if (lastItem.equals("nav_articles"))
            {
                navigationView.setCheckedItem(R.id.nav_articles);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_articles));
            }
            else if (lastItem.equals("nav_users"))
            {
                navigationView.setCheckedItem(R.id.nav_users);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_users));
            }
            else if (lastItem.equals("nav_signOut"))
            {
                navigationView.setCheckedItem(R.id.nav_signOut);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_signOut));
            }



    }



    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);

            }
            else

            {
                signOut();
            }

        } else
        {
            getSupportFragmentManager().popBackStack();
        }

    }

    private void signOut()
    {

        final SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.MY_PREFERENCES), Context.MODE_PRIVATE);

        AlertDialog.Builder alertDialogLogOut = new AlertDialog.Builder(MainActivity.this);

        alertDialogLogOut.setTitle(R.string.SignOutTitle);
        alertDialogLogOut.setMessage(R.string.SignOutMessage);
        alertDialogLogOut.setIcon(R.drawable.ic_logo);


        alertDialogLogOut.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("rememberPasswordBoolean", false);
                        editor.putString("rememberLogin", "");
                        editor.putString("rememberPassword", "");
                        editor.apply();

                        finish();
                        dialog.cancel();
                    }
                });


        alertDialogLogOut.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                        onResume();
                    }
                });


        alertDialogLogOut.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        removeBackStack();

        if (id == R.id.nav_workout)
        {

            Intent toWorkout = new Intent(this, WorkoutActivity.class);
            startActivity(toWorkout);

        }

        else if (id == R.id.nav_history)
        {


            String title = getResources().getString(R.string.history);
            getSupportActionBar().setTitle(title);


            if (lastItem.equals("nav_historyFromWorkout"))
            {

                lastItem = "nav_history";

                Fragment fragmentHistoryMap = new FragmentHistoryMap();

                Bundle bundle = new Bundle();
                bundle.putInt("positionInList", 0);
                fragmentHistoryMap.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerLayout, fragmentHistoryMap);

                fragmentTransaction.commit();

               // this.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).addToBackStack(null).commit();
            }
            else
            {
                lastItem = "nav_history";
                FragmentHistory fragmentHistory = new FragmentHistory();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerLayout, fragmentHistory);

                fragmentTransaction.commit();
            }

        }

        else if (id == R.id.nav_calendar)
        {
            lastItem = "nav_calendar";

            String title = getResources().getString(R.string.calendar);
            getSupportActionBar().setTitle(title);

            FragmentCalendar fragmentCalendar = new FragmentCalendar();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerLayout, fragmentCalendar);

            fragmentTransaction.commit();
        }

        else if (id == R.id.nav_statistics)
        {
            lastItem = "nav_statistics";

            String title = getResources().getString(R.string.statistics);
            getSupportActionBar().setTitle(title);

            FragmentStatistics fragmentStatistics = new FragmentStatistics();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerLayout, fragmentStatistics);

            fragmentTransaction.commit();
        }

        else if (id == R.id.nav_calculators)
        {
            lastItem = "nav_calculators";

            String title = getResources().getString(R.string.calculators);
            getSupportActionBar().setTitle(title);

            FragmentCalculators fragmentCalculators = new FragmentCalculators();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerLayout, fragmentCalculators);

            fragmentTransaction.commit();

        }


        else if (id == R.id.nav_account)
        {
            lastItem = "nav_account";

            String title = getResources().getString(R.string.account);
            getSupportActionBar().setTitle(title);

            FragmentAccount fragmentAccount = new FragmentAccount();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerLayout, fragmentAccount);

            fragmentTransaction.commit();
        }

        else if (id == R.id.nav_articles)
        {
            lastItem = "nav_articles";

            String title = getResources().getString(R.string.articles);
            getSupportActionBar().setTitle(title);

            FragmentArticle fragmentArticle = new FragmentArticle();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerLayout, fragmentArticle);

            fragmentTransaction.commit();
        }

        else if (id == R.id.nav_users)
        {
            lastItem = "nav_users";

            String title = getResources().getString(R.string.users);
            getSupportActionBar().setTitle(title);

            FragmentUsers fragmentUsers = new FragmentUsers();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerLayout, fragmentUsers);

            fragmentTransaction.commit();
        }

        else if (id == R.id.nav_signOut)
        {
            //lastItem = "nav_signOut";

            signOut();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }



    public void removeBackStack()
    {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        for (int i = 0; i < count ; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }




}
