package com.example.stephen.bigtoptricks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

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
        protected void onPostExecute(String recipes) {
            // Iterate though the list of recipes and add them to the DB
            Log.d("LOG", "asdf onPostExecute" + recipes.substring(0,1234));
        }
    }
    ///////////////////////////////// END RECIPE DATA FETCH TASK ///////////////////////////////////
