package codepath.articlesearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Model.Article;

public class ArticleActivity extends AppCompatActivity {

    public static final String selectedArticle = "selectedArticle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Article chosenArticle = getIntent().getParcelableExtra(selectedArticle);
    }
}
