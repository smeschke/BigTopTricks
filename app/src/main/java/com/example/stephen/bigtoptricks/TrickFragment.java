package com.example.stephen.bigtoptricks;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;


public class TrickFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_TRICK_NAME = "trick_name";
    public static final String ARG_TRICK_PR = "trick_pr";
    public static final String ARG_TRICK_GOAL = "trick_goal";
    public static final String ARG_TIME_TRAINED = "time_trained";
    public static final String ARG_TRICK_DESCRIPTION = "trick_description";
    public static final String ARG_TRICK_ID = "id";
    public String mTrickName;
    public String mTrickPr;
    public String mTrickGoal;
    public String mTrickDescription;
    public String mTimeTrained;
    public int mId;
    Chronometer mChronometer;
    public long mStartTime;
    public boolean mTraining;
    public Button mTrainingButton;
    TextView trainingTimeTextView;

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
        mId = getArguments().getInt(ARG_TRICK_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("LOG", "asdf mId onCreateView" + mId);

        View rootView = inflater.inflate(R.layout.fragment_trick, container, false);
        trainingTimeTextView = rootView.findViewById(R.id.fragment_time_trained_text_view);
        trainingTimeTextView.setText("Time spent training this trick: " + mTimeTrained);
        ((TextView) rootView.findViewById(R.id.fragment_trick_description_text_view)).setText("Description: " + mTrickDescription);
        ((TextView) rootView.findViewById(R.id.fragment_trick_goal_text_view)).setText("Goal: " + mTrickGoal);
        ((TextView) rootView.findViewById(R.id.fragment_trick_name_text_view)).setText(mTrickName);
        ((TextView) rootView.findViewById(R.id.fragment_trick_pr_text_view)).setText("PR: " + mTrickPr);
        mTrainingButton = (Button) rootView.findViewById(R.id.fragment_start_training_button);
        mTrainingButton.setOnClickListener(this);
        mChronometer = ((Chronometer) rootView.findViewById(R.id.fragment_chronometer));
        return rootView;

    }

    @Override
    public void onClick(View v) {
        if (!mTraining) {
            Toast.makeText(getActivity(), "Start Juggling!", Toast.LENGTH_SHORT).show();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mTraining = true;
            mTrainingButton.setText("Finished Training.");
            mStartTime = System.currentTimeMillis();
        } else {
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mTraining = false;
            mTrainingButton.setText("Start Juggling!");
            // Calculate training duration
            long trainingTime = System.currentTimeMillis() - mStartTime;
            trainingTime = trainingTime / 1000;
            // Calculate the total time that this trick has been trained
            long totalTime = trainingTime + Long.parseLong(mTimeTrained);
            // Insert a record of this trick into the database
            insert_trick(mTrickPr, Long.toString(trainingTime), mTrickDescription, mTrickName, "yes", mTrickGoal);
            // Update metadata trick
            update_trick(mId, Long.toString(totalTime));
            // Inform user
            Toast.makeText(getActivity(), "Trained for " + trainingTime + " seconds. \n Record added to DB.",
                    Toast.LENGTH_SHORT).show();
            trainingTimeTextView.setText("Time spent training this trick: " + Long.toString(totalTime));
        }
    }

    public void update_trick(int id, String time){
        Uri uri = Contract.listEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, mTrickPr);
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, time);
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, mTrickDescription);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, mTrickName);
        cv.put(Contract.listEntry.COLUMN_IS_RECORD, "no");
        cv.put(Contract.listEntry.COLUMN_GOAL, mTrickGoal);
        int result = getActivity().getContentResolver().update(
                Contract.listEntry.CONTENT_URI, cv, Integer.toString(id), null);
    }

    public void insert_trick(String pr, String time, String description, String name, String record,
                             String goal) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, mTrickPr);
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, mTimeTrained);
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, mTrickDescription);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, mTrickName);
        cv.put(Contract.listEntry.COLUMN_IS_RECORD, record);
        cv.put(Contract.listEntry.COLUMN_GOAL, mTrickGoal);
        // Insert the content values via a ContentResolver
        Uri uri = getActivity().getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);
    }
}