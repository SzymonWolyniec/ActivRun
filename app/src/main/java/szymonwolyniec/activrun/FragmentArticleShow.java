package szymonwolyniec.activrun;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentArticleShow extends Fragment {


    View view;
    int positionInListArticle;
    String title, text, author, date;
    @Bind(R.id.articleShowTitle)
    TextView articleShowTitle;
    @Bind(R.id.articleShowDate)
    TextView articleShowDate;
    @Bind(R.id.articleShowText)
    TextView articleShowText;
    @Bind(R.id.articleShowAuthor)
    TextView articleShowAuthor;
    @Bind(R.id.articleShowBackBtn)
    Button articleShowBackBtn;


    public FragmentArticleShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        positionInListArticle = this.getArguments().getInt("positionInListArticle");

        view = inflater.inflate(R.layout.fragment_article_show, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        DataBaseMain bd = new DataBaseMain(getActivity());
        Cursor kurs = bd.giveAllArticleDesc();


        if (kurs.getCount() != 0) {
            kurs.moveToPosition(positionInListArticle);
            title = kurs.getString(1);
            text = kurs.getString(2);
            author = kurs.getString(3);
            date = kurs.getString(4);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            articleShowText.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        articleShowTitle.setText(title);
        articleShowText.setText(text);
        articleShowAuthor.setText(" ~ " + author);
        articleShowDate.setText(date);


        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.articleShowBackBtn)
    public void backToArticlesList() {
        getFragmentManager().popBackStack();
    }
}
