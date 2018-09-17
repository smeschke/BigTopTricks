package com.example.stephen.bigtoptricks.addTricks;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.data.Contract;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class AddTrick extends AppCompatActivity {

    private EditText mTrickNameEditText;
    private EditText mTrickDescriptionEditText;
    private EditText mGoalCatchesEditText;
    private EditText mPropTypeEditText;
    private final static String mUnique = "unique_delimiter";

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
            Log.d("LOG", "asdf intent has extras");
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
            mTrickDescriptionEditText.setText(mTrickDescription.substring(0, 234) + "...");
        }
        else{
            Log.d("LOG", "asdf intent has NONONON extras");
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
        Log.d("LOG", "asdf trick Name: " + trickName);


        // determine if trick name is already in the db
        boolean is_unique = true;

        // Can't have a trick with no name
        if (trickName.length()<1) is_unique = false;
        if (trickDescription.length()<1) trickDescription = "No description for this trick.";
        if (goal.length()<1) goal = "No goal for this trick.";
        if (propType.length()<1) propType = "No prop type for this trick.";

        // Get access to the preferences for list of trick names
        ArrayList<String> mTrickNames = new ArrayList<String>();
        // Get access to the preferences
        SharedPreferences settings = getApplicationContext().
                getSharedPreferences("log", 0);
        SharedPreferences.Editor editor = settings.edit();
        String tricks_string = settings.getString("tricks", "");
        Log.d("LOG", "asdf tricks String: " + tricks_string);
        String[] stringLIst = tricks_string.split(mUnique);
        for (int i = 0; i < stringLIst.length; i++) {
            mTrickNames.add(stringLIst[i]);
            Log.d("LOG", "asdf : " + stringLIst[i]);
        }
        if(mTrickNames.contains(trickName)) is_unique=false;

        if (is_unique) {
            // Fill content values with trick attributes
            Log.d("LOG", "asdf add trick to db: " + mTrickCapacity + mTrickAnimation
                    + mTrickDifficulty + mTrickTutorial + mTrickSiteswap);
            ContentValues cv = new ContentValues();
            cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, "0");
            cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, "0");
            cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, trickDescription);
            cv.put(Contract.listEntry.COLUMN_TRICK_NAME, trickName);
            cv.put(Contract.listEntry.COLUMN_IS_META, "yes");
            cv.put(Contract.listEntry.COLUMN_HIT, "0");
            cv.put(Contract.listEntry.COLUMN_MISS, "0");
            cv.put(Contract.listEntry.COLUMN_RECORD, "0");
            cv.put(Contract.listEntry.COLUMN_PROP_TYPE, propType);
            cv.put(Contract.listEntry.COLUMN_GOAL, goal);
            cv.put(Contract.listEntry.COLUMN_SITESWAP, mTrickSiteswap);
            cv.put(Contract.listEntry.COLUMN_ANIMAION, mTrickAnimation);
            cv.put(Contract.listEntry.COLUMN_SOURCE, mTrickSource);
            cv.put(Contract.listEntry.COLUMN_DIFFICULTY, mTrickDifficulty);
            cv.put(Contract.listEntry.COLUMN_CAPACITY, mTrickCapacity);
            cv.put(Contract.listEntry.COLUMN_TUTORIAL, mTrickTutorial);
            // Insert the content values via a ContentResolver
            Uri uri = getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);

            // Inform the user that the trick has been added to the DB
            Toast.makeText(this,
                    mTrickName + " has been entered into the training DB.",
                    Toast.LENGTH_SHORT).show();

            // Reset the hints on the edittext boxes
            mTrickNameEditText.setHint("Enter Trick Name...");
            mTrickDescriptionEditText.setHint("Enter Trick Description (optional)...");
            mGoalCatchesEditText.setHint("Goal (#catches, #reps, time, etc...");


            // update list of trick names in shared preferences
            editor.putString("tricks", tricks_string + mUnique + trickName).commit();

            ////////////////////// FIREBASE ANALYTICS //////////////////////////////////////////////
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "add activity id");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "add activity item name");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        } else {
            Log.d("LOG", "asdf contains");
            Toast.makeText(this, "Trick name is already on the training manifest.\n" +
                    "Please, enter a unique name for this trick.", Toast.LENGTH_SHORT).show();
        }
    }
}
