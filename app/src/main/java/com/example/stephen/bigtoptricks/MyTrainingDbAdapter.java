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
public class MyTrainingDbAdapter extends RecyclerView.Adapter<MyTrainingDbAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    //TODO (4b) Create ItemClickListener
    private ItemClickListener mClickListener;
    //TODO (2) get data from constructor
    private List<Tricks> mTricks;

    // Create MyRecyclerViewAdapter
    MyTrainingDbAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    //TODO (5) inflate
    // Inflates the cell layout from recyclerview_item.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trick_list_item, parent, false);
        return new ViewHolder(view);
    }

    // Binds each poster to the cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the text in the textview
        String name = mTricks.get(position).getName();
        String pr = mTricks.get(position).getPr();
        String goal = mTricks.get(position).getGoal();
        String timeTrained = mTricks.get(position).getTime_trained();
        holder.trickNameTextView.setText(name);
        if (pr!=null) holder.prCatchesTextView.setText("PR: " + pr);
        if (goal!=null) holder.goalCatchesTextView.setText("Goal: " + goal);
        if (timeTrained!=null) holder.timeTrainedTextView.setText("Time Trained: " + timeTrained);
    }

    @Override
    public int getItemCount() {

        try {
            return mTricks.size();
        } catch (Exception e) {
            return 0;
        }
    }

    // TODO (6) create swap cursor method to reset the data
    void swapCursor(Cursor data) {
        ArrayList<Tricks> listTricks = new ArrayList<>();
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            String name = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
            String goal = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_GOAL));
            String pr = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_PERSONAL_RECORD));
            String time_trained = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TIME_TRAINED));
            Tricks tricks = new Tricks();
            tricks.setName(name);
            tricks.setGoal(goal);
            tricks.setPr(pr);
            tricks.setTime_trained(time_trained);
            listTricks.add(tricks);
        }
        mTricks = listTricks;
        notifyDataSetChanged();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    //TODO (4a) Create interface for the click listener
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    //TODO (1) create ViewHolder class - implement OnClickListener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView trickNameTextView;
        final TextView goalCatchesTextView;
        final TextView prCatchesTextView;
        final TextView timeTrainedTextView;

        // Set the click listener to the image view
        // When a user clicks on an image --> something happens based on the image a user clicked.
        ViewHolder(View itemView) {
            super(itemView);
            timeTrainedTextView = itemView.findViewById(R.id.text_view_time_trained);
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
}