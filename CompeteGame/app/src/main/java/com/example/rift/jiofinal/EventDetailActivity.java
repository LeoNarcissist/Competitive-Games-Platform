package com.example.rift.jiofinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.rift.jiofinal.Adapter.SectionsPageAdapter;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Toolbar mActionBarToolbar;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    String currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Log.d(TAG, "onCreate: Starting.");

        Intent intent = getIntent();
        currentEvent = (String) intent.getSerializableExtra("eventObject");

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(currentEvent);
        // mActionBarToolbar.setLogo(R.drawable.ic_logo);

       mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContestsFragment(), "Contests");
        adapter.addFragment(new LeaderBoardFragment(), "Leader Board");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
