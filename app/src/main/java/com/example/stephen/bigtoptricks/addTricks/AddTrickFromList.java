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
import android.util.Log;
import android.view.View;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Tricks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.stephen.bigtoptricks.TrickDetail.ARG_TRICK_OBJECT;

public class AddTrickFromList extends AppCompatActivity
        implements com.example.stephen.bigtoptricks.addTricks.mSiteswapListAdapter.mAdapterOnClickHandler {

    //initialize an adapter and recyclerView
    private mSiteswapListAdapter mSiteswapListAdapter;
    private RecyclerView mList;
    private String mJsonString;
    LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick_from_list);

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

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Library of Tricks");

        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/smeschke/juggling/master/tricks.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //code for recycler view
        mList = (RecyclerView) findViewById(R.id.recycler_view_siteswap_list);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(mLayoutManager);
        mList.setHasFixedSize(true);
        mSiteswapListAdapter = new mSiteswapListAdapter(this, this);
        mList.setAdapter(mSiteswapListAdapter);

        // Get access to the preferences
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
        // Do in background gets the json recipe data from internet
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
