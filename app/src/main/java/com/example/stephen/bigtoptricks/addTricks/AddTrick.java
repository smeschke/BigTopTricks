package com.example.stephen.bigtoptricks.addTricks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Trick;
import com.example.stephen.bigtoptricks.TricksWidget;
import com.example.stephen.bigtoptricks.data.Actions;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.example.stephen.bigtoptricks.Training.ARG_LIST_KEY;
import static com.example.stephen.bigtoptricks.Training.ARG_SP_LOG_KEY;
import static com.example.stephen.bigtoptricks.Training.ARG_TRICK_OBJECT;
import static com.example.stephen.bigtoptricks.Training.mUnique;

public class AddTrick extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText mTrickNameEditText;
    private EditText mTrickDescriptionEditText;
    private EditText mGoalCatchesEditText;
    private String mName;
    private String mDescription;
    private String mCapacity;
    private String mSiteswap;
    private String mSource;
    private String mAnimation;
    private String mTutorial;
    private String mPropFromSpinner;
    private String mDifficulty;
    private boolean mUseDefaultDescription = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick);

        mTrickNameEditText = findViewById(R.id.edit_text_trick_name);
        mTrickDescriptionEditText = findViewById(R.id.edit_text_trick_description);
        mGoalCatchesEditText = findViewById(R.id.edit_text_goal_catches);
        mGoalCatchesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        Spinner mPropSpinner = findViewById(R.id.edit_text_prop_type);

        // Code for the spinner is adapted from: https://developer.android.com/guide/topics/ui/controls/spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prop_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mPropSpinner.setAdapter(adapter);
        mPropSpinner.setOnItemSelectedListener(this);

        if (getIntent().hasExtra(ARG_TRICK_OBJECT)) {
            // The intent has a trick object extra if the user has picked a trick from the Library
            // Bind the data from that trick into the text views, and set the class variables
            Trick mTrick = Objects.requireNonNull(getIntent().getExtras()).getParcelable(ARG_TRICK_OBJECT);
            mName = Objects.requireNonNull(mTrick).getName();
            mSiteswap = mTrick.getSiteswap();
            mSource = mTrick.getSource();
            mAnimation = mTrick.getAnimation();
            mTutorial = mTrick.getTutorial();
            mDifficulty = mTrick.getDifficulty();
            mCapacity = mTrick.getCapacity();
            mDescription = mTrick.getDescription();
            if (mName.length() > 0) mTrickNameEditText.setText(mName);
            if (mDescription.length() > 0) {
                // If the description is really long, display a substring for ascetic purposes
                mTrickDescriptionEditText.setText(mDescription.substring(0, 60) + "...");
                mUseDefaultDescription = true;
            }
        }

        Button addTrickButton = findViewById(R.id.button_add_trick_to_database);
        addTrickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_db();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // Get the name from the spinner, and save it as the class variable
        mPropFromSpinner = parent.getItemAtPosition(pos).toString();
    }

    @Override // Position zero is default - balls
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void add_to_db() {
        // Get values from user's input
        mName = mTrickNameEditText.getText().toString();
        if (!mUseDefaultDescription) mDescription = mTrickDescriptionEditText.getText().toString();
        String goal = mGoalCatchesEditText.getText().toString();

        // Determine if the goal is a valid integer - https://stackoverflow.com/questions/10120212/how-to-determine-if-an-input-in-edittext-is-an-integer
        boolean goal_is_valid = false;
        try {
            int num = Integer.parseInt(goal);
            if (num > 0) goal_is_valid = true;
        } catch (NumberFormatException e) {
            Log.d("LOG", "myLogs goal is not valid");
        }

        // Can't have a trick with no name (check for null parameters)
        boolean name_is_valid = true;
        if (mName.length() < 1) name_is_valid = false;
        if (mDescription.length() < 1) mDescription = getString(R.string.default_description);
        if (goal.length() < 1) goal = getString(R.string.default_goal);
        if (mSiteswap == null) mSiteswap = getString(R.string.none_entered);
        if (mSource == null) mSource = getString(R.string.none_entered);
        if (mDifficulty == null) mDifficulty = getString(R.string.none_entered);
        if (mTutorial == null) mTutorial = getString(R.string.none_entered);
        if (mCapacity == null) mCapacity = getString(R.string.none_entered);
        if (mAnimation == null) mAnimation = getString(R.string.none_entered);

        // Determine if trick name is already in the db (lines 145 to 155 are a rather inelegant solution).
        boolean trick_is_unique = true;
        // Get access to the preferences for list of trick names
        ArrayList<String> trickNames = new ArrayList<>();
        // Look through all the tricks in the DB
        SharedPreferences settings = getApplicationContext().getSharedPreferences(ARG_SP_LOG_KEY, 0);
        String tricks_string = settings.getString(ARG_LIST_KEY, "");
        String[] stringLIst = tricks_string.split(mUnique);
        Collections.addAll(trickNames, stringLIst);
        if (trickNames.contains(mName)) trick_is_unique = false;

        // If the user's input is good, the trick can be added to the training database.
        // If the user's input is bad, tell the user what is wrong.
        if (trick_is_unique && goal_is_valid && name_is_valid) {
            // USERS INPUT IS GOOD
            Actions.insert_trick(this, "0", "0", mDescription,
                    mName, "yes", "0", "0", "0", mPropFromSpinner, goal,
                    mSiteswap, mAnimation, mSource, mDifficulty, mCapacity,
                    mTutorial, getString(R.string.location_not_available));
            // Inform the user that the trick has been added to the DB
            Toast.makeText(this, mName + " " + getString(R.string.into_db),
                    Toast.LENGTH_SHORT).show();

            // Reset the hints on the EditText boxes
            mTrickNameEditText.setText("");
            mTrickDescriptionEditText.setText("");
            mGoalCatchesEditText.setText("");
            mTrickNameEditText.setHint(R.string.name_hint);
            mTrickDescriptionEditText.setHint(R.string.description_hint);
            mGoalCatchesEditText.setHint(R.string.goal_hint);

            // update list of trick names in shared preferences
            settings.edit().putString(ARG_LIST_KEY, tricks_string + mUnique + mName).commit();

            // Obtain the FirebaseAnalytics instance.
            // https://firebase.google.com/docs/analytics/android/start/
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Add Trick Activity");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Trick Name: " + mName);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            // Update the widget
            trickNames.add(mName);
            writeToWidget(trickNames);

        } else {
            // PROBLEM WITH USER'S INPUT
            // The user is trying to enter a trick with name==""
            if (!name_is_valid)
                Toast.makeText(this, R.string.fail_name, Toast.LENGTH_LONG).show();
            // The user has not specified a valid integer > 0 for the goal
            if (!goal_is_valid && name_is_valid && trick_is_unique)
                Toast.makeText(this, R.string.fail_goal, Toast.LENGTH_LONG).show();
            // The user is trying to enter a trick name that already exists in the database
            if (goal_is_valid && name_is_valid)
                Toast.makeText(this, R.string.fail_unique, Toast.LENGTH_LONG).show();
        }
    }

    // Update the list of tricks in the widget - similar to ingredients widget in project 3
    private void writeToWidget(ArrayList<String> names) {
        // Create a string that will contain the title and a list of the tricks in the training database
        StringBuilder output = new StringBuilder(getString(R.string.widget_title));
        // Add each trick name on a new line (u2022 is a bullet point)
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).length() > 0) output.append("\u2022 ").append(names.get(i)).append("\n");
        }
        // Get app widget manager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        // Create a remote view
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.tricks_widget);
        ComponentName thisWidget = new ComponentName(this, TricksWidget.class);
        // Set the widget text and update the widget
        remoteViews.setTextViewText(R.id.appwidget_text, output.toString());
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
