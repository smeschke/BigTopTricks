package com.example.stephen.bigtoptricks.addTricks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Tricks;
import com.example.stephen.bigtoptricks.TricksWidget;
import com.example.stephen.bigtoptricks.data.Actions;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import static com.example.stephen.bigtoptricks.TrickDetail.ARG_LIST_KEY;
import static com.example.stephen.bigtoptricks.TrickDetail.ARG_SP_LOG_KEY;
import static com.example.stephen.bigtoptricks.TrickDetail.ARG_TRICK_OBJECT;
import static com.example.stephen.bigtoptricks.TrickDetail.mUnique;

public class AddTrick extends AppCompatActivity {

    private EditText mTrickNameEditText;
    private EditText mTrickDescriptionEditText;
    private EditText mGoalCatchesEditText;
    private EditText mPropTypeEditText;
    private String mName;
    private String mDescription;
    private String mCapacity;
    private String mSiteswap;
    private String mSource;
    private String mAnimation;
    private String mTutorial;
    private String mDifficulty;
    private Tricks mTricks;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean mUseDefaultDescription = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick);

        mTrickNameEditText = (EditText) findViewById(R.id.edit_text_trick_name);
        mTrickDescriptionEditText = (EditText) findViewById(R.id.edit_text_trick_description);
        mGoalCatchesEditText = (EditText) findViewById(R.id.edit_text_goal_catches);
        mGoalCatchesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPropTypeEditText = (EditText) findViewById(R.id.edit_text_prop_type);

        if (getIntent().hasExtra(ARG_TRICK_OBJECT)) {
            mTricks = getIntent().getExtras().getParcelable(ARG_TRICK_OBJECT);
            mName = mTricks.getName();
            mSiteswap = mTricks.getSiteswap();
            mSource = mTricks.getSource();
            mAnimation = mTricks.getAnimation();
            mTutorial = mTricks.getTutorial();
            mDifficulty = mTricks.getDifficulty();
            mCapacity = mTricks.getCapacity();
            mDescription = mTricks.getDescription();
            if (mName.length() > 0) mTrickNameEditText.setText(mName);
            if (mDescription.length() > 0) {
                mTrickDescriptionEditText.setText(mDescription.substring(0, 60) + "...");
                mUseDefaultDescription = true;
            }
        }

        Button addTrickButton = (Button) findViewById(R.id.button_add_trick_to_database);
        addTrickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_db();
            }
        });
    }

    public void add_to_db() {
        // Get values from
        mName = mTrickNameEditText.getText().toString();
        if (!mUseDefaultDescription) mDescription = mTrickDescriptionEditText.getText().toString();
        String goal = mGoalCatchesEditText.getText().toString();
        String propType = mPropTypeEditText.getText().toString();

        // Determine if trick name is already in the db
        boolean is_unique = true;

        // Can't have a trick with no name (check for null parameters)
        if (mName.length() < 1) is_unique = false;
        if (mDescription.length() < 1) mDescription = getString(R.string.default_description);
        if (goal.length() < 1) goal = getString(R.string.default_goal);
        if (propType.length() < 1) propType = getString(R.string.default_prop_type);
        if (mSiteswap== null) mSiteswap = "None Entered";
        if (mSource== null) mSource = "None Entered";
        if (mDifficulty== null) mDifficulty = "None Entered";
        if (mTutorial== null) mTutorial = "None Entered";
        if (mCapacity== null) mCapacity = "None Entered";
        if (mAnimation== null) mAnimation = "None Entered";

        // Get access to the preferences for list of trick names
        ArrayList<String> trickNames = new ArrayList<String>();
        // Look through all the tricks in the DB
        SharedPreferences settings = getApplicationContext().getSharedPreferences(ARG_SP_LOG_KEY, 0);
        String tricks_string = settings.getString(ARG_LIST_KEY, "");
        String[] stringLIst = tricks_string.split(mUnique);
        for (int i = 0; i < stringLIst.length; i++) trickNames.add(stringLIst[i]);
        if (trickNames.contains(mName)) is_unique = false;

        if (is_unique) {
            // Insert the trick into the database
            Actions.insert_trick(this, "0", "0", mDescription,
                    mName, "yes", "0", "0", "0", propType, goal,
                    mSiteswap, mAnimation, mSource, mDifficulty, mCapacity,
                    mTutorial, "no location");
            // Inform the user that the trick has been added to the DB
            Toast.makeText(this,mName + " " + getString(R.string.into_db),
                    Toast.LENGTH_SHORT).show();
            // Reset the hints on the edittext boxes
            mTrickNameEditText.setHint(R.string.name_hint);
            mTrickDescriptionEditText.setHint(R.string.description_hint);
            mGoalCatchesEditText.setHint(R.string.goal_hint);
            // update list of trick names in shared preferences
            settings.edit().putString(ARG_LIST_KEY, tricks_string + mUnique + mName).commit();
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "add activity id");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "add activity item name");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            // Update the widget
            trickNames.add(mName);
            writeToWidget(trickNames);
        } else {
            // The user is trying to enter a trick name that already exists in the database
            Toast.makeText(this, getString(R.string.already_in_db) +
                    getString(R.string.make_unique_name), Toast.LENGTH_SHORT).show();
        }
    }

    public void writeToWidget(ArrayList<String> names){
        String output = "Big Top Tricks\nTraining Database:\n";
        for (int i = 0; i < names.size(); i++) {
            if(names.get(i).length()>0) output = output + names.get(i) + "\n";
        }
        // Get app widget manager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        // Create a remote view
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.tricks_widget);
        ComponentName thisWidget = new ComponentName(this, TricksWidget.class);
        // Set the widget text and update the widget
        Log.d("LOG", "asdf " + names.size()+ output);
        remoteViews.setTextViewText(R.id.appwidget_text, output);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        // Tell the user that the ingredients have been added to the widget
        Toast.makeText(this, "Info written to widget", Toast.LENGTH_SHORT).show();
    }
}
