package com.example.stephen.bigtoptricks;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.addTricks.TrickDiscovery;
import com.example.stephen.bigtoptricks.data.Actions;
import com.example.stephen.bigtoptricks.data.Contract;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class Training extends AppCompatActivity {

    public final static String mUnique = "unique_delimiter";
    public static final String ARG_TRICK_OBJECT = "trick_object";
    public final static String ARG_LIST_KEY = "list_key";
    public final static String ARG_SP_LOG_KEY = "log";
    private String mName;
    private String mPr;
    private String mGoal;
    private String mDescription;
    private String mTimeTrained;
    private String mId;
    private String mHits;
    private String mMisses;
    private String mPropType;
    private String mRecords;
    private String mCapacity;
    private String mSiteswap;
    private String mSource;
    private String mAnimation;
    private String mTutorial;
    private String mDifficulty;
    private String mCatchCount;
    private Tricks mTrick;
    private Chronometer mChronometer;
    private long mStartTime;
    private boolean mTraining;
    private Button mTrainingButton;
    private Button mHitButton;
    private Button mMissButton;
    private TextView mTrainingTimeTextView;
    private TextView mPrTextView;
    private GraphView mGraphView;
    private TextView mHitMissTextView;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private String mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Collecting juggling data is more important than battery life to tele-socialize after training
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Request permission for fine location
        ActivityCompat.requestPermissions(Training.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        // up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Construct clients: GeoDataClient, PlaceDetectionClient, FusedLocationProviderClient
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // This function updates the mLocation global variable
        get_place_and_log();

        // Get the trick that the user clicked on in the main activity from the intent
        mTrick = getIntent().getExtras().getParcelable(ARG_TRICK_OBJECT);
        mName = mTrick.getName();
        mRecords = mTrick.getRecord();
        mPropType = mTrick.getProp_type();
        mPr = mTrick.getPr();
        mMisses = mTrick.getMiss();
        mGoal = mTrick.getGoal();
        mTimeTrained = mTrick.getTime_trained();
        mHits = mTrick.getHit();
        mSiteswap = mTrick.getSiteswap();
        mSource = mTrick.getSource();
        mAnimation = mTrick.getAnimation();
        mTutorial = mTrick.getTutorial();
        mDifficulty = mTrick.getDifficulty();
        mCapacity = mTrick.getCapacity();
        mId = mTrick.getId();
        mDescription = mTrick.getDescription();

        // Get references to all of the text views
        mTrainingTimeTextView = findViewById(R.id.time_trained_text_view);
        mTrainingTimeTextView.setText(getString(R.string.time_spent_training) + " " + mTimeTrained);
        ((TextView) findViewById(R.id.trick_goal_text_view)).setText(getString(R.string.goal) + " " + mGoal);
        ((TextView) findViewById(R.id.trick_name_text_view)).setText(mName + " " + mPropType);
        mPrTextView = (TextView) findViewById(R.id.trick_pr_text_view);
        mPrTextView.setText(getString(R.string.pr) + " " + mPr);
        mHitMissTextView = (TextView) findViewById(R.id.hitMissTextView);
        mHitMissTextView.setText(mHits + " / " + mMisses);

        // Now the buttons
        mTrainingButton = (Button) findViewById(R.id.start_training_button);
        mTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                training_button();
            }
        });
        mHitButton = (Button) findViewById(R.id.hit_button);
        mHitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hit_button();
            }
        });
        mMissButton = (Button) findViewById(R.id.miss_button);
        mMissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miss_button();
            }
        });

        // Create the chronometer
        mChronometer = ((Chronometer) findViewById(R.id.chronometer));

        // Create and initialize the graph with the record data
        mGraphView = (GraphView) findViewById(R.id.trick_description_text_view);
        initGraph(mGraphView, mTrick.getRecord());
    }

    public void initGraph(GraphView graph, String records) {
        // This code was adapted from the GraphView sample code
        // The records are stored in a list of numbers seperated by a comma, for ex: 10,20,32,42
        String[] items = records.split(",");
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{});
        for (int i = 0; i < items.length; i++) {
            series.appendData(new DataPoint(i, Integer.parseInt(items[i])), true, 40);
        }
        series.setAnimated(true);
        graph.addSeries(series);
        graph.getViewport().setMinX(0d);
        graph.getViewport().setMaxX(items.length);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    public void training_button() {
        // This activity runs in one of two modes, denoted by the boolean mTraining
        // If mTraining: the chronometer is running and the user is juggling, the button says "stop training"
        // If !mTraining: the chronometer is at zero and the button stays "start trining"
        if (!mTraining) {
            // User has started juggling
            Toast.makeText(this, R.string.start_juggling, Toast.LENGTH_SHORT).show();
            // Start the chronometer at the current system time
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            // Update class variable
            mTraining = true;
            // Reset the text on the training button from 'start' to 'finished'
            mTrainingButton.setText(R.string.finished_training);
            // Record the start time of the chronometer
            mStartTime = System.currentTimeMillis();
        } else {
            // User has stopped juggling
            mChronometer.stop();
            // Reset the chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            // Update the class variable, and reset the text on the button
            mTraining = false;
            mTrainingButton.setText(R.string.start_juggling);

            // Calculate training duration
            long trainingTime = (System.currentTimeMillis() - mStartTime) / 1000;
            final String traingTimeString = Long.toString(trainingTime);
            long longTimeTrained = Long.parseLong(mTimeTrained);
            final String totalTime = Long.toString(trainingTime + longTimeTrained);
            // Update the class variable to reflect new total training time
            mTimeTrained = totalTime;

            // Create an alert dialog asking user if they want to enter a catch count
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.catch_count_question);

            // Create edit text for the number of catches
            final EditText catchCountEditText = new EditText(this);
            catchCountEditText.setHint(R.string.enter_catch_count);
            catchCountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(catchCountEditText);

            // Set up the 'add' and 'cancel' buttons
            builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get the number of catches that the user entered into the edit text
                    String catchCount = catchCountEditText.getText().toString();
                    // Update the class variable (needed because here is an inner class)
                    mCatchCount = catchCount;
                    // Update mRecords to reflect new catch count
                    mRecords = mRecords + "," + catchCount;
                    // If the catch count is greater than the personal record, update the personal record in the metadata trick.
                    if (Integer.parseInt(catchCount) > Integer.parseInt(mPr)) {
                        // Congratulate the user on a new PR
                        Toast.makeText(getApplicationContext(), R.string.new_pr_message, Toast.LENGTH_SHORT).show();
                        // Update the mPr variable
                        mPr = catchCount;
                    } else {
                        // It is not a pr, but the meta trick still needs to be updated to increase the time and store the record
                        Toast.makeText(getApplicationContext(), R.string.record_logged, Toast.LENGTH_SHORT).show();
                    }
                    finished_training(traingTimeString);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicks no and does not add a catch count
                    mCatchCount = "not recorded";
                    // Close the dialog
                    dialog.cancel();
                    finished_training(traingTimeString);
                }
            });
            builder.show();
        }
    }

    // when a user if finished training, data has to be written to the DB, and views have to be updated
    public void finished_training(String trainingTime){
        // Update the UI, as the pr may have changed.
        mPrTextView.setText(getString(R.string.pr) + " " + mPr);
        // Update the time trained
        mTrainingTimeTextView.setText(getString(R.string.time_spent_training) + " " + mTimeTrained);
        // Update the graph
        initGraph(mGraphView, mRecords);
        // Add this record to the database
        insert_trick(trainingTime, mCatchCount, "0", "0");
        // Update the meta data for this trick
        update_trick_metadata();
    }

    //%%%%%%%%%%%%%%%%%%%%% Start hit/miss buttons %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    public void miss_button() {
        mMisses = Integer.toString(Integer.parseInt(mMisses) + 1);
        updateHitAndMiss("no", "1");
    }

    public void hit_button() {
        mHits = Integer.toString(Integer.parseInt(mHits) + 1);
        updateHitAndMiss("1", "no");
    }

    public void updateHitAndMiss(String hitsVal, String missVal) {
        mHitMissTextView.setText(mHits + " / " + mMisses);
        update_trick_metadata();
        insert_trick("0", "0", hitsVal, missVal);
        Toast.makeText(this, R.string.record_logged, Toast.LENGTH_SHORT).show();
    }
    //%%%%%%%%%%%%%%%%%%%%% Stop hit/miss buttons %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    //++++++++++++++++++++++++++++++++ START THREE BUTTONS OPTIONS +++++++++++++++++++++++++++++++++
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    // This allows the user to switch between favorites, popular, or highest rated
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.menu_remove_trick) {
            remove_trick(Integer.parseInt(mId));
            finish();
            Toast.makeText(this, R.string.trick_removed, Toast.LENGTH_LONG).show();
        }
        if (itemThatWasClickedId == R.id.menu_show_detail) {
            Intent toTrickDiscovery = new Intent(this, TrickDiscovery.class);
            toTrickDiscovery.putExtra(ARG_TRICK_OBJECT, mTrick);
            startActivity(toTrickDiscovery);
        }
        if (itemThatWasClickedId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //++++++++++++++++++++++++++++++++ END THREE BUTTONS OPTIONS +++++++++++++++++++++++++++++++++++

    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; START DB METHODS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    public void remove_trick(int id) {
        // Build uri with the movie json that needs to be deleted
        Uri uri = Contract.listEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
        // Delete single row
        int test = getContentResolver().delete(uri, null, null);
        // Get access to the preferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(ARG_SP_LOG_KEY, 0);
        String[] stringLIst = settings.getString(ARG_LIST_KEY, "").split(mUnique);
        // get list of trick names
        ArrayList<String> mTrickNames = new ArrayList<String>();
        for (int i = 0; i < stringLIst.length; i++) mTrickNames.add(stringLIst[i]);
        // remove this trick from list of trick names
        mTrickNames.remove(mName);
        // create new list of strings that does not contain the removed trick
        String output_string = "";
        for (int i = 0; i < mTrickNames.size(); i++) output_string += mTrickNames.get(i) + mUnique;
        // update list of trick names in shared preferences
        settings.edit().putString(ARG_LIST_KEY, output_string).commit();
    }

    public void update_trick_metadata() {
        update_mLocation();
        // Update the meta data for the trick that is in the database
        Actions.update_trick(this, mId, mPr, mTimeTrained, mDescription, mName, "yes", mHits,
                mMisses, mRecords, mPropType, mGoal, mSiteswap, mAnimation, mSource,
                mDifficulty, mCapacity, mTutorial, "no location");
    }

    public void insert_trick(String time, String record, String hits, String misses) {
        update_mLocation();
        // Insert the trick into the database
        Actions.insert_trick(this, "0", time, mDescription, mName, "no",
                hits, misses, record, mPropType, mGoal, mSiteswap, mAnimation,
                mSource, mDifficulty, mCapacity, mTutorial, mLocation);
    }
    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; END DB METHODS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ START LOCATION METHODS $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    private void get_place_and_log() {
        // This code is adapted from the sample on the developer website
        @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult =
                mPlaceDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener
                (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                            mLocation = "" + likelyPlaces.get(0).getPlace().getName();
                            // Release the place likelihood buffer, to avoid memory leaks.
                            likelyPlaces.release();
                        }
                    }
                });
    }
    public void update_mLocation() {
        // Check to make sure that mLocation actually has a value
        if (mLocation == null) mLocation = "Location not avaliable.";
        if (mLocation.length() < 2) mLocation = "Location not avaliable.";
    }
    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ END LOCATION METHODS $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
}