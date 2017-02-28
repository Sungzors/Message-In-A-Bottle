package com.example.sungwon.messageinabottle;

import android.net.Uri;

/**
 * Created by SungWon on 2/27/2017.
 */

public final class Constants {

    private Constants() {
    }

    public static final String TAG = "ExampleGeofencingApp";

    // Timeout for making a connection to GoogleApiClient (in milliseconds).
    public static final long CONNECTION_TIME_OUT_MS = 100;

    public static final int NOTIFICATION_ID = 1;
    public static final String ANDROID_BUILDING_ID = "1";
    public static final String YERBA_BUENA_ID = "2";

    public static final String ACTION_CHECK_IN = "check_in";
    public static final String GEOFENCE_DATA_ITEM_PATH = "/geofenceid";
    public static final Uri GEOFENCE_DATA_ITEM_URI =
            new Uri.Builder().scheme("wear").path(GEOFENCE_DATA_ITEM_PATH).build();
    public static final String ACTION_DELETE_DATA_ITEM = "delete_data_item";
    public static final String KEY_GEOFENCE_ID = "geofence_id";
}
