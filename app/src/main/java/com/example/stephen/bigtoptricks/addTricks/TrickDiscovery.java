package com.example.stephen.bigtoptricks.addTricks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.R;
import com.example.stephen.bigtoptricks.Tricks;
import com.example.stephen.bigtoptricks.addTricks.AddTrick;

import java.util.ArrayList;

import static com.example.stephen.bigtoptricks.TrickDetail.ARG_TRICK_OBJECT;

public class TrickDiscovery extends AppCompatActivity {

    private String mTrickName;
    private String mTrickDescription;

    private String mName;
    private String mPr;
    private String mGoal;
    private String mDescription;
    private String mTimeTrained;
    private String mId;
    private String mHits;
    private String mMisses;
    private String mPropType;
    private String mRecords;
    private String mCapacity;
    private String mSiteswap;
    private String mSource;
    private String mAnimation;
    private String mTutorial;
    private String mDifficulty;
    private Tricks mTricks;


    private ArrayList<String> mTrickDetails = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_discovery);

        mTricks = getIntent().getExtras().getParcelable(ARG_TRICK_OBJECT);
        mName = mTricks.getName();
        mRecords = mTricks.getRecord();
        mPropType = mTricks.getProp_type();
        mPr = mTricks.getPr();
        mMisses = mTricks.getMiss();
        mGoal = mTricks.getGoal();
        mTimeTrained = mTricks.getTime_trained();
        mHits = mTricks.getHit();
        mSiteswap = mTricks.getSiteswap();
        mSource = mTricks.getSource();
        mAnimation = mTricks.getAnimation();
        mTutorial = mTricks.getTutorial();
        mDifficulty = mTricks.getDifficulty();
        mCapacity = mTricks.getCapacity();
        mId = mTricks.getId();
        mDescription = mTricks.getDescription();

        mTrickDetails = getIntent().getStringArrayListExtra("details");
        Log.d("LOG", "asdf made it    " + mTrickDetails.toString());

        TextView title_text_view = (TextView) findViewById(R.id.discovery_title_text_view);
        title_text_view.setText(mTrickDetails.get(0));

        TextView details_text_view = (TextView) findViewById(R.id.discover_description_text_view);
        details_text_view.setText(Html.fromHtml(mTrickDetails.get(7)));

        WebView wv = (WebView) findViewById(R.id.discovery_animation);
        wv.loadUrl(mTrickDetails.get(3));

        mTrickName = mTrickDetails.get(0);
        mTrickDescription = mTrickDetails.get(7);

        TextView siteswap = (TextView) findViewById(R.id.discovery_siteswap_text_view);
        siteswap.setText("Siteswap: " + mTrickDetails.get(2));

        TextView capacity = (TextView) findViewById(R.id.discovery_capacity_text_view);
        capacity.setText("Capacity: " + mTrickDetails.get(1));

        TextView source = (TextView) findViewById(R.id.discovery_source_text_view);
        source.setText("Source: " + mTrickDetails.get(8));

        TextView tutorial = (TextView) findViewById(R.id.discovery_tutorial_text_view);
        String tutorial_text= mTrickDetails.get(4);
        //if (tutorial_text.length()<1){ tutorial_text = "No tutorial avaliable.";}
        tutorial.setText("Tutorial: " + tutorial_text);

        TextView difficulty = (TextView) findViewById(R.id.discovery_difficulty_text_view);
        String difficulty_text = mTrickDetails.get(5);

        difficulty.setText("Difficulty: " + mTrickDetails.get(5));


    }

    public void addTrick(View view){
        Intent toAddTrick = new Intent(this, AddTrick.class);
        toAddTrick.putStringArrayListExtra("details", mTrickDetails);
        startActivity(toAddTrick);
    }

    public void toUrl(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTrickDetails.get(4)));
        startActivity(browserIntent);
    }
}
