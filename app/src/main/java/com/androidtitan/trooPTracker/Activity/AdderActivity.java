package com.androidtitan.trooptracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Fragment.AdderFragment;
import com.androidtitan.trooptracker.Fragment.DivAdderFragment;
import com.androidtitan.trooptracker.Interface.AdderInterface;

//ToDo: receive whether this is a SOLDIER or DIVISION
public class AdderActivity extends FragmentActivity implements AdderInterface {
    private final String ADD_FRAG_TAG = "adderTag";
    private final String ADD_FRAG_TAG_ZAP = "divAdderTag";

    private FragmentManager fragMag;
    private FragmentTransaction fragTran;
    private AdderFragment adderFragment;
    private DivAdderFragment divAdderFragment;

    private int divisionIndex;
    private int soldierIndex;
    private String soldierFname;
    private String soldierLname;

    private Boolean isDivisionAdder = false;
    private Boolean isEditAdder = false;
    private int adderDivisionIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);


        Intent intent = getIntent();
        isDivisionAdder = intent.getBooleanExtra("landingBool", false);
        isEditAdder = intent.getBooleanExtra("landingEdit", false);
        adderDivisionIndex = intent.getIntExtra("landingDivision", -1);

        if(isDivisionAdder == true) {

            //onOrientationChange Block
            if (savedInstanceState != null) {
                //savedInstanceState, fragment may exist. Look up the instance that already exists by tag
                divAdderFragment = (DivAdderFragment) getSupportFragmentManager().findFragmentByTag(ADD_FRAG_TAG_ZAP);
            }

            else if (divAdderFragment == null) {
                divAdderFragment = new DivAdderFragment();

                Bundle args = new Bundle();
                args.putInt("landingDivision", adderDivisionIndex); //onAdd -- -1
                args.putBoolean("landingEdit", isEditAdder); //onAdd -- false
                args.putBoolean("landingBool", isDivisionAdder);//onAdd -- true
                divAdderFragment.setArguments(args);
            }

            if (!divAdderFragment.isInLayout()) {

                fragMag = getSupportFragmentManager();
                fragTran = fragMag.beginTransaction();
                fragTran.addToBackStack(null).replace(R.id.landingContainer, divAdderFragment, ADD_FRAG_TAG_ZAP).commit();
            }
        }
        else {
            try {
                getSoldierEditItems();

            } catch (NullPointerException e) {
                Log.e("AAonCreate", String.valueOf(e));
            }

            //onOrientationChange Block
            if (savedInstanceState != null) {
                //savedInstanceState, fragment may exist. Look up the instance that already exists by tag
                adderFragment = (AdderFragment) getSupportFragmentManager().findFragmentByTag(ADD_FRAG_TAG);
            } else if (adderFragment == null) {
                adderFragment = new AdderFragment();
            }
            if (!adderFragment.isInLayout()) {
                fragMag = getSupportFragmentManager();
                fragTran = fragMag.beginTransaction();
                fragTran.addToBackStack(null).replace(R.id.landingContainer, adderFragment, ADD_FRAG_TAG).commit();
            }
        }

    }

    @Override
    public void onBackPressed() {

        if(isDivisionAdder == true) {
            isDivisionAdder = false;
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }
        else {
            divInteraction(divisionIndex);
        }
    }

    //called from SecondF2AInterface.  Passes integer so main activity can page to
    //newly added soldier's division
    @Override
    public void divInteraction(int divSelected) {
        Intent intent = new Intent(this, ChampionActivity.class);
        intent.putExtra("landingDivision", divSelected);
        startActivity(intent);
    }


    public String getSoldierEditItems() {
        Intent intent = getIntent();
        divisionIndex = intent.getIntExtra("editSoloDivIndex", -1);
        soldierIndex = intent.getIntExtra("editSoloIndex", -1);
        soldierFname = intent.getStringExtra("editSoloFirst");
        soldierLname = intent.getStringExtra("editSoloLast");

        Bundle editArgs = new Bundle();
        editArgs.putInt("editSoloDivIndex", divisionIndex);
        editArgs.putInt("editSoloIndex", soldierIndex);
        editArgs.putString("editSoloFirst", soldierFname);
        editArgs.putString("editSoloLast", soldierLname);
        adderFragment = new AdderFragment();
        adderFragment.setArguments(editArgs);

        Log.e("AAgetSoldierEditItems", "selection: " + soldierIndex);


        return soldierFname; //this is simply a check to make sure we received
        // for the purpose of the try/catch block
    }
}
