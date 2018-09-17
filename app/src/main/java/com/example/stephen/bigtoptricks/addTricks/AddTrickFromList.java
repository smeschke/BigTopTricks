package com.example.stephen.bigtoptricks.addTricks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.stephen.bigtoptricks.R;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddTrickFromList extends AppCompatActivity implements com.example.stephen.bigtoptricks.addTricks.mSiteswapListAdapter.mAdapterOnClickHandler {

    //initialize variables for resources from strings.xml
    private ArrayList<String> pattern_list = new ArrayList<>();
    private ArrayList<String> capacity_list = new ArrayList<>();
    private ArrayList<String> difficulty_list = new ArrayList<>();
    private ArrayList<String> sources_list = new ArrayList<>();
    //initialize an adapter and recyclerView
    private mSiteswapListAdapter mSiteswapListAdapter;
    private RecyclerView mList;
    private String mJsonString;
    LinearLayoutManager mLayoutManager;
    ArrayList<String> mTrickNames;
    ArrayList<String> mTrickCapacities;
    ArrayList<String> mTrickDifficulties;
    ArrayList<String> mTrickSources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick_from_list);

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
        mSiteswapListAdapter = new mSiteswapListAdapter(this, this, pattern_list, capacity_list, difficulty_list, sources_list);
        mList.setAdapter(mSiteswapListAdapter);

        // Get access to the preferences
        //Log.d("LOG", "asdf fetch tas");
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
            ArrayList<String> trick_details = JsonUtils.parseIndividualTrick(mJsonString, position - 1);
            toTrickDiscovery.putStringArrayListExtra("details", trick_details);
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
            //Log.d("LOG", "asdf onPostExecute do in background" );
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
            // Iterate though the list of recipes and add them to the DB
            mJsonString = trick_data;
            try {
                mTrickNames = JsonUtils.getTrickNames(trick_data);
                mTrickCapacities = JsonUtils.getTrickCapicities(trick_data);
                mTrickDifficulties = JsonUtils.getTrickDifficulties(trick_data);
                mTrickSources = JsonUtils.getTrickSources(trick_data);
            } catch (JSONException e) {
                Log.d("LOG", "asdf list did not parse correctly");
            }
            //Log.d("LOG", "asdf onPostExecute" + trick_data.substring(0,1234));
            mSiteswapListAdapter.swapCursor(mTrickNames, mTrickCapacities, mTrickDifficulties, mTrickSources);
        }
    }
    ///////////////////////////////// END RECIPE DATA FETCH TASK ///////////////////////////////////
}
