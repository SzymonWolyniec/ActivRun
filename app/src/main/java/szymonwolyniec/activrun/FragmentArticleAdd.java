package szymonwolyniec.activrun;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentArticleAdd extends Fragment {


    @Bind(R.id.articleAddTitleET)
    EditText articleAddTitleET;
    @Bind(R.id.articleAddTextET)
    EditText articleAddTextET;
    @Bind(R.id.articleAddAuthorET)
    EditText articleAddAuthorET;
    @Bind(R.id.articleAddDateTV)
    TextView articleAddDateTV;
    @Bind(R.id.articleAdminAddSave)
    Button articleAdminAddSave;
    @Bind(R.id.articleAdminAddBack)
    Button articleAdminAddBack;


    public FragmentArticleAdd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_add, container, false);
        ButterKnife.bind(this, view);
        Locale.setDefault(new Locale("en", "US"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String articleDate = sdf.format(new Date());

        articleAddDateTV.setText(articleDate);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.articleAdminAddSave)
    public void saveArticle() {

        if ((TextUtils.isEmpty(articleAddTitleET.getText())))
            Toast.makeText(getActivity(), getResources().getString(R.string.emptyTitleArticle),Toast.LENGTH_SHORT).show();
        else if ((TextUtils.isEmpty(articleAddTextET.getText())))
            Toast.makeText(getActivity(), getResources().getString(R.string.emptyText),Toast.LENGTH_SHORT).show();
        else if ((TextUtils.isEmpty(articleAddAuthorET.getText())))
            Toast.makeText(getActivity(), getResources().getString(R.string.emptyAuthor),Toast.LENGTH_SHORT).show();
        else
        {
            DataBaseMain bd = new DataBaseMain(getActivity());

            String title = articleAddTitleET.getText().toString();
            String text = articleAddTextET.getText().toString();
            String author = articleAddAuthorET.getText().toString();
            String date = articleAddDateTV.getText().toString();

            bd.addArticle(title, text, author, date);

            backFromAddArticle();
        }
    }

    @OnClick(R.id.articleAdminAddBack)
    public void backFromAddArticle() {

        FragmentArticle fragmentArticle = new FragmentArticle();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerLayout, fragmentArticle);

        fragmentTransaction.commit();
    }

}
