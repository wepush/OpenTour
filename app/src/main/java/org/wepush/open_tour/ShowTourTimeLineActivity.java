
package org.wepush.open_tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.wepush.open_tour.fragments_dialogs.InsufficientSettingsDialogFragment;
import org.wepush.open_tour.fragments_dialogs.ListViewTimeLineFragment;
import org.wepush.open_tour.services.TourAlgorithmTask;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;
import org.wepush.open_tour.utils.SphericalMercator;
import org.wepush.open_tour.utils.TimeLineAdapter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ShowTourTimeLineActivity extends AppCompatActivity {
    public ProgressBar progressBar;
    public LinearLayout ll1;
    private ListView lw;
    private static LayoutInflater inflater;
    private ListViewTimeLineFragment listViewTimeLineFragment;
    public ArrayList<Site> siteToStamp;
    private ArrayList<String> idSitesToShow;
    private ArrayList<String> showTimeSitesToShow;

    private final static int ZOOM=17;
    private static org.osmdroid.views.MapView map;
    private IMapController mapController;
    private MapEventsOverlay overlayNoEventos;
    private MapEventsReceiver mapNoEventsReceiver;

    private boolean disableFab;

    private static ArrayList<org.osmdroid.bonuspack.overlays.Marker> geoPointMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.showtourtimeline_activity);



        progressBar=(ProgressBar)findViewById(R.id.progressBarTourTimeLine);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        new TourAlgorithmTask(this, "random").execute();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTimeLine);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        lw = (ListView) findViewById(R.id.lwShowTimeLine);
        inflater= getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header_with_map, lw, false);
        header.setClickable(false);

        lw.addHeaderView(header);


        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowNavigationShowTourTimeLine);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity.destroyTourPreferences(getBaseContext());

                if (siteToStamp!=null) {
                    siteToStamp.clear();
                    disableFab=true;
                }

                startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
                finish();
            }
        });




        android.support.design.widget.FloatingActionButton fabButton=(android.support.design.widget.FloatingActionButton)findViewById(R.id.floatButtonInShowingTour);
        fabButton.setRippleColor(getResources().getColor(R.color.orange500));

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     if (disableFab) {
                         HomeActivity.destroyTourPreferences(getBaseContext());
                         startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
                         finish();


                     } else {

                         Intent i = new Intent(getBaseContext(), LiveMapActivity.class);
                         i.setAction(Constants.INTENT_FROM_SHOWTOURTL);
                         i.putStringArrayListExtra("id", idSitesToShow);
                         i.putStringArrayListExtra("showingTime", showTimeSitesToShow);

                         startActivity(i);
                         finish();
                     }

                }
            });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeActivity.destroyTourPreferences(getBaseContext());
        if (siteToStamp!=null) {
            siteToStamp.clear();
            disableFab=true;
        }
        startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
        finish();
    }


    @Override
    public void onResume(){
        super.onResume();
        if (geoPointMarkers!= null) {
            fillMapAgain(geoPointMarkers);
        }
    }



    public void showResultFromAlgorithm() {
        LinearLayout llView=(LinearLayout) this.findViewById(R.id.linearLayoutList);
        llView.setBackgroundColor(this.getResources().getColor(R.color.white));



        TextView firstElement=(TextView) findViewById(R.id.txtTitleFirstElement);
        firstElement.setText(getResources().getString(R.string.startingTour));

        TextView addressElement=(TextView) findViewById(R.id.txtAddressFirstElement);
        addressElement.setText(Repository.retrieve(this, Constants.WHERE_SAVE, String.class));

        TextView timeElement=(TextView) findViewById(R.id.txtStartingTimeTimeLine);
        timeElement.setText(Repository.retrieve(this,Constants.STARTING_TIME_READABLE_FORMAT,String.class));

        listViewTimeLineFragment = new ListViewTimeLineFragment();

        lw.setAdapter(new TimeLineAdapter(this, siteToStamp));

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - 1;

                if (position > -1) {

                  Intent intentToShowSite=new Intent(getBaseContext(),DetailsActivity.class);
                    intentToShowSite.setAction(Constants.INTENT_FROM_SHOWTOURTL);
                    intentToShowSite.putExtra("siteId", siteToStamp.get(position).id);
                    startActivity(intentToShowSite);



                }
            }
        });


        idSitesToShow = new ArrayList<>();
        for (Site site : siteToStamp) {
            idSitesToShow.add(site.id);
        }

        showTimeSitesToShow = new ArrayList<>();
        for (Site site : siteToStamp) {
            showTimeSitesToShow.add(site.showingTime);
        }

       map = (org.osmdroid.views.MapView) findViewById(R.id.mapSummaryTL);


        if (TextUtils.equals(Repository.retrieve(this, Constants.ACTIVATE_ONLINE_CONNECTION, String.class), Constants.ONLINE_CONNECTION_OFF)) {
            XYTileSource mCustomTileSource = new XYTileSource("MapQuest",
                    ResourceProxy.string.mapquest_osm, 13, 17, 300, ".jpg", new String[]{
                    "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile4.mqcdn.com/tiles/1.0.0/map/"});
            map.setTileSource(mCustomTileSource);
            map.setBuiltInZoomControls(false);
            map.setMultiTouchControls(false);
            map.setUseDataConnection(false);
        }


        mapController = map.getController();
        mapController.setZoom(ZOOM);
        mapController.setCenter(findTourCenter(siteToStamp));

        mapNoEventsReceiver=new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                Toast.makeText(getBaseContext(), R.string.goLiveFloating,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                Toast.makeText(getBaseContext(), R.string.goLiveFloating,Toast.LENGTH_SHORT).show();
               return false;
            }
        };

        overlayNoEventos = new MapEventsOverlay(this, mapNoEventsReceiver);
        map.getOverlays().add(overlayNoEventos);



        double totalPrice=0.0;
        TextView txtSummaryMoney=(TextView)findViewById(R.id.txtMoneySummary);

        TextView txtTourLasting=(TextView) findViewById(R.id.txtTimeSummary);
        txtTourLasting.setText(Repository.retrieve(this,Constants.TIME_TO_SPEND,String.class));

        TextView txtNumberSites=(TextView) findViewById(R.id.txtNumberSiteSummary);
        txtNumberSites.setText(String.valueOf(siteToStamp.size()));

        double totalKM=0.0;
        TextView txtDistance=(TextView) findViewById(R.id.txtKmSummary);


        if (geoPointMarkers!=null){

        }
            geoPointMarkers = new ArrayList<>();

            for (Site site : siteToStamp) {

                GeoPoint gp = new GeoPoint(site.latitude, site.longitude);
                org.osmdroid.bonuspack.overlays.Marker mark = new org.osmdroid.bonuspack.overlays.Marker(map);
                mark.setPosition(gp);
                mark.setIcon(getResources().getDrawable(R.drawable.pin_red));
                mark.setTitle(site.name);
                map.getOverlays().add(mark);
                map.invalidate();
                geoPointMarkers.add(mark);



                totalPrice = totalPrice + showPrice(site);

            }
            txtSummaryMoney.setText(String.valueOf(totalPrice) + " €");

            txtDistance.setText(String.valueOf(showDistance()) + " km");

            txtTourLasting.setText(convertSecondToHHMMString(Double.valueOf(Repository.retrieve(this, Constants.TIME_TO_SPEND, String.class))));




    }



