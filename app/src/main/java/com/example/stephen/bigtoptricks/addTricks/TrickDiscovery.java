package com.example.stephen.bigtoptricks.addTricks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Trick;

import java.util.Objects;

import static com.example.stephen.bigtoptricks.Training.ARG_HIDE_ADD_BUTTON;
import static com.example.stephen.bigtoptricks.Training.ARG_TRICK_OBJECT;

public class TrickDiscovery extends AppCompatActivity {

    private String mTutorial;
    private Trick mTrick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        // Get the trick that the user clicked on in the Library of Trick
        mTrick = (getIntent().getExtras()).getParcelable(ARG_TRICK_OBJECT);
        String mName = (mTrick).getName();
        String mSiteswap = mTrick.getSiteswap();
        String mSource = mTrick.getSource();
        String mAnimation = mTrick.getAnimation();
        mTutorial = mTrick.getTutorial();
        String mDifficulty = mTrick.getDifficulty();
        String mCapacity = mTrick.getCapacity();
        String mDescription = mTrick.getDescription();

        // Set text views and web view
        TextView title_text_view = findViewById(R.id.discovery_title_text_view);
        title_text_view.setText(mName);
        TextView details_text_view = findViewById(R.id.discover_description_text_view);
        details_text_view.setText(Html.fromHtml(mDescription));
        WebView wv = findViewById(R.id.discovery_animation);
        wv.loadUrl(mAnimation);
        TextView siteswap = findViewById(R.id.discovery_siteswap_text_view);
        siteswap.setText(getString(R.string.siteswap) + " " + mSiteswap);
        TextView capacity = findViewById(R.id.discovery_capacity_text_view);
        capacity.setText(getString(R.string.capacity) + " "  + mCapacity);
        TextView source = findViewById(R.id.discovery_source_text_view);
        source.setText(getString(R.string.source) + " "  + mSource);
        TextView tutorial = findViewById(R.id.discovery_tutorial_text_view);
        tutorial.setText(getString(R.string.tutorial) + " "  + mTutorial);
        TextView difficulty = findViewById(R.id.discovery_difficulty_text_view);
        difficulty.setText(getString(R.string.difficulty) + " "  + mDifficulty);

        // Should the add button be hidden?
        if (getIntent().hasExtra(ARG_HIDE_ADD_BUTTON)) {
            FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
    }

    public void addTrick(View view){
        Intent toAddTrick = new Intent(this, AddTrick.class);
        toAddTrick.putExtra(ARG_TRICK_OBJECT, mTrick);
        startActivity(toAddTrick);
    }

    public void toUrl(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTutorial));
        startActivity(browserIntent);
    }
}
