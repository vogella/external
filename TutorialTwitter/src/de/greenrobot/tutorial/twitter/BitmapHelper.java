package de.greenrobot.tutorial.twitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * Twitter Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */

public class BitmapHelper {
    public interface BitmapCallback {
        void onBitmapDownloaded(String url, Bitmap bitmap);
    }

    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    private final HttpClient httpClient;
    private final Activity activity;
    private final BitmapCallback callback;

    private Map<String, Bitmap> bitmaps;
    private Set<String> inProgress;

    public BitmapHelper(Activity activity, HttpClient httpClient, BitmapCallback callback) {
        this.activity = activity;
        this.httpClient = httpClient;
        this.callback = callback;

        bitmaps = new HashMap<String, Bitmap>();
        inProgress = new HashSet<String>();
    }

    public synchronized Future<Bitmap> submitBitmapUrl(final String url) {
        if (!inProgress.contains(url)) {
            inProgress.add(url);
            Future<Bitmap> future = threadPool.submit(new Callable<Bitmap>() {

                @Override
                public Bitmap call() throws Exception {
                    final Bitmap bitmap = downloadBitmap(url);
                    synchronized (BitmapHelper.this) {
                        bitmaps.put(url, bitmap);
                    }
                    if (callback != null) {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                callback.onBitmapDownloaded(url, bitmap);
                            }
                        });
                    }
                    return bitmap;
                }
            });
            return future;
        } else {
            return null;
        }
    }

    public Bitmap downloadBitmap(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url.toString());
        HttpResponse response = httpClient.execute(request);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        } else {
            throw new IOException("Download of bitmap failed, HTTP response code " + statusCode + " - "
                    + statusLine.getReasonPhrase());
        }
    }

    public synchronized Bitmap getBitmap(String url) {
        return bitmaps.get(url);
    }

}
