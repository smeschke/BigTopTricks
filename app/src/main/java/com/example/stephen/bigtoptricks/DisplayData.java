package com.example.stephen.bigtoptricks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DisplayData extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final int LOADER_ID = 42;
    private Cursor mCursor;
    private Button mOutputButton;
    private List<Tricks> mTricks;
    private String mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        // up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }



    protected void output_data(View view) throws IOException {
/*        Log.d("LOG", "asdf isExternalStorageWritable: " + isExternalStorageWritable());
        Log.d("LOG", "asdf isExternalStorageReadable: " + isExternalStorageReadable());
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "juggling_data");
        //File file1 = getDir(Environment.DIRECTORY_PICTURES + "/test",//Context.MODE_PRIVATE);



        //File file2 = getPublicAlbumStorageDir("jugglingTest");*/

        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "juggling_data");
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "jugglingfile");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("m juggling text");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
        }
    }

    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("LOG", "asdf Directory not created");
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    ///////////////////////////////////START CURSOR LOADER METHODS /////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        /*return new CursorLoader(this,
                Contract.listEntry.CONTENT_URI,
                null,
                Contract.listEntry.COLUMN_IS_META + "=?",
                new String[]{"no"},
                Contract.listEntry.COLUMN_TIMESTAMP);}*/
        // for now load all the elements in the database for testing purposes
        return new CursorLoader(this, Contract.listEntry.CONTENT_URI, null,
                null, null, Contract.listEntry.COLUMN_TIMESTAMP);
    }

    // When loading is finished, swap in the new data
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        bindView(data);
    }

    // Reset the loader
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    /////////////////////////////////// END CURSOR LOADER METHODS //////////////////////////////////

    private void bindView(Cursor data) {
        ArrayList<Tricks> listTricks = new ArrayList<>();
        String text = "";
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            String name = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
            String goal = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_GOAL));
            String pr = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_PERSONAL_RECORD));
            String timeTrained = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TIME_TRAINED));
            String catchCount = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_RECORD));
            String location = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_LOCATION));

            Tricks tricks = new Tricks();
            tricks.setName(name);
            tricks.setGoal(goal);
            tricks.setPr(pr);
            tricks.setTime_trained(timeTrained);
            tricks.setLocation(location);
            listTricks.add(tricks);

            text += name + "\n"
                    + "    NumCatches: " + catchCount + "\n"
                    + "    Time Trained: " + timeTrained + "\n"
                    + "    Location: " + location + "\n\n";
        }
        mTricks = listTricks;
        TextView database_textview = (TextView) findViewById(R.id.database_textview);
        database_textview.setText(text);
        mText = text;
    }
}
