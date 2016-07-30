package codepath.articlesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
        final Article chosenArticle = getIntent().getParcelableExtra(selectedArticle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareArticle(chosenArticle);
            }
        });

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

    private void shareArticle(Article article) {
        Intent shareContent = new Intent(Intent.ACTION_SEND);
        shareContent.setType("text/plain");
        shareContent.putExtra(Intent.EXTRA_SUBJECT, article.getHeadline());
        shareContent.putExtra(Intent.EXTRA_TEXT, article.getWebURL());
        startActivity(Intent.createChooser(shareContent, "Share Article URL"));
    }


}
