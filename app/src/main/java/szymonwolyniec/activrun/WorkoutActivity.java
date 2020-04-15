package szymonwolyniec.activrun;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkoutActivity extends FragmentActivity implements OnMapReadyCallback {


    // Binds
    @Bind(R.id.playButton)
    FloatingActionButton playButton;
    @Bind(R.id.stopButton)
    FloatingActionButton stopButton;
    @Bind(R.id.speedTV)
    TextView speedTV;
    @Bind(R.id.timeTV)
    TextView timeTV;
    @Bind(R.id.distanceTV)
    TextView distanceTV;
    @Bind(R.id.paceTV)
    TextView paceTV;
    @Bind(R.id.caloriesTV)
    TextView caloriesTV;
    @Bind(R.id.avgSpeedTV)
    TextView avgSpeedTV;
    @Bind(R.id.workoutTimeTV)
    TextView workoutTimeTV;
    @Bind(R.id.avgPaceTV)
    TextView avgPaceTV;
    @Bind(R.id.caloriesHourTV)
    TextView caloriesHourTV;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.layoutResize)
    LinearLayout layoutResize;
    @Bind(R.id.timeMinTV)
    TextView timeMinTV;
    @Bind(R.id.distanceShowHide)
    LinearLayout distanceShowHide;
    @Bind(R.id.speedShowHide)
    LinearLayout speedShowHide;
    @Bind(R.id.avgSpeedShowHide)
    LinearLayout avgSpeedShowHide;
    @Bind(R.id.caloriesShowHide)
    LinearLayout caloriesShowHide;
    @Bind(R.id.timeMinShowHide)
    LinearLayout timeMinShowHide;
    @Bind(R.id.workoutTimeShowHide)
    LinearLayout workoutTimeShowHide;
    @Bind(R.id.paceShowHide)
    LinearLayout paceShowHide;
    @Bind(R.id.avgPaceShowHide)
    LinearLayout avgPaceShowHide;
    @Bind(R.id.caloriesHourShowHide)
    LinearLayout caloriesHourShowHide;
    @Bind(R.id.timeShowHide)
    LinearLayout timeShowHide;
    @Bind(R.id.noLocationDataTV)
    TextView noLocationDataTV;

    private final int MY_LOCATION_REQUEST_CODE = 5;
    public GoogleMap mMap;


    // Distance, speed
    float lat, lng, latOld, lngOld, distance, mySpeed, myPace;
    long myTime, myRealTime;
    double averageSpeed = 0, averagePace = 0, km_hTOmeter_min = 0, caloriesPerHour = 0, calories = 0, caloriesShow = 0;
    int myPaceMin, myPaceSec, averagePaceMin, averagePaceSec;


    boolean isPlay = false;
    boolean first = true;

    boolean freeRun = false;
    boolean continueNewRoute = true;
    boolean firstRoute = true;
    @Bind(R.id.pauseButton)
    FloatingActionButton pauseButton;

    // Polyline draw

    private PolylineOptions routeOpts = null;
    private Polyline route = null;

    private PolylineOptions routeOpts2 = null;
    private Polyline route2 = null;

    List<Polyline> myPolylinesList = new ArrayList<>();


    int polylineLenght;
    LatLng polylineHandler;

    Polyline drawPolyline;
    List<LatLng> myCoordinatesListDraw;

    // Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    // Timer
    Timer timer;
    MyTimerTask timerTask;
    volatile boolean timerRealWorks = false;
    volatile boolean timerWorkoutWorks = false;
    float realTime = 0;
    long realCalcTime;
    float workoutTime = 0;
    long workoutCalcTime;
    float workoutTimeAll;

    int workoutSeconds = 0;
    int workoutMinutes = 0;
    int workoutHours = 0;

    int realSeconds = 0;
    int realMinutes = 0;
    int realHours = 0;

    boolean startStopIsClicked = false;
    boolean canProgramicallyStartStop = false;
    boolean locationUpdatesOn = false;

    float highOld, highNew, nachylenieTerenu, srednieNachylenieTerenu = 0f;
    int srednieNachylenieLiczba = 1;

    int weight;

    DateFormat dateFormatStart, dateFormatStop;
    DateFormat dateFormatTimeStart, dateFormatTimeStop;
    String dateStart, dateTimeStart, dateStop, dateTimeStop;

    boolean isLocationWorking = false;
    boolean smallStatisticWindow = false;

    int delayThreadNumber = 0;
    boolean stopButonClicked = false;
    boolean isfirstAvailableLocation = true;
    boolean isPauseBeforeDataLost = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity_layout);
        ButterKnife.bind(this);
        Locale.setDefault(new Locale("en", "US"));

        DataBaseMain bd = new DataBaseMain(this);
        Cursor cursor = bd.getWeight();
        cursor.moveToFirst();
        weight = cursor.getInt(0);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pauseButton.hide();


        locationRequest = new LocationRequest();
        //locationRequest.setInterval(7500); //use a value fo about 10 to 15s for a real app
        locationRequest.setFastestInterval(1000);
        //locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Pierwszy odczyt GPS jeśli to możliwe

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                    }
                }


            });
        }


        // Kolejne odczyty GPS jeśli to możliwe

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {
                    //Update UI with location data
                    if (location != null) {
                        if (isLocationWorking == false) {
                            isLocationWorking = true;
                            startPauseButtonClicked();
                        }

                        dataCalcAndShow(location);
                        drawRoute(location);
                    } } }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);

                if (locationAvailability.isLocationAvailable()) {

                    noLocationDataTV.setText("");


                    if (isfirstAvailableLocation)
                        isfirstAvailableLocation = false;
                    else
                    {
                        playButton.show();
                        if(!isPauseBeforeDataLost) startPauseButtonClicked();
                    }



                }
                else
                {
                    if(isPlay)
                    {
                        isPauseBeforeDataLost = false;
                        startPauseButtonClicked();
                        if (!isfirstAvailableLocation) playButton.hide();
                    }
                    else
                    {
                        isPauseBeforeDataLost = true;
                        if (!isfirstAvailableLocation) playButton.hide();
                    }



                    if (isfirstAvailableLocation)
                    {
                        stopLocationUpdates();

                        SpannableString myWarningText = new SpannableString(getResources().getString(R.string.noLocationData) + "\n\n" + getResources().getString(R.string.noGPS) + getResources().getString(R.string.noGPSTryAgain));
                        myWarningText.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.underTextColor)), 0, myWarningText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        noLocationDataTV.setText(myWarningText);

                    }
                    else
                    {
                        SpannableString myWarningText = new SpannableString(getResources().getString(R.string.noLocationData) + "\n\n" + getResources().getString(R.string.noGPS));
                        myWarningText.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.underTextColor)), 0, myWarningText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        noLocationDataTV.setText(myWarningText);
                    }


                }
            }


        };


    }


    public void onMapReady(GoogleMap map) {

        mMap = map;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        }
        else
            ActivityCompat.requestPermissions(WorkoutActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
    }

    public void myTimer() {
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }


    public void dataCalcAndShow(Location location) {


        if (first) { //first = true


            lat = (float) location.getLatitude();
            lng = (float) location.getLongitude();
            highNew = (float) location.getAltitude();
            myTime = System.currentTimeMillis();


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18));
            first = false;
        } else // first = false
        {

            latOld = lat;
            lngOld = lng;
            highOld = highNew;


            lat = (float) location.getLatitude();
            lng = (float) location.getLongitude();
            highNew = (float) location.getAltitude();

            Location newLocation = new Location("NewPoints");
            Location oldLocation = new Location("OldPoints");


            oldLocation.setLatitude(latOld);
            oldLocation.setLongitude(lngOld);

            newLocation.setLatitude(lat);
            newLocation.setLongitude(lng);

            // Sprawdzenie ile milisekund minęło, jeśli callback nie wykona się w odstępie 1000 planowanych
            myRealTime = (System.currentTimeMillis() - myTime);
            myTime = System.currentTimeMillis();


            nachylenieTerenu = (highNew - highOld) / (oldLocation.distanceTo(newLocation));

            if (Float.isNaN(nachylenieTerenu) || (nachylenieTerenu < 0) || Float.isInfinite(nachylenieTerenu))
                nachylenieTerenu = 0f;


            if (!freeRun) {
                distance += oldLocation.distanceTo(newLocation);
                averageSpeed = (distance / (workoutTimeAll / 1000)) * 3.6;

                if (averageSpeed > 0) {
                    averagePace = (3600 / averageSpeed) / 60;
                    averagePaceMin = (int) averagePace;
                    averagePaceSec = (int) ((averagePace - averagePaceMin) * 60);

                    srednieNachylenieTerenu = (float) (srednieNachylenieTerenu * srednieNachylenieLiczba + nachylenieTerenu) / (++srednieNachylenieLiczba);

                    km_hTOmeter_min = averageSpeed / 0.06;
                    caloriesPerHour = 3.5 + (km_hTOmeter_min * 0.2) + (km_hTOmeter_min * 0.9 * srednieNachylenieTerenu);
                    caloriesPerHour = (caloriesPerHour / 3.5) * weight;
                    caloriesPerHour = (int) caloriesPerHour;

                    km_hTOmeter_min = mySpeed / 0.06;
                    calories = 3.5 + (km_hTOmeter_min * 0.2) + (km_hTOmeter_min * 0.9 * nachylenieTerenu);
                    calories = (calories / 3.5) * weight;
                    calories = calories / (3600000 / myRealTime);
                    caloriesShow = caloriesShow + calories;

                }


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18));
            }


            mySpeed = (oldLocation.distanceTo(newLocation) * (myRealTime/1000) * 3.6f);


            if (mySpeed > 0) {
                myPace = (3600 / mySpeed) / 60;
                myPaceMin = (int) myPace;
                myPaceSec = (int) ((myPace - myPaceMin) * 60);

            }


            if (isPlay && mySpeed < 1 && canProgramicallyStartStop == true) {
                start();
            } // Start -> Pause
            if (!isPlay && mySpeed > 1 && canProgramicallyStartStop == true) {
                start();
            } // Pause -> Start


            distanceTV.setText(String.format("%.3f", distance / 1000) + " km");
            speedTV.setText(String.format("%.2f", mySpeed) + " km/h");
            avgSpeedTV.setText(String.format("%.2f", averageSpeed) + " km/h");
            paceTV.setText(myPaceMin + ":" + String.format("%02d", myPaceSec) + " min/km");
            avgPaceTV.setText(averagePaceMin + ":" + String.format("%02d", averagePaceSec) + " min/km");
            caloriesTV.setText(String.valueOf((int) caloriesShow));
            caloriesHourTV.setText(String.valueOf((int) caloriesPerHour));


        }
    }


    public void drawRoute(Location location) {

        if (!freeRun) //freeRun = false
        {

            if (continueNewRoute) // continueNewRoute = true;
            {
                if (firstRoute) // firstRoute = true;
                {
                    dateFormatStart = new SimpleDateFormat("dd/MM/yyyy");
                    dateFormatTimeStart = new SimpleDateFormat("HH:mm:ss");

                    dateStart = dateFormatStart.format(Calendar.getInstance().getTime());
                    dateTimeStart = dateFormatTimeStart.format(Calendar.getInstance().getTime());

                    routeOpts = new PolylineOptions()
                            .color(Color.BLUE)
                            .width(5 /* TODO: respect density! */)
                            .geodesic(true);
                    route = mMap.addPolyline(routeOpts);
                    route.setVisible(true);


                    firstRoute = false;
                    continueNewRoute = false;
                }
                else // firstRoute = false;
                {
                    myPolylinesList.add(route);

                    polylineLenght = route.getPoints().size();
                    polylineHandler = route.getPoints().get(polylineLenght - 1);

                    routeOpts = new PolylineOptions()
                            .color(Color.BLUE)
                            .width(5 /* TODO: respect density! */)
                            .geodesic(true);
                    route = mMap.addPolyline(routeOpts);
                    route.setVisible(true);


                    LatLng myCoordinates = new LatLng(polylineHandler.latitude, polylineHandler.longitude);
                    List<LatLng> myCoordinatesList = route.getPoints();
                    myCoordinatesList.add(myCoordinates);
                    route.setPoints(myCoordinatesList);


                    myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    myCoordinatesList = route.getPoints();
                    myCoordinatesList.add(myCoordinates);
                    route.setPoints(myCoordinatesList);

                    continueNewRoute = false;

                }

            } else // continueNewRoute = false;
            {

                if (routeOpts != null) {

                    LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    List<LatLng> myCoordinatesList = route.getPoints();
                    myCoordinatesList.add(myCoordinates);
                    route.setPoints(myCoordinatesList);
                }
            }
        } else //freeRun = true
        {
            if (continueNewRoute) // continueNewRoute = true;
            {

                myPolylinesList.add(route);
                polylineLenght = route.getPoints().size();
                polylineHandler = route.getPoints().get(polylineLenght - 1);


                routeOpts = new PolylineOptions()
                        .color(Color.RED)
                        .width(5 /* TODO: respect density! */)
                        .geodesic(true);
                route = mMap.addPolyline(routeOpts);
                route.setVisible(true);


                LatLng myCoordinates = new LatLng(polylineHandler.latitude, polylineHandler.longitude);
                List<LatLng> myCoordinatesList = route.getPoints();
                myCoordinatesList.add(myCoordinates);
                route.setPoints(myCoordinatesList);

                myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                myCoordinatesList = route.getPoints();
                myCoordinatesList.add(myCoordinates);
                route.setPoints(myCoordinatesList);

                continueNewRoute = false;
            } else // continueNewRoute = false;
            {
                if (routeOpts != null) {

                    LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    List<LatLng> myCoordinatesList = route.getPoints();
                    myCoordinatesList.add(myCoordinates);
                    route.setPoints(myCoordinatesList);
                }
            }

        }

    }


    public void stopWorkout() {
        stopLocationUpdates();
        timerRealWorks = false;
        myPolylinesList.add(route);

        dateFormatStop = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatTimeStop = new SimpleDateFormat("HH:mm:ss");

        dateStop = dateFormatStop.format(Calendar.getInstance().getTime());
        dateTimeStop = dateFormatTimeStop.format(Calendar.getInstance().getTime());


        String saveCoordinatesToBD = "";
        String saveCoordinatesToBDStatic = "";
        boolean blueRoad = true;
        Polyline savePolyline;
        List<LatLng> myCoordinatesListSave;
        LatLng saveLatLng;

        for (int i = 0; i < myPolylinesList.size(); i++) {

            // Pobranie komórki (jedna polylinia) do savePolyline [Polyline]
            savePolyline = myPolylinesList.get(i);

            // Pobranie wszystkich współrzędnych jednej polylini do tablicy
            myCoordinatesListSave = savePolyline.getPoints();

            if (i == 0) {
                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + "&markers=color:green%7Clabel:S%7C" + Double.toString(myCoordinatesListSave.get(0).latitude) + "," + Double.toString(myCoordinatesListSave.get(0).longitude);
            }


            if (blueRoad) {
                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + "&path=color:0x0000FFFF";
                blueRoad = false;
            } else {
                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + "&path=color:0xFF0000FF";
                blueRoad = true;
            }


            // Przejście po wszystkich współrzędnych polylini
            for (int j = 0; j < myCoordinatesListSave.size(); j++) {
                saveLatLng = myCoordinatesListSave.get(j);

                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + "|";

                saveCoordinatesToBD = saveCoordinatesToBD + Double.toString(saveLatLng.latitude);
                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + Double.toString(saveLatLng.latitude);

                saveCoordinatesToBD = saveCoordinatesToBD + "*"; // * <-- separator między szerokością,a długością geograficzną
                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + ",";

                saveCoordinatesToBD = saveCoordinatesToBD + Double.toString(saveLatLng.longitude);
                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + Double.toString(saveLatLng.longitude);

                saveCoordinatesToBD = saveCoordinatesToBD + "@"; // @ <-- separator współrzędnych, współrzędna = szerokośc i długość geograficzna

            }

            saveCoordinatesToBD = saveCoordinatesToBD + "#"; // # <-- separator polylini

            if (i == myPolylinesList.size() - 1) {
                saveCoordinatesToBDStatic = saveCoordinatesToBDStatic + "&markers=color:red%7Clabel:F%7C" + Double.toString(myCoordinatesListSave.get(myCoordinatesListSave.size() - 1).latitude) + "," + Double.toString(myCoordinatesListSave.get(myCoordinatesListSave.size() - 1).longitude);
            }
        }

        DataBaseMain bd = new DataBaseMain(this);
        bd.addWorkout(String.format("%02d:%02d:%02d", realHours, realMinutes, realSeconds), String.format("%02d:%02d:%02d", workoutHours, workoutMinutes, workoutSeconds)
                , String.format("%.3f", distance / 1000), String.format("%.2f", averageSpeed), averagePaceMin + ":" + String.format("%02d", averagePaceSec)
                , Integer.toString((int) caloriesShow), dateStart, dateTimeStart, dateStop, dateTimeStop, saveCoordinatesToBD, saveCoordinatesToBDStatic);


        Cursor kurs = bd.giveUser();

        if (kurs.getCount() != 0) {

            kurs.moveToFirst();


            String totalWorkoutTimeFromDB = kurs.getString(12);
            String totalDistanceFromDB = kurs.getString(13);
            String totalCaloriesFromDB = kurs.getString(14);

            String totalWorkoutTimeFromDBHours = totalWorkoutTimeFromDB.substring(0, totalWorkoutTimeFromDB.length() - 6);
            String totalWorkoutTimeFromDBMinutes = totalWorkoutTimeFromDB.substring(totalWorkoutTimeFromDBHours.length() + 1, totalWorkoutTimeFromDB.length() - 3);
            String totalWorkoutTimeFromDBSeconds = totalWorkoutTimeFromDB.substring(totalWorkoutTimeFromDBHours.length() + 4, totalWorkoutTimeFromDB.length());

            long workoutTimeSecondsFromBD = Long.valueOf(totalWorkoutTimeFromDBHours) * 3600 + (long) Integer.valueOf(totalWorkoutTimeFromDBMinutes) * 60 + (long) Integer.valueOf(totalWorkoutTimeFromDBSeconds);

            long trainingWorkoutTimeSeconds = (long) workoutHours * 3600 + (long) workoutMinutes * 60 + (long) workoutSeconds;

            long totalWorkoutTimeSeconds = workoutTimeSecondsFromBD + trainingWorkoutTimeSeconds;

            long totalHours;
            int totalMinutes, totalSeconds;

            totalHours = (long) (totalWorkoutTimeSeconds / 3600);
            totalWorkoutTimeSeconds = totalWorkoutTimeSeconds - totalHours * 3600;

            totalMinutes = (int) (totalWorkoutTimeSeconds / 60);
            totalWorkoutTimeSeconds = totalWorkoutTimeSeconds - totalMinutes * 60;

            totalSeconds = (int) totalWorkoutTimeSeconds;

            double totalDistance = Double.valueOf(totalDistanceFromDB) + (distance / 1000);

            Long totalCalories = Long.valueOf(totalCaloriesFromDB) + (long) caloriesShow;


            bd.editUserStatistic(String.format("%02d:%02d:%02d", (int) totalHours, totalMinutes, totalSeconds), String.format("%.3f", (float) totalDistance), String.valueOf(totalCalories));


        }


        Intent backFromWorkout = new Intent(this, MainActivity.class);
        backFromWorkout.putExtra("itemToChecked", "nav_historyFromWorkout");
        startActivity(backFromWorkout);
        finish();
    }


    @OnClick(R.id.playButton)
    public void setPlayButton()
    {
        startPauseButtonClicked();
    }

    @OnClick(R.id.pauseButton)
    public void setPauseButton()
    {
        startPauseButtonClicked();
    }


    public void startPauseButtonClicked() {
        if (isLocationWorking) startStopIsClicked = true;
        else startStopIsClicked = false;

        start();
    }


    public void start() {

        if (isPlay) // Start -> Pause
        {
            timerWorkoutWorks = false;

            continueNewRoute = true;
            freeRun = true;

            first = true;

            playButton.show();
            pauseButton.hide();
            stopButton.show();
            isPlay = false;

            if (!(route.getPoints().size() > 0)) {
                isfirstAvailableLocation = true;
                firstRoute = true;
                stopLocationUpdates();
                timerRealWorks = false;
                resetTimerTime();
            }
        }
        else // Pause -> Start
        {

            if (isLocationWorking) {


                timerWorkoutWorks = true;
                workoutCalcTime = System.currentTimeMillis();

                if (!timerRealWorks) {
                    timerRealWorks = true;
                    myTimer();
                }


                continueNewRoute = true;
                freeRun = false;
                first = true;

                playButton.hide();
                pauseButton.show();
                stopButton.hide();
                isPlay = true;

            }

            if (!locationUpdatesOn) startLocationUpdates();
        }


        if (isLocationWorking) {


            if (startStopIsClicked == true && isPlay == true) {


                canProgramicallyStartStop = false;


                final Thread delayThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            delayThreadNumber++;
                            int numberOfThread = delayThreadNumber;
                            Thread.sleep(15000);
                            if (isPlay && numberOfThread == delayThreadNumber) {
                                canProgramicallyStartStop = true;
                            }

                        } catch (InterruptedException e) {

                        }
                    }
                };

                delayThread.start();


            } else if (startStopIsClicked == true && isPlay == false)
                canProgramicallyStartStop = false;
            else if (startStopIsClicked == false && isPlay == true)
                canProgramicallyStartStop = true;
            else if (startStopIsClicked == false && isPlay == false)
                canProgramicallyStartStop = true;

            startStopIsClicked = false;
        }

    }

    public void resetTimerTime() {
        realCalcTime = 0;
        realTime = 0;
        realSeconds = 0;
        realMinutes = 0;
        realHours = 0;

        workoutTimeAll = 0;
        workoutCalcTime = 0;
        workoutTime = 0;
        workoutSeconds = 0;
        workoutMinutes = 0;
        workoutHours = 0;

        timeTV.setText(String.format("%02d:%02d:%02d", realHours, realMinutes, realSeconds));
        timeMinTV.setText(String.format("%02d:%02d:%02d", workoutHours, workoutMinutes, workoutSeconds));
        workoutTimeTV.setText(String.format("%02d:%02d:%02d", workoutHours, workoutMinutes, workoutSeconds));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == MY_LOCATION_REQUEST_CODE) {


            if (permissions.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    finish();
                    startActivity(getIntent());
                } else {

                    // Show rationale and request permission.
                }
            } else

            {
                Toast.makeText(this, getResources().getString(R.string.neededLocation), Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isPlay) {
            startLocationUpdates();
        }
    }


    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

        locationUpdatesOn = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        locationUpdatesOn = false;
    }


    @OnClick(R.id.stopButton)
    public void stopButtonClickStopWorkout() {
        stopButonClicked = true;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }

    public void exitDialog() {

        AlertDialog.Builder alertDialogFinishWorkout = new AlertDialog.Builder(WorkoutActivity.this);

        alertDialogFinishWorkout.setTitle(R.string.FinishWorkoutTitle);
        alertDialogFinishWorkout.setMessage(R.string.FinishWorkoutMessage);
        alertDialogFinishWorkout.setIcon(R.drawable.ic_logo);


        alertDialogFinishWorkout.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        playButton.hide();
                        pauseButton.hide();
                        stopButton.hide();

                        if (!stopButonClicked)
                            startPauseButtonClicked();


                        if (firstRoute == false)
                            stopWorkout();
                        else
                        {
                            stopLocationUpdates();
                            finish();
                        }


                    }
                });


        alertDialogFinishWorkout.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stopButonClicked = false;
                        dialog.cancel();
                    }
                });


        alertDialogFinishWorkout.show();
    }


    @OnClick(R.id.layoutResize)
    public void setLayoutResize() {

        if (smallStatisticWindow) {
            timeMinShowHide.setVisibility(View.GONE);
            timeShowHide.setVisibility(View.VISIBLE);
            workoutTimeShowHide.setVisibility(View.VISIBLE);
            speedShowHide.setVisibility(View.VISIBLE);
            paceShowHide.setVisibility(View.VISIBLE);
            avgSpeedShowHide.setVisibility(View.VISIBLE);
            avgPaceShowHide.setVisibility(View.VISIBLE);
            caloriesShowHide.setVisibility(View.VISIBLE);
            caloriesHourShowHide.setVisibility(View.VISIBLE);
            smallStatisticWindow = false;
        } else {
            timeMinShowHide.setVisibility(View.VISIBLE);
            timeShowHide.setVisibility(View.GONE);
            workoutTimeShowHide.setVisibility(View.GONE);
            speedShowHide.setVisibility(View.GONE);
            paceShowHide.setVisibility(View.GONE);
            avgSpeedShowHide.setVisibility(View.GONE);
            avgPaceShowHide.setVisibility(View.GONE);
            caloriesShowHide.setVisibility(View.GONE);
            caloriesHourShowHide.setVisibility(View.GONE);

            smallStatisticWindow = true;
        }


    }


    class MyTimerTask extends TimerTask {
        public MyTimerTask() {
            realCalcTime = System.currentTimeMillis();
        }


        @Override
        public void run()

        {
            if (timerRealWorks) {

                realTime = realTime + (System.currentTimeMillis() - realCalcTime);

                realCalcTime = System.currentTimeMillis();

                realSeconds = (int) Math.round(realTime / 1000l);


                if (realSeconds >= 60) {
                    realTime = 0;
                    realSeconds = 0;
                    realMinutes++;

                    if (realMinutes >= 60) {
                        realMinutes = 0;
                        realHours++;
                    }
                }


                if (timerWorkoutWorks) {
                    workoutTimeAll = workoutTimeAll + (System.currentTimeMillis() - workoutCalcTime);
                    workoutTime = workoutTime + (System.currentTimeMillis() - workoutCalcTime);
                    workoutCalcTime = System.currentTimeMillis();
                    workoutSeconds = (int) Math.round(workoutTime / 1000l);

                    if (workoutSeconds >= 60) {
                        workoutTime = 0;
                        workoutSeconds = 0;
                        workoutMinutes++;

                        if (workoutMinutes >= 60) {
                            workoutMinutes = 0;
                            workoutHours++;
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        timeTV.setText(String.format("%02d:%02d:%02d", realHours, realMinutes, realSeconds));
                        timeMinTV.setText(String.format("%02d:%02d:%02d", workoutHours, workoutMinutes, workoutSeconds));
                        workoutTimeTV.setText(String.format("%02d:%02d:%02d", workoutHours, workoutMinutes, workoutSeconds));
                    }
                });

            } else {
                timer.cancel();
                timer.purge();
            }


        }

    }


}



