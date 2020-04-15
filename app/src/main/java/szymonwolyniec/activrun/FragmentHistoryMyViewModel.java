package szymonwolyniec.activrun;

import android.support.annotation.NonNull;

public class FragmentHistoryMyViewModel
{

    private String dateText;
    private String distanceText;
    private String timeText;
    private String avgSpeedText;
    private String googleStaticMap;


    public FragmentHistoryMyViewModel(@NonNull final String dateText, @NonNull final String distanceText, @NonNull final String timeText, @NonNull final String avgSpeedText, @NonNull final String googleStaticMap)
    {
        setSimpleText(dateText, distanceText, timeText, avgSpeedText, googleStaticMap);
    }


    @NonNull
    public String getDateText()
    {
        return dateText;
    }

    @NonNull
    public String getDistanceText()
    {
        return distanceText;
    }

    @NonNull
    public String getTimeText()
    {
        return timeText;
    }

    @NonNull
    public String getAvgSpeedText()
    {
        return avgSpeedText;
    }

    @NonNull
    public String getGoogleStaticMap()
    {
        return googleStaticMap;
    }


    public void setSimpleText(@NonNull final String dateText, @NonNull final String distanceText, @NonNull final String timeText, @NonNull final String avgSpeedText, @NonNull final String googleStaticMap)
    {
        this.dateText = dateText;
        this.distanceText = distanceText;
        this.timeText = timeText;
        this.avgSpeedText = avgSpeedText;
        this.googleStaticMap = googleStaticMap;


    }

}
