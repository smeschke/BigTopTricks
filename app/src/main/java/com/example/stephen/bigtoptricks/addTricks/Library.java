package com.example.stephen.bigtoptricks.addTricks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.stephen.bigtoptricks.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static com.example.stephen.bigtoptricks.Training.ARG_TRICK_OBJECT;

public class Library extends AppCompatActivity
        implements MyLibraryAdapter.mAdapterOnClickHandler {

    // Initialize an adapter and recyclerView
    private MyLibraryAdapter mSiteswapListAdapter;
    private String mJsonString;
    private LinearLayoutManager mLayoutManager;
    private int mScrollPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // https://stackoverflow.com/questions/51318506/up-navigation-in-fragments-toolbar
        AppCompatActivity appCompatActivity = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
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
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.library_title));

        // Create url to list of tricks
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/smeschke/juggling/master/tricks.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Code for recycler view
        RecyclerView mList = findViewById(R.id.recycler_view_siteswap_list);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(mLayoutManager);
        mList.setHasFixedSize(true);
        mSiteswapListAdapter = new MyLibraryAdapter(this, this);
        mList.setAdapter(mSiteswapListAdapter);

        // Start a new FetchTricksFromJson task. This fetches data from the internet and displays it on the list
        new FetchTricksFromJson().execute(url);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        int scroll_position = mLayoutManager.findFirstVisibleItemPosition();
        savedInstanceState.putInt("position", scroll_position);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mScrollPosition = savedInstanceState.getInt("position");
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
                    JsonUtils.parseIndividualTrickToObject(mJsonString, position - 1));
            startActivity(toTrickDiscovery);
        }
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ END ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    ///////////////////////////////// START TRICKS DATA FETCH TASK /////////////////////////////////
    class FetchTricksFromJson extends AsyncTask<URL, Void, String>

    {
        // Do in background gets the json juggling tricks data from internet
        @Override
        protected String doInBackground(URL... urls) {
            Log.d("LOG", "myLogs do in background");
            String fetchResults = null;
            InputStream is = getResources().openRawResource(R.raw.tricks);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            fetchResults = writer.toString();

            // Return the results to the onPostExecute method
            return fetchResults;
        }

        // On post execute task
        @Override
        protected void onPostExecute(String trick_data) {
            mJsonString = trick_data;
            try {Log.d("LOG", "myLogs end onPostExecute Library start");
                mSiteswapListAdapter.swapCursor(trick_data);
                // Preserve scroll position
                mLayoutManager.scrollToPosition(mScrollPosition);
                Log.d("LOG", "myLogs end onPostExecute Library end");
            } catch (JSONException e) {
                Log.d("LOG", "myLogs end onPostExecute Library json exception");
            }
        }
    }
}


