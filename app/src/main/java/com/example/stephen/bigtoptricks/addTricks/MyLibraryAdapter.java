package com.example.stephen.bigtoptricks.addTricks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Tricks;

import org.json.JSONException;

import java.util.List;

public class MyLibraryAdapter extends RecyclerView.Adapter<MyLibraryAdapter.mAdapterViewHolder> {

    //get context
    private final Context mContext;
    //set up the click handler
    final private mAdapterOnClickHandler mClickHandler;
    //create string for output list
    private String mJson;
    //create list of trick objects that will populate the recycler view
    private List<Tricks> mTricks;

    // Constructor gets context and click handler
    public MyLibraryAdapter(@NonNull Context context,
                            mAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    // When view holder is created, inflate the views
    @Override
    public MyLibraryAdapter.mAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = 0;
        if (viewType == 0) layoutId = R.layout.library_list_item_custom;
        if (viewType == 1) layoutId = R.layout.library_list_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new mAdapterViewHolder(view);
    }

    //override getItemViewType (from S11.02)
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    //bind data to view holder
    @Override
    public void onBindViewHolder(MyLibraryAdapter.mAdapterViewHolder holder, int position) {
        Tricks tricks = new Tricks();
        if (position == 0) tricks.setName("Create Custom Trick");
        else tricks = mTricks.get(position-1);
        String name = tricks.getName();
        String capacity = tricks.getCapacity();
        String difficulty = tricks.getDifficulty();
        String source = tricks.getSource();
        holder.textView.setText(name);
        // Create capacity string (one * for every object)
        String capacityStr = "";
        try {
            for (int i = 0; i < Integer.parseInt(capacity); i++) capacityStr += "* ";
        } catch (Exception e) {
            capacityStr = "";
        }
        // Create difficulty string (one + for every difficulty level)
        String difficultyStr = "";
        try {
            for (int i = 0; i < Integer.parseInt(difficulty); i++) difficultyStr += "+ ";
        } catch (Exception e) {
            difficultyStr = "";
        }
        holder.capacityTextView.setText(capacityStr);
        holder.difficultyTextView.setText(difficultyStr);
        holder.sourcesTextView.setText(source);
    }

    //How many? The size of the output_list.
    @Override
    public int getItemCount() {
        try {
            return JsonUtils.getNumberOfTricks(mJson);
        } catch (Exception e) {
            return 0;
        }
    }

    //set up clicks
    public interface mAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    //setting up the recycler view, and clicks
    class mAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        //initialize views
        public final TextView textView;
        public final TextView capacityTextView;
        public final TextView difficultyTextView;
        public final TextView sourcesTextView;

        //super the views so that they can be bound - set click listener too
        mAdapterViewHolder(View view) {
            super(view);
            //image  and text views for tricks
            textView = (TextView) view.findViewById(R.id.siteswap_list_item_textview);
            capacityTextView = (TextView) view.findViewById(R.id.siteswap_list_item_capacity);
            difficultyTextView = (TextView) view.findViewById(R.id.siteswap_list_item_difficulty);
            sourcesTextView = (TextView) view.findViewById(R.id.siteswap_list_item_source);
            //set on click listener
            itemView.setOnClickListener(this);
        }

        //when a user taps the list
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            // Send the user to the appropriate trick discovery activity
            mClickHandler.onClick(adapterPosition);
        }
    }

    // TODO (6) create swap cursor method to reset the data
    void swapCursor(String json) throws JSONException {
        mJson = json;
        // Parse the tricks out of the JSON data
        mTricks = JsonUtils.parseLimitedObjects(json);
        notifyDataSetChanged();
    }
}
