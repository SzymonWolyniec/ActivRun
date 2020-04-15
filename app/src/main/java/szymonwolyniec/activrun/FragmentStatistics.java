package szymonwolyniec.activrun;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStatistics extends Fragment {


    @Bind(R.id.totalWorkoutTime)
    TextView totalWorkoutTime;
    @Bind(R.id.totalDistance)
    TextView totalDistance;
    @Bind(R.id.totalCalories)
    TextView totalCalories;

    public FragmentStatistics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveUser();

        if (kurs.getCount() != 0) {

            kurs.moveToFirst();


            String totalWorkoutTimeStr = kurs.getString(12);
            String totalDistanceStr = kurs.getString(13);
            String totalCaloriesStr = kurs.getString(14);



            totalWorkoutTime.setText(totalWorkoutTimeStr);
            totalDistance.setText(totalDistanceStr + " km");
            totalCalories.setText(totalCaloriesStr);
            

        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
