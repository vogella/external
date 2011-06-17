package de.greenrobot.tutorial.twitter;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Application;


/*
 * Twitter Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */
public class App extends Application {
    private HttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public synchronized HttpClient getHttpClient() {
        if (httpClient == null) {
            HttpParams params = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
            HttpConnectionParams.setSoTimeout(params, 20 * 1000);
            HttpConnectionParams.setSocketBufferSize(params, 8192);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

            httpClient = new DefaultHttpClient(manager, params);
        }
        return httpClient;
    }

}
