
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
    private TextView txtFirstAddress,txtFirstSiteTime;
//    private Type type;





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




    public void showResultFromAlgorithm(){


//         DECOMMENTARE QUANDO SI UTILIZZERA' LA POSIZIONE ATTUALE DELL'UTENTE DA TOURALGORITHMTASK

//        txtFirstAddress=(TextView)findViewById(R.id.txtAddressFirstElement);
//        txtFirstAddress.setText(Repository.retrieve(this, Constants.WHERE_SAVE,String.class));
//        txtFirstSiteTime=(TextView)findViewById(R.id.txtStartingTimeTimeLine);
//        txtFirstSiteTime.setText(Repository.retrieve(this, Constants.WHEN_SAVE,String.class));

        for (Site site: siteToStamp) {
        }

        listViewTimeLineFragment=new ListViewTimeLineFragment();


        for (int i=0; i<siteToStamp.size(); i++){
        }
        lw.setAdapter(new TimeLineAdapter(this, siteToStamp));

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - 1;

                if (position > -1) {

                    Intent intentToShowSite=new Intent(getBaseContext(),ShowDetailsActivity.class);
                    intentToShowSite.putExtra("siteId",siteToStamp.get(position).id);
                    startActivity(intentToShowSite);


                }
                //  Toast.makeText(getBaseContext(),"Prossimamente!",Toast.LENGTH_SHORT).show();
            }
        });



        //preparazione dell'array di id da passare a DiscoveryActivity per la visualizzazione/interazione

        idSitesToShow=new ArrayList<>();
        for (Site site: siteToStamp){
            idSitesToShow.add(site.id);
        }


        //SEZIONE RELATIVA ALLA SUMMERIZING MAPPA

        MapView mv=(MapView)findViewById(R.id.mapSummaryTL);
        mv.setCenter(findTourCenter(siteToStamp));
        //TODO marker da cancellare al rilascio: sole funzionalità di controllo debug del centro
        mv.addMarker(new Marker("", "", findTourCenter(siteToStamp)));
        mv.setZoom(14);
        PathOverlay paths=new PathOverlay(Color.CYAN,2);

        for (Site site: siteToStamp){
            LatLng lt=new LatLng(site.latitude,site.longitude);
            Marker mark=new Marker(site.name,"",lt);
            mark.setMarker(getResources().getDrawable(R.drawable.pin_blue));
            //creazione paths
            paths.addPoint(lt);

            mv.addMarker(mark);
        }

            mv.getOverlays().add(paths);


    }//fine showResultForAlgorithm


    private LatLng findTourCenter(ArrayList<Site> a){
        double latitude=0.0;
        double longitude=0.0;
        for (Site s: a){
            latitude=latitude+s.latitude;
            longitude=longitude+s.longitude;
        }
        LatLng lt=new LatLng(latitude/(Double.valueOf(a.size())),longitude/(Double.valueOf(a.size())));
        Log.d("miotag","CENTRO: "+lt.getLatitude()+" ,"+lt.getLongitude());
        return lt;
    }

}//fine Classe
