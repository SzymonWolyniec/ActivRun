package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistory extends Fragment {


    @Bind(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    int positionInList;
    FragmentHistoryMyAdapter adapter;
    View view;


    public FragmentHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        final FragmentHistoryMyAdapter adapter = new FragmentHistoryMyAdapter(generateSimpleListAllWorkout());
        RecyclerView recyclerView = (RecyclerView) historyRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            // Nie używane przeciąganie
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                positionInList = viewHolder.getAdapterPosition();

                deleteWorkoutDialog(positionInList);


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(historyRecyclerView);

        return view;
    }


    private List<FragmentHistoryMyViewModel> generateSimpleListAllWorkout() {

        List<FragmentHistoryMyViewModel> simpleViewModelList = new ArrayList<>();

        String url = "https://maps.googleapis.com/maps/api/staticmap?";
        url += "&size=600x300";
        url += "&maptype=roadmap";
        url += "&mobile=true";


        String staticMapURL;

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveAllWorkout();

        if (kurs.getCount() != 0) {

            kurs.moveToLast();
            do {

                staticMapURL = url;

                // Pobranie wartości z kolumn (0-3)


                String workoutTime = kurs.getString(2);
                String distance = kurs.getString(3);
                String avgSpeed = kurs.getString(4);
                String date = kurs.getString(7);
                String dateTime = kurs.getString(8);
                dateTime = dateTime.substring(0, dateTime.length() - 3);
                staticMapURL += kurs.getString(12);
                staticMapURL += "&key=" + "AIzaSyCj80x6EGndGx07Hy7zuaLx_KFsHKlogV0";

                String dateStr = date + "  " + dateTime;
                String distanceStr =  distance + " km";
                String timeStr = workoutTime;
                String avgSpeedStr = avgSpeed + " km/h";

                simpleViewModelList.add(new FragmentHistoryMyViewModel(dateStr, distanceStr, timeStr, avgSpeedStr, staticMapURL));


                // Dopóki kursor może poruszać się w dół (po wynikach)
            } while (kurs.moveToPrevious());
        }

        return simpleViewModelList;
    }

    public void deleteWorkoutDialog(final int positionInList)
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

                        String workoutID;
                        DataBaseMain bd = new DataBaseMain(getActivity());
                        Cursor kurs = bd.giveWorkoutID();

                        if (kurs.getCount() != 0)
                        {
                            kurs.moveToPosition(positionInList);
                            workoutID = kurs.getString(0);

                            refreshAllUserStatistic(Integer.valueOf(workoutID));
                            bd.deleteWorkout(Integer.valueOf(workoutID));

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(FragmentHistory.this).attach(FragmentHistory.this).commit();
                        }
                        dialog.cancel();

                    }
                });


        alertDialogDeleteWorkout.setNegativeButton(getResources().getString(R.string.NO),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(FragmentHistory.this).attach(FragmentHistory.this).commit();
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
