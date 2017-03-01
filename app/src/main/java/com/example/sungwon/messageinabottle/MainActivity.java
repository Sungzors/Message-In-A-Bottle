package com.example.sungwon.messageinabottle;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainMenuFragment.OnMMFragmentInteractionListener, MemoryFragment.OnMemoryFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    BottomNavigationView mBottomNav;
    TextView mTestText;
    Fragment mFragment;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;

    List<Geofence> mGeofenceList;
    SimpleGeofenceStore mGeofenceStorage;
    SimpleGeofence mGeofence;
    PendingIntent mGeofencePendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        mFragment = MainMenuFragment.newInstance("create");
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
        setupGoogleAPIClient();
        mGeofenceStorage = new SimpleGeofenceStore(this);
        mGeofenceList = new ArrayList<Geofence>();

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

    //sets up Google API Client for Location service
    private void setupGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    //sets up views
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
    public void onMMFragmentInteraction(Location loc) {
        MainMenuFragment mmfrag = (MainMenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        mmfrag.setLocation(loc, mGeofenceList.size());
    }

    @Override
    public void onMemoryFragmentInteraction(Uri uri) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocation();
    }

    //communication with fragment as well as used to refreshing location
    public void checkLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mTestText.setText("Current location: " + String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));
        }
        onMMFragmentInteraction(mLastLocation);
    }

    //insert geofence
    public void insertGeofence(SimpleGeofence simplegeofence) {
        //null check
        if (simplegeofence == null) {
            Toast.makeText(this, "Geofence has not been registered properly", Toast.LENGTH_SHORT).show();
            return;
        }
        Geofence geofence = simplegeofence.toGeofence();
        mGeofenceStorage.setGeofence(simplegeofence.getId(), simplegeofence);
        mGeofenceList.add(geofence);
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(simplegeofence.getTransitionType());
        builder.addGeofence(geofence);
        GeofencingRequest request = builder.build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                request,
                getGeofencePendingIntent()
        ).setResultCallback(this);
        Toast.makeText(this, "Geofence created", Toast.LENGTH_SHORT).show();
    }

    private PendingIntent getGeofencePendingIntent(){
        if (mGeofencePendingIntent!= null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {

    }
}
