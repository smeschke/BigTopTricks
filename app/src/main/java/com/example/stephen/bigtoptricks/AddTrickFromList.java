package com.example.stephen.bigtoptricks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddTrickFromList extends AppCompatActivity implements mSiteswapListAdapter.mAdapterOnClickHandler {

    //initialize variables for resources from strings.xml
    private ArrayList<String> pattern_list = new ArrayList<>();
    private String[] patterns;
    //initialize an adapter and recyclerView
    private mSiteswapListAdapter mSiteswapListAdapter;
    private RecyclerView mList;
    private String mJsonString;
    LinearLayoutManager mLayoutManager;
    ArrayList<String> mTrickNames;

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
        mSiteswapListAdapter = new mSiteswapListAdapter(this, this, pattern_list);
        mList.setAdapter(mSiteswapListAdapter);

        // Get access to the preferences
        Log.d("LOG", "asdf fetch tas");
        new fetch().execute(url);
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ START ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @Override
    public void onClick(final int position) {
        ArrayList<String> trick_details = JsonUtils.parseIndividualTrick(mJsonString, position);

        Intent toTrickDiscovery = new Intent(this, TrickDiscovery.class);
        //toAddTrick.putExtra("trickName", pattern_list.get(position));
        toTrickDiscovery.putStringArrayListExtra("details", trick_details);
        //toAddTrick.putExtra("trickDesc", "There is no description for this trick. This trick was part of the siteswap list.");
        startActivity(toTrickDiscovery);
        //Log.d("LOG", "asdf onClick Add trick from list " + trick_details.toString());
        //Log.d("LOG", "asdf onClick Add trick from list " + position );
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ END ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    ///////////////////////////////// START RECIPE DATA FETCH TASK /////////////////////////////////
    class fetch extends AsyncTask<URL, Void, String>

    {
        // Do in background gets the json recipe data from internet
        @Override
        protected String doInBackground(URL... urls) {
            String fetchResults = null;
            Log.d("LOG", "asdf onPostExecute do in background" );
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
            } catch (JSONException e) {
                Log.d("LOG", "asdf list did not parse correctly");
            }
            Log.d("LOG", "asdf onPostExecute" + trick_data.substring(0,1234));
            mSiteswapListAdapter.swapCursor(mTrickNames);
        }
    }
    ///////////////////////////////// END RECIPE DATA FETCH TASK ///////////////////////////////////
}
