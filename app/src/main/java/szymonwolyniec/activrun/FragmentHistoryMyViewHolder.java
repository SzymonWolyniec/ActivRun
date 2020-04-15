package szymonwolyniec.activrun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FragmentHistoryMyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private TextView dateTextView;
    private TextView distanceTextView;
    private TextView timeTextView;
    private TextView avgSpeedTextView;
    private ImageView imageView;



    public FragmentHistoryMyViewHolder(final View itemView)
    {
        super(itemView);
        dateTextView = (TextView) itemView.findViewById(R.id.dateSimpleItem);
        distanceTextView = (TextView) itemView.findViewById(R.id.distanceSimpleItem);
        timeTextView = (TextView) itemView.findViewById(R.id.timeSimpleItem);
        avgSpeedTextView = (TextView) itemView.findViewById(R.id.avgSpeedSimpleItem);
        imageView = itemView.findViewById(R.id.imageView);
        itemView.setOnClickListener(this);


    }

    public void bindData(final FragmentHistoryMyViewModel viewModel)
    {

        dateTextView.setText(viewModel.getDateText());
        distanceTextView.setText(viewModel.getDistanceText());
        timeTextView.setText(viewModel.getTimeText());
        avgSpeedTextView.setText(viewModel.getAvgSpeedText());


       Picasso.with(imageView.getContext())
                .load(viewModel.getGoogleStaticMap())
                .placeholder(R.drawable.placeholder_picasso)
                .error(R.drawable.error_picasso)
                .into(imageView);

    }


    @Override
    public void onClick(View v)
    {
        AppCompatActivity activity = (MainActivity) v.getContext();
        Fragment myFragment = new FragmentHistoryMap();

        Bundle bundle = new Bundle();
        int positionInList = getAdapterPosition();
        bundle.putInt("positionInList", positionInList);
        myFragment.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).addToBackStack(null).commit();


    }
}
