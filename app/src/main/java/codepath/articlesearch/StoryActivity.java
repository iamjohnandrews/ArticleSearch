package codepath.articlesearch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Model.Article;

public class StoryActivity extends AppCompatActivity {
    public static final String selectedArticle = "selectedArticle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Article chosenArticle = getIntent().getParcelableExtra(selectedArticle);
        setTitle(chosenArticle.getHeadline());
        loadWebViewInsideActivity(chosenArticle);
    }

    private void loadWebViewInsideActivity(Article article) {
        WebView wvStory = (WebView) findViewById(R.id.wvArticle);
        wvStory.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvStory.loadUrl(article.getWebURL());
    }
}
