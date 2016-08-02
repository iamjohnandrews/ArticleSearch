package codepath.articlesearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Adapter.StoryAdapter;
import Model.Article;
import Model.SearchCriteria;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements AdvancedSearchFragment.AdvancedSearchListener {

    static final String ARTICLE_SEARCH_AP_IKEY = "b23e6d5a33524222962cfd893384d385";
    static final String NY_TIMES_BASE_URI = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    static final String SAVED_SEARCHCRITERIA = "savedSearchCriteria";
    GridView gvResults;
    AsyncHttpClient client;
    ArrayList<Article> articles;
    StoryAdapter adapter;
    SearchCriteria searchCriteria;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        client = new AsyncHttpClient();
        setupViews();
        searchCriteria = getSavedSearchCriteria();
        if (searchCriteria.query != null) {
            customLoadMoreDataFromApi(0, searchCriteria);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCriteria.query = query;
                customLoadMoreDataFromApi(0, searchCriteria);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCriteria.query = newText;
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

    @Override
    public void onCompletedUserInput(SearchCriteria criteria) {
        hideSoftKeyboard();
        if (criteria.query != null) {
            searchCriteria.query = criteria.query;
        }
        if (criteria.beginDate != null) {
            searchCriteria.beginDate = criteria.beginDate;
        }
        if (criteria.sort != null) {
            searchCriteria.sort = criteria.sort;
        }
        if (criteria.category != null) {
            searchCriteria.category = criteria.category;
        }
        articles.clear();
        customLoadMoreDataFromApi(0, criteria);
    }

    @Override
    protected void onStop() {
        super.onStop();
        persistSearchCriteria();
    }

    private void customLoadMoreDataFromApi(int pageNumber, SearchCriteria criteria) {

        RequestParams params = new RequestParams();
        params.put("api-key", ARTICLE_SEARCH_AP_IKEY);
        params.put("page", pageNumber);
        params.put("q", criteria.query);

        if (criteria.beginDate != null) {
            params.put("begin_date", criteria.beginDate);
        }

        if (criteria.sort != null) {
            params.put("sort", criteria.sort);
        }
        if (criteria.category != null) {
            params.put("fq", criteria.category);
        }

        client.get(NY_TIMES_BASE_URI, params, new JsonHttpResponseHandler() {
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

    private void hideSoftKeyboard(){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
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
                customLoadMoreDataFromApi(page, searchCriteria);
            }
        });
    }

    private void navigateToChosenArticle(Article chosenArticle) {
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra(StoryActivity.selectedArticle, chosenArticle);
        startActivity(intent);
    }

    private void persistSearchCriteria() {
        try {
            FileOutputStream outStream = openFileOutput(SAVED_SEARCHCRITERIA, MODE_PRIVATE);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeObject(searchCriteria);
            objectOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SearchCriteria getSavedSearchCriteria() {
        SearchCriteria criteria = new SearchCriteria();

        try {
            FileInputStream inStream = openFileInput(SAVED_SEARCHCRITERIA);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream
            );
            criteria = (SearchCriteria) objectInStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return criteria;
    }
}
