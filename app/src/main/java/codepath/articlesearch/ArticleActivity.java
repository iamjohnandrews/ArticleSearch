package codepath.articlesearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Model.Article;

public class ArticleActivity extends AppCompatActivity {

    public static final String selectedArticle = "selectedArticle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

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
