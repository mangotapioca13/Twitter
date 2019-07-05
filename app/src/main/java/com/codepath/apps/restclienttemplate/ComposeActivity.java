package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    private ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = new TwitterClient(this);

        // find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create Intent to fetch the url that was passed here
        Intent intent = getIntent();
        String url = intent.getStringExtra("profileImageUrl");

        // fetch views so that Glide can attach the image
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        Glide.with(this)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(this, 30, 0))
                .into(ivProfileImage);
    }

    public void onSubmit(View v) {
        // get value of the text entered into the EditText
        EditText etCompose = (EditText) findViewById(R.id.etCompose);
        String messageTweet = etCompose.getText().toString();

        client.sendTweet(messageTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // methods to handle click on the Cancel menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // finish() just brings you back to previous activity
        // in this case, it would be the TimelineActivity
        finish();
        return super.onOptionsItemSelected(item);
    }
}