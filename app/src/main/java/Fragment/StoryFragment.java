package Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapter.StoryAdapter;
import Model.Article;
import codepath.articlesearch.R;


public class StoryFragment extends Fragment {

    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView storyRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_story, container, false);
        ArrayList<Article> articles = getArguments().getParcelableArrayList("articles");

        StoryAdapter adapter = new StoryAdapter(articles);
        storyRecycler.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        storyRecycler.setLayoutManager(gridLayoutManager);

        return storyRecycler;
    }
    
}
