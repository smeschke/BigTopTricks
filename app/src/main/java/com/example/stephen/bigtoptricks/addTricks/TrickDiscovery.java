package com.example.stephen.bigtoptricks.addTricks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Tricks;

import static com.example.stephen.bigtoptricks.Training.ARG_TRICK_OBJECT;

public class TrickDiscovery extends AppCompatActivity {

    private String mName;
    private String mDescription;
    private String mCapacity;
    private String mSiteswap;
    private String mSource;
    private String mAnimation;
    private String mTutorial;
    private String mDifficulty;
    private Tricks mTricks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        // Get the trick that the user clicked on in the Library of Tricks
        mTricks = getIntent().getExtras().getParcelable(ARG_TRICK_OBJECT);
        mName = mTricks.getName();
        mSiteswap = mTricks.getSiteswap();
        mSource = mTricks.getSource();
        mAnimation = mTricks.getAnimation();
        mTutorial = mTricks.getTutorial();
        mDifficulty = mTricks.getDifficulty();
        mCapacity = mTricks.getCapacity();
        mDescription = mTricks.getDescription();

        TextView title_text_view = (TextView) findViewById(R.id.discovery_title_text_view);
        title_text_view.setText(mName);
        TextView details_text_view = (TextView) findViewById(R.id.discover_description_text_view);
        details_text_view.setText(Html.fromHtml(mDescription));
        WebView wv = (WebView) findViewById(R.id.discovery_animation);
        wv.loadUrl(mAnimation);
        TextView siteswap = (TextView) findViewById(R.id.discovery_siteswap_text_view);
        siteswap.setText("Siteswap: " + mSiteswap);
        TextView capacity = (TextView) findViewById(R.id.discovery_capacity_text_view);
        capacity.setText("Capacity: " + mCapacity);
        TextView source = (TextView) findViewById(R.id.discovery_source_text_view);
        source.setText("Source: " + mSource);
        TextView tutorial = (TextView) findViewById(R.id.discovery_tutorial_text_view);
        tutorial.setText("Tutorial: " + mTutorial);
        TextView difficulty = (TextView) findViewById(R.id.discovery_difficulty_text_view);
        difficulty.setText("Difficulty: " + mDifficulty);
    }

    public void addTrick(View view){
        Intent toAddTrick = new Intent(this, AddTrick.class);
        toAddTrick.putExtra(ARG_TRICK_OBJECT, mTricks);
        startActivity(toAddTrick);
    }

    public void toUrl(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTutorial));
        startActivity(browserIntent);
    }
}
