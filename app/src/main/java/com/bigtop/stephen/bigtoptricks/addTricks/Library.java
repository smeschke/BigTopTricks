package com.bigtop.stephen.bigtoptricks.addTricks;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bigtop.stephen.bigtoptricks.R;
import com.bigtop.stephen.bigtoptricks.Trick;
import com.bigtop.stephen.bigtoptricks.data.Actions;
import com.bigtop.stephen.bigtoptricks.data.Contract;

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

import static com.bigtop.stephen.bigtoptricks.Training.ARG_TRICK_OBJECT;

public class Library extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MyLibraryAdapter.mAdapterOnClickHandler {

    // Initialize an adapter and recyclerView
    private MyLibraryAdapter mSiteswapListAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mJsonHasBeenRead;
    public String PREFS_KEY = "prefs";
    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mJsonHasBeenRead = getApplication().getSharedPreferences(PREFS_KEY, 0).getBoolean("lib_read", false);

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

        if (mJsonHasBeenRead) {
            // Initialize loader
            int LOADER_ID = 42;
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        } else {
            // Start a new FetchTricksFromJson task. This fetches data from the internet and displays it on the list
            new FetchTricksFromJson().execute(url);
        }
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
            mCursor.moveToPosition(position - 1);

            String name = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
            String capacity = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_CAPACITY));
            String difficulty = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_DIFFICULTY));
            String source = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_SOURCE));
            String siteswap = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_SITESWAP));
            String tutorial = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TUTORIAL));
            String animation = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.Column_ANIMATION));
            String description = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TRICK_DESCRIPTION));

            Trick trick = new Trick();
            trick.setName(name);
            trick.setCapacity(capacity);
            trick.setDifficulty(difficulty);
            trick.setSource(source);
            trick.setSiteswap(siteswap);
            trick.setAnimation(animation);
            trick.setTutorial(tutorial);
            trick.setDescription(description);
            toTrickDiscovery.putExtra(ARG_TRICK_OBJECT, trick);

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
            try {
                // Read the tricks from the JSON string and put them into the db
                for (int i = 0; i < JsonUtils.getNumberOfTricks(trick_data); i++) {

                    Trick trick = JsonUtils.parseIndividualTrickToObject(trick_data, i);

                    Actions.insert_trick(getApplicationContext(), "0", "0", trick.getDescription(),
                            trick.getName(), "library", "0", "0", "0", "0", "0",
                            trick.getSiteswap(), trick.getAnimation(), trick.getSource(), trick.getDifficulty(),
                            trick.getCapacity(), trick.getTutorial());
                }

                // Tell the application that the db has been read
                getApplicationContext().getSharedPreferences(PREFS_KEY, 0).edit().putBoolean("lib_read", true).commit();

            } catch (Exception e) {
                Log.d("LOG", "myLogs end onPostExecute Library json exception");
            }

            // Recreate the activity to use the cursor loader now that mJsonRead is true
            recreate();
        }
    }

    ///////////////////////////////////START CURSOR LOADER METHODS /////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                Contract.listEntry.CONTENT_URI,
                null,
                Contract.listEntry.COLUMN_IS_META + "=?",
                new String[]{"library"},
                Contract.listEntry.COLUMN_TIMESTAMP);
    }

    // When loading is finished, swap in the new data
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mSiteswapListAdapter.swapCursor(data);
        Log.d("LOG", "myLogs loader onLoadFinished" + " " + data.getCount());
    }

    // Reset the loader
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d("LOG", "myLogs loader reset");
    }
    /////////////////////////////////// END CURSOR LOADER METHODS //////////////////////////////////
}


