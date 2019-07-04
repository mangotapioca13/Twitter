package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private final int REQUEST_CODE = 100;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // instantiate the client
        client = TwitterApp.getRestClient(this);

        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        // adds a horizontal divider between each tweet
        DividerItemDecoration itemDeco = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTweets.addItemDecoration(itemDeco);

        // instantiate the arraylist (data source)
        tweets = new ArrayList<>();

        // construct the adapter from this data source
        tweetAdapter = new TweetAdapter(tweets);

        // RecyclerView setup (layout manager, use adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        // set the adapter
        rvTweets.setAdapter(tweetAdapter);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        populateTimeline();
    }

    public void fetchTimelineAsync(int page) {
        // Remember to CLEAR OUT old items before appending in the new ones
        tweetAdapter.clear();

        // refresh the view so that all the new tweets and updates come in
        populateTimeline();

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);

    }

    // put the onCreateOptionsMenu in the activity that houses where you would want to go next
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miCompose) {
            launchComposeView();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Log.d("TwitterClient", response.toString());
                // iterate through the JSON Array
                // for each entry, deserialize the JSON object
                for(int i = 0; i < response.length(); i++) {
                    // convert each object to a Tweet model

                    // add that Tweet model to our data source

                    // notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    public void launchComposeView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE); // brings up the second activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check request code and result code first
        if ((requestCode == REQUEST_CODE) && (resultCode == RESULT_OK)) {
            // Use data parameter
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));

            // add the tweet and update the screen to see it
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);

            // Toast.makeText(this, "Tweet added!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Parceling", "FAILED");
        }
    }
}