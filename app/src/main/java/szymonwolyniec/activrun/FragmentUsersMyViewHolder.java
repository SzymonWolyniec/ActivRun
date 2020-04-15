package szymonwolyniec.activrun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class FragmentUsersMyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private TextView nickTextView;
    private TextView nameAndSurnameTextView;


    public FragmentUsersMyViewHolder(final View itemView)
    {
        super(itemView);
        nickTextView = (TextView) itemView.findViewById(R.id.nickSimpleItem);
        nameAndSurnameTextView = (TextView) itemView.findViewById(R.id.nameAndSurnameSimpleItem);
        itemView.setOnClickListener(this);
    }

    public void bindData(final FragmentUsersMyViewModel viewModel)
    {

        nickTextView.setText(viewModel.getNickText());
        nameAndSurnameTextView.setText(viewModel.getNameAndSurnameText());

    }


    @Override
    public void onClick(View v)
    {
        AppCompatActivity activity = (MainActivity) v.getContext();
        Fragment myFragment = new FragmentUsersEdit();

        Bundle bundle = new Bundle();
        int positionInListUser = getAdapterPosition();
        bundle.putInt("positionInListUser", positionInListUser);
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).addToBackStack(null).commit();

    }
}