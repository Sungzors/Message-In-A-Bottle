package com.example.sungwon.messageinabottle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainMenuFragment.OnMMFragmentInteractionListener, MemoryFragment.OnMemoryFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    BottomNavigationView mBottomNav;
    TextView mTestText;
    Fragment mFragment;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;

    List<Geofence> mGeofenceList;
    SimpleGeofenceStore mGeofenceStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        mFragment = MainMenuFragment.newInstance("create");
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
        setupGoogleAPIClient();



    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void setupGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void setupView() {
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mTestText = (TextView) findViewById(R.id.testText);
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main_menu:
                        mBottomNav.setItemBackgroundResource(R.color.orange);
                        mTestText.setText("Main Menu Selected!!!");
                        mFragment = MainMenuFragment.newInstance("Passing");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
                        break;
                    case R.id.action_memory:
                        mBottomNav.setItemBackgroundResource(R.color.gray);
                        mTestText.setText("Memory Selected!!");
                        mFragment = MemoryFragment.newInstance("Passing");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
                        break;
                    case R.id.action_send:
                        mBottomNav.setItemBackgroundResource(R.color.colorPrimary);
                        mTestText.setText("Send Selected!");
                        break;
                    case R.id.action_setting:
                        mBottomNav.setItemBackgroundResource(R.color.gray2);
                        mTestText.setText("Boring ol' Setting selected.");
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onMMFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMemoryFragmentInteraction(Uri uri) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            mTestText.setText("Current location: " + String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