private GeoPoint findTourCenter(ArrayList<Site> a){
        double latitude = 0.0;
        double longitude = 0.0;
        for (Site s : a){
            latitude=latitude+s.latitude;
            longitude=longitude+s.longitude;
        }
            GeoPoint gp=new GeoPoint(latitude/(Double.valueOf(a.size())),longitude/(Double.valueOf(a.size())));
        return gp;
    }


    public  void showDummyActivity(){
        disableFab=true;
        FragmentManager fm = getSupportFragmentManager();
        InsufficientSettingsDialogFragment hf = new InsufficientSettingsDialogFragment();
        hf.show(fm, "badsettings_fragment");

    }

    private double showPrice(Site site){

        JSONArray jsonTickets=new JSONArray();
        String ticketsJson=site.tickets;



        try {
            jsonTickets = new JSONArray(ticketsJson);

            if(jsonTickets!=null && jsonTickets.length()>0) {
                    JSONObject jsonTick=jsonTickets.getJSONObject(0); //got the i-position of ticket array as Object

                  return   Double.valueOf(jsonTick.getString("price"));

            }


        } catch(JSONException e){
            e.printStackTrace();

        }
        return 0.0;
    }

    private String showDistance(){
        double d=0.0;
                for (int i=0; i<siteToStamp.size()-1; i++){
                    d=d+ SphericalMercator.getDistanceFromLatLonInKm(siteToStamp.get(i), siteToStamp.get(i + 1));
                }

                    final DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    df.setRoundingMode(RoundingMode.HALF_UP);
                   return df.format(d);


    }

    private String convertSecondToHHMMString(double secondingTime)
    {
        int secondTime=(int)Math.round(secondingTime);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondTime*1000L));

        return time;

    }


    @Override
    public void onPause(){
        super.onPause();
        if (map != null) {
           for (org.osmdroid.bonuspack.overlays.Marker mark: geoPointMarkers){
               map.getOverlays().remove(mark);
               map.getOverlays().clear();
               map.getTileProvider().createTileCache();
               map.getTileProvider().detach();
           }
        }

    }

    private boolean fillMapAgain(ArrayList<Marker> geoArray){

        if (geoArray!=null){


            for (Marker mark: geoPointMarkers){

                map.getOverlays().add(mark);
                map.invalidate();


            }




        }


            return false;
    }


}
