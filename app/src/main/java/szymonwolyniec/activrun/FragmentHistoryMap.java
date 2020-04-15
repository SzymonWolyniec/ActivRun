package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistoryMap extends Fragment implements OnMapReadyCallback {


    @Bind(R.id.showTimeDataTV)
    TextView showTimeDataTV;
    @Bind(R.id.showWorkoutTimeDataTV)
    TextView showWorkoutTimeDataTV;
    @Bind(R.id.showDistanceDataTV)
    TextView showDistanceDataTV;
    @Bind(R.id.showAvgSpeedDataTV)
    TextView showAvgSpeedDataTV;
    @Bind(R.id.showStartDateDataTV)
    TextView showStartDateDataTV;
    @Bind(R.id.showCaloriesDataTV)
    TextView showCaloriesDataTV;
    @Bind(R.id.showAvgPaceDataTV)
    TextView showAvgPaceDataTV;
    @Bind(R.id.showStopDateDataTV)
    TextView showStopDateDataTV;
    @Bind(R.id.showCommentDataTV)
    TextView showCommentDataTV;
    @Bind(R.id.historyEditBtn)
    Button historyEditBtn;
    private GoogleMap mMap;
    private MapView mapView;

    private View view;
    private int positionInList;
    private String nr;

    public FragmentHistoryMap() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       positionInList = this.getArguments().getInt("positionInList");


        view = inflater.inflate(R.layout.fragment_history_map, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        mapView = view.findViewById(R.id.fragmentMapMapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveAllWorkoutDesc();

        if (kurs.getCount() != 0) {

            kurs.moveToPosition(positionInList);


            nr = kurs.getString(0);

            String startTimeStr = kurs.getString(8);
            startTimeStr = startTimeStr.substring(0, startTimeStr.length() - 3);

            String stopTimeStr = kurs.getString(10);
            stopTimeStr = stopTimeStr.substring(0, stopTimeStr.length() - 3);


            showTimeDataTV.setText(kurs.getString(1));

            showWorkoutTimeDataTV.setText(kurs.getString(2));

            showDistanceDataTV.setText(kurs.getString(3) + " km");

            showCaloriesDataTV.setText(kurs.getString(6) + " kcal");

            showAvgSpeedDataTV.setText(kurs.getString(4) + " km/h");

            String avgPace = kurs.getString(5);
            if (avgPace.length() > 6) avgPace = "??:??";
            showAvgPaceDataTV.setText( avgPace + " min/km");

            showStartDateDataTV.setText(kurs.getString(7) + "  " + startTimeStr);

            showStopDateDataTV.setText(kurs.getString(9) + "  " + stopTimeStr);

            showCommentDataTV.setText(kurs.getString(13));


        }

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLineFromBD();


    }


    public void getLineFromBD() {

        String latLngFromBD;

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor cursor = bd.getWorkoutLatLng(Integer.valueOf(nr));
        cursor.moveToFirst();
        latLngFromBD = cursor.getString(0);

        char znak;

        PolylineOptions routeOptsBD;
        Polyline routeBD;

        List<LatLng> myCoordinatesListDB = new ArrayList<>();

        boolean latitude = true;
        boolean evenRoute = true;

        String latitudeBD = "", longtitudeBD = "";

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng markerLatLng = null;
        boolean firstCoordinate = true;


        routeOptsBD = new PolylineOptions()
                .color(Color.BLUE)
                .width(8 /* TODO: respect density! */)
                .geodesic(true);
        routeBD = mMap.addPolyline(routeOptsBD);
        routeBD.setVisible(true);

        LatLng myCoordinates;

        // Przeszukiwanie w STRINGU
        for (int i = 0; i < latLngFromBD.length(); i++) {
            znak = latLngFromBD.charAt(i);

            // Separator długość*szerokość
            if (znak == '*') {
                latitude = false;
            }
            // Separator długość,szerokość@długość,szerokość
            else if (znak == '@') {
                myCoordinates = new LatLng(Double.parseDouble(latitudeBD), Double.parseDouble(longtitudeBD));
                myCoordinatesListDB.add(myCoordinates);
                routeBD.setPoints(myCoordinatesListDB);

                builder.include(myCoordinates);

                markerLatLng = myCoordinates;

                if (firstCoordinate) {
                    mMap.addMarker(new MarkerOptions().position(markerLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_marker)));
                    firstCoordinate = false;
                }

                latitudeBD = "";
                longtitudeBD = "";
                latitude = true;
            }
            // Separator polylinie#polyline
            else if (znak == '#') {
                myCoordinatesListDB.clear();

                evenRoute = !evenRoute;
                if (evenRoute) routeOptsBD.color(Color.BLUE);
                else routeOptsBD.color(Color.RED);

                routeBD = mMap.addPolyline(routeOptsBD);
                routeBD.setVisible(true);
            }

            // Jeśli to inny znak (-,.,0-9)
            else {

                if (latitude) // Szerokośc geograficzna
                {
                    latitudeBD = latitudeBD + Character.toString(znak);
                } else // Długość geograficzna
                {
                    longtitudeBD = longtitudeBD + Character.toString(znak);
                }
            }

        }


        mMap.addMarker(new MarkerOptions().position(markerLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.stop_marker)));


        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);


        mMap.animateCamera(cu);

    }

    @OnClick(R.id.historyEditBtn)
    public void edit() {
        AppCompatActivity activity = (MainActivity) view.getContext();
        Fragment myFragment = new FragmentHistoryEdit();

        Bundle bundle = new Bundle();
        bundle.putInt("positionInList", Integer.valueOf(positionInList));
        myFragment.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.historyDeleteBtn)
    public void deleteWorkout()
    {
        AlertDialog.Builder alertDialogDeleteWorkout = new AlertDialog.Builder(getActivity());

        alertDialogDeleteWorkout.setTitle(R.string.removeWorkout);
        alertDialogDeleteWorkout.setMessage(R.string.removeWorkoutMessage);
        alertDialogDeleteWorkout.setIcon(R.drawable.ic_logo);


        alertDialogDeleteWorkout.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        String workoutID ;
                        DataBaseMain bd = new DataBaseMain(getActivity());
                        Cursor kurs = bd.giveWorkoutID();

                        if (kurs.getCount() != 0)
                        {
                            kurs.moveToPosition(positionInList);
                            workoutID = kurs.getString(0);

                            refreshAllUserStatistic(Integer.valueOf(workoutID));
                            bd.deleteWorkout(Integer.valueOf(nr));

                            FragmentHistory fragmentHistory = new FragmentHistory();

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.containerLayout, fragmentHistory);

                            fragmentTransaction.commit();
                        }
                        dialog.cancel();

                    }
                });


        alertDialogDeleteWorkout.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        dialog.cancel();
                    }
                });


        alertDialogDeleteWorkout.show();
    }


    private void refreshAllUserStatistic(int workoutID) {

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveUser();

        if (kurs.getCount() != 0) {

            kurs.moveToFirst();


            String totalWorkoutTimeFromDB = kurs.getString(12);
            double totalDistanceFromDB = Double.valueOf(kurs.getString(13));
            long totalCaloriesFromDB = Long.valueOf(kurs.getString(14));

            String totalWorkoutTimeFromDBHours = totalWorkoutTimeFromDB.substring(0, totalWorkoutTimeFromDB.length() - 6);
            String totalWorkoutTimeFromDBMinutes = totalWorkoutTimeFromDB.substring(totalWorkoutTimeFromDBHours.length() + 1, totalWorkoutTimeFromDB.length() - 3);
            String totalWorkoutTimeFromDBSeconds = totalWorkoutTimeFromDB.substring(totalWorkoutTimeFromDBHours.length() + 4, totalWorkoutTimeFromDB.length());

            long workoutTimeSecondsFromBD = Long.valueOf(totalWorkoutTimeFromDBHours)*3600 + (long)Integer.valueOf(totalWorkoutTimeFromDBMinutes)*60 + (long)Integer.valueOf(totalWorkoutTimeFromDBSeconds);



            kurs = bd.giveWorkoutByID(workoutID);

            if (kurs.getCount() != 0) {
                kurs.moveToFirst();

                String deletedTrainingWorkoutTime = kurs.getString(2);
                double deletedTrainingDistance = Double.valueOf(kurs.getString(3));
                long deletedTrainingCalories = Long.valueOf(kurs.getString(6));


                String deletedTrainingWorkoutTimeHours = deletedTrainingWorkoutTime.substring(0, deletedTrainingWorkoutTime.length() - 6);
                String deletedTrainingWorkoutTimeMinutes = deletedTrainingWorkoutTime.substring(deletedTrainingWorkoutTimeHours.length() + 1, deletedTrainingWorkoutTime.length() - 3);
                String deletedTrainingWorkoutTimeSeconds = deletedTrainingWorkoutTime.substring(deletedTrainingWorkoutTimeHours.length() + 4, deletedTrainingWorkoutTime.length());

                long deletedTrainingWorkoutTimeSecondsAll = Long.valueOf(deletedTrainingWorkoutTimeHours)*3600 + (long)Integer.valueOf(deletedTrainingWorkoutTimeMinutes)*60 + (long)Integer.valueOf(deletedTrainingWorkoutTimeSeconds);

                long totalWorkoutTimeSeconds = workoutTimeSecondsFromBD - deletedTrainingWorkoutTimeSecondsAll;

                long totalHours;
                int totalMinutes, totalSeconds;

                totalHours = (long) (totalWorkoutTimeSeconds / 3600);
                totalWorkoutTimeSeconds = totalWorkoutTimeSeconds - totalHours * 3600;

                totalMinutes = (int)(totalWorkoutTimeSeconds/60);
                totalWorkoutTimeSeconds = totalWorkoutTimeSeconds - totalMinutes * 60;

                totalSeconds = (int) totalWorkoutTimeSeconds;


                double totalDistance = totalDistanceFromDB - deletedTrainingDistance;

                Long totalCalories = totalCaloriesFromDB - deletedTrainingCalories;


                bd.editUserStatistic(String.format("%02d:%02d:%02d", (int)totalHours, totalMinutes, totalSeconds), String.format("%.3f", (float) totalDistance), String.valueOf(totalCalories));
            }

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
