package com.example.stephen.bigtoptricks.addTricks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.stephen.bigtoptricks.R;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.stephen.bigtoptricks.Training.ARG_TRICK_OBJECT;

public class Library extends AppCompatActivity
        implements MyLibraryAdapter.mAdapterOnClickHandler {

    //initialize an adapter and recyclerView
    private MyLibraryAdapter mSiteswapListAdapter;
    private RecyclerView mList;
    private String mJsonString;
    LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // https://stackoverflow.com/questions/51318506/up-navigation-in-fragments-toolbar
        AppCompatActivity appCompatActivity = ((AppCompatActivity) this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.exo_controls_previous);
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        // Custom setting for the up navigation
        // Must do this to preserve scroll position in ArticleListActivity
        // https://stackoverflow.com/questions/30679133/override-up-button-in-action-bar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set title on collapsing toolbar
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.library_title));

        // Create url to list of tricks
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/smeschke/juggling/master/tricks.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Code for recycler view
        mList = (RecyclerView) findViewById(R.id.recycler_view_siteswap_list);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(mLayoutManager);
        mList.setHasFixedSize(true);
        mSiteswapListAdapter = new MyLibraryAdapter(this, this);
        mList.setAdapter(mSiteswapListAdapter);

        // Start a new fetch task. This fetches data from the internet and displays it on the list
        new fetch().execute(url);
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ START ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @Override
    public void onClick(final int position) {
        if (position == 0) {
            // User wants to enter a custom trick
            Intent toAddTrick = new Intent(this, AddTrick.class);
            startActivity(toAddTrick);
        } else {
            // User wants to look at the details for a trick in the library
            Intent toTrickDiscovery = new Intent(this, TrickDiscovery.class);
            // Parse details about trick from JSON, and put it in the intent
            toTrickDiscovery.putExtra(ARG_TRICK_OBJECT,
                    JsonUtils.parseIndividualTrickToOBject(mJsonString, position-1));
            startActivity(toTrickDiscovery);
        }
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ END ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    ///////////////////////////////// START RECIPE DATA FETCH TASK /////////////////////////////////
    class fetch extends AsyncTask<URL, Void, String>

    {
        // Do in background gets the json juggling tricks data from internet
        @Override
        protected String doInBackground(URL... urls) {
            String fetchResults = null;
            try {
                fetchResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Return the results to the onPostExecute method
            return fetchResults;
        }

        // On post execute task
        @Override
        protected void onPostExecute(String trick_data) {
            mJsonString = trick_data;
            try {
                mSiteswapListAdapter.swapCursor(trick_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    ///////////////////////////////// END RECIPE DATA FETCH TASK ///////////////////////////////////
}
