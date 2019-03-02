package com.example.rift.jiofinal;

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


public class ChessaActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private static final String baseUrl = "https://realchess.azurewebsites.net/";
    String gameName;
    WebView mWebView;
    String round;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);



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


        mWebView.loadUrl(baseUrl);


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
