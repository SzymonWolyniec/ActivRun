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
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentArticle extends Fragment {


    @Bind(R.id.articleRecyclerView)
    RecyclerView articleRecyclerView;
    @Bind(R.id.articleAdminAdd)
    Button articleAdminAdd;

    View view;
    int positionInListArticle;

    public FragmentArticle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));


        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor cursor = bd.giveUser();
        cursor.moveToFirst();
        String userLogin = cursor.getString(10);



        final FragmentArticleMyAdapter adapter = new FragmentArticleMyAdapter(generateListAllArticle());
        RecyclerView recyclerView = (RecyclerView) articleRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        if(!userLogin.equals("Admin"))
        {
            articleAdminAdd.setVisibility(View.GONE);
        }
        else
        {
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


                // Nie używane przeciąganie
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {


                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                    positionInListArticle = viewHolder.getAdapterPosition();

                    deleteArticleDialog(positionInListArticle);


                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(articleRecyclerView);
        }




        return view;
    }


    public void deleteArticleDialog(final int positionInListArticle)
    {

        AlertDialog.Builder alertDialogDeleteWorkout = new AlertDialog.Builder(getActivity());

        alertDialogDeleteWorkout.setTitle(R.string.removeArticle);
        alertDialogDeleteWorkout.setMessage(R.string.removeArticleMessage);
        alertDialogDeleteWorkout.setIcon(R.drawable.ic_logo);


        alertDialogDeleteWorkout.setPositiveButton(getResources().getString(R.string.YES),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        String nr;
                        DataBaseMain bd = new DataBaseMain(getActivity());
                        Cursor kurs = bd.giveArticleID();

                        if (kurs.getCount() != 0)
                        {
                            kurs.moveToPosition(positionInListArticle);
                            nr = kurs.getString(0);
                            bd.deleteArticle(Integer.valueOf(nr));

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(FragmentArticle.this).attach(FragmentArticle.this).commit();
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
                        ft.detach(FragmentArticle.this).attach(FragmentArticle.this).commit();
                        dialog.cancel();
                    }
                });


        alertDialogDeleteWorkout.show();
    }



    private List<FragmentArticleMyViewModel> generateListAllArticle() {
        List<FragmentArticleMyViewModel> simpleViewModelList = new ArrayList<>();

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveAllArticle();

        if (kurs.getCount() != 0) {

            kurs.moveToLast();
            do {

                String articleTitle = kurs.getString(1);
                String articleAuthor = " ~ " + kurs.getString(3);

                simpleViewModelList.add(new FragmentArticleMyViewModel(articleTitle, articleAuthor));

            } while (kurs.moveToPrevious());
        }
        return simpleViewModelList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.articleAdminAdd)
    public void onViewClicked() {

        FragmentArticleAdd fragmentArticleAdd = new FragmentArticleAdd();


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerLayout, fragmentArticleAdd);

        fragmentTransaction.commit();
    }
}
