package com.example.stephen.bigtoptricks;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class TrickFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_TRICK_NAME = "trick_name";
    public static final String ARG_TRICK_PR = "trick_pr";
    public static final String ARG_TRICK_GOAL = "trick_goal";
    public static final String ARG_TIME_TRAINED = "time_trained";
    public static final String ARG_TRICK_DESCRIPTION = "trick_description";
    public static final String ARG_TRICK_ID = "id";
    public static final String ARG_TRICK_PROP_TYPE = "prop";
    public static final String ARG_RECORD_ID = "records";
    public static final String ARG_HITS = "hits";
    public static final String ARG_MISSES = "misses";
    private String mTrickName;
    private String mTrickPr;
    private String mTrickGoal;
    private String mTrickDescription;
    private String mTimeTrained;
    private String mId;
    private String mHits;
    private String mMisses;
    private String mPropType;
    private String mRecords;
    Chronometer mChronometer;
    public long mStartTime;
    public boolean mTraining;
    public Button mTrainingButton;
    Button mHitButton;
    Button mMissButton;
    TextView mTrainingTimeTextView;
    TextView mPrTextView;
    GraphView mGraphView;
    TextView mHitMissTextView;

    public TrickFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrickFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrickFragment newInstance(String param1, String param2) {
        TrickFragment fragment = new TrickFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrickName = getArguments().getString(ARG_TRICK_NAME);
        mTimeTrained = getArguments().getString(ARG_TIME_TRAINED);
        mTrickDescription = getArguments().getString(ARG_TRICK_DESCRIPTION);
        mTrickGoal = getArguments().getString(ARG_TRICK_GOAL);
        mTrickPr = getArguments().getString(ARG_TRICK_PR);
        mId = getArguments().getString(ARG_TRICK_ID);
        mHits = getArguments().getString(ARG_HITS);
        mMisses = getArguments().getString(ARG_MISSES);
        mPropType = getArguments().getString(ARG_TRICK_PROP_TYPE);
        mRecords = getArguments().getString(ARG_RECORD_ID);
        Log.d("LOG", "asdf mID: " + mHits + " " + mMisses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Log.d("LOG", "asdf mId onCreateView" + mId);
        // Get references to all of the text views
        View rootView = inflater.inflate(R.layout.fragment_trick, container, false);
        mTrainingTimeTextView = rootView.findViewById(R.id.fragment_time_trained_text_view);
        mTrainingTimeTextView.setText("Time spent training this trick: " + mTimeTrained);
        //((TextView) rootView.findViewById(R.id.fragment_trick_description_text_view)).setText("Records: " + mRecords);
        ((TextView) rootView.findViewById(R.id.fragment_trick_goal_text_view)).setText("Goal: " + mTrickGoal);
        ((TextView) rootView.findViewById(R.id.fragment_trick_name_text_view)).setText(mTrickName + " " + mPropType);
        mPrTextView = (TextView) rootView.findViewById(R.id.fragment_trick_pr_text_view);
        mPrTextView.setText("PR: " + mTrickPr);

        // Now the buttons
        mTrainingButton = (Button) rootView.findViewById(R.id.fragment_start_training_button);
        mTrainingButton.setOnClickListener(this);
        mHitButton = (Button) rootView.findViewById(R.id.hit_button);
        mHitButton.setOnClickListener(this);
        mMissButton = (Button) rootView.findViewById(R.id.miss_button);
        mMissButton.setOnClickListener(this);

        mChronometer = ((Chronometer) rootView.findViewById(R.id.fragment_chronometer));
        mHitMissTextView = (TextView) rootView.findViewById(R.id.hitMissTextView);
        mHitMissTextView.setText(mHits + " / " + mMisses);
        mGraphView = (GraphView) rootView.findViewById(R.id.fragment_trick_description_text_view);
        initGraph(mGraphView, mRecords);

        return rootView;
    }

    public void initGraph(GraphView graph, String records) {
        String[] items = records.split(",");
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{});
        for (int i = 0; i < items.length; i++) {
            Log.d("LOG", "asdf record item: " + items[i]);
            series.appendData(new DataPoint(i, Integer.parseInt(items[i])), true, 40);
        }

        //series.setSpacing(50); // 50% spacing between bars
        series.setAnimated(true);
        graph.addSeries(series);

        // set the viewport wider than the data, to have a nice view
        graph.getViewport().setMinX(0d);
        graph.getViewport().setMaxX(items.length);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    public void training_button(){
        if (!mTraining) {
            // User has started juggling
            Toast.makeText(getActivity(), "Start Juggling!", Toast.LENGTH_SHORT).show();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mTraining = true;
            mTrainingButton.setText("Finished Training.");
            mStartTime = System.currentTimeMillis();
        } else {
            // User has stopped juggling
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mTraining = false;
            mTrainingButton.setText("Start Juggling!");

            // Calculate training duration
            long trainingTime = System.currentTimeMillis() - mStartTime;
            trainingTime = trainingTime / 1000;
            final String traingTimeString = Long.toString(trainingTime);
            // Calculate the total time that this trick has been trained
            final String totalTime = Long.toString(trainingTime + Long.parseLong(mTimeTrained));

            // Create an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Do you want to add a catch count?");
            // Ask the user to enter a goal
            final EditText goal = new EditText(getActivity());
            goal.setHint("Enter Catch Count...");
            goal.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(goal);
            // Set up the buttons
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String catchCount = goal.getText().toString();
                    Log.d("LOG", "asdf mCatchCount: " + catchCount);
                    // Update mRecords to reflect new catch count
                    mRecords = mRecords + "," + catchCount;
                    // User enters a value (hopefully) and clicks add
                    // If the catch count is greater than the personal record,
                    // then update the personal record in the metadata trick.
                    if (Integer.parseInt(catchCount) > Integer.parseInt(mTrickPr)) {
                        // Update metadata trick
                        update_trick(mId, catchCount, totalTime, mTrickDescription, mTrickName,
                                mRecords, mTrickGoal, mPropType, mHits, mMisses);
                        Toast.makeText(getContext(), "Great! A new PR!", Toast.LENGTH_SHORT).show();
                        // Update the mTrickPr variable
                        mTrickPr = catchCount;
                    }
                    // it is not a pr, but the meta trick still needs to be updated to increase the time and store the record
                    else {
                        update_trick(mId, mTrickPr, totalTime, mTrickDescription, mTrickName,
                                mRecords, mTrickGoal, mPropType, mHits, mMisses);
                        Toast.makeText(getContext(), "Record Logged.", Toast.LENGTH_SHORT).show();
                    }

                    // Insert a record of this trick into the data base
                    insert_trick(mTrickPr, traingTimeString, mTrickDescription, mTrickName,
                            catchCount, mTrickGoal, mPropType, "no", "no");

                    // now that new data has been logged, it's time to update the UI to reflect the NEW PR!
                    // Update the time trained
                    mTrainingTimeTextView.setText("Time spent training this trick: " + totalTime);
                    // If needed, update the pr
                    mPrTextView.setText("PR: " + mTrickPr);
                    Log.d("LOG", "asdf mTrickPR: " + mTrickPr);
                    // Update the graph
                    initGraph(mGraphView, mRecords);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicks no, so there is not need to check if the catch count is greater
                    // update the metadata trick
                    update_trick(mId, mTrickPr, totalTime, mTrickDescription, mTrickName,
                            mRecords, mTrickGoal, mPropType, mHits, mMisses);
                    Toast.makeText(getContext(), "Record Logged.", Toast.LENGTH_SHORT).show();

                    // than the pr, so just enter a record of training this trick in the db
                    insert_trick(mTrickPr, traingTimeString, mTrickDescription, mTrickName,
                            "0", mTrickGoal, mPropType, "no", "no");

                    // now that new data has been logged, it's time to update the UI to reflect the NEW PR!
                    // Update the time trained
                    mTrainingTimeTextView.setText("Time spent training this trick: " + totalTime);
                    // If needed, update the pr
                    mPrTextView.setText("PR: " + mTrickPr);
                    Log.d("LOG", "asdf mTrickPR: " + mTrickPr);
                    // Update the graph
                    initGraph(mGraphView, mRecords);

                    dialog.cancel();
                }
            });
            builder.show();
        }
    }


    @Override
    public void onClick(View v) {
        // Get the button_type
        int button_type = v.getId();
        if (button_type==R.id.hit_button){
            mHits = Integer.toString(Integer.parseInt(mHits) + 1);
            mHitMissTextView.setText(mHits + " / " + mMisses);

            update_trick(mId, mTrickPr, mTimeTrained, mTrickDescription, mTrickName,
                    mRecords, mTrickGoal, mPropType, mHits, mMisses);
            Toast.makeText(getContext(), "Record Logged.", Toast.LENGTH_SHORT).show();

            // than the pr, so just enter a record of training this trick in the db
            insert_trick(mTrickPr, mTimeTrained, mTrickDescription, mTrickName,
                    "0", mTrickGoal, mPropType, "1", "no");
        }

        if (button_type==R.id.fragment_start_training_button){
            Log.d("LOG", "asdf training button");
            training_button();
        }

        if (button_type==R.id.miss_button){
            mMisses = Integer.toString(Integer.parseInt(mMisses) + 1);
            mHitMissTextView.setText(mHits + " / " + mMisses);

            update_trick(mId, mTrickPr, mTimeTrained, mTrickDescription, mTrickName,
                    mRecords, mTrickGoal, mPropType, mHits, mMisses);
            Toast.makeText(getContext(), "Record Logged.", Toast.LENGTH_SHORT).show();

            // than the pr, so just enter a record of training this trick in the db
            insert_trick(mTrickPr, "0", mTrickDescription, mTrickName,
                    "0", mTrickGoal, mPropType, "no", "1");
        }
    }

    public void update_trick(String id, String pr, String time, String description, String name, String record,
                             String goal, String propType, String hits, String misses) {
        Uri uri = Contract.listEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, pr);
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, time);
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, description);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, name);
        cv.put(Contract.listEntry.COLUMN_IS_META, "yes");
        cv.put(Contract.listEntry.COLUMN_RECORD, record);
        cv.put(Contract.listEntry.COLUMN_HIT, hits);
        cv.put(Contract.listEntry.COLUMN_MISS, misses);
        cv.put(Contract.listEntry.COLUMN_PROP_TYPE, propType);
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        int result = getActivity().getContentResolver().update(
                Contract.listEntry.CONTENT_URI, cv, id, null);
        Log.d("LOG", "asdf result: " + result + " PR:" + pr + " Record: " + record);
    }

    public void insert_trick(String pr, String time, String description, String name, String record,
                             String goal, String propType, String hits, String misses) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, pr);
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, time);
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, description);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, name);
        cv.put(Contract.listEntry.COLUMN_HIT, hits);
        cv.put(Contract.listEntry.COLUMN_MISS, misses);
        cv.put(Contract.listEntry.COLUMN_IS_META, "no");
        cv.put(Contract.listEntry.COLUMN_PROP_TYPE, propType);
        cv.put(Contract.listEntry.COLUMN_RECORD, record);
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        // Insert the content values via a ContentResolver
        Uri uri = getActivity().getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);
    }
}