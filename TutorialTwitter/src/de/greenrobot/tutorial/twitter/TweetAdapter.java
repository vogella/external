package de.greenrobot.tutorial.twitter;

import java.util.List;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.tutorial.twitter.BitmapHelper.BitmapCallback;

/*
 * Twitter Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */
public class TweetAdapter extends BaseAdapter implements BitmapCallback {

    private HttpClient httpClient;
    private LayoutInflater inflater;
    private final List<Tweet> tweets;
    private BitmapHelper bitmapHelper;

    public TweetAdapter(Activity activity, List<Tweet> tweets) {
        this.tweets = tweets;
        httpClient = ((App) activity.getApplication()).getHttpClient();
        inflater = activity.getLayoutInflater();
        bitmapHelper = new BitmapHelper(activity, httpClient, this);
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        } else {
            rowView = inflater.inflate(R.layout.tweet_row, null);
        }
        Tweet tweet = tweets.get(position);
        TextView tweetTextView = (TextView) rowView.findViewById(R.id.TextViewTweetText);
        tweetTextView.setText(tweet.getText());

        TextView userNameView = (TextView) rowView.findViewById(R.id.TextViewUserName);
        userNameView.setText(tweet.getUser());

        String userIconUrl = tweet.getUserIconUrl();
        Bitmap bitmap = getBitmap(userIconUrl);
        ImageView userIconView = (ImageView) rowView.findViewById(R.id.ImageViewUserIcon);
        userIconView.setImageBitmap(bitmap);

        return rowView;
    }

    private Bitmap getBitmap(String userIconUrl) {
        Bitmap bitmap = bitmapHelper.getBitmap(userIconUrl);
        if (bitmap == null) {
            bitmapHelper.submitBitmapUrl(userIconUrl);
        }
        return bitmap;
    }

    @Override
    public void onBitmapDownloaded(String url, Bitmap bitmap) {
        notifyDataSetChanged();
    }

}
