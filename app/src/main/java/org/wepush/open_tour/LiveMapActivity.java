package org.wepush.open_tour;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.wepush.open_tour.fragments_dialogs.AreYouSureFragment;
import org.wepush.open_tour.fragments_dialogs.LivePagerFragment;
import org.wepush.open_tour.fragments_dialogs.LiveTutorialFragment;
import org.wepush.open_tour.fragments_dialogs.NoGpsDialog;
import org.wepush.open_tour.services.SiteNotificationService;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.RecyclerAdapter;
import org.wepush.open_tour.utils.RecyclerViewHolder;
import org.wepush.open_tour.utils.Repository;
import org.wepush.open_tour.utils.SphericalMercator;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * Created by antoniocoppola on 02/07/15.
 */
public class LiveMapActivity extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,AreYouSureFragment.OnCompleteListener {


    private RecyclerView recyclerView;
    private static ArrayList<String> idsFromShowTL;
    private static ArrayList<String>showingTimeShowTL;
    private static ArrayList<String> distanceShowTL;
    private static ArrayList<Boolean> arrayOfNotificationSent;

    private ArrayList<Location> arrayOfLocation;
    public static ArrayList<Site> liveSites;

    public static Context context;

    private ViewPager viewLivePager;
    private ViewLivePagerAdapter viewLivePagerAdapter;

    private LocationRequest mLocationRequest;

    private final static int ZOOM=17;
    private org.osmdroid.views.MapView map;
    private MapEventsOverlay overlayEventos;
    private MapEventsOverlay overlayNoEventos;
    private MapEventsReceiver  mapNoEventsReceiver;
    private IMapController mapController;

    private GeoPoint actualGeoPoint;

    private boolean sure=false;




    private static final double RADIUS=0.002;

//to avoid conflicts, in "actualUserLocalization" is used a custom GoogleApiCient

    private GoogleApiClient liveGoogleApiClient;
    private LocationManager serviceGPS;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livemap_layout);

        context=this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDiscovery);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowNavigationDiscovery);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                    HomeActivity.destroyTourPreferences(getBaseContext());
