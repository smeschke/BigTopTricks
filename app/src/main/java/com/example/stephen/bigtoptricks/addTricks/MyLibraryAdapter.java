package com.example.stephen.bigtoptricks.addTricks;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Trick;
import com.example.stephen.bigtoptricks.data.Contract;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryAdapter extends RecyclerView.Adapter<MyLibraryAdapter.mAdapterViewHolder> {

    //get context
    private final Context mContext;
    //set up the click handler
    final private mAdapterOnClickHandler mClickHandler;
    //create string for output list
    private String mJson;
    //create list of trick objects that will populate the recycler view
    private List<Trick> mTricks;

    // Constructor gets context and click handler
    public MyLibraryAdapter(@NonNull Context context,
                            mAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    // When view holder is created, inflate the views
    @NonNull
    @Override
    public MyLibraryAdapter.mAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull MyLibraryAdapter.mAdapterViewHolder holder, int position) {
        // Create a new trick to put in the recycler view item
        Trick trick = new Trick();
        // IF position is zero, use 'create custom trick' for position zero
        if (position == 0) {
            if (position == 0 && getItemCount() == 1) trick.setName("Loading: Please wait up to one minute");
            else {
                trick.setName(mContext.getString(R.string.create_custom_trick));
            }
        }
        // ELSE, use a trick from the library to make the rest of the list
        else trick = mTricks.get(position - 1);

        String name = trick.getName();
        String capacity = trick.getCapacity();
        String difficulty = trick.getDifficulty();
        String source = trick.getSource();

        holder.textView.setText(name);
        holder.capacityTextView.setText(capacity);
        holder.sourcesTextView.setText(source);

        try {
            if (difficulty.equals("1"))
                holder.difficultyImageView.setImageResource(R.drawable.diff1);
            if (difficulty.equals("2"))
                holder.difficultyImageView.setImageResource(R.drawable.diff2);
            if (difficulty.equals("3"))
                holder.difficultyImageView.setImageResource(R.drawable.diff3);
            if (difficulty.equals("4"))
                holder.difficultyImageView.setImageResource(R.drawable.diff4);
            if (difficulty.equals("5"))
                holder.difficultyImageView.setImageResource(R.drawable.diff5);
            if (difficulty.equals("6"))
                holder.difficultyImageView.setImageResource(R.drawable.diff6);
            if (difficulty.equals("7"))
                holder.difficultyImageView.setImageResource(R.drawable.diff7);
            if (difficulty.equals("8"))
                holder.difficultyImageView.setImageResource(R.drawable.diff8);
            if (difficulty.equals("9"))
                holder.difficultyImageView.setImageResource(R.drawable.diff9);
            if (difficulty.equals("10"))
                holder.difficultyImageView.setImageResource(R.drawable.diff10);
        } catch (Exception e) {
            holder.difficultyImageView.setImageResource(R.drawable.diff0);
        }
    }

    //How many? The size of the output_list.
    @Override
    public int getItemCount() {
        try {
            return mTricks.size() + 1;
        } catch (Exception e) {
            return 1;
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
        final TextView textView;
        final TextView capacityTextView;
        final ImageView difficultyImageView;
        final TextView sourcesTextView;

        //super the views so that they can be bound - set click listener too
        mAdapterViewHolder(View view) {
            super(view);
            //image  and text views for tricks
            textView = view.findViewById(R.id.siteswap_list_item_textview);
            capacityTextView = view.findViewById(R.id.siteswap_list_item_capacity);
            difficultyImageView = view.findViewById(R.id.library_difficulty_image_view);
            sourcesTextView = view.findViewById(R.id.siteswap_list_item_source);
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
    void swapCursor(Cursor data) {
        /*mJson = json;
        // Parse the tricks out of the JSON data
        mTricks = JsonUtils.parseLimitedObjects(json);
        notifyDataSetChanged();*/

        ArrayList<Trick> listTricks = new ArrayList<>();
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            String name = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
            String capacity = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_CAPACITY));
            String difficulty = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_DIFFICULTY));
            String source = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_SOURCE));
            String siteswap = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_SITESWAP));
            Trick trick = new Trick();
            trick.setName(name);
            trick.setCapacity(capacity);
            trick.setDifficulty(difficulty);
            trick.setSource(source);
            trick.setSiteswap(siteswap);
            listTricks.add(trick);
        }
        mTricks = listTricks;
        notifyDataSetChanged();
    }
}
