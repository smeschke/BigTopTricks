package com.example.stephen.bigtoptricks;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

public class AddRoutine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);
    }

    public void easy_ball_routine(View view){
        add_to_db("Three ball cascade",
                "Juggling three balls. Tossing each ball to the opposite hand. " +
                        "Should be around face height.",
                "100 catches");
        add_to_db("Two balls in one hand",
                "Practice with each hand individually. If one hand becomes better, " +
                        "switch to the weaker hand. This is slower and easier if the balls are thrown higher",
                "20 catches");
        // Inform the user that the trick has been added to the DB
        Toast.makeText(this, "Easy Ball Routine has been added to the DB", Toast.LENGTH_SHORT).show();
    }
    public void medium_ball_routine(View view){
        add_to_db("4 ball fountain",
                "Start with two in each hand. In this trick the balls should not cross from the left hand to the right hand. Make the throws higher than eye level. If you are chasing after the balls, try to collect them in your hands. Stand up straight and start again."
                ,"50 catches");
        add_to_db("5 ball cascade"
                ,"This is like the three ball cascade, but with two more balls. The top of the pattern should look exactly like the top of the three ball cascade pattern. Make those throws high! The balls should peak between 1 and 2 feet above the top of the head. Good luck, this trick is tough!"
                ,"25 catches");

        // Inform the user that the trick has been added to the DB
        Toast.makeText(this, "Medium Ball Routine has been added to the DB", Toast.LENGTH_SHORT).show();

    }
    public void easy_club_routine(View view){
        add_to_db("Spin Control",
                "Clubs are more difficult, because each throw must have the right height and the right amount of spin so that the club can be caught by the handle. Start with only one club and work on throwing the club from one hand to another, so that the club spins exactly once. Either avoid hitting yourself in the face, or invest in a mouth guard."
                ,"100 catches");
        add_to_db("3 club cascade",
                "This trick should only ve attempted after spin control has been mastered. Work on releasing two clubs from one hand before attempting the cascade."
                ,"20 catches.");
        // Inform the user that the trick has been added to the DB
        Toast.makeText(this, "Easy Club Routine has been added to the DB", Toast.LENGTH_SHORT).show();

    }

    public void add_to_db(String trickName, String trickDescription, String goal){
        // Fill content values with trick attributes
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, "0");
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, "0");
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, trickDescription);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, trickName);
        cv.put(Contract.listEntry.COLUMN_IS_RECORD, "no");
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        cv.put(Contract.listEntry.COLUMN_RECORD, "0");
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);
    }
}