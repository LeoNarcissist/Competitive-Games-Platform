package com.example.rift.jiofinal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContestsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String[]> {
    public static final String URL = "http://dobbster.ml/jiogame/leaderboard/play.php";
    private static final int EVENT_LOADER_ID = 1;
    public TextView round, time;
    String currentEvent;
    String eligibility = null;
    String roundS = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contests_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        currentEvent = (String) intent.getSerializableExtra("eventObject");

        Button button = (Button) getView().findViewById(R.id.btn_play);


        /*
        Getting Connectivity service.
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EVENT_LOADER_ID, null, this);
        } else {
            /*
            This code will work when there is no internet connectivity.
             */
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_LONG).show();
        }


        // getFragmentManager().beginTransaction().detach(ContestsFragment.this).attach(ContestsFragment.this).commit();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (eligibility.equals(1)) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("eventObject", currentEvent);
                    extras.putString("roundObject", roundS);
                    intent.putExtras(extras);
                    startActivity(intent);
              ////  } else if (eligibility.equals(0)) {
                    Toast.makeText(getActivity(), "You are not eligible to play!", Toast.LENGTH_LONG).show();
              //  } else {
               //     Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_LONG).show();
              //  }
            }
        });

        round = (TextView) getView().findViewById(R.id.round);
        time = (TextView) getView().findViewById(R.id.time);

    }


    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        //Create a preference file

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("uid", SaveSharedPreference.getUserId(getContext()));
        uriBuilder.appendQueryParameter("gamename", "chess");

        return new GameLoader(getContext(), uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] eventsList) {
        if (eventsList[0] != null && eventsList[0] != "") {
            round.setText(eventsList[0]);
            roundS = eventsList[0];
        }
        if (eventsList[2] != null && eventsList[2] != "") {
            time.setText(eventsList[2]);
        }

        if (eventsList[1] != null && eventsList[1] != "") {
            eligibility = eventsList[1];
        }

    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {
    }
}
