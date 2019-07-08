package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Parcel
public class Tweet {

    // list out all the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public String timeAgo;
    public String heartCount;
    public String retweetCount;
    public String mediaUrl;

    public Tweet() { }

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.timeAgo = Tweet.getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.heartCount = Integer.toString(jsonObject.getInt("favorite_count"));
        tweet.retweetCount = Integer.toString(jsonObject.getInt("retweet_count"));

        JSONObject obj = jsonObject.getJSONObject("entities");
        if(obj.has("media")) {
            JSONArray obj2 = obj.getJSONArray("media");
            JSONObject obj3 = obj2.getJSONObject(0);
            tweet.mediaUrl = obj3.getString("media_url_https");
        } else {
            tweet.mediaUrl = null;
        }

        // retrieve media url if there is one
//        if (jsonObject.has("extended_entities")) {
//            if (jsonObject.getJSONObject("entities").has("media")) {
//                tweet.mediaUrl = jsonObject.getJSONObject("extended_entities")
//                        .getJSONArray("media").getJSONObject(0)
//                        .getString("media_url_https");
//            }
//        } else {
//            tweet.mediaUrl = null;
//        }

        return tweet;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";

        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate.toLowerCase();
    }
}