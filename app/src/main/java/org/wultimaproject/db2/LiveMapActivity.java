package org.wultimaproject.db2;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;


import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.wultimaproject.db2.fragments_dialogs.LaunchingSettingFragment;
import org.wultimaproject.db2.fragments_dialogs.LivePagerFragment;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.structures.DB1SqlHelper;
import org.wultimaproject.db2.structures.Site;
import org.wultimaproject.db2.utils.RecyclerAdapter;
import org.wultimaproject.db2.utils.Repository;
import org.wultimaproject.db2.utils.SphericalMercator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class LiveMapActivity extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {


    private RecyclerView recyclerView;
    private static ArrayList<String> idsFromShowTL;
    private static ArrayList<String>showingTimeShowTL;
    private static ArrayList<String> distanceShowTL;
    private ArrayList<Site> sitesToShow;
    private ArrayList<Location> arrayOfLocation;
    public static ArrayList<Site> liveSites;

    private com.mapbox.mapboxsdk.views.MapView mv;
    private Marker mark;
    private ViewPager viewLivePager;
    private ViewLivePagerAdapter viewLivePagerAdapter;

    private LocationRequest mLocationRequest;

    private final static int ZOOM=17;
    private org.osmdroid.views.MapView map;
    private MapEventsOverlay overlayEventos;
    private MapEventsReceiver mapEventsReceiver;
    private IMapController mapController;

//    private static final int FIRST_LIVE_PAGE=0;
//    private static final int SECOND_LIVE_PAGE=1;
//    private static int counting=0;

    private static final double RADIUS=0.002;

//to avoid conflicts, in "actualUserLocalization" is used a custom GoogleApiCient

   private GoogleApiClient liveGoogleApiClient;
//actual Location of the user

//    private Location actualUserLocation;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livemap_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDiscovery);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowNavigationDiscovery);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });



    //Section RECYCLERVIEW
        //retrieve ids from intent directly
        Intent intent=getIntent();
        idsFromShowTL=new ArrayList<>();
        showingTimeShowTL=new ArrayList<>();
        distanceShowTL=new ArrayList<>();
        arrayOfLocation=new ArrayList<>();
        idsFromShowTL=intent.getStringArrayListExtra("id");
        showingTimeShowTL=intent.getStringArrayListExtra("showingTime");
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
        //0807
//        RecyclerAdapter recAdapter=new RecyclerAdapter(idsFromShowTL);
        RecyclerAdapter recAdapter=new RecyclerAdapter(idsFromShowTL,showingTimeShowTL,distanceShowTL,this);


//        RecyclerAdapter recAdapter=new RecyclerAdapter(liveSites);
        recyclerView.setAdapter(recAdapter);

//        23 luglio
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });


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



//    //Section MAPPA commentata 17/07 per OSMdroid
////        mv=(MapView) findViewById(R.id.mapDiscovery);
////        LatLng llng=new LatLng(45.470303,9.190306);
////        mv.setCenter(llng);
//
//        map = (org.osmdroid.views.MapView) findViewById(R.id.mapLive);
//        map.setTileSource(new XYTileSource("MapQuest",
//                ResourceProxy.string.mapquest_osm, 17, 17, 300, ".jpg", new String[]{
//                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
//                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
//                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
//                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
//
//
//
//
//        map.setBuiltInZoomControls(false);
//        map.setMultiTouchControls(false);
//        map.setUseDataConnection(false);
//        mapController = map.getController();
//        mapController.setZoom(ZOOM);
////        mapController.setCenter(new GeoPoint(Double.valueOf(Repository.retrieve(this, Constants.LATITUDE_STARTING_POINT,String.class)),Double.valueOf(Repository.retrieve(this,Constants.LONGITUDE_STARTING_POINT,String.class))));
//
//     //TODO settare il centro della visuale sul PRIMO elemento della lista in dettagli
//       mapController.setCenter(new GeoPoint(45.4699939,9.1809641));
//        Log.d("miotag", "initial location");



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
//                Log.d("miotag","onPageScrolled");

            }

            @Override
            public void onPageSelected(int position) {
                viewLivePager.setCurrentItem(position);
//                Log.d("miotag","onPageSelected");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("miotag","onPageScrollStateChanged");
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




        //Section MAPPA commentata 17/07 per OSMdroid
//        mv=(MapView) findViewById(R.id.mapDiscovery);
//        LatLng llng=new LatLng(45.470303,9.190306);
//        mv.setCenter(llng);

        map = (org.osmdroid.views.MapView) findViewById(R.id.mapLive);
        map.setTileSource(new XYTileSource("MapQuest",
                ResourceProxy.string.mapquest_osm, 17, 17, 300, ".jpg", new String[]{
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));




        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(false);
        map.setUseDataConnection(false);
        mapController = map.getController();
        mapController.setZoom(ZOOM);
//        mapController.setCenter(new GeoPoint(Double.valueOf(Repository.retrieve(this, Constants.LATITUDE_STARTING_POINT,String.class)),Double.valueOf(Repository.retrieve(this,Constants.LONGITUDE_STARTING_POINT,String.class))));

        //TODO settare il centro della visuale sul PRIMO elemento della lista in dettagli
        mapController.setCenter(new GeoPoint(arrayOfLocation.get(0).getLatitude(),arrayOfLocation.get(0).getLongitude()));
        Log.d("miotag", "initial location");


        //GoogleMapClient implementation
        liveGoogleApiClient=new GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();




//TODO: DECOMMENTARE a seguire per riattivare il navigatore
        liveGoogleApiClient.connect();
//        actualUserLocalization();

    }//fine onCreate

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
            bundle.putString("type",liveSites.get(position).typeOfSite);

//Seconds of ViewPager
//            bundle.putString("distanceCovered",distanceShowTL.get(position));
//            bundle.putString("picture",liveSites.get(position).pictureUrl);
//            bundle.putString("time",showingTimeShowTL.get(position));
//            bundle.putString("siteId",idsFromShowTL.get(position));



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

        return df.format(SphericalMercator.getDistanceFromLatLonInKm(actualSite,nextSite));

    }