//                    startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
//                    finish();
                areYouSure();

            }
        });


        serviceGPS = (LocationManager) getSystemService(LOCATION_SERVICE);



        idsFromShowTL=new ArrayList<>();
        showingTimeShowTL=new ArrayList<>();
        distanceShowTL=new ArrayList<>();
        arrayOfLocation=new ArrayList<>();
        arrayOfNotificationSent=new ArrayList<>();
        Intent intent=getIntent();

        if (TextUtils.equals(intent.getAction(),Constants.INTENT_FROM_SHOWTOURTL)) {
            idsFromShowTL = intent.getStringArrayListExtra("id");
            showingTimeShowTL = intent.getStringArrayListExtra("showingTime");
        } else if (TextUtils.equals(intent.getAction(),Constants.INTENT_FROM_SHOWDETAILS)){
            String json=Repository.retrieve(this, "idsToSave", String.class);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            idsFromShowTL= gson.fromJson(json, type);

            String json2=Repository.retrieve(this, "showToSave", String.class);
            Gson gson2 = new Gson();
            showingTimeShowTL=gson2.fromJson(json2,type);

        }
        Gson gson=new Gson();
        String json=gson.toJson(idsFromShowTL);
        Repository.save(this,"idsToSave",json);

        Gson gson2=new Gson();
        String json2=gson2.toJson(showingTimeShowTL);
        Repository.save(this,"showToSave",json2);


        //ArrayList to keep distances between sites, passing it to recyclerview
        for (int i=0; i<idsFromShowTL.size()-1; i++){
            distanceShowTL.add(showNextDistance(idsFromShowTL.get(i),idsFromShowTL.get(i+1)));

        }
        //this last item in distanceShowTL is added to pair its size with others ArrayList
        distanceShowTL.add(getResources().getString(R.string.end_of_tour));

        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewDiscovery);

        //link to LayoutManager
        final LinearLayoutManager lManager=new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(lManager);
        RecyclerAdapter recAdapter=new RecyclerAdapter(idsFromShowTL,showingTimeShowTL,distanceShowTL,this);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean scrollingUp;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // Or use dx for horizontal scrolling
                scrollingUp = dx < 0;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // Make sure scrolling has stopped before snapping
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // layoutManager is the recyclerview's layout manager which you need to have reference in advance
                    int visiblePosition = scrollingUp ? lManager.findFirstVisibleItemPosition()
                            : lManager.findLastVisibleItemPosition();
                    int completelyVisiblePosition = scrollingUp ? lManager
                            .findFirstCompletelyVisibleItemPosition() : lManager
                            .findLastCompletelyVisibleItemPosition();
                    // Check if we need to snap
                    if (visiblePosition != completelyVisiblePosition) {
                        recyclerView.smoothScrollToPosition(visiblePosition);
                        return;
                    }

                }
            }});




        //ViewPager for LiveSiteDetail

        liveSites=new ArrayList<>();
        for (String s: idsFromShowTL){
            liveSites.add(DB1SqlHelper.getInstance(this).getSite(s));

        }




        viewLivePager=(ViewPager) findViewById(R.id.viewLivePager);
        viewLivePager.setCurrentItem(1);
        viewLivePagerAdapter=new ViewLivePagerAdapter(getSupportFragmentManager());
        viewLivePager.setAdapter(viewLivePagerAdapter);

        viewLivePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewLivePager.setCurrentItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                recyclerView.smoothScrollToPosition(viewLivePager.getCurrentItem());


            }
        });


        //section Live Geolocalization

        //filling up ArrayList with lat/long for everySite through idsFromShowTL

        for (String s: idsFromShowTL){
            Site site=DB1SqlHelper.getInstance(this).getSite(s);
            Location l=new Location("gpsprovider");
            l.setLatitude(site.latitude);
            l.setLongitude(site.longitude);
            arrayOfLocation.add(l);
        }

        showingOfflineMap();

        //GoogleMapClient implementation
        liveGoogleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        liveGoogleApiClient.connect();

        for (int i=0; i<idsFromShowTL.size(); i++){
            arrayOfNotificationSent.add(false);
        }

    }//fine onCreate




    @Override
    public void onBackPressed(){

        areYouSure();

    }



    private static class ViewLivePagerAdapter extends FragmentStatePagerAdapter {

        public ViewLivePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position) {


            final Bundle bundle = new Bundle();
            final LivePagerFragment fragment = new LivePagerFragment();
//First 3 info of ViewPager
            bundle.putString("title", liveSites.get(position).name);
            bundle.putString("description",liveSites.get(position).address);
            bundle.putString("id",liveSites.get(position).id);

            fragment.setArguments(bundle);
            return fragment;


        }






        @Override
        public int getCount() {


            return (liveSites.size());
        }




    }//fine ViewLivePagerAdapter


    //show distance between actual site and next site in recyclerview(topside)
    private String showNextDistance (String idActualSite, String idNextSite){

        Site actualSite=DB1SqlHelper.getInstance(this).getSite(idActualSite);
        Site nextSite=DB1SqlHelper.getInstance(this).getSite(idNextSite);
        final DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(3);
        df.setRoundingMode(RoundingMode.HALF_UP);

        return df.format(SphericalMercator.getDistanceFromLatLonInKm(actualSite, nextSite));

    }


//SECTION: LIVE GEOLOCALIZATION
//To avoid some issues with geofencing, the localization and decision about which site the user is around will be made with mere math calcs on lat/long
//Every site shown in map will be associated to its lat/long and the decision will be made everytime a new Location from GPS is registered

    //methods required by GoogleApiInterfaces

    public void onConnected(Bundle hintBundle) {


        if(!(serviceGPS.isProviderEnabled(LocationManager.GPS_PROVIDER))){

//            showAppropriateDialog(Constants.SHOW_NO_GPS_DIALOG);
            FragmentManager fmNoGps = getSupportFragmentManager();
            NoGpsDialog hfNoGps = new NoGpsDialog();
            hfNoGps.show(fmNoGps, "nogps_dialog");

        }

        else {
            Location previousLocation = LocationServices.FusedLocationApi.getLastLocation(
                    liveGoogleApiClient);

            actualUserLocalization(previousLocation);
            mLocationRequest = createLocationRequest();
            if (mLocationRequest != null) {
                startLocationUpdates();
            }
        }
    }

    public void onConnectionSuspended(int i) {


    }

    public void onConnectionFailed(ConnectionResult connResult) {
        Toast.makeText(this, R.string.connectionFailed, Toast.LENGTH_LONG).show();
    }
