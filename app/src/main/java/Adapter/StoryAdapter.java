package Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import Model.Article;
import codepath.articlesearch.R;

/**
 * Created by andrj148 on 7/31/16.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private Article[] articles;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_story, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article givenArticle = articles[position];
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
    }

    @Override
    public int getItemCount() {
        return articles.length;
    }

    public StoryAdapter(Article[] articleSearchResults) {
        this.articles = articleSearchResults;
    }
}
