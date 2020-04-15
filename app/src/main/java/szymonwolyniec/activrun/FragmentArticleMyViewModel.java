package szymonwolyniec.activrun;

import android.support.annotation.NonNull;

public class FragmentArticleMyViewModel {

    private String title;
    private String author;



    public FragmentArticleMyViewModel(@NonNull final String title, @NonNull final String author)
    {
        setSimpleText(title, author);
    }


    @NonNull
    public String getTitle()
    {
        return title;
    }

    @NonNull
    public String getAuthor()
    {
        return author;
    }

    public void setSimpleText(@NonNull final String title, @NonNull final String author)
    {
        this.title = title;
        this.author = author;
    }

}