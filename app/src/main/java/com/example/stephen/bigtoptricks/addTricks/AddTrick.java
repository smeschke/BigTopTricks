package com.example.stephen.bigtoptricks.addTricks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.data.Actions;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import static com.example.stephen.bigtoptricks.TrickDetail.mUnique;

public class AddTrick extends AppCompatActivity {

    private EditText mTrickNameEditText;
    private EditText mTrickDescriptionEditText;
    private EditText mGoalCatchesEditText;
    private EditText mPropTypeEditText;
    private String mTrickName;
    private String mTrickDescription;
    private String mTrickCapacity;
    private String mTrickSiteswap;
    private String mTrickSource;
    private String mTrickAnimation;
    private String mTrickTutorial;
    private String mTrickDifficulty;
    private ArrayList<String> mTrickDetails = new ArrayList<String>();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick);

        mTrickNameEditText = (EditText) findViewById(R.id.edit_text_trick_name);
        mTrickDescriptionEditText = (EditText) findViewById(R.id.edit_text_trick_description);
        mGoalCatchesEditText = (EditText) findViewById(R.id.edit_text_goal_catches);
        mGoalCatchesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPropTypeEditText = (EditText) findViewById(R.id.edit_text_prop_type);

        if (getIntent().hasExtra("details")) {
            mTrickDetails = getIntent().getStringArrayListExtra("details");
            mTrickName = mTrickDetails.get(0);
            mTrickCapacity = mTrickDetails.get(1);
            mTrickSiteswap = mTrickDetails.get(2);
            mTrickAnimation = mTrickDetails.get(3);
            mTrickTutorial = mTrickDetails.get(4);
            mTrickDifficulty = mTrickDetails.get(5);
            mTrickDescription = mTrickDetails.get(7);
            mTrickSource = mTrickDetails.get(8);
            mTrickNameEditText.setText(mTrickName);
            mTrickDescriptionEditText.setText(mTrickDescription.substring(0, 60) + "...");
        } else {
            mTrickName = "custom trick";
            mTrickCapacity = "custom trick";
            mTrickSiteswap = "custom trick";
            mTrickAnimation = "custom trick";
            mTrickTutorial = "custom trick";
            mTrickDifficulty = "custom trick";
            mTrickDescription = "custom trick";
            mTrickSource = "custom trick";
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
        String trickName = mTrickNameEditText.getText().toString();
        String trickDescription = mTrickDescriptionEditText.getText().toString();
        String goal = mGoalCatchesEditText.getText().toString();
        String propType = mPropTypeEditText.getText().toString();

        // Determine if trick name is already in the db
        boolean is_unique = true;

        // Can't have a trick with no name (check for null parameters)
        if (trickName.length() < 1) is_unique = false;
        if (trickDescription.length() < 1) trickDescription = "No description for this trick.";
        if (goal.length() < 1) goal = "No goal for this trick.";
        if (propType.length() < 1) propType = "No prop type for this trick.";

        // Get access to the preferences for list of trick names
        ArrayList<String> mTrickNames = new ArrayList<String>();
        // Look through all the tricks in the DB
        SharedPreferences settings = getApplicationContext().getSharedPreferences("log", 0);
        String tricks_string = settings.getString("tricks", "");
        String[] stringLIst = tricks_string.split(mUnique);
        for (int i = 0; i < stringLIst.length; i++) mTrickNames.add(stringLIst[i]);
        if (mTrickNames.contains(trickName)) is_unique = false;
        if (is_unique) {
            // Insert the trick into the database
            Actions.insert_trick(this, "0", "0", trickDescription,
                    trickName, "yes", "0", "0", "0", propType, goal,
                    mTrickSiteswap, mTrickAnimation, mTrickSource, mTrickDifficulty, mTrickCapacity,
                    mTrickTutorial);
            // Inform the user that the trick has been added to the DB
            Toast.makeText(this,mTrickName + " has been entered into the training DB.",
                    Toast.LENGTH_SHORT).show();
            // Reset the hints on the edittext boxes
            mTrickNameEditText.setHint("Enter Trick Name...");
            mTrickDescriptionEditText.setHint("Enter Trick Description (optional)...");
            mGoalCatchesEditText.setHint("Goal (#catches, #reps, time, etc...");
            // update list of trick names in shared preferences
            settings.edit().putString("tricks", tricks_string + mUnique + trickName).commit();
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "add activity id");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "add activity item name");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        } else {
            // The user is trying to enter a trick name that already exists in the database
            Toast.makeText(this, "Trick name is already on the training manifest.\n" +
                    "Please, enter a unique name for this trick.", Toast.LENGTH_SHORT).show();
        }
    }
}
