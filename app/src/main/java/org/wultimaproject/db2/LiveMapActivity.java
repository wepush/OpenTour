package org.wultimaproject.db2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

import org.wultimaproject.db2.utils.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class LiveMapActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<String> idsFromShowTL;

    private com.mapbox.mapboxsdk.views.MapView mv;
    private Marker mark;

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
        idsFromShowTL=intent.getStringArrayListExtra("id");


        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewDiscovery);

        //link to LayoutManager
        LinearLayoutManager lManager=new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lManager);
        RecyclerAdapter recAdapter=new RecyclerAdapter(idsFromShowTL);

        recyclerView.setAdapter(recAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    //Section MAPPA
        mv=(MapView) findViewById(R.id.mapDiscovery);
        LatLng llng=new LatLng(45.470303,9.190306);
        mv.setCenter(llng);






    }//fine onCreate


}