//method that will return actual user location every X seconds

    private void actualUserLocalization(Location actualUserLocation){
        double maxDistance=Double.MAX_EXPONENT;
        int nearestSitePosition=0;

        //move the map to center userLocation
        updateUI(actualUserLocation);

        //now all methods to move recycler/viewpager


        for (int i=0; i<arrayOfLocation.size(); i++){
            if (
                    (actualUserLocation.getLatitude() > arrayOfLocation.get(i).getLatitude() - RADIUS) &&
                            (actualUserLocation.getLatitude() < arrayOfLocation.get(i).getLatitude() + RADIUS) &&

                            (actualUserLocation.getLongitude() > arrayOfLocation.get(i).getLongitude() - RADIUS) &&
                            (actualUserLocation.getLongitude() > arrayOfLocation.get(i).getLongitude() + RADIUS)
                    ) {

//if all the conditions are true, then the user is inside a radius's site: to distinguish which site is closer in the events user is inside multiple radius
//check on the distance between actualPosition and site location
                double minDistance=SphericalMercator.getDistanceFromLatLonInKm
                        (
                                actualUserLocation.getLatitude(),actualUserLocation.getLongitude(),
                                arrayOfLocation.get(i).getLatitude(), arrayOfLocation.get(i).getLongitude()
                        );
                if (minDistance<maxDistance){
                    maxDistance=minDistance;
                    nearestSitePosition=i;
                }//fine check sulla distanza
            }//fine check if in area or not
        } //fine Fore
//at the end of the for I'll have nearestSitePosition that point to the site (retrieving it from array's position number) in which area the user is. Move the ViewPager and RecyclerView to match this site
        int positionInRecycler=checkForCorrispondentLocation(arrayOfLocation.get(nearestSitePosition).toString());
        moveViewPagerAndRecyclerView(positionInRecycler);




    }//fine actualUserLocalization


    private int checkForCorrispondentLocation(String locationInString){

//NB: since arrayOfLocation was made from idsFromShowTL, ids and coords of corrispondent position belong to the same Site
//so here, arrayOfLocation.get(i) point to the idsFromShowTL that I need to retrieve.
//more: the same position rules on recycler view, so i can return just the position
        Location thisLocation=new Location("gpsprovider");
        for (int i=0; i<arrayOfLocation.size();i++){
            if ( TextUtils.equals(arrayOfLocation.get(i).toString(), locationInString)){
                return i;
            }

        }
        return -1;
    }


    private void moveViewPagerAndRecyclerView(final int i){
        Log.d("miotag","LIVE posizione: "+i);

        recyclerView.smoothScrollToPosition(i);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

        RecyclerViewHolder rVh=(RecyclerViewHolder)recyclerView.findViewHolderForLayoutPosition(i);
        rVh.setBackgroundAsHighlighted();

            }
        }, 500);


        viewLivePager.setCurrentItem(i);

        //here the logic for notification

        showNotificationFor(i);


    }


    private void updateUI(Location l){


        actualGeoPoint=new GeoPoint(l.getLatitude(),l.getLongitude());
        map=(MapView) findViewById(R.id.mapLive);
        mapController = map.getController();
        mapController.setZoom(ZOOM);

        mapController.setCenter(actualGeoPoint);
        org.osmdroid.bonuspack.overlays.Marker mark = new org.osmdroid.bonuspack.overlays.Marker(map);
        mark.setPosition(actualGeoPoint);
        mark.setIcon(getResources().getDrawable(R.drawable.pin_blue));
        mark.setTitle(getResources().getString(R.string.your_position));
        mark.showInfoWindow();
        map.getOverlays().add(mark);
        map.invalidate();

    }



    protected LocationRequest createLocationRequest() {
        LocationRequest mLoRequest = new LocationRequest();
        mLoRequest.setInterval(30000);
        mLoRequest.setFastestInterval(20000);
        mLoRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLoRequest;
    }
    //interface for LocationListener
    private void startLocationUpdates(){

        LocationServices.FusedLocationApi.requestLocationUpdates(liveGoogleApiClient, mLocationRequest, this);

    }


    @Override
    public void onLocationChanged(Location location) {
        actualUserLocalization(location);
    }

    protected void stopLocationUpdates() {
        if (liveGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    liveGoogleApiClient, this);
            liveGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        stopLocationUpdates();
        liveGoogleApiClient.disconnect();
        map.getOverlays().remove(overlayEventos);
        map.getOverlays().clear();
        map.getTileProvider().createTileCache();
        map.getTileProvider().detach();


    }


    @Override
    public void onStop(){
        super.onStop();
        stopLocationUpdates();
        if (liveGoogleApiClient != null) {
            if (liveGoogleApiClient.isConnected()) {
                liveGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        if (liveGoogleApiClient != null) {
            if (!(liveGoogleApiClient.isConnected())) {
                liveGoogleApiClient.connect();
            }
        }


        String json=Repository.retrieve(this, "idsToSave", String.class);
        if ((json!= null) || (json!="")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            idsFromShowTL= gson.fromJson(json, type);

            String json2=Repository.retrieve(this, "showToSave", String.class);
            Gson gson2 = new Gson();
            showingTimeShowTL=gson2.fromJson(json2,type);
        }
        fillingUpWithPins();

        //section for tutorial


        boolean tutorialNotSeen=Repository.retrieve(this, Constants.TUTORIAL_LIVE_ACTIVITY_NOT_SEEN,Boolean.class);
//        boolean tutorialNotSeen=true;
        if (tutorialNotSeen) {

            FragmentManager fmLiveTutorial=getSupportFragmentManager();
            LiveTutorialFragment sLive=new LiveTutorialFragment();
            sLive.show(fmLiveTutorial,"setting_tutorial_fragment");

            Repository.save(this, Constants.TUTORIAL_LIVE_ACTIVITY_NOT_SEEN, false);
        }

    }

    private void showingOfflineMap() {

        map = (org.osmdroid.views.MapView) findViewById(R.id.mapLive);

        if (TextUtils.equals(Repository.retrieve(this, Constants.ACTIVATE_ONLINE_CONNECTION, String.class), Constants.ONLINE_CONNECTION_OFF)) {
            map.setTileSource(new XYTileSource("MapQuest",
                    ResourceProxy.string.mapquest_osm, 17, 17, 300, ".jpg", new String[]{
                    "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
            map.setBuiltInZoomControls(false);
            map.setMultiTouchControls(false);
            map.setUseDataConnection(false);
        }


        mapController = map.getController();
        mapController.setZoom(ZOOM);

        //TODO settare il centro della visuale sul PRIMO elemento della lista in dettagli
        mapController.setCenter(new GeoPoint(arrayOfLocation.get(0).getLatitude(), arrayOfLocation.get(0).getLongitude()));
        mapNoEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                return false;
            }
        };

        overlayNoEventos = new MapEventsOverlay(this, mapNoEventsReceiver);
        map.getOverlays().add(overlayNoEventos);
    }


    private void fillingUpWithPins(){
        ArrayList<GeoPoint>gpList=new ArrayList<>();
//        PathOverlay myPath = new PathOverlay(Color.RED, this);
        Polyline myPolyline=new Polyline(this);
        myPolyline.setWidth(5.0f);
        myPolyline.setColor(Color.RED);
        if (actualGeoPoint != null) {
            gpList.add(actualGeoPoint);
        }
        for (Site site: liveSites) {

            GeoPoint gp = new GeoPoint(site.latitude, site.longitude);
            org.osmdroid.bonuspack.overlays.Marker mark = new org.osmdroid.bonuspack.overlays.Marker(map);
            mark.setPosition(gp);
            mark.setIcon(getResources().getDrawable(R.drawable.pin_red));
            mark.setTitle(site.name);
           gpList.add(gp);
            map.getOverlays().add(mark);
            map.invalidate();

        }

        //drawing direction lines
            myPolyline.setPoints(gpList);
            map.getOverlays().add(myPolyline);
            map.invalidate();

    }

    private void showNotificationFor(int positionSiteToNotify){

        if (!arrayOfNotificationSent.get(positionSiteToNotify)) {

            Site site = DB1SqlHelper.getInstance(this).getSite(idsFromShowTL.get(positionSiteToNotify));
            arrayOfNotificationSent.set(positionSiteToNotify,true);
            Intent intent = new Intent(this, SiteNotificationService.class);
            Bundle bundle = new Bundle();
            bundle.putString("idNotification",site.id);
            bundle.putString("titleNotification", site.name);
            bundle.putString("addressNotification", site.address + ", " + site.addressCivic);
            bundle.putString("categoryNotification",site.typeOfSite);
            bundle.putString("pictureUrlNotification",site.pictureUrl);

            intent.putExtras(bundle);
            startService(intent);

        }  else {

        }


    }

    private void areYouSure(){
        FragmentManager asFrag=getSupportFragmentManager();
        AreYouSureFragment sureFragment=new AreYouSureFragment();
        sureFragment.show(asFrag, "areyousure_fragment");
    }

    public void onComplete(boolean sure) {
       this.sure=sure;

        if (sure){
            HomeActivity.destroyTourPreferences(getBaseContext());
            startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
            finish();
        }

    }



}


