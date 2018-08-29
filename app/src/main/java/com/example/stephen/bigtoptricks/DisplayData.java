package com.example.stephen.bigtoptricks;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.data.Contract;

import org.w3c.dom.Text;

public class DisplayData extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private Cursor mCursor;
    private final int LOADER_ID = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_data);

        // Initialize loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    ///////////////////////////////////START CURSOR LOADER METHODS /////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, Contract.listEntry.CONTENT_URI,null,
                null,null, Contract.listEntry.COLUMN_TIMESTAMP);}
    // When loading is finished, swap in the new data
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        bindView();
    }
    // Reset the loader
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    /////////////////////////////////// END CURSOR LOADER METHODS //////////////////////////////////


    private void bindView() {
        String text = "";
        for (int position = 0; position < mCursor.getCount(); position++){
            mCursor.moveToPosition(position);
            String name = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_TRICK_NAME));
            String pr = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_PERSONAL_RECORD));
            String catchCount = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_RECORD));
            text += name + " NumCatches: " + catchCount + " PR: " + pr + "\n";
            Log.d("LOG", "asdf " + catchCount);
        }
        TextView database_textview = (TextView) findViewById(R.id.database_textview);
        database_textview.setText(text);
    }
}
