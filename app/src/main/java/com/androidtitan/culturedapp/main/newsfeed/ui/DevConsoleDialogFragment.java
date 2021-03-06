package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.culturedapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DevConsoleDialogFragment extends DialogFragment {
    private final String TAG = getClass().getSimpleName();

    private DevConsoleCallback callbackListener;

    @BindView(R.id.dev_console_navigation_view)
    NavigationView navigationView;

    public DevConsoleDialogFragment() {
        // Required empty public constructor
    }

    public static DevConsoleDialogFragment newInstance() {
        DevConsoleDialogFragment fragment = new DevConsoleDialogFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dev_console_dialog_fragment, container, false);
        ButterKnife.bind(this, v);

        navigationView.setNavigationItemSelectedListener((item) -> {

            switch (item.getItemId()) {

                case R.id.data_menu_item:


                    break;

                default:

                    Log.e(TAG, "Incorrect navigation drawer item selected");
            }
            return true;
        });

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DevConsoleCallback) {
            callbackListener = (DevConsoleCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement DevConsoleCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbackListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface DevConsoleCallback {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
