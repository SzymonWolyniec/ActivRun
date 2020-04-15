package szymonwolyniec.activrun;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBaseMain extends SQLiteOpenHelper {


    public static final String BD_NAME = "ActivRunBD";
    public static final String USER_TABLE = "UserBD";
    public static final String ARTICLE_TABLE = "ArticleBD";
    public String WORKOUT_USER_TABLE = "WorkoutsOf";
    public String CALENDAR_USER_TABLE = "CalendarOf";
    public static int version = 1;
    public static String loginOfUser;


    public DataBaseMain(Context context) {
        super(context, BD_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                " CREATE TABLE " + USER_TABLE + " (" + "nr integer primary key autoincrement ," + "name text not null," + "surname text not null,"
                        + "day integer not null," + "month integer not null," + "year integer not null,"
                        + "sex text not null," + "height integer not null," + "weight integer not null,"
                        + "email text not null," + "login text not null," +  "password text not null," + "workoutTimeSum text not null," + "distanceSum text not null," + "kcalSum text not null);" + "");

        db.execSQL(
                " CREATE TABLE " + ARTICLE_TABLE + " (" + "nr integer primary key autoincrement ," + "articleTitle text not null," + "articleText text not null,"
                        + "articleAuthor text not null," +"articleDate text not null);" + "");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(
                " CREATE TABLE " + WORKOUT_USER_TABLE + loginOfUser + " (" + "nr integer primary key autoincrement ,"
                        + "time text not null," + "workoutTime text not null," + "distance text not null,"
                        + "avgSpeed text not null," + "avgPace text not null,"
                        + "kcal text not null,"
                        + "dateStart text not null," + "dateTimeStart text not null," + "dateStop text not null," + "dateTimeStop text not null,"
                        + "LatLng text not null," + "LatLngStaticMap text not null,"
                        + "Comment text not null);" + "");

        db.execSQL(
                " CREATE TABLE " + CALENDAR_USER_TABLE + loginOfUser + " (" + "nr integer primary key autoincrement ,"
                        + "miliseconds text not null," + "title text not null," + "notes text not null," + "color integer not null);" + "");

    }

    public boolean isTableExistForThisUser(SQLiteDatabase db, String login) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + WORKOUT_USER_TABLE + login + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void setVersion(int dbVersion) {
        version = dbVersion;
    }

    public void setUserLogin(String login) {
        loginOfUser = login;
    }

    public void addUser(String userName, String userSurname, int userDay, int userMonth, int userYear, String userSex, int userHeight, int userWeight, String userEmail, String userLogin, String userPassword) {

        // Uchwyt do BD, get..., gdyż będą wprowadzane zmiany w BD
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("name", userName);
        wartosci.put("surname", userSurname);
        wartosci.put("day", userDay);
        wartosci.put("month", userMonth);
        wartosci.put("year", userYear);
        wartosci.put("sex", userSex);
        wartosci.put("height", userHeight);
        wartosci.put("weight", userWeight);
        wartosci.put("email", userEmail);
        wartosci.put("login", userLogin);
        wartosci.put("password", userPassword);
        wartosci.put("workoutTimeSum", "00:00:00");
        wartosci.put("distanceSum", "0.000");
        wartosci.put("kcalSum", "0");

        // nazwa tabeli do której będą wrzucane dane, lista wartości klasy ContentValues którą uzupełnilismy wcześniej
        db.insertOrThrow(USER_TABLE, null, wartosci);

    }


    public void addAdmin(String password) {

        // Uchwyt do BD, get..., gdyż będą wprowadzane zmiany w BD
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("name", "---");
        wartosci.put("surname", "---");
        wartosci.put("day", 1);
        wartosci.put("month", 1);
        wartosci.put("year", 1990);
        wartosci.put("sex", "---");
        wartosci.put("height", 175);
        wartosci.put("weight", 70);
        wartosci.put("email", "---");
        wartosci.put("login", "Admin");
        wartosci.put("password", password);
        wartosci.put("workoutTimeSum", "00:00:00");
        wartosci.put("distanceSum", "0.000");
        wartosci.put("kcalSum", "0");

        // nazwa tabeli do której będą wrzucane dane, lista wartości klasy ContentValues którą uzupełnilismy wcześniej
        db.insertOrThrow(USER_TABLE, null, wartosci);

    }

    public boolean adminExist() {

        SQLiteDatabase db = getReadableDatabase();
        String admin = "admin";

        String[] argumenty = {""+admin,};
        Cursor kursor = db.query(USER_TABLE, null, "login=?", argumenty, null, null, null);


        if (kursor.getCount() <= 0) {
            kursor.close();
            return false;
        }
        kursor.close();
        return true;
    }



    public Cursor giveAllUsers() {

        // Podanie jakie kolumny chce się odczytać oraz ich kolejność
        String[] kolumny = {"nr", "name", "surname", "day", "month", "year", "sex", "height", "weight", "email", "login", "password" , "workoutTimeSum", "distanceSum", "kcalSum"};

        // Uchwyt do BD, jedynie odczyt
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(USER_TABLE, kolumny, null, null, null, null, null, null);

        return kursor;
    }


    public Cursor giveUser() {

        String login = loginOfUser;
        SQLiteDatabase db = getReadableDatabase();

        String[] kolumny = {"nr", "name", "surname", "day", "month", "year", "sex", "height", "weight", "email", "login", "password" , "workoutTimeSum", "distanceSum", "kcalSum"};
        String[] argumenty = {"" + login};
        Cursor kursor = db.query(USER_TABLE, kolumny, "login=?", argumenty, null, null, null);


        return  kursor;
    }

    public Cursor giveUserByListPosition() {


        String[] kolumny = {"nr", "name", "surname", "day", "month", "year", "sex", "height", "weight", "email", "login"};
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(USER_TABLE, kolumny, null, null, null, null, "nr", null);

        return kursor;
    }

    public Cursor giveUserIDAndLogin() {


        String[] kolumny = {"nr","login"};
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(USER_TABLE, kolumny, null, null, null, null, "nr", null);

        return kursor;
    }

    public void deleteUser(int nr, String login) {
        SQLiteDatabase db = getWritableDatabase();
        String[] argumenty = {"" + nr};
        db.delete(USER_TABLE, "nr=?", argumenty);
        db.execSQL("DROP TABLE IF EXISTS '" + WORKOUT_USER_TABLE + login + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CALENDAR_USER_TABLE + login + "'");
    }



    public void editUserStatistic (String workoutTimeSum, String distanceSum, String kcalSum)
    {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues wartosci = new ContentValues();

            // Pierwsza nazwa kolumny, drugi nasze dane
            wartosci.put("workoutTimeSum", workoutTimeSum);
            wartosci.put("distanceSum", distanceSum);
            wartosci.put("kcalSum", kcalSum);

            String[] argumenty = {loginOfUser+""};
            db.update(USER_TABLE,wartosci,"login=?", argumenty);
    }


    public void editUserData (String name, String surname, int day, int month , int year , int height, int weight, String email, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("name", name);
        wartosci.put("surname", surname);
        wartosci.put("day", day);
        wartosci.put("month", month);
        wartosci.put("year", year);
        wartosci.put("height", height);
        wartosci.put("weight", weight);
        wartosci.put("email", email);
        wartosci.put("password", password);

        String[] argumenty = {loginOfUser+""};
        db.update(USER_TABLE,wartosci,"login=?", argumenty);
    }


    public void editUserDataByAdmin (int nr, String login, String name, String surname, int day, int month , int year , int height, int weight, String email)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("login", login);
        wartosci.put("name", name);
        wartosci.put("surname", surname);
        wartosci.put("day", day);
        wartosci.put("month", month);
        wartosci.put("year", year);
        wartosci.put("height", height);
        wartosci.put("weight", weight);
        wartosci.put("email", email);

        String[] argumenty = {nr+""};
        db.update(USER_TABLE,wartosci,"nr=?", argumenty);
    }

    public void resetPassword (int nr, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        wartosci.put("password", password);

        String[] argumenty = {nr+""};
        db.update(USER_TABLE,wartosci,"nr=?", argumenty);
    }


    public boolean loginExist(String login) {

        SQLiteDatabase db = getWritableDatabase();

        String[] argumenty = {"" + login};
        Cursor kursor = db.query(USER_TABLE, null, "login=?", argumenty, null, null, null);


        if (kursor.getCount() <= 0) {
            kursor.close();
            return false;
        }

        kursor.close();
        return true;
    }


    public boolean emailExist(String email) {

        SQLiteDatabase db = getWritableDatabase();

        String[] argumenty = {"" + email};
        Cursor kursor = db.query(USER_TABLE, null, "email=?", argumenty, null, null, null);


        if (kursor.getCount() <= 0) {
            kursor.close();
            return false;
        }
        kursor.close();
        return true;
    }

    public boolean loginDataCorrect(String login, String password) {

        SQLiteDatabase db = getWritableDatabase();

        String[] argumenty = {"" + login, "" + password};
        Cursor kursor = db.query(USER_TABLE, null, "login=? AND password=?", argumenty, null, null, null);


        if (kursor.getCount() <= 0) {
            kursor.close();
            return false;
        }
        kursor.close();
        return true;
    }


    public boolean passwordCorrect(String password) {

        SQLiteDatabase db = getWritableDatabase();

        String[] argumenty = {"" + loginOfUser, "" + password};
        Cursor kursor = db.query(USER_TABLE, null, "login=? AND password=?", argumenty, null, null, null);


        if (kursor.getCount() <= 0) {
            kursor.close();
            return false;
        }
        kursor.close();
        return true;
    }


    public Cursor getWeight() {

        String login = loginOfUser;
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{"weight"};
        String[] argumenty = {"" + login};
        Cursor kursor = db.query(USER_TABLE, columns, "login=?", argumenty, null, null, null);


        return  kursor;
    }







    public void addWorkout(String time, String workoutTime, String distance, String avgSpeed, String avgPace, String kcal, String dateStart, String dateTimeStart, String dateStop, String dateTimeStop, String LatLng, String LatLngStaticMap)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("time", time);
        wartosci.put("workoutTime", workoutTime);
        wartosci.put("distance", distance);
        wartosci.put("avgSpeed", avgSpeed);
        wartosci.put("avgPace", avgPace);
        wartosci.put("kcal", kcal);
        wartosci.put("dateStart", dateStart);
        wartosci.put("dateTimeStart", dateTimeStart);
        wartosci.put("dateStop", dateStop);
        wartosci.put("dateTimeStop", dateTimeStop);
        wartosci.put("LatLng", LatLng);
        wartosci.put("LatLngStaticMap", LatLngStaticMap);
        wartosci.put("Comment", "");

        db.insertOrThrow(WORKOUT_USER_TABLE + loginOfUser, null, wartosci);

    }

    public Cursor getWorkoutLatLng (int idOfWorkout)
    {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{"LatLng"};
        String[] argumenty = {"" + idOfWorkout};
        Cursor kursor = db.query(WORKOUT_USER_TABLE + loginOfUser, columns, "nr=?", argumenty, null, null, null);

        return  kursor;
    }

    public Cursor giveWorkoutID() {


        String[] kolumny = {"nr"};
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(WORKOUT_USER_TABLE + loginOfUser, kolumny, null, null, null, null, "nr"+" DESC", null);

        return kursor;
    }


    public Cursor giveAllWorkout() {

        // Podanie jakie kolumny chce się odczytać oraz ich kolejność
        String[] kolumny = {"nr", "time", "workoutTime", "distance", "avgSpeed", "avgPace", "kcal", "dateStart", "dateTimeStart", "dateStop", "dateTimeStop", "LatLng", "LatLngStaticMap", "Comment"};

        // Uchwyt do BD, jedynie odczyt
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(WORKOUT_USER_TABLE + loginOfUser, kolumny, null, null, null, null, null, null);

        return kursor;
    }

    public Cursor giveAllWorkoutDesc() {

        // Podanie jakie kolumny chce się odczytać oraz ich kolejność
        String[] kolumny = {"nr", "time", "workoutTime", "distance", "avgSpeed", "avgPace", "kcal", "dateStart", "dateTimeStart", "dateStop", "dateTimeStop", "LatLng", "LatLngStaticMap", "Comment"};

        // Uchwyt do BD, jedynie odczyt
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(WORKOUT_USER_TABLE + loginOfUser, kolumny, null, null, null, null, "nr"+" DESC", null);

        return kursor;
    }

    public void deleteWorkout(int nr) {
        SQLiteDatabase db = getWritableDatabase();
        String[] argumenty = {"" + nr};
        db.delete(WORKOUT_USER_TABLE + loginOfUser, "nr=?", argumenty);
    }

    public Cursor giveWorkoutByID(int nr) {

        SQLiteDatabase db = getReadableDatabase();
        String[] kolumny = {"nr", "time", "workoutTime", "distance", "avgSpeed", "avgPace", "kcal", "dateStart", "dateTimeStart", "dateStop", "dateTimeStop", "LatLng", "LatLngStaticMap", "Comment"};
        String[] argumenty = {"" + nr};
        Cursor kursor = db.query(WORKOUT_USER_TABLE + loginOfUser, kolumny,"nr=?", argumenty, null, null, null);

        return kursor;
    }


    public void editWorkout(int nr, String time, String workoutTime, String distance, String avgSpeed, String avgPace, String kcal, String comment) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("time", time);
        wartosci.put("workoutTime", workoutTime);
        wartosci.put("distance", distance);
        wartosci.put("avgSpeed", avgSpeed);
        wartosci.put("avgPace", avgPace);
        wartosci.put("kcal", kcal);
        wartosci.put("Comment", comment);

        String[] argumenty = {nr+""};
        db.update(WORKOUT_USER_TABLE + loginOfUser,wartosci,"nr=?", argumenty);
    }




    public Cursor calendarGiveAll() {

        // Podanie jakie kolumny chce się odczytać oraz ich kolejność
        String[] kolumny = {"miliseconds", "title", "notes", "color"};

        // Uchwyt do BD, jedynie odczyt
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(CALENDAR_USER_TABLE + loginOfUser, kolumny, null, null, null, null, null, null);

        return kursor;
    }


    public void addToCalendar(String miliseconds , String title, String notes , int color)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("miliseconds", miliseconds);
        wartosci.put("title", title);
        wartosci.put("notes", notes);
        wartosci.put("color", color);

        db.insertOrThrow(CALENDAR_USER_TABLE + loginOfUser, null, wartosci);

    }


    public void editEvent(String miliseconds , String title, String notes , int color)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("miliseconds", miliseconds);
        wartosci.put("title", title);
        wartosci.put("notes", notes);
        wartosci.put("color", color);

        String[] argumenty = {miliseconds+""};
        db.update(CALENDAR_USER_TABLE + loginOfUser,wartosci,"miliseconds=?", argumenty);

    }

    public void removeEvent(String miliseconds) {
        SQLiteDatabase db = getWritableDatabase();
        String[] argumenty = {"" + miliseconds};
        db.delete(CALENDAR_USER_TABLE + loginOfUser, "miliseconds=?", argumenty);
    }


    public Cursor giveAllArticle() {
        // Podanie jakie kolumny chce się odczytać oraz ich kolejność
        String[] kolumny = {"nr", "articleTitle", "articleText" , "articleAuthor" , "articleDate"};

        // Uchwyt do BD, jedynie odczyt
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(ARTICLE_TABLE, kolumny, null, null, null, null, null, null);

        return kursor;
    }

    public Cursor giveAllArticleDesc() {

        // Podanie jakie kolumny chce się odczytać oraz ich kolejność
        String[] kolumny = {"nr", "articleTitle", "articleText" , "articleAuthor" , "articleDate"};

        // Uchwyt do BD, jedynie odczyt
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(ARTICLE_TABLE, kolumny, null, null, null, null, "nr"+" DESC", null);

        return kursor;
    }

    public void addArticle(String articleTitle , String articleText, String articleAuthor, String articleDate)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("articleTitle", articleTitle);
        wartosci.put("articleText", articleText);
        wartosci.put("articleAuthor", articleAuthor);
        wartosci.put("articleDate", articleDate);

        db.insertOrThrow(ARTICLE_TABLE, null, wartosci);

    }

    public void editArticle(int nr , String articleTitle, String articleText , String articleAuthor, String articleDate)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues wartosci = new ContentValues();

        // Pierwsza nazwa kolumny, drugi nasze dane
        wartosci.put("nr", nr);
        wartosci.put("articleTitle", articleTitle);
        wartosci.put("articleText", articleText);
        wartosci.put("articleAuthor", articleAuthor);
        wartosci.put("articleDate", articleDate);

        String[] argumenty = {nr+""};
        db.update(ARTICLE_TABLE,wartosci,"nr=?", argumenty);

    }

    public Cursor giveArticleID() {


        String[] kolumny = {"nr"};
        SQLiteDatabase db = getReadableDatabase();

        // Nazwa tabeli z jakiej będą pobierane dane, lista kolumn które będziemy pobierać (reszta to where, having, group by...)
        Cursor kursor = db.query(ARTICLE_TABLE, kolumny, null, null, null, null, "nr"+" DESC", null);

        return kursor;
    }

    public void deleteArticle(int nr) {
        SQLiteDatabase db = getWritableDatabase();
        String[] argumenty = {"" + nr};
        db.delete(ARTICLE_TABLE, "nr=?", argumenty);
    }


}