////package org.wepush.open_tour;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.support.v7.app.AppCompatActivity;
////import android.support.v7.widget.Toolbar;
////import android.text.TextUtils;
////import android.view.View;
////import android.widget.ImageView;
////
////import org.osmdroid.ResourceProxy;
////import org.osmdroid.api.IMapController;
////import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
////import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
////import org.osmdroid.tileprovider.tilesource.XYTileSource;
////import org.osmdroid.util.GeoPoint;
////import org.osmdroid.views.MapView;
////import org.wepush.open_tour.structures.DB1SqlHelper;
////import org.wepush.open_tour.structures.Site;
////import org.wepush.open_tour.utils.Constants;
////import org.wepush.open_tour.utils.Repository;
////
////import java.util.ArrayList;
////
/////**
//// * Created by antoniocoppola on 28/09/15.
//// */
////public class DiscoveryActivity extends AppCompatActivity {
////
////    private final static int ZOOM=17;
////    private MapView map;
////    private IMapController mapController;
////    private MapEventsOverlay overlayNoEventos;
////    private MapEventsReceiver  mapNoEventsReceiver;
////    private ArrayList<Site> arrayAllLocations;
////
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState){
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.discovery_activity);
////
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDiscoveryActivity);
////        toolbar.setTitle("");
////        setSupportActionBar(toolbar);
////
////
////        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowNavigationDiscovery);
////        backArrow.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                HomeActivity.destroyTourPreferences(getBaseContext());
////                startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
////                finish();
////            }
////        });
////
////        //TODO inizializzare array da db, completare i metodi mancanti
////
////
////        arrayAllLocations= DB1SqlHelper.getInstance(this).getSites();
////
////        showDiscoveryMap();
////        pinPointAllLocations();
////
////
////    }//fine onCreate
////
////
////
////
////
////
////    private void showDiscoveryMap() {
////        map = (org.osmdroid.views.MapView) findViewById(R.id.mapDiscoveryActivity);
////
////        if (TextUtils.equals(Repository.retrieve(this, Constants.ACTIVATE_ONLINE_CONNECTION, String.class), Constants.ONLINE_CONNECTION_OFF)) {
////            map.setTileSource(new XYTileSource("MapQuest",
////                    ResourceProxy.string.mapquest_osm, 17, 17, 300, ".jpg", new String[]{
////                    "http://otile1.mqcdn.com/tiles/1.0.0/map/",
////                    "http://otile2.mqcdn.com/tiles/1.0.0/map/",
////                    "http://otile3.mqcdn.com/tiles/1.0.0/map/",
////                    "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
////            map.setBuiltInZoomControls(false);
////            map.setMultiTouchControls(false);
////            map.setUseDataConnection(false);
////        }
////
////
////        mapController = map.getController();
////        mapController.setZoom(ZOOM);
////
////        //TODO settare il centro della visuale sul PRIMO elemento della lista in dettagli
////        mapController.setCenter(new GeoPoint(38.115890, 13.361356));
////        mapNoEventsReceiver = new MapEventsReceiver() {
////            @Override
////            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
////                return false;
////            }
////
////            @Override
////            public boolean longPressHelper(GeoPoint geoPoint) {
////                return false;
////            }
////        };
////
////        overlayNoEventos = new MapEventsOverlay(this, mapNoEventsReceiver);
////        map.getOverlays().add(overlayNoEventos);
////    }
////
////    private void pinPointAllLocations(){
////
////        for (Site site: arrayAllLocations) {
////
////            GeoPoint gp = new GeoPoint(site.latitude, site.longitude);
////            org.osmdroid.bonuspack.overlays.Marker mark = new org.osmdroid.bonuspack.overlays.Marker(map);
////            mark.setPosition(gp);
////            mark.setIcon(getResources().getDrawable(R.drawable.pin_red));
////            mark.setTitle(site.name);
////            map.getOverlays().add(mark);
////            map.invalidate();
////
////        }
////
////    }
////
////    @Override
////    public void onBackPressed() {
////        super.onBackPressed();
////        HomeActivity.destroyTourPreferences(getBaseContext());
////        startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
////        finish();
////    }
////
////
////}
//
//package org.wepush.open_tour;
//
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//
//import org.wepush.open_tour.fragments_dialogs.DiscoveryPreviewPagerFragment;
//import org.wepush.open_tour.utils.DiscoveryPreviewPagerAdapter;
//
//public class DiscoveryActivity extends AppCompatActivity{
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.discovery_activity);
//    }
//
//    private void initViewPagerAndTabs() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerDiscoveryAdapter);
//        DiscoveryPreviewPagerAdapter pagerAdapter = new DiscoveryPreviewPagerAdapter(getSupportFragmentManager(),this);
//        viewPager.setAdapter(pagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
//    }
//
//
//}
//
//
//
