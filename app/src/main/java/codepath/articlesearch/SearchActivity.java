package codepath.articlesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.StoryAdapter;
import Model.Article;
import Model.SearchCriteria;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    static final String ArticleSearchAPIkey = "b23e6d5a33524222962cfd893384d385";
    static final String nyTimesBaseURI = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    GridView gvResults;
    AsyncHttpClient client;
    String searchQuery;
    ArrayList<Article> articles;
    StoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = new AsyncHttpClient();
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.action_filter);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                customLoadMoreDataFromApi(0);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("DEBUG", "error occured on search query entry");
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            showAdvancedSearchDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void customLoadMoreDataFromApi(int pageNumber) {

        RequestParams params = new RequestParams();
        params.put("api-key", ArticleSearchAPIkey);
        params.put("page", pageNumber);
        params.put("q", searchQuery);

        SearchCriteria criteria = new SearchCriteria();
        criteria.query = searchQuery;

        /*
          'q': "tennis",
          'fq': "serena",
          'begin_date': "20150603",
          'sort': "oldest"
         */

        client.get(nyTimesBaseURI, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJSONresults = null;

                try {
                    articleJSONresults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJSONresults)); // making changes directly to adapter allows me to avoid method notifyDataSetChanged()
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void showAdvancedSearchDialog() {
        AdvancedSearchFragment advancedSearchFragment = new AdvancedSearchFragment();
        advancedSearchFragment.show(getFragmentManager(), "AdvancedSearchFragment");
    }


    private void setupViews() {
        articles = new ArrayList<>();
        adapter = new StoryAdapter(articles);
        adapter.setListner(new StoryAdapter.Listener() {
            @Override
            public void onClick(Article selectedArticle) {
                navigateToChosenArticle(selectedArticle);
            }
        });

        RecyclerView storyRecycler = (RecyclerView) findViewById(R.id.storyRecycleView);
        storyRecycler.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        storyRecycler.setLayoutManager(gridLayoutManager);
        storyRecycler.setHasFixedSize(true);
        storyRecycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
    }

    private void navigateToChosenArticle(Article chosenArticle) {
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra(StoryActivity.selectedArticle, chosenArticle);
        startActivity(intent);
    }

}
