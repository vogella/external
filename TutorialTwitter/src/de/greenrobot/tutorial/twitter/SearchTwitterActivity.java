package de.greenrobot.tutorial.twitter;

import java.util.List;

import org.apache.http.client.HttpClient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Twitter Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */

public class SearchTwitterActivity extends ListActivity {
    private final class SearchTask extends AsyncTask<String, Void, List<Tweet>> {
        private ProgressDialog progressDialog;
        private String toastText;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SearchTwitterActivity.this);
            progressDialog.setMessage("Suche...");
            progressDialog.show();
        }

        @Override
        protected List<Tweet> doInBackground(String... params) {
            String searchText = params[0];
            MyTwitter myTwitter = new MyTwitter(httpClient);
            try {
                return myTwitter.search(searchText);
            } catch (Exception e) {
                toastText = "Could not search twitter";
                Log.e("MyTwitter", toastText, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Tweet> result) {
            progressDialog.dismiss();
            if (toastText != null) {
                Toast.makeText(SearchTwitterActivity.this, toastText, 1500).show();
            }
            if (result != null) {
                TweetAdapter adapter = new TweetAdapter(SearchTwitterActivity.this, result);
                setListAdapter(adapter);
            }
        }
    }

    private EditText searchView;
    private HttpClient httpClient;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        searchView = (EditText) findViewById(R.id.EditTextSearch);
        httpClient = ((App) getApplication()).getHttpClient();
    }

    public void search(View view) {
        String text = searchView.getText().toString().trim();
        if (text.length() == 0) {
            Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fehler").setMessage("Bitte geben Sie einen Text ein.").setIcon(
                    android.R.drawable.ic_dialog_alert).setNeutralButton("OK", null);
            builder.create().show();
        } else {
            SearchTask searchTask = new SearchTask();
            searchTask.execute(text);
        }

    }
}