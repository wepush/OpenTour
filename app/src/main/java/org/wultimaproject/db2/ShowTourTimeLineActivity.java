
package org.wultimaproject.db2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.views.MapView;

import java.lang.reflect.Type;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.wultimaproject.db2.fragments_dialogs.ListViewTimeLineFragment;
import org.wultimaproject.db2.services.TourAlgorithmTask;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.structures.Site;
import org.wultimaproject.db2.utils.Repository;
import org.wultimaproject.db2.utils.TimeLineAdapter;

import java.util.ArrayList;

public class ShowTourTimeLineActivity extends AppCompatActivity {
    public ProgressBar progressBar;
    private ListView lw;
    private static LayoutInflater inflater;
    private ListViewTimeLineFragment listViewTimeLineFragment;
    public ArrayList<Site> siteToStamp;
    private ArrayList<String> idSitesToShow;
    private ArrayList<String> showTimeSitesToShow;
    private TextView txtFirstAddress,txtFirstSiteTime;
//    private Type type;

//17/07 section for osm drod
    private final static int ZOOM=17;
    private org.osmdroid.views.MapView map;
    private MapEventsOverlay overlayEventos;
    private  MapEventsReceiver mapEventsReceiver;
    private IMapController mapController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TourAlgorithmTask(this,"random").execute();




        setContentView(R.layout.showtourtimeline_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTimeLine);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        lw = (ListView) findViewById(R.id.lwShowTimeLine);
        inflater= getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header_with_map, lw, false);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Gson gson=new Gson();
//                String idToSend=gson.toJson(idSitesToShow);
//                Intent i=new Intent(getBaseContext(), DiscoveryActivity.class);
//                i.putExtra("id",idToSend);

                Intent i=new Intent(getBaseContext(),LiveMapActivity.class);

                i.putStringArrayListExtra("id",idSitesToShow);
                i.putStringArrayListExtra("showingTime",showTimeSitesToShow);
                Log.d("miotag","intent"+i.getStringArrayListExtra("id"));
                startActivity(i);
            }
        });
        lw.addHeaderView(header);


        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowNavigationShowTourTimeLine);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               Repository.removeEverything(getBaseContext());
                HomeActivity.destroyTourPreferences(getBaseContext());
                siteToStamp.clear();

                startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
                finish();
            }
        });
    }//fine onCreate

//    @Override
//    public void onStop(){
//        super.onStop();
//       HomeActivity.destroyTourPreferences(this);
//    }




    public void showResultFromAlgorithm() {


//         DECOMMENTARE QUANDO SI UTILIZZERA' LA POSIZIONE ATTUALE DELL'UTENTE DA TOURALGORITHMTASK

//        txtFirstAddress=(TextView)findViewById(R.id.txtAddressFirstElement);
//        txtFirstAddress.setText(Repository.retrieve(this, Constants.WHERE_SAVE,String.class));
//        txtFirstSiteTime=(TextView)findViewById(R.id.txtStartingTimeTimeLine);
//        txtFirstSiteTime.setText(Repository.retrieve(this, Constants.WHEN_SAVE,String.class));

        for (Site site : siteToStamp) {
        }

        listViewTimeLineFragment = new ListViewTimeLineFragment();


        for (int i = 0; i < siteToStamp.size(); i++) {
        }
        lw.setAdapter(new TimeLineAdapter(this, siteToStamp));

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - 1;

                if (position > -1) {

                    Intent intentToShowSite = new Intent(getBaseContext(), ShowDetailsActivity.class);
                    intentToShowSite.putExtra("siteId", siteToStamp.get(position).id);

//                    intentToShowSite.putExtra("time",siteToStamp.get(position).showingTime);
                    startActivity(intentToShowSite);


                }
                //  Toast.makeText(getBaseContext(),"Prossimamente!",Toast.LENGTH_SHORT).show();
            }
        });


        //preparazione dell'array di id e showingTime da passare a LiveMapActivity per la visualizzazione/interazione

        idSitesToShow = new ArrayList<>();
        for (Site site : siteToStamp) {
            idSitesToShow.add(site.id);
        }

        showTimeSitesToShow = new ArrayList<>();
        for (Site site : siteToStamp) {
            showTimeSitesToShow.add(site.showingTime);
        }


        //SEZIONE RELATIVA ALLA SUMMERIZING MAPPA: Commented out 17/07 to make room for OSMDroid

//        MapView mv=(MapView)findViewById(R.id.mapSummaryTL);
//        mv.setCenter(findTourCenter(siteToStamp));
//        //TODO marker da cancellare al rilascio: sole funzionalità di controllo debug del centro
//        mv.addMarker(new Marker("", "", findTourCenter(siteToStamp)));
//        mv.setZoom(14);
//        PathOverlay paths=new PathOverlay(Color.CYAN,2);
//
//        for (Site site: siteToStamp){
//            LatLng lt=new LatLng(site.latitude,site.longitude);
//            Marker mark=new Marker(site.name,"",lt);
//            mark.setMarker(getResources().getDrawable(R.drawable.pin_blue));
//            //creazione paths
//            paths.addPoint(lt);
//
//            mv.addMarker(mark);
//        }
//
//            mv.getOverlays().add(paths);
//
//
//    }//fine showResultForAlgorithm
//
//
//    private LatLng findTourCenter(ArrayList<Site> a){
//        double latitude=0.0;
//        double longitude=0.0;
//        for (Site s: a){
//            latitude=latitude+s.latitude;
//            longitude=longitude+s.longitude;
//        }
//        LatLng lt=new LatLng(latitude/(Double.valueOf(a.size())),longitude/(Double.valueOf(a.size())));
//        Log.d("miotag","CENTRO: "+lt.getLatitude()+" ,"+lt.getLongitude());
//        return lt;
//    }


        map = (org.osmdroid.views.MapView) findViewById(R.id.mapSummaryTL);
        map.setTileSource(new XYTileSource("MapQuest",
                ResourceProxy.string.mapquest_osm, 13, 17, 300, ".jpg", new String[]{
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(false);
        map.setUseDataConnection(false);
        mapController = map.getController();
        mapController.setZoom(ZOOM);
        mapController.setCenter(findTourCenter(siteToStamp));
//        overlayEventos = new MapEventsOverlay(this, mapEventsReceiver);
//
//        GeoPoint gping=new GeoPoint(siteToStamp.get(0).latitude,siteToStamp.get(0).longitude);
//        Log.d("miotag", "geopoin generato: " + gping.getLatitude() + ", " + gping.getLongitude());
//        org.osmdroid.bonuspack.overlays.Marker marking = new org.osmdroid.bonuspack.overlays.Marker(map);
//        marking.setPosition(gping);
//        marking.setIcon(getResources().getDrawable(R.drawable.pin_blue));
//        map.getOverlays().add(startMarker);
//
//        overlayEventos = new MapEventsOverlay(this, mapEventsReceiver);
//        map.getOverlays().add(overlayEventos);

        for (Site site: siteToStamp){

            Log.d("miotag","devo mostrare il sito "+site.name+" con coords: "+site.latitude+", "+site.longitude);
            GeoPoint gp=new GeoPoint(site.latitude,site.longitude);
            Log.d("miotag", "geopoin generato: " + gp.getLatitude() + ", " + gp.getLongitude());
            org.osmdroid.bonuspack.overlays.Marker mark = new org.osmdroid.bonuspack.overlays.Marker(map);
            mark.setPosition(gp);
            mark.setIcon(getResources().getDrawable(R.drawable.pin_blue));
            map.getOverlays().add(mark);
//            overlayEventos = new MapEventsOverlay(this, mapEventsReceiver);
//            map.getOverlays().add(overlayEventos);
            map.invalidate();

        }






    }//fine showResultTimeLine



private GeoPoint findTourCenter(ArrayList<Site> a){
        double latitude = 0.0;
        double longitude = 0.0;
        for (Site s : a){
            latitude=latitude+s.latitude;
            longitude=longitude+s.longitude;
        }
//        LatLng lt=new LatLng(latitude/(Double.valueOf(a.size())),longitude/(Double.valueOf(a.size())));
            GeoPoint gp=new GeoPoint(latitude/(Double.valueOf(a.size())),longitude/(Double.valueOf(a.size())));
        Log.d("miotag","CENTRO: "+gp.getLatitude()+" ,"+gp.getLongitude());
        return gp;
    }

    }//fine Classe
