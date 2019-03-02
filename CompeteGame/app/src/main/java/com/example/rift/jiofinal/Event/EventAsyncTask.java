package com.example.rift.jiofinal.Event;


import android.os.AsyncTask;

import java.util.ArrayList;

public class EventAsyncTask extends AsyncTask<String, Void, ArrayList<Event>> {

    private EventAsyncResponse mEventAsyncResponse = null;

    public EventAsyncTask(EventAsyncResponse eventAsyncResponse) {
        mEventAsyncResponse = eventAsyncResponse;
    }

    @Override
    protected ArrayList<Event> doInBackground(String... strings) {
        return EventExtractData.initiateConnection(strings[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Event> events) {
        if (events == null) {
            return;
        }
        mEventAsyncResponse.processFinish(events);
    }

}
