package szymonwolyniec.activrun;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentCalculators extends Fragment {


    @Bind(R.id.paceCalcTV)
    TextView paceCalcTV;
    @Bind(R.id.timeCalcTV)
    TextView timeCalcTV;
    @Bind(R.id.distanceCalcTV)
    TextView distanceCalcTV;
    @Bind(R.id.lapTimeCalcTV)
    TextView lapTimeCalcTV;
    @Bind(R.id.bmiCalcTV)
    TextView bmiCalcTV;
    @Bind(R.id.kcalCalcTV)
    TextView kcalCalcTV;
    @Bind(R.id.pulseCalcTV)
    TextView pulseCalcTV;
    @Bind(R.id.paceKmET)
    EditText paceKmET;
    @Bind(R.id.paceMetersET)
    EditText paceMetersET;
    @Bind(R.id.paceHoursET)
    EditText paceHoursET;
    @Bind(R.id.paceMinutsET)
    EditText paceMinutsET;
    @Bind(R.id.paceSecondET)
    EditText paceSecondET;
    @Bind(R.id.pacePaceTV)
    TextView pacePaceTV;
    @Bind(R.id.paceSpeedTV)
    TextView paceSpeedTV;
    @Bind(R.id.paceCalcBtn)
    Button paceCalcBtn;
    @Bind(R.id.timeMinET)
    EditText timeMinET;
    @Bind(R.id.timeSecET)
    EditText timeSecET;
    @Bind(R.id.timeKmET)
    EditText timeKmET;
    @Bind(R.id.timeMetersET)
    EditText timeMetersET;
    @Bind(R.id.timeTimeET)
    TextView timeTimeET;
    @Bind(R.id.timeCalcBtn)
    Button timeCalcBtn;
    @Bind(R.id.distancePaceMinTV)
    EditText distancePaceMinTV;
    @Bind(R.id.distancePaceSecTV)
    EditText distancePaceSecTV;
    @Bind(R.id.distanceHourTV)
    EditText distanceHourTV;
    @Bind(R.id.distanceMinutsTV)
    EditText distanceMinutsTV;
    @Bind(R.id.distanceSecTV)
    EditText distanceSecTV;
    @Bind(R.id.distanceDistaceTV)
    TextView distanceDistaceTV;
    @Bind(R.id.distanceCalcBtn)
    Button distanceCalcBtn;
    @Bind(R.id.conversionCalcTV)
    TextView conversionCalcTV;
    @Bind(R.id.conversionPaceRadioBtn)
    RadioButton conversionPaceRadioBtn;
    @Bind(R.id.conversionSpeedRadioBtn)
    RadioButton conversionSpeedRadioBtn;
    @Bind(R.id.conversionRadioGroup)
    RadioGroup conversionRadioGroup;
    @Bind(R.id.conversionMinET)
    EditText conversionMinET;
    @Bind(R.id.conversionSecET)
    EditText conversionSecET;
    @Bind(R.id.conversionKmET)
    EditText conversionKmET;
    @Bind(R.id.conversionCalcBtn)
    Button conversionCalcBtn;
    @Bind(R.id.lapKmET)
    EditText lapKmET;
    @Bind(R.id.lapMetersET)
    EditText lapMetersET;
    @Bind(R.id.lapHoursET)
    EditText lapHoursET;
    @Bind(R.id.lapMinutsET)
    EditText lapMinutsET;
    @Bind(R.id.lapSecET)
    EditText lapSecET;
    @Bind(R.id.lapCalcBtn)
    Button lapCalcBtn;
    @Bind(R.id.lapCalcKmTV)
    TextView lapCalcKmTV;
    @Bind(R.id.lapCalcLapTimeTV)
    TextView lapCalcLapTimeTV;
    @Bind(R.id.bmiHeightET)
    EditText bmiHeightET;
    @Bind(R.id.bmiWeightET)
    EditText bmiWeightET;
    @Bind(R.id.bmiBMITV)
    TextView bmiBMITV;
    @Bind(R.id.bmiCalcBtn)
    Button bmiCalcBtn;
    @Bind(R.id.kcalKmET)
    EditText kcalKmET;
    @Bind(R.id.kcalMetersET)
    EditText kcalMetersET;
    @Bind(R.id.kcalHourET)
    EditText kcalHourET;
    @Bind(R.id.kcalMinutsET)
    EditText kcalMinutsET;
    @Bind(R.id.kcalSecET)
    EditText kcalSecET;
    @Bind(R.id.kcalSlopeET)
    EditText kcalSlopeET;
    @Bind(R.id.kcalBurntTV)
    TextView kcalBurntTV;
    @Bind(R.id.kcalCalcBtn)
    Button kcalCalcBtn;
    @Bind(R.id.kcalWeightET)
    EditText kcalWeightET;
    @Bind(R.id.pulseMaleRadioBtn)
    RadioButton pulseMaleRadioBtn;
    @Bind(R.id.pulcaFemaleRadioBtn)
    RadioButton pulcaFemaleRadioBtn;
    @Bind(R.id.pulseRadioGroup)
    RadioGroup pulseRadioGroup;
    @Bind(R.id.pulseAgeET)
    EditText pulseAgeET;
    @Bind(R.id.pulseWeightET)
    EditText pulseWeightET;
    @Bind(R.id.pulseHRTV)
    TextView pulseHRTV;
    @Bind(R.id.pulseCalcBtn)
    Button pulseCalcBtn;

    public FragmentCalculators() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculators, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));


        return view;
    }

    @OnClick(R.id.paceCalcBtn)
    public void calcPace() {

        float kilometers = 0, hours = 0;
        float speed, pace;
        int paceMin, paceSec;

        if ((!TextUtils.isEmpty(paceKmET.getText()) || !TextUtils.isEmpty(paceMetersET.getText())) && (!TextUtils.isEmpty(paceHoursET.getText()) || !TextUtils.isEmpty(paceMinutsET.getText()) || !TextUtils.isEmpty(paceSecondET.getText()))) {

            if (!TextUtils.isEmpty(paceKmET.getText()))
                kilometers += Float.valueOf(paceKmET.getText().toString());


            if (!TextUtils.isEmpty(paceMetersET.getText()))
                kilometers += Float.valueOf(paceMetersET.getText().toString()) / 1000;


            if (!TextUtils.isEmpty(paceHoursET.getText()))
                hours += Float.valueOf(paceHoursET.getText().toString());


            if (!TextUtils.isEmpty(paceMinutsET.getText()))
                hours += Float.valueOf(paceMinutsET.getText().toString()) / 60;


            if (!TextUtils.isEmpty(paceSecondET.getText()))
                hours += Float.valueOf(paceSecondET.getText().toString()) / 3600;


            speed = kilometers / hours;
            pace = (float) ((hours * 3600000) / (kilometers * 1000)) / 60;

            paceMin = (int) pace;
            paceSec = (int) ((pace - paceMin) * 60);

            String paceStr = getResources().getString(R.string.pace);
            String speedStr = getResources().getString(R.string.speed);

            pacePaceTV.setText(String.valueOf(paceStr + " " + String.format("%02d", paceMin) + ":" + String.format("%02d", paceSec) + " min/km"));
            paceSpeedTV.setText(speedStr + " " + String.format("%.2f", speed) + " km/h");

        }

    }


    @OnClick(R.id.timeCalcBtn)
    public void calcTime() {

        float second = 0, kilometers = 0, time;
        int timeHours, timeMin, timeSec;


        if ((!TextUtils.isEmpty(timeMinET.getText()) || !TextUtils.isEmpty(timeSecET.getText())) && (!TextUtils.isEmpty(timeKmET.getText()) || !TextUtils.isEmpty(timeMetersET.getText()))) {
            if (!TextUtils.isEmpty(timeMinET.getText()))
                second += Float.valueOf(timeMinET.getText().toString()) * 60;

            if (!TextUtils.isEmpty(timeSecET.getText()))
                second += Float.valueOf(timeSecET.getText().toString());

            if (!TextUtils.isEmpty(timeKmET.getText()))
                kilometers += Float.valueOf(timeKmET.getText().toString());

            if (!TextUtils.isEmpty(timeMetersET.getText()))
                kilometers += Float.valueOf(timeMetersET.getText().toString()) / 1000;


            time = kilometers * second;

            timeHours = (int) time / 3600;
            time = time - timeHours * 3600;


            timeMin = (int) time / 60;
            time = time - timeMin * 60;


            timeSec = (int) time;

            String timeStr = getResources().getString(R.string.time);

            timeTimeET.setText(timeStr + " " + String.format("%02d", timeHours) + " h " + String.format("%02d", timeMin) + " m " + String.format("%02d", timeSec) + " s ");

        }


    }

    @OnClick(R.id.distanceCalcBtn)
    public void calcDistance() {

        float paceSec = 0, timeHour = 0;

        if ((!TextUtils.isEmpty(distancePaceMinTV.getText()) || !TextUtils.isEmpty(distancePaceSecTV.getText())) && (!TextUtils.isEmpty(distanceHourTV.getText()) || !TextUtils.isEmpty(distanceMinutsTV.getText()) || !TextUtils.isEmpty(distanceSecTV.getText()))) {
            if (!TextUtils.isEmpty(distancePaceMinTV.getText()))
                paceSec += Float.valueOf(distancePaceMinTV.getText().toString()) * 60;

            if (!TextUtils.isEmpty(distancePaceSecTV.getText()))
                paceSec += Float.valueOf(distancePaceSecTV.getText().toString());

            if (!TextUtils.isEmpty(distanceHourTV.getText()))
                timeHour += Float.valueOf(distanceHourTV.getText().toString());

            if (!TextUtils.isEmpty(distanceMinutsTV.getText()))
                timeHour += Float.valueOf(distanceMinutsTV.getText().toString()) / 60;

            if (!TextUtils.isEmpty(distanceSecTV.getText()))
                timeHour += Float.valueOf(distanceSecTV.getText().toString()) / 3600;

            float speed = 3600 / paceSec;
            float distance = speed * timeHour;

            int distanceKm = (int) distance;
            distance = distance - distanceKm;

            int distanceMeters = (int) (distance * 1000);


            String distanceStr = getResources().getString(R.string.distance);

            distanceDistaceTV.setText(distanceStr + " " + String.format("%02d", distanceKm) + " km " + String.format("%02d", distanceMeters) + " m");

        }

    }

    @OnClick(R.id.conversionPaceRadioBtn)
    public void paceToSpeedChange() {
        conversionKmET.setEnabled(false);

        conversionMinET.setEnabled(true);
        conversionSecET.setEnabled(true);

    }

    @OnClick(R.id.conversionSpeedRadioBtn)
    public void speedToPaceChange() {
        conversionKmET.setEnabled(true);

        conversionMinET.setEnabled(false);
        conversionSecET.setEnabled(false);
    }

    @OnClick(R.id.conversionCalcBtn)
    public void conversion() {
        if (conversionPaceRadioBtn.isChecked()) {
            if (!TextUtils.isEmpty(conversionMinET.getText()) || !TextUtils.isEmpty(conversionSecET.getText())) {
                int sec = 0;
                float speed;

                if (!TextUtils.isEmpty(conversionMinET.getText()))
                    sec += Integer.valueOf(conversionMinET.getText().toString()) * 60;

                if (!TextUtils.isEmpty(conversionSecET.getText()))
                    sec += Integer.valueOf(conversionSecET.getText().toString());

                speed = (float) 3600 / sec;

                conversionKmET.setText(String.format("%.3f", speed));

            }

        } else {

            if (!TextUtils.isEmpty(conversionKmET.getText())) {
                float speed;
                int paceMin, paceSec;

                speed = Float.valueOf(conversionKmET.getText().toString());

                paceSec = (int) (3600 / speed);
                paceMin = paceSec / 60;

                paceSec = paceSec - paceMin * 60;

                conversionMinET.setText(String.format("%02d", paceMin));
                conversionSecET.setText(String.format("%02d", paceSec));

            }
        }
    }


    @OnClick(R.id.lapCalcBtn)
    public void lapTimeCalc() {
        if ((!TextUtils.isEmpty(lapKmET.getText()) || !TextUtils.isEmpty(lapMetersET.getText())) && (!TextUtils.isEmpty(lapHoursET.getText()) || !TextUtils.isEmpty(lapMinutsET.getText()) || !TextUtils.isEmpty(lapSecET.getText()))) {

            int second = 0, meters = 0;
            int goalHours = 0, goalMinutes = 0, goalSeconds = 0;

            if (!TextUtils.isEmpty(lapKmET.getText()))
                meters += Integer.valueOf(lapKmET.getText().toString()) * 1000;

            if (!TextUtils.isEmpty(lapMetersET.getText()))
                meters += Integer.valueOf(lapMetersET.getText().toString());

            if (!TextUtils.isEmpty(lapHoursET.getText())) {
                second += Integer.valueOf(lapHoursET.getText().toString()) * 3600;
                goalHours = Integer.valueOf(lapHoursET.getText().toString());
            }

            if (!TextUtils.isEmpty(lapMinutsET.getText())) {
                second += Integer.valueOf(lapMinutsET.getText().toString()) * 60;
                goalMinutes = Integer.valueOf(lapMinutsET.getText().toString());
            }

            if (!TextUtils.isEmpty(lapSecET.getText())) {
                second += Integer.valueOf(lapSecET.getText().toString());
                goalSeconds = Integer.valueOf(lapSecET.getText().toString());
            }


            double oneMeter = (double) second / meters;

            int fullKm = (int) meters / 1000;
            int rest = (int) meters - fullKm * 1000;

            String kmStr = getResources().getString(R.string.kilometer);
            String lapTimeStr = getResources().getString(R.string.lapTime);

            lapCalcKmTV.setText(kmStr);
            lapCalcLapTimeTV.setText(lapTimeStr);

            int lapTimeHours, lapTimeMinutes, lapTimeSeconds;


            for (int i = 1; i <= fullKm; i++) {
                lapCalcKmTV.setText(lapCalcKmTV.getText() + "\n" + String.valueOf(i));

                lapTimeSeconds = (int) (1000 * i * oneMeter);

                lapTimeHours = (int) lapTimeSeconds / 3600;
                lapTimeSeconds = lapTimeSeconds - lapTimeHours * 3600;

                lapTimeMinutes = (int) lapTimeSeconds / 60;
                lapTimeSeconds = lapTimeSeconds - lapTimeMinutes * 60;


                lapCalcLapTimeTV.setText(lapCalcLapTimeTV.getText() + "\n" + String.format("%02d", lapTimeHours) + ":" + String.format("%02d", lapTimeMinutes) + ":" + String.format("%02d", lapTimeSeconds));

            }

            if (rest > 0) {
                float endDistance = (float) meters / 1000;
                lapCalcKmTV.setText(lapCalcKmTV.getText() + "\n" + (String.format("%.3f", endDistance)));
                lapCalcLapTimeTV.setText(lapCalcLapTimeTV.getText() + "\n" + String.format("%02d", goalHours) + ":" + String.format("%02d", goalMinutes) + ":" + String.format("%02d", goalSeconds));

            }


        }
    }

    @OnClick(R.id.bmiCalcBtn)
    public void bmiCalc() {
        if (!TextUtils.isEmpty(bmiHeightET.getText()) && !TextUtils.isEmpty(bmiWeightET.getText())) {

            float height = Float.valueOf(bmiHeightET.getText().toString());
            height = height / 100;

            float weight = Float.valueOf(bmiWeightET.getText().toString());

            float bmi = weight / (height * height);

            String bmiStr = getResources().getString(R.string.bmi);

            bmiBMITV.setText(bmiStr + " " + String.format("%.2f", bmi));

        }
    }

    @OnClick(R.id.kcalCalcBtn)
    public void kcalCalc() {
        if ((!TextUtils.isEmpty(kcalKmET.getText()) || !TextUtils.isEmpty(kcalMetersET.getText()))
                && (!TextUtils.isEmpty(kcalHourET.getText()) || !TextUtils.isEmpty(kcalMinutsET.getText()) || !TextUtils.isEmpty(kcalSecET.getText()))
                && (!TextUtils.isEmpty(kcalSlopeET.getText()))
                && (!TextUtils.isEmpty(kcalWeightET.getText())))

        {

            float kilometers = 0, hours = 0;
            float speed, avgSlope;


            if (!TextUtils.isEmpty(kcalKmET.getText()))
                kilometers += Float.valueOf(kcalKmET.getText().toString());


            if (!TextUtils.isEmpty(kcalMetersET.getText()))
                kilometers += Float.valueOf(kcalMetersET.getText().toString()) / 1000;


            if (!TextUtils.isEmpty(kcalHourET.getText()))
                hours += Float.valueOf(kcalHourET.getText().toString());


            if (!TextUtils.isEmpty(kcalMinutsET.getText()))
                hours += Float.valueOf(kcalMinutsET.getText().toString()) / 60;


            if (!TextUtils.isEmpty(kcalSecET.getText()))
                hours += Float.valueOf(kcalSecET.getText().toString()) / 3600;


            avgSlope = Float.valueOf(kcalSlopeET.getText().toString());
            avgSlope = (avgSlope / 100);

            float weight = Float.valueOf(kcalWeightET.getText().toString());


            speed = kilometers / hours;

            float km_hTOmeter_min = (float) (speed / 0.06);

            double calories = 3.5 + (km_hTOmeter_min * 0.2) + (km_hTOmeter_min * 0.9 * avgSlope);
            calories = (calories / 3.5) * weight;

            int caloriesShow = (int) calories;


            String kcalStr = getResources().getString(R.string.burntKcal);

            kcalBurntTV.setText(kcalStr + " " + String.valueOf(caloriesShow));


        }
    }

    @OnClick(R.id.pulseCalcBtn)
    public void pulseCalc() {
        if (!TextUtils.isEmpty(pulseAgeET.getText()) && !TextUtils.isEmpty(pulseWeightET.getText()))
        {
            float age = Float.valueOf(pulseAgeET.getText().toString());
            float weight = Float.valueOf(pulseWeightET.getText().toString());
            double hrMax = 210 - (0.5 * age) - (0.022 * weight);

            if(pulseMaleRadioBtn.isChecked())
                hrMax = hrMax + 4;


            String pulseStr = getResources().getString(R.string.maxHR);
            pulseHRTV.setText(pulseStr + " " + String.format("%.2f", hrMax));

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
