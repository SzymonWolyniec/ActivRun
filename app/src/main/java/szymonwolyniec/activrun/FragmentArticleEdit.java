package szymonwolyniec.activrun;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentArticleEdit extends Fragment {


    @Bind(R.id.articleEditTitle)
    EditText articleEditTitle;
    @Bind(R.id.articleEditText)
    EditText articleEditText;
    @Bind(R.id.articleEditAuthor)
    EditText articleEditAuthor;
    @Bind(R.id.articleEditDate)
    TextView articleEditDate;
    @Bind(R.id.articleEditSaveBtn)
    Button articleEditSaveBtn;
    @Bind(R.id.articleEditBackBtn)
    Button articleEditBackBtn;

    View view;
    int positionInListArticle, nr;
    String title, text, author, date;
    @Bind(R.id.articleEditDeleteBtn)
    Button articleEditDeleteBtn;

    public FragmentArticleEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        positionInListArticle = this.getArguments().getInt("positionInListArticle");

        view = inflater.inflate(R.layout.fragment_article_edit, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveAllArticleDesc();


        if (kurs.getCount() != 0) {
            kurs.moveToPosition(positionInListArticle);
            nr = kurs.getInt(0);
            title = kurs.getString(1);
            text = kurs.getString(2);
            author = kurs.getString(3);
            date = kurs.getString(4);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            articleEditTitle.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            articleEditText.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            articleEditAuthor.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        }

        articleEditTitle.setText(title);
        articleEditText.setText(text);
        articleEditAuthor.setText(author);
        articleEditDate.setText(date);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.articleEditSaveBtn)
    public void saveEditArticle() {

        title = articleEditTitle.getText().toString();
        text = articleEditText.getText().toString();
        author = articleEditAuthor.getText().toString();

        DataBaseMain bd = new DataBaseMain(getActivity());
        bd.editArticle(nr, title, text, author, date);

        backToArticleList();
    }

    @OnClick(R.id.articleEditBackBtn)
    public void backToArticleList() {

        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.articleEditDeleteBtn)
    public void deleteArticle()
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


                        DataBaseMain bd = new DataBaseMain(getActivity());
                        bd.deleteArticle(Integer.valueOf(nr));

                        AppCompatActivity activity = (MainActivity) view.getContext();
                        Fragment myFragment = new FragmentArticle();

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, myFragment).commit();

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
}
