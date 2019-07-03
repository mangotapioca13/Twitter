package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Tweet {

    // list out all the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public String timeAgo;

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.timeAgo = Tweet.getRelativeTimeAgo(jsonObject.getString("created_at"));

        // Log.i("time", tweet.timeAgo);
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

        return reformatTime(relativeDate);
    }

    // return the relativeDate as "#m" or "#h"
    public static String reformatTime(String relativeDate) {
        String reformatted = relativeDate.split(" ")[0];
        String typeTime = relativeDate.split(" ")[1];

        // check whether the time was in minutes or hours
        if (typeTime.charAt(0) == 'm') {
            reformatted += "m";
        } else if (typeTime.charAt(0) == 's') {
            reformatted += "s";
        } else {
            reformatted += "h";
        }

        // Log.i("new time", reformatted);
        return reformatted;
    }
}