package com.example.sungwon.messageinabottle;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.Geofence;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the interface
 * to handle interaction events.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Location mLocation;
    private int mID;

    TextView mLocRec;
    Button mRefresh;
    Button mCreateGeo;
    SimpleGeofence mGeofence;

    private OnMMFragmentInteractionListener mListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(String param1) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLocRec = (TextView)view.findViewById(R.id.locationreceived);
        mRefresh = (Button)view.findViewById(R.id.refreshButton);
        mCreateGeo = (Button)view.findViewById(R.id.createGeo);
        mRefresh.setOnClickListener(this);
        mCreateGeo.setOnClickListener(this);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMMFragmentInteractionListener) {
            mListener = (OnMMFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMMFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setLocation(Location loc, int i){
        mLocation = loc;
        mID = i;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.refreshButton:
                ((MainActivity)getActivity()).checkLocation();
                mLocRec.setText(String.valueOf("Altitude: " + mLocation.getAltitude()));
                break;
            case R.id.createGeo:
                MainActivity activity = ((MainActivity)getActivity());
                activity.checkLocation();
                mGeofence = new SimpleGeofence(
                        String.valueOf(mID),
                        mLocation.getLatitude(),
                        mLocation.getLongitude(),
                        500.0f,
                        -1,
                        Geofence.GEOFENCE_TRANSITION_DWELL
                );
                activity.insertGeofence(mGeofence);
                break;
        }
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
    public interface OnMMFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMMFragmentInteraction(Location loc);
    }
}
