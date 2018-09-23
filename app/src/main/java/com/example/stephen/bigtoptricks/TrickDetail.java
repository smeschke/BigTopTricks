package com.example.stephen.bigtoptricks;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.addTricks.TrickDiscovery;
import com.example.stephen.bigtoptricks.data.Actions;
import com.example.stephen.bigtoptricks.data.Contract;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class TrickDetail extends AppCompatActivity {

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
    private Tricks mTricks;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_detail);

        mTricks = getIntent().getExtras().getParcelable(ARG_TRICK_OBJECT);
        mName = mTricks.getName();
        mRecords = mTricks.getRecord();
        mPropType = mTricks.getProp_type();
        mPr = mTricks.getPr();
        mMisses = mTricks.getMiss();
        mGoal = mTricks.getGoal();
        mTimeTrained = mTricks.getTime_trained();
        mHits = mTricks.getHit();
        mSiteswap = mTricks.getSiteswap();
        mSource = mTricks.getSource();
        mAnimation = mTricks.getAnimation();
        mTutorial = mTricks.getTutorial();
        mDifficulty = mTricks.getDifficulty();
        mCapacity = mTricks.getCapacity();
        mId = mTricks.getId();
        mDescription = mTricks.getDescription();

        // Get references to all of the text views
        mTrainingTimeTextView = findViewById(R.id.time_trained_text_view);
        mTrainingTimeTextView.setText(getString(R.string.time_spent_training) + " " + mTimeTrained);
        ((TextView) findViewById(R.id.trick_goal_text_view)).setText(getString(R.string.goal) + " " + mGoal);
        ((TextView) findViewById(R.id.trick_name_text_view)).setText(mName + " " + mPropType);
        mPrTextView = (TextView) findViewById(R.id.trick_pr_text_view);
        mPrTextView.setText(getString(R.string.pr) + " " + mPr);

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

        mChronometer = ((Chronometer) findViewById(R.id.chronometer));
        mHitMissTextView = (TextView) findViewById(R.id.hitMissTextView);
        mHitMissTextView.setText(mHits + " / " + mMisses);
        mGraphView = (GraphView) findViewById(R.id.trick_description_text_view);
        initGraph(mGraphView, mRecords);
    }

    public void initGraph(GraphView graph, String records) {
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
        if (!mTraining) {
            // User has started juggling
            Toast.makeText(this, R.string.start_juggling, Toast.LENGTH_SHORT).show();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mTraining = true;
            mTrainingButton.setText(R.string.finished_training);
            mStartTime = System.currentTimeMillis();
        } else {
            // User has stopped juggling
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mTraining = false;
            mTrainingButton.setText(R.string.start_juggling);

            // Calculate training duration
            long trainingTime = (System.currentTimeMillis() - mStartTime) / 1000;
            final String traingTimeString = Long.toString(trainingTime);
            // Calculate the total time that this trick has been trained
            long longTimeTrained = Long.parseLong(mTimeTrained);
            final String totalTime = Long.toString(trainingTime + longTimeTrained);
            Log.d("LOG", "asdf TrainingTime: " + trainingTime + " mTimeTrained: " + longTimeTrained + " totalTime: " + totalTime);

            // Create an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.catch_count_question);
            // Ask the user to enter a goal
            final EditText catchCountEditText = new EditText(this);
            catchCountEditText.setHint(R.string.enter_catch_count);
            catchCountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(catchCountEditText);
            // Set up the buttons
            builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String catchCount = catchCountEditText.getText().toString();
                    // Update mRecords to reflect new catch count
                    mRecords = mRecords + "," + catchCount;
                    // User enters a value and clicks add
                    // If the catch count is greater than the personal record,
                    // then update the personal record in the metadata trick.
                    if (Integer.parseInt(catchCount) > Integer.parseInt(mPr)) {
                        Toast.makeText(getApplicationContext(), R.string.new_pr_message, Toast.LENGTH_SHORT).show();
                        // Update the mPr variable
                        mPr = catchCount;
                    }
                    // it is not a pr, but the meta trick still needs to be updated to increase the time and store the record
                    else
                        Toast.makeText(getApplicationContext(), R.string.record_logged, Toast.LENGTH_SHORT).show();

                    // Update metadata trick
                    update_trick(mId, catchCount, totalTime, mDescription, mName,
                            mRecords, mGoal, mPropType, mHits, mMisses);

                    // Insert a record of this trick into the data base
                    insert_trick(mPr, traingTimeString, mDescription, mName,
                            catchCount, mGoal, mPropType, "no", "no");

                    // now that new data has been logged, it's time to update the UI to reflect the NEW PR!
                    // Update the time trained
                    mTrainingTimeTextView.setText(getString(R.string.time_spent_training) + " " + totalTime);
                    // If needed, update the pr
                    mPrTextView.setText(getString(R.string.pr) + " " + mPr);
                    // Update the graph
                    initGraph(mGraphView, mRecords);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicks no, so there is not need to check if the catch count is greater
                    // update the metadata trick
                    update_trick(mId, mPr, totalTime, mDescription, mName,
                            mRecords, mGoal, mPropType, mHits, mMisses);
                    Toast.makeText(getApplicationContext(), R.string.record_logged, Toast.LENGTH_SHORT).show();

                    // than the pr, so just enter a record of training this trick in the db
                    insert_trick(mPr, traingTimeString, mDescription, mName,
                            "0", mGoal, mPropType, "no", "no");

                    // now that new data has been logged, it's time to update the UI to reflect the NEW PR!
                    // Update the time trained
                    mTrainingTimeTextView.setText(getString(R.string.time_spent_training) + totalTime);
                    // If needed, update the pr
                    mPrTextView.setText(getString(R.string.pr) + " " + mPr);
                    // Update the graph
                    initGraph(mGraphView, mRecords);

                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    public void miss_button() {
        mMisses = Integer.toString(Integer.parseInt(mMisses) + 1);
        update_hits("no", "1");
    }

    public void hit_button() {
        mHits = Integer.toString(Integer.parseInt(mHits) + 1);
        update_hits("1", "no");
    }

    public void update_hits(String hitsVal, String missVal) {
        mHitMissTextView.setText(mHits + " / " + mMisses);
        update_trick(mId, mPr, mTimeTrained, mDescription, mName,
                mRecords, mGoal, mPropType, mHits, mMisses);
        insert_trick(mPr, "0", mDescription, mName,
                "0", mGoal, mPropType, hitsVal, missVal);
        Toast.makeText(this, R.string.record_logged, Toast.LENGTH_SHORT).show();
    }

    public void update_trick(String id, String pr, String time, String description, String name,
                             String record, String goal, String propType, String hits, String misses) {
        Actions.update_trick(this, id, pr, time, description, name, "yes", hits,
                misses, record, propType, goal, mSiteswap, mAnimation, mSource,
                mDifficulty, mCapacity, mTutorial);
    }

    public void insert_trick(String pr, String time, String description, String name, String record,
                             String goal, String propType, String hits, String misses) {

        Actions.insert_trick(this, "0", "0", description, name, "no",
                "0", "0", "0", propType, goal, mSiteswap, mAnimation,
                mSource, mDifficulty, mCapacity, mTutorial);
    }

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
            toTrickDiscovery.putExtra(ARG_TRICK_OBJECT, mTricks);
            startActivity(toTrickDiscovery);
        }
        if (itemThatWasClickedId == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this,
                    MainActivity.class));
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
    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; END DB METHODS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
}
