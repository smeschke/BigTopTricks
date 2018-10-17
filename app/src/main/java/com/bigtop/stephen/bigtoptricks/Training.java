package com.bigtop.stephen.bigtoptricks;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigtop.stephen.bigtoptricks.addTricks.TrickDiscovery;
import com.bigtop.stephen.bigtoptricks.data.Actions;
import com.bigtop.stephen.bigtoptricks.data.Contract;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;

public class Training extends AppCompatActivity {

    public final static String mUnique = "unique_delimiter";
    public static final String ARG_TRICK_OBJECT = "trick_object";
    public final static String ARG_LIST_KEY = "list_key";
    public final static String ARG_HIDE_ADD_BUTTON = "hide_add_button";
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
    private Trick mTrick;
    private Chronometer mChronometer;
    private long mStartTime;
    private boolean mTraining;
    private Button mTrainingButton;
    private TextView mTrainingTimeTextView;
    private TextView mPrTextView;
    private GraphView mGraphView;
    private TextView mHitMissTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Keep screen on while in training activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Request permission for fine location
        ActivityCompat.requestPermissions(Training.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        // up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        ((TextView) findViewById(R.id.trick_name_text_view)).setText(mName);
        mPrTextView = findViewById(R.id.trick_pr_text_view);
        mPrTextView.setText(getString(R.string.pr) + " " + mPr);
        mHitMissTextView = findViewById(R.id.hitMissTextView);
        mHitMissTextView.setText(mHits + " / " + mMisses);

        // Now the buttons
        mTrainingButton = findViewById(R.id.start_training_button);
        mTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                training_button();
            }
        });
        Button mHitButton = findViewById(R.id.hit_button);
        mHitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hit_button();
            }
        });
        Button mMissButton = findViewById(R.id.miss_button);
        mMissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miss_button();
            }
        });

        // Set the prop type image view
        ImageView propTypeImageView = findViewById(R.id.training_db_prop_type);
        if (mPropType.equals(getString(R.string.Ball)))
            propTypeImageView.setImageResource(R.drawable.ball);
        if (mPropType.equals(getString(R.string.Club)))
            propTypeImageView.setImageResource(R.drawable.clubs);
        if (mPropType.equals(getString(R.string.Ring)))
            propTypeImageView.setImageResource(R.drawable.ring);
        if (mPropType.equals(getString(R.string.Poi)))
            propTypeImageView.setImageResource(R.drawable.poi);
        if (mPropType.equals(getString(R.string.Knife)))
            propTypeImageView.setImageResource(R.drawable.knife);
        if (mPropType.equals(getString(R.string.Chainsaw)))
            propTypeImageView.setImageResource(R.drawable.saw);
        if (mPropType.equals(getString(R.string.Bowling)))
            propTypeImageView.setImageResource(R.drawable.bowling);

        // Create the chronometer
        mChronometer = findViewById(R.id.chronometer);

        // Create and initialize the graph with the record data
        mGraphView = findViewById(R.id.trick_description_text_view);
        initGraph(mGraphView, mTrick.getRecord());
    }

    private void initGraph(GraphView graph, String records) {
        // This code was adapted from the GraphView sample code
        // The records are stored in a list of numbers separated by a comma, for ex: 10,20,32,42
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

    private void training_button() {
        // This activity runs in one of two modes, denoted by the boolean mTraining
        // If mTraining: the chronometer is running and the user is juggling, the button says "stop training"
        // If !mTraining: the chronometer is at zero and the button stays "start training"
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
            final String trainingTimeString = Long.toString(trainingTime);
            long longTimeTrained = Long.parseLong(mTimeTrained);
            // Update the class variable to reflect new total training time
            mTimeTrained = Long.toString(trainingTime + longTimeTrained);

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
                    finished_training(trainingTimeString);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicks no and does not add a catch count
                    mCatchCount = getString(R.string.not_recorded);
                    // Close the dialog
                    dialog.cancel();
                    finished_training(trainingTimeString);
                }
            });
            builder.show();
        }
    }

    // when a user if finished training, data has to be written to the DB, and views have to be updated
    private void finished_training(String trainingTime) {
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
    private void miss_button() {
        mMisses = Integer.toString(Integer.parseInt(mMisses) + 1);
        updateHitAndMiss("no", "1");
    }

    private void hit_button() {
        mHits = Integer.toString(Integer.parseInt(mHits) + 1);
        updateHitAndMiss("1", "no");
    }

    private void updateHitAndMiss(String hitsVal, String missVal) {
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
            // Hide the add button in the discovery activity
            toTrickDiscovery.putExtra(ARG_HIDE_ADD_BUTTON, "hide it");
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
    private void remove_trick(int id) {
        // Build uri with the movie json that needs to be deleted
        Uri uri = Contract.listEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
        // Delete single row
        getContentResolver().delete(uri, null, null);
        // Get access to the preferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(ARG_SP_LOG_KEY, 0);
        String[] stringLIst = settings.getString(ARG_LIST_KEY, "").split(mUnique);
        // get list of trick names
        ArrayList<String> mTrickNames = new ArrayList<>(Arrays.asList(stringLIst));
        // remove this trick from list of trick names
        mTrickNames.remove(mName);
        // create new list of strings that does not contain the removed trick
        StringBuilder output_string = new StringBuilder();
        for (int i = 0; i < mTrickNames.size(); i++)
            output_string.append(mTrickNames.get(i)).append(mUnique);
        // update list of trick names in shared preferences
        settings.edit().putString(ARG_LIST_KEY, output_string.toString()).commit();
    }

    private void update_trick_metadata() {
        // Update the meta data for the trick that is in the database
        Actions.update_trick(this, mId, mPr, mTimeTrained, mDescription, mName, "yes", mHits,
                mMisses, mRecords, mPropType, mGoal, mSiteswap, mAnimation, mSource,
                mDifficulty, mCapacity, mTutorial);
    }

    private void insert_trick(String time, String record, String hits, String misses) {
        // Insert the trick into the database
        Actions.insert_trick(this, "0", time, mDescription, mName, "no",
                hits, misses, record, mPropType, mGoal, mSiteswap, mAnimation,
                mSource, mDifficulty, mCapacity, mTutorial);
    }
    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; END DB METHODS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
}
