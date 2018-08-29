package com.example.stephen.bigtoptricks;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.data.Contract;

import java.util.ArrayList;
import java.util.List;

//TODO (3) extend RV View Holder (implement onCreate, onBind, and getItemCount)
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //TODO (4b) Create ItemClickListener
    private ItemClickListener mClickListener;

    //TODO (2) get data from constructor
    private List<String> mTrickNames;
    private List<String> mGoals;
    private List<String> mPrs;

    private final LayoutInflater mInflater;
    private final Context mContext;

    // Create MyRecyclerViewAdapter
    MyAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    //TODO (5) inflate
    // Inflates the cell layout from recyclerview_item.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // Binds each poster to the cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the text in the textview
        holder.trickNameTextView.setText(mTrickNames.get(position));
        holder.prCatchesTextView.setText("PR: " + mPrs.get(position));
        holder.goalCatchesTextView.setText("Goal: " + mGoals.get(position));
        Log.d("LOG", "asdf onBindViewHolder: " + mTrickNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mTrickNames.size();
    }

    // TODO (6) create swap cursor method to reset the data
    void swapCursor(Cursor data) {
        // Move through the cursor and extract the movie poster urls.
        List<String> trickNames = new ArrayList<>();
        List<String> goals = new ArrayList<>();
        List<String> prs = new ArrayList<>();
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            String name = data.getString(data.getColumnIndex(
                    Contract.listEntry.COLUMN_TRICK_NAME));
            String goal = data.getString(data.getColumnIndex(
                    Contract.listEntry.COLUMN_GOAL));
            String pr = data.getString(data.getColumnIndex(
                    Contract.listEntry.COLUMN_PERSONAL_RECORD));
            String is_record = data.getString(data.getColumnIndex(
                    Contract.listEntry.COLUMN_IS_RECORD));
            if (is_record.equals("no")) {
                // Get the name of the trick
                trickNames.add(name);
                // Get the users goal for the trick
                goals.add(goal);
                // Get the users personal record
                prs.add(pr);
            }
        }
        mTrickNames = trickNames;
        mGoals = goals;
        mPrs = prs;
        notifyDataSetChanged();
    }

    //TODO (1) create ViewHolder class - implement OnClickListener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView trickNameTextView;
        final TextView goalCatchesTextView;
        final TextView prCatchesTextView;

        // Set the click listener to the image view
        // When a user clicks on an image --> something happens based on the image a user clicked.
        ViewHolder(View itemView) {
            super(itemView);
            trickNameTextView = itemView.findViewById(R.id.text_view_trick_name_list_item);
            goalCatchesTextView = itemView.findViewById(R.id.text_view_goal_catches_list_item);
            prCatchesTextView = itemView.findViewById(R.id.text_view_pr_catches_list_item);
            itemView.setOnClickListener(this);
        }

        //TODO (4c) 'wire' into ViewHolder
        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition());
        }
    }

    //TODO (4a) Create interface for the click listener
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}