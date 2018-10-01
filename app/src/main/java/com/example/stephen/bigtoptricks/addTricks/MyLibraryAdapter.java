package com.example.stephen.bigtoptricks.addTricks;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        // Create a new trick to put in the recycler view item
        Tricks tricks = new Tricks();
        // Use 'create custom trick' for position zero
        if (position == 0) tricks.setName(mContext.getString(R.string.create_custom_trick));
        // Use a tricks from the library to make the rest of the list
        else tricks = mTricks.get(position-1);

        String name = tricks.getName();
        String capacity = tricks.getCapacity();
        String difficulty = tricks.getDifficulty();
        String source = tricks.getSource();
        holder.textView.setText(name);

        //holder.difficultyImageView.setImageResource(R.drawable.diff1);
        // Display appropriate image for difficulty
        try {
            if (difficulty.equals("1")) holder.difficultyImageView.setImageResource(R.drawable.diff1);
            if (difficulty.equals("2")) holder.difficultyImageView.setImageResource(R.drawable.diff2);
            if (difficulty.equals("3")) holder.difficultyImageView.setImageResource(R.drawable.diff3);
            if (difficulty.equals("4")) holder.difficultyImageView.setImageResource(R.drawable.diff4);
            if (difficulty.equals("5")) holder.difficultyImageView.setImageResource(R.drawable.diff5);
            if (difficulty.equals("6")) holder.difficultyImageView.setImageResource(R.drawable.diff6);
            if (difficulty.equals("7")) holder.difficultyImageView.setImageResource(R.drawable.diff7);
            if (difficulty.equals("8")) holder.difficultyImageView.setImageResource(R.drawable.diff8);
            if (difficulty.equals("9")) holder.difficultyImageView.setImageResource(R.drawable.diff9);
            if (difficulty.equals("10")) holder.difficultyImageView.setImageResource(R.drawable.diff10);
        } catch (Exception e) {
            holder.difficultyImageView.setImageResource(R.drawable.diff0);
        }

        holder.capacityTextView.setText(capacity);
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
        public final ImageView difficultyImageView;
        public final TextView sourcesTextView;

        //super the views so that they can be bound - set click listener too
        mAdapterViewHolder(View view) {
            super(view);
            //image  and text views for tricks
            textView = (TextView) view.findViewById(R.id.siteswap_list_item_textview);
            capacityTextView = (TextView) view.findViewById(R.id.siteswap_list_item_capacity);
            difficultyImageView = (ImageView) view.findViewById(R.id.library_difficulty_image_view);
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
