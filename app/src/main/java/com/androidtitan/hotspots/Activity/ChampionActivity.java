package com.androidtitan.hotspots.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidtitan.hotspots.Fragment.ChampionListFragment;
import com.androidtitan.hotspots.Interface.ChampionInterface;
import com.androidtitan.hotspots.R;

public class ChampionActivity extends AppCompatActivity implements ChampionInterface {
    private static final String TAG = "ChampionActivity";

    private static final int MAP_ACTIVITY_REQUEST = 1;

    public static final String SELECTION_TO_MAP = "selectionToMap";
    public static final String FIRST_VISIT_BOOL = "firstVisitBool";

    ChampionListFragment championFragment;

    int selectionIndex = - 1;
    boolean isFirstMapVisit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

        //Data Section
/*
    This code is coming from AdderActivity

        Intent intent = getIntent();
        shouldCursorUpdate = intent.getBooleanExtra(AdderActivity.CURSOR_UPDATE, false);
        Bundle args = new Bundle();
        args.putBoolean(AdderActivity.CURSOR_UPDATE, shouldCursorUpdate);*/

        //View Section
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        championFragment = new ChampionListFragment();
        //championFragment.setArguments(args);


        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
        fragTran.replace(R.id.championContainer, championFragment, "championFragment").commit();

    }

    @Override
    public void soldierPasser(int locationInt, String localName) {

        Intent intent = new Intent(this, AdderActivity.class);
        intent.putExtra("editSoloIndex", locationInt);
        intent.putExtra("editSoloFirst", localName);
        startActivity(intent);
    }

    //this is going to be used for navigation once we have more pieces
    //All soldiers and All map
    @Override
    public void drawerListViewSelection(int selection) {

    }

    @Override
    public void setListViewSelection(int selection) {
        selectionIndex = selection;
    }

    @Override
    public void selectionToMap(int selection) {

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SELECTION_TO_MAP, selection);

        if(isFirstMapVisit) {
            intent.putExtra(FIRST_VISIT_BOOL, true);
            startActivityForResult(intent, MAP_ACTIVITY_REQUEST);
        } else {
            intent.putExtra(FIRST_VISIT_BOOL, false);
            startActivity(intent);
        }
    }

    //this will permanently set it to unvisited.  Rather then having it display once for each person
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MAP_ACTIVITY_REQUEST) {
            if(resultCode == RESULT_OK) {
                isFirstMapVisit = data.getExtras().getBoolean(MapsActivity.SAVED_INITIAL_BOOL); //this is the extra we are getting back
                Log.e(TAG, "received:" + String.valueOf(isFirstMapVisit));
            }
        }
    }
    public int getListViewSelection() {
        return selectionIndex;
    }


    @Override
    public void onBackPressed() {
        //disable the back button
    }


}
