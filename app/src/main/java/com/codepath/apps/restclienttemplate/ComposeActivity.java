package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = new TwitterClient(this);
    }

    public void onSubmit(View v) {
        // get value of the text entered into the EditText
        EditText etCompose = (EditText) findViewById(R.id.etCompose);
        String messageTweet = etCompose.getText().toString();

        client.sendTweet(messageTweet, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("ComposeActivity", "Failed to send network request.");
                error.printStackTrace();
            }
        });

        // closes the activity and returns to first screen
        this.finish();
    }

//    public void onClickTweet(View v) {
//        // get value of the text entered into the EditText
//        EditText etCompose = (EditText) findViewById(R.id.etCompose);
//        String messageTweet = etCompose.getText().toString();
//
//        // Log.i("message", messageTweet);
//        // FIGURE OUT HOW TO CONFIGURE BUTTON TO SEND MESSAGE TO UPDATE
//    }
}
