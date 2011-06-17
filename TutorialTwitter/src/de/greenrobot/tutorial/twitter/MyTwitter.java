package de.greenrobot.tutorial.twitter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/*
 Twitter Entwickler Doku:http://dev.twitter.com/
 Twitter Suche: http://dev.twitter.com/doc/get/search
 Beispiel: http://api.twitter.com/1/users/show.json?screen_name=greenrobot_de
 */

/*
 * Twitter Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */

public class MyTwitter {
    private final HttpClient httpClient;

    public MyTwitter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Tweet> search(String searchText) throws ClientProtocolException, IOException, JSONException {
        String encoded = URLEncoder.encode(searchText);

        HttpUriRequest request = new HttpGet("http://search.twitter.com/search.json?q=" + encoded);
        HttpResponse response = httpClient.execute(request);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            return parseSearch(string);
        } else {
            throw new IOException("HTTP code " + statusCode);
        }
    }

    private List<Tweet> parseSearch(String string) throws JSONException {
        JSONObject root = new JSONObject(string);
        JSONArray jsonArray = root.getJSONArray("results");
        int length = jsonArray.length();
        Log.d("MyTwitter", "Got " + length + " search results");
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(length);
        for (int i = 0; i < length; i++) {
            Tweet tweet = parseSearchTweet(jsonArray.getJSONObject(i));
            tweets.add(tweet);
            System.out.println(tweet);
        }
        return tweets;
    }

    private Tweet parseSearchTweet(JSONObject jsonObject) throws JSONException {
        String text = jsonObject.getString("text");
        String user = jsonObject.getString("from_user");
        String userIconUrl = jsonObject.getString("profile_image_url");
        return new Tweet(user, userIconUrl, text);
    }

}
