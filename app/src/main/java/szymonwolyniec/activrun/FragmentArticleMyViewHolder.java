package szymonwolyniec.activrun;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;



public class FragmentArticleMyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private TextView titleTextView;
    private TextView authorTextView;


    public FragmentArticleMyViewHolder(final View itemView)
    {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.articleTitle);
        authorTextView = (TextView) itemView.findViewById(R.id.articleAuthor);
        itemView.setOnClickListener(this);
    }

    public void bindData(final FragmentArticleMyViewModel viewModel)
    {

        titleTextView.setText(viewModel.getTitle());
        authorTextView.setText(viewModel.getAuthor());

    }

    @Override
    public void onClick(View v) {

        AppCompatActivity activityContext = (MainActivity) v.getContext();
        DataBaseMain bd = new DataBaseMain(activityContext);
        Cursor cursor = bd.giveUser();
        cursor.moveToFirst();
        String userLogin = cursor.getString(10);

        if(userLogin.equals("Admin"))
        {
            AppCompatActivity activity = (MainActivity) v.getContext();
            Fragment myFragment = new FragmentArticleEdit();

            Bundle bundle = new Bundle();
            int positionInListArticle = getAdapterPosition();
            bundle.putInt("positionInListArticle", positionInListArticle);
            myFragment.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).addToBackStack(null).commit();
        }
        else
        {
            AppCompatActivity activity = (MainActivity) v.getContext();
            Fragment myFragment = new FragmentArticleShow();

            Bundle bundle = new Bundle();
            int positionInListArticle = getAdapterPosition();
            bundle.putInt("positionInListArticle", positionInListArticle);
            myFragment.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).addToBackStack(null).commit();
        }


    }



}
