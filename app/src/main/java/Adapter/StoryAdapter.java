package Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Article;
import codepath.articlesearch.R;

/**
 * Created by andrj148 on 7/31/16.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private ArrayList<Article> articles;
    private Listener listner;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public static interface Listener {
        public void onClick(Article selectedArticle);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_story, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Article givenArticle = articles.get(position);
        CardView cardView = holder.cardView;

        ImageView imageView = (ImageView) cardView.findViewById(R.id.ivStoryImage);
        imageView.setImageResource(0);
        String thumbnail = givenArticle.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(cardView.getContext()).load(thumbnail).into(imageView);
        }

        TextView textView = (TextView) cardView.findViewById(R.id.tvStoryHeadline);
        String headline = givenArticle.getHeadline() + System.getProperty("line.separator") + givenArticle.getDate();
        textView.setText(headline);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listner != null) {
                    listner.onClick(givenArticle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public StoryAdapter(ArrayList<Article> articleSearchResults) {
        this.articles = articleSearchResults;
    }

    public void setListner(Listener classListener) {
        this.listner = classListener;
    }
}
