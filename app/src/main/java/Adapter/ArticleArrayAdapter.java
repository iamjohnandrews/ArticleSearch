package Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import Model.Article;

/**
 * Created by andrj148 on 7/29/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);

    }
}
