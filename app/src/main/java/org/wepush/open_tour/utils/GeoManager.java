//package org.wultimaproject.db2.utils;
//
//import android.app.Application;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
//
//import io.fabric.sdk.android.Fabric;
//import SettingTourActivity;
//import LookUpIntentService;
//import Constants;
//
///**
// * Created by Antonio on 20/04/2015.
// */
//public class GeoManager extends Application implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
//    public GoogleApiClient mGoogleApiClient;
//    private Location mLastLocation;
//
//
//    private boolean wrongAddressFormat;
//
//    private PendingIntent mPendingIntent;
//
//    private SettingTourActivity.AddressResultReceiver mResultReceiver;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Fabric.with(this, new Crashlytics());
//
//
//    }
//
//
//
//    public void setAddressFlag(boolean flag){
//        wrongAddressFormat=flag;
//    }
//
//    public boolean getAddressFlag(){
//        return wrongAddressFormat;
//    }
//
//
//
//    public void createClient(){
//
//        mGoogleApiClient=new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//
//    }
//
//    public GoogleApiClient getClient(){
//        return mGoogleApiClient;
//    }
//
//    public void connectClient(){
//        mGoogleApiClient.connect();
//    }
//
//    public void disconnectClient(){
//        mGoogleApiClient.disconnect();
//    }
//
//    public boolean isClientConnected(){
//        return	mGoogleApiClient.isConnected();
//
//    }
//
//    public void onConnected(Bundle hintBundle){
//
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//       if (mLastLocation != null){
//
//           Intent intent=new Intent(getApplicationContext(),LookUpIntentService.class);
//            mResultReceiver=new SettingTourActivity.AddressResultReceiver(new Handler());
//           intent.putExtra(Constants.RECEIVER, mResultReceiver);
//           intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
//
//           startService(intent);
//       }
//
//    }//fine onConnected
//
//    public void onConnectionSuspended(int i){
//        mGoogleApiClient.disconnect();
//        Log.d("miotag","client connection: INTERRUPTED");
//        Toast.makeText(this, "Client Connection: SUSPENDED!", Toast.LENGTH_LONG).show();
//
//    }
//
//    public void onConnectionFailed(ConnectionResult connResult){
//        Log.d("miotag","connection to LocalServices lost ");
//        Toast.makeText(this, "Lost Connection: No Signal!", Toast.LENGTH_LONG).show();
//    }
//
//
//
//
//    public boolean isGpsOn(){
//        LocationManager serviceGPS = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean enabledGPS = serviceGPS
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return enabledGPS;
//    }
//
//
//
//
//}
//
