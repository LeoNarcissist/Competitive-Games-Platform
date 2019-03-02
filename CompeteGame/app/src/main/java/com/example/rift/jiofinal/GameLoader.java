package com.example.rift.jiofinal;


import android.content.Context;

public class GameLoader extends android.support.v4.content.AsyncTaskLoader<String[]> {

    private String mUrl;

    public GameLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String[] loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return GameData.initiateConnection(mUrl);
    }
}
