package com.example.stephen.bigtoptricks;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

public class AddTrick extends AppCompatActivity {

    private EditText mTrickNameEditText;
    private EditText mTrickDescriptionEditText;
    private EditText mGoalCatchesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick);

        mTrickNameEditText = (EditText) findViewById(R.id.edit_text_trick_name);
        mTrickDescriptionEditText = (EditText) findViewById(R.id.edit_text_trick_description);
        mGoalCatchesEditText = (EditText) findViewById(R.id.edit_text_goal_catches);
        mGoalCatchesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        // If the user has selected a trick from the list activity, there will be extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("trickName")) mTrickNameEditText.setText(extras.getString("trickName"));
            if (extras.containsKey("trickDesc")) mTrickDescriptionEditText.setText(extras.getString("trickDesc"));
        }

        Button addTrickButton = (Button) findViewById(R.id.button_add_trick_to_database);
        addTrickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_db();
            }
        });
    }

    public void add_to_db(){
        String trickName = mTrickNameEditText.getText().toString();
        String trickDescription = mTrickDescriptionEditText.getText().toString();
        String goal = mGoalCatchesEditText.getText().toString();

        // Fill content values with trick attributes
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, "0");
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, "0");
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, trickDescription);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, trickName);
        cv.put(Contract.listEntry.COLUMN_IS_META, "yes");
        cv.put(Contract.listEntry.COLUMN_RECORD, "0");
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);

        // Inform the user that the trick has been added to the DB
        Toast.makeText(this, "add to db "+ uri.toString(), Toast.LENGTH_SHORT).show();

        // Reset the hints on the edittext boxes
        mTrickNameEditText.setHint("Enter Trick Name...");
        mTrickDescriptionEditText.setHint("Enter Trick Description (optional)...");
        mGoalCatchesEditText.setHint("Goal (#catches, #reps, time, etc...");
    }
}