//SECTION: LIVE GEOLOCALIZATION
//To avoid some issues with geofencing, the localization and decision about which site the user is around will be made with mere math calcs on lat/long
//Every site shown in map will be associated to its lat/long and the decision will be made everytime a new Location from GPS is registered

    //methods required by GoogleApiInterfaces

                        public void onConnected(Bundle hintBundle) {
                            Location previousLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    liveGoogleApiClient);

                            Log.d("miotag","luogo trovato con coordinate: "+previousLocation.getLatitude()+", "+previousLocation.getLongitude()+", "+previousLocation.getAccuracy());

                            actualUserLocalization(previousLocation);
                            mLocationRequest=createLocationRequest();
                            if (mLocationRequest != null) {
                                startLocationUpdates();
                            }
                        }

                        public void onConnectionSuspended(int i) {


                        }

                        public void onConnectionFailed(ConnectionResult connResult) {


                        }
//method that will return actual user location every X seconds

    private void actualUserLocalization(Location actualUserLocation){
        double maxDistance=Double.MAX_EXPONENT;
        int nearestSitePosition=0;

        Log.d("miotag","actuaUserLocalization con location:"+" "+actualUserLocation.getLatitude()+", "+actualUserLocation.getLongitude());

        //move the map to center userLocation
        updateUI(actualUserLocation);

        //now all methods to move recycler/viewpager


        for (int i=0; i<arrayOfLocation.size(); i++){
            Log.d("miotag","arrayOFLocation posizione entro cui rientriare: "+arrayOfLocation.get(i).getLatitude()+", "+arrayOfLocation.get(i).getLongitude());
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
                Log.d("miotag","distanza attuale: "+minDistance);
                if (minDistance<maxDistance){
                    maxDistance=minDistance;
                    nearestSitePosition=i;
                    Log.d("miotag","NEAREST position+minDistance: "+nearestSitePosition+","+minDistance);
                }//fine check sulla distanza
            }//fine check if in area or not
        } //fine Fore
//at the end of the for I'll have nearestSitePosition that point to the site (retrieving it from array's position number) in which area the user is. Move the ViewPager and RecyclerView to match this site
        int positionInRecycler=checkForCorrispondentLocation(arrayOfLocation.get(nearestSitePosition).toString());
        Log.d("miotag","positionInRecycler: "+positionInRecycler);
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


    private void moveViewPagerAndRecyclerView(int i){

        recyclerView.smoothScrollToPosition(i);
        viewLivePager.setCurrentItem(i);


    }


    private void updateUI(Location l){


        Log.d("miotag", "sposto la mappa in ponew GeoPoint(sizione: " + l.getLatitude() + ", " + l.getLongitude());
        GeoPoint actualGeoPoint=new GeoPoint(l.getLatitude(),l.getLongitude());
        map=(MapView) findViewById(R.id.mapLive);
        mapController = map.getController();
        mapController.setZoom(ZOOM);

        mapController.setCenter(actualGeoPoint);
        org.osmdroid.bonuspack.overlays.Marker mark = new org.osmdroid.bonuspack.overlays.Marker(map);
        mark.setPosition(actualGeoPoint);
        mark.setIcon(getResources().getDrawable(R.drawable.pin_blue));
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
        Log.d("miotag","LOCATION UPDATE Con: "+location.getLatitude()+", "+location.getLongitude());
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
//TODO decommentare per rilascio
        stopLocationUpdates();
//        if (liveGoogleApiClient != null) {
//            if (liveGoogleApiClient.isConnected()) {
//                liveGoogleApiClient.disconnect();
//            }
//        }
        liveGoogleApiClient.disconnect();
    }




}


