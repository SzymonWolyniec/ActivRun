package szymonwolyniec.activrun;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentArticleMyAdapter extends RecyclerView.Adapter
{
    private List<FragmentArticleMyViewModel> models = new ArrayList<>();

    public FragmentArticleMyAdapter(final List<FragmentArticleMyViewModel> viewModels)
    {
        if (viewModels != null)
        {
            this.models.addAll(viewModels);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new FragmentArticleMyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        ((FragmentArticleMyViewHolder) holder).bindData(models.get(position));
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    @Override
    public int getItemViewType(final int position)
    {
        return R.layout.fragment_article_simple_item;
    }
}
