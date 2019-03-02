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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rift.jiofinal.Adapter.CustomAdapter;
import com.example.rift.jiofinal.Event.Event;
import com.example.rift.jiofinal.Event.EventLoader;

import java.util.ArrayList;


public class LeaderBoardFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Event>> {
    public static final String URL = "http://dobbster.ml/jiogame/leaderboard/scores.php";
    private static final String TAG = "LeaderBoardFragment";
    private static final int EVENT_LOADER_ID = 1;
    //Declare the adapter, RecyclerView and our custom ArrayList
    RecyclerView recyclerView;
    CustomAdapter adapter;
    View noInternetScreenView;
    View noDataView;
    View loadingScreenView;
    LinearLayoutManager mLayoutManager;
    String currentEvent;
    private ArrayList<Event> events = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leader_board_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CustomAdapter(getActivity(), events);
        setHasOptionsMenu(true);
        recyclerView.setHasFixedSize(true);

        Intent intent = getActivity().getIntent();
        currentEvent = (String) intent.getSerializableExtra("eventObject");

        /*
        Initialising default Views
         */
        loadingScreenView = getView().findViewById(R.id.loading_screen);
        noInternetScreenView = getView().findViewById(R.id.no_internet_screen);
        noDataView = getView().findViewById(R.id.no_data);


        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh);

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
            recyclerView.setVisibility(View.GONE);
            loadingScreenView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.VISIBLE);
        }

        /*
         * * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * * performs a swipe-to-refresh gesture.
         * */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  startActivity(new Intent(getActivity(), MainActivity.class)); //reload MainActivity
                //  getActivity().finish();
                getFragmentManager().beginTransaction().detach(LeaderBoardFragment.this).attach(LeaderBoardFragment.this).commit();
            }
        });
    }


    @Override
    public Loader<ArrayList<Event>> onCreateLoader(int id, Bundle args) {
        //Create a preference file

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("gamename",  "chess");


        return new EventLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Event>> loader, ArrayList<Event> eventsList) {
        if (eventsList.size() != 0) {
            events.clear();
            events.addAll(eventsList);
            //We set the array to the adapter
            adapter.setListContent(events);
            //We in turn set the adapter to the RecyclerView
            recyclerView.setAdapter(adapter);
            loadingScreenView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            loadingScreenView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Event>> loader) {
        events.clear();
    }

}
