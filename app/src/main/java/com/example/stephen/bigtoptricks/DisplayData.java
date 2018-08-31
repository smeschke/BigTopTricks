package com.example.stephen.bigtoptricks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.data.Contract;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

public class DisplayData extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mCursor;
    private final int LOADER_ID = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_data);


        //GraphView graph = (GraphView) findViewById(R.id.graph);
        //initGraph(graph);

        // Initialize loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void initGraph(GraphView graph) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 5),
                new DataPoint(2, -3),
                new DataPoint(3, 2)
        });
        series.setSpacing(50); // 50% spacing between bars
        series.setAnimated(true);
        graph.addSeries(series);

        // set the viewport wider than the data, to have a nice view
        graph.getViewport().setMinX(0d);
        graph.getViewport().setMaxX(4d);
        graph.getViewport().setXAxisBoundsManual(true);
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
        bindView();
    }

    // Reset the loader
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    /////////////////////////////////// END CURSOR LOADER METHODS //////////////////////////////////


    private void bindView() {
        String text = "";
        for (int position = 0; position < mCursor.getCount(); position++) {
            mCursor.moveToPosition(position);
            String name = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_TRICK_NAME));
            String catchCount = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_RECORD));
            String timeTrained = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_TIME_TRAINED));
            String pr = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_PERSONAL_RECORD));
            String isMeta = mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_IS_META));
            String id= mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry._ID));
            String timestamp= mCursor.getString(mCursor.getColumnIndex(
                    Contract.listEntry.COLUMN_TIMESTAMP));
            text += name + "\n"
                    + "    NumCatches: " + catchCount + "\n"
                    + "    Time Trained: " + timeTrained + "\n"
                    + "    PR: " + pr + "\n"
                    + "    ID: " + id + "\n"
                    + "    TimeStamp: " + timestamp + "\n"
                    + "    Meta: " + isMeta + "\n\n";
            Log.d("LOG", "asdf " + catchCount);
        }
        TextView database_textview = (TextView) findViewById(R.id.database_textview);
        database_textview.setText(text);
    }
}
