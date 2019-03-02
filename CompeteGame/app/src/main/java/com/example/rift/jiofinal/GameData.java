package com.example.rift.jiofinal;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


class GameData {

    /**
     * This function initiates the process of making HTTP connection and returns ArrayList of events.
     *
     * @param stringUrl
     * @return
     */

    static String[] initiateConnection(String stringUrl) {
        String jsonResponse = "";
        URL url = getURL(stringUrl);
        Log.e("EventExtractData", "Error response code : " + stringUrl);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("EventAsyncTask", "Error establishing Connection!!!");
        }
        return extractFromJson(jsonResponse);
    }

    /**
     * This function take a string url and the URL Object.
     *
     * @param stringUrl
     * @return
     */
    private static URL getURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("EventExtractData", "URL Exception => Not able to convert to url object.");
        }
        return url;
    }

    /**
     * Returns jsonresponse in String format.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.e("hello1", "1");
            urlConnection.setRequestMethod("GET");
            Log.e("hello1", "2");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.connect();
            Log.e("hello1", "3");
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.e("EventExtractData", "Error response code : " + jsonResponse);
            } else {
                Log.e("EventExtractData", "Error response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("EventExtractData", "Error IOExeception");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return String from inputstream
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return String of event Objects.
     *
     * @param jsonData
     * @return
     */
    private static String[] extractFromJson(String jsonData) {
        String[] events = new String[3];
        try {
            Log.e("EventExtractData", jsonData);
            JSONObject baseObject = new JSONObject(jsonData);
            events[1] = baseObject.getString("allow");
            events[0] = baseObject.getString("round");
            String [] str = baseObject.getString("start").split("\\s+");
            events[2] = str [1];
        } catch (JSONException e) {
            Log.e("EventExtractData", "JSON data extract error.");
        }
        return events;
    }

}
