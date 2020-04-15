package szymonwolyniec.activrun;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistoryEdit extends Fragment {

    String nr;
    @Bind(R.id.historyEditTimeHours)
    EditText historyEditTimeHours;
    @Bind(R.id.historyEditTimeMinutes)
    EditText historyEditTimeMinutes;
    @Bind(R.id.historyEditTimeSeconds)
    EditText historyEditTimeSeconds;
    @Bind(R.id.historyEditWorkoutTimeHours)
    EditText historyEditWorkoutTimeHours;
    @Bind(R.id.historyEditWorkoutTimeMinutes)
    EditText historyEditWorkoutTimeMinutes;
    @Bind(R.id.historyEditWorkoutTimeSeconds)
    EditText historyEditWorkoutTimeSeconds;
    @Bind(R.id.historyEditKilometers)
    EditText historyEditKilometers;
    @Bind(R.id.historyEditCalories)
    EditText historyEditCalories;
    @Bind(R.id.historyEditComment)
    EditText historyEditComment;

    View view;
    int positionInList;

    public FragmentHistoryEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_edit, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        positionInList = this.getArguments().getInt("positionInList");

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveAllWorkoutDesc();

       if (kurs.getCount() != 0) {


            kurs.moveToPosition(positionInList);
            nr = kurs.getString(0);

            String timeStr = kurs.getString(1);
            String timeHoursStr = timeStr.substring(0, timeStr.length() - 6);
            String timeMinutesStr = timeStr.substring(3, timeStr.length() - 3);
            String timeSecondsStr = timeStr.substring(6, timeStr.length());

            historyEditTimeHours.setText(timeHoursStr);
            historyEditTimeMinutes.setText(timeMinutesStr);
            historyEditTimeSeconds.setText(timeSecondsStr);

           String workoutTimeStr = kurs.getString(2);
           String workoutTimeHoursStr = workoutTimeStr.substring(0, workoutTimeStr.length() - 6);
           String workoutTimeMinutesStr = workoutTimeStr.substring(3, workoutTimeStr.length() - 3);
           String workoutTimeSecondsStr = workoutTimeStr.substring(6, workoutTimeStr.length());

           historyEditWorkoutTimeHours.setText(workoutTimeHoursStr);
           historyEditWorkoutTimeMinutes.setText(workoutTimeMinutesStr);
           historyEditWorkoutTimeSeconds.setText(workoutTimeSecondsStr);

           historyEditKilometers.setText(kurs.getString(3));
           historyEditCalories.setText(kurs.getString(6));
           historyEditComment.setText(kurs.getString(13));

        }

        return view;
    }

    @OnClick(R.id.historyEditSaveBtn)
    public void saveEdit()
    {


         if (TextUtils.isEmpty(historyEditTimeHours.getText()) || TextUtils.isEmpty(historyEditTimeMinutes.getText()) || TextUtils.isEmpty(historyEditTimeSeconds.getText()) ||
                TextUtils.isEmpty(historyEditWorkoutTimeHours.getText()) || TextUtils.isEmpty(historyEditWorkoutTimeMinutes.getText()) || TextUtils.isEmpty(historyEditWorkoutTimeSeconds.getText()) ||
                TextUtils.isEmpty(historyEditKilometers.getText()) || TextUtils.isEmpty(historyEditCalories.getText()))
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.emptyField) , Toast.LENGTH_SHORT).show();
        }
        else
         {
             int timeHours = Integer.valueOf(historyEditTimeHours.getText().toString());
             int timeMinutes = Integer.valueOf(historyEditTimeMinutes.getText().toString());
             int timeSeconds = Integer.valueOf(historyEditTimeSeconds.getText().toString());

             int workoutTimeHours = Integer.valueOf(historyEditWorkoutTimeHours.getText().toString());
             int workoutTimeMinutes = Integer.valueOf(historyEditWorkoutTimeMinutes.getText().toString());
             int workoutTimeSeconds = Integer.valueOf(historyEditWorkoutTimeSeconds.getText().toString());

             float distanceKm = Float.valueOf(historyEditKilometers.getText().toString());
             String caloriesSave = historyEditCalories.getText().toString();

             String comment = historyEditComment.getText().toString();

             boolean workoutTimeisLonger = false;

             if (workoutTimeHours > timeHours)
             {
                 workoutTimeisLonger = true;
             }

             else if (workoutTimeHours == timeHours)
             {
                 if (workoutTimeMinutes > timeMinutes)
                 {
                     workoutTimeisLonger = true;
                 }

                 else if (workoutTimeMinutes == timeMinutes)
                 {
                     if (workoutTimeSeconds > timeSeconds)
                         workoutTimeisLonger = true;

                 }
             }




         if (workoutTimeisLonger)
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.workoutTimeLonger) , Toast.LENGTH_SHORT).show();
        }

        else if (timeMinutes > 59 || timeSeconds > 59)
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.incorrectData) + " \"" + getResources().getString(R.string.time) + "\"" , Toast.LENGTH_SHORT).show();
        }


        else if (workoutTimeMinutes > 59 || workoutTimeSeconds > 59)
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.incorrectData) + " \"" + getResources().getString(R.string.workoutTime) + "\"" , Toast.LENGTH_SHORT).show();
        }

        else
        {
            String timeSave = String.format("%02d", timeHours) + ":" + String.format("%02d", timeMinutes) + ":" + String.format("%02d", timeSeconds);
            String workoutTimeSave = String.format("%02d", workoutTimeHours) + ":" + String.format("%02d", workoutTimeMinutes) + ":" + String.format("%02d", workoutTimeSeconds);
            String distanceSave = String.format("%.3f", distanceKm);


            float hours = workoutTimeHours + (float) workoutTimeMinutes/60 + (float) workoutTimeSeconds/3600;

            float avgSpeed = (distanceKm / hours);
            String avgSpeedSave = String.format("%.2f", avgSpeed);

            float avgPace =  ((hours * 3600000) / (distanceKm * 1000)) / 60;

            int avgPaceMin = (int) avgPace;
            int avgPaceSec = (int) ((avgPace - avgPaceMin) * 60);

            String avgPaceSave = String.format("%02d", avgPaceMin) + ":" + String.format("%02d", avgPaceSec);


            DataBaseMain bd = new DataBaseMain(getActivity());



            long newTimeSeconds = (long)workoutTimeHours*3600 + (long) workoutTimeMinutes*60 + (long) workoutTimeSeconds;
            double newDistanceKm = Double.valueOf(historyEditKilometers.getText().toString());
            long newCalories = Long.valueOf(historyEditCalories.getText().toString());

            refreshAllUserStatistic(Integer.valueOf(nr), newTimeSeconds, newDistanceKm, newCalories);

            bd.editWorkout(Integer.valueOf(nr), timeSave, workoutTimeSave, distanceSave, avgSpeedSave, avgPaceSave, caloriesSave, comment);

            getFragmentManager().popBackStack();

        }

        }

    }


    private void refreshAllUserStatistic(int workoutID, long newTimeSeconds, double newDistanceKm, long newCalories) {

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

                String editedOldTrainingWorkoutTime = kurs.getString(2);
                double editedOldTrainingDistance = Double.valueOf(kurs.getString(3));
                long editedOldTrainingCalories = Long.valueOf(kurs.getString(6));


                String editedOldTrainingWorkoutTimeHours = editedOldTrainingWorkoutTime.substring(0, editedOldTrainingWorkoutTime.length() - 6);
                String editedOldTrainingWorkoutTimeMinutes = editedOldTrainingWorkoutTime.substring(editedOldTrainingWorkoutTimeHours.length() + 1, editedOldTrainingWorkoutTime.length() - 3);
                String editedOldTrainingWorkoutTimeSeconds = editedOldTrainingWorkoutTime.substring(editedOldTrainingWorkoutTimeHours.length() + 4, editedOldTrainingWorkoutTime.length());

                long editedOldTrainingWorkoutTimeSecondsAll = Long.valueOf(editedOldTrainingWorkoutTimeHours)*3600 + (long)Integer.valueOf(editedOldTrainingWorkoutTimeMinutes)*60 + (long)Integer.valueOf(editedOldTrainingWorkoutTimeSeconds);

                long oldNewWorkoutTimeSecondsDifference = editedOldTrainingWorkoutTimeSecondsAll - newTimeSeconds;
                double oldNewDistanceDifference = editedOldTrainingDistance - newDistanceKm;
                long oldNewCaloriesDifference = editedOldTrainingCalories - newCalories;





                long totalWorkoutTimeSeconds = workoutTimeSecondsFromBD - oldNewWorkoutTimeSecondsDifference;

                long totalHours;
                int totalMinutes, totalSeconds;

                totalHours = (long) (totalWorkoutTimeSeconds / 3600);
                totalWorkoutTimeSeconds = totalWorkoutTimeSeconds - totalHours * 3600;

                totalMinutes = (int)(totalWorkoutTimeSeconds/60);
                totalWorkoutTimeSeconds = totalWorkoutTimeSeconds - totalMinutes * 60;

                totalSeconds = (int) totalWorkoutTimeSeconds;


                double totalDistance = totalDistanceFromDB - oldNewDistanceDifference;

                Long totalCalories = totalCaloriesFromDB - oldNewCaloriesDifference;


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
