package com.example.rift.jiofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Dumadu on 07-Dec-18.
 */


public class WebViewActivity extends AppCompatActivity {
    public static final String URL = "http://dobbster.ml/jiogame/leaderboard/update.php";
    private static final String TAG = "WebViewActivity";
    private static final String baseUrl = "https://kettlemind-contest-d289c.firebaseapp.com/";
    public ProgressDialog progressDialog;
    String gameName;
    WebView mWebView;
    String round;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);


        /**
         * Game Names : Anagrams, Analogy, OddOne, Series
         */

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        gameName = extras.getString("eventObject");
        round = extras.getString("roundObject");

        /**
         * Rounds start from 1
         */

        Toast.makeText(getBaseContext(), round, Toast.LENGTH_SHORT).show();

        mWebView = findViewById(R.id.webview);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());


        WebSettings ws = mWebView.getSettings();
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //Load fresh data
        mWebView.clearCache(true);
        mWebView.clearHistory();


        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");


        mWebView.loadUrl(baseUrl + gameName + "/index.html?round=" + round);


    }


    public class JavaScriptInterface {
        Context mContext;
        int Score = 0;


        JavaScriptInterface(Context c) {
            mContext = c;
        }


        /**
         * The round does not exist for the given game
         */
        @JavascriptInterface
        public void roundNotFound() {
            Toast.makeText(mContext, "Invalid round", Toast.LENGTH_SHORT).show();
        }


        /**
         * Called each time the score is updated,
         * Keeping this upto date helps handle the back button press
         *
         * @param score Current score
         */
        @JavascriptInterface
        public void postScore(String score) {
            Score = Integer.parseInt(score);
            Log.d(TAG, "postScore: " + Score);
            Toast.makeText(mContext, "postScore" + Score, Toast.LENGTH_SHORT).show();
        }

        /**
         * Called on game over
         *
         * @param score Final score
         */
        @JavascriptInterface
        public void onGameOver(String score) {
            Score = Integer.parseInt(score);
            Log.d(TAG, "onGameOver: " + Score);
            Toast.makeText(mContext, "onGameOver" + Score, Toast.LENGTH_SHORT).show();
            finish();

            Uri baseUri = Uri.parse(URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("uid", SaveSharedPreference.getUserId(getBaseContext()));
            uriBuilder.appendQueryParameter("gamename", "chess");
            uriBuilder.appendQueryParameter("round", round);
            uriBuilder.appendQueryParameter("score", String.valueOf((Score)));


            Lichterkette job = new Lichterkette();
            job.execute(uriBuilder.toString());


        }


        /**
         * Called after downloading the files
         */
        @JavascriptInterface
        public void onGameStart() {
            Log.d(TAG, "onGameStart: ");
            Toast.makeText(mContext, "onGameStart", Toast.LENGTH_SHORT).show();
        }
    }
}


class Lichterkette extends AsyncTask<String, Void, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}