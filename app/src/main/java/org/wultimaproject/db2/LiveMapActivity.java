package org.wultimaproject.db2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

import org.wultimaproject.db2.fragments_dialogs.LaunchingSettingFragment;
import org.wultimaproject.db2.fragments_dialogs.LivePagerFragment;
import org.wultimaproject.db2.structures.DB1SqlHelper;
import org.wultimaproject.db2.structures.Site;
import org.wultimaproject.db2.utils.RecyclerAdapter;
import org.wultimaproject.db2.utils.SphericalMercator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class LiveMapActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<String> idsFromShowTL;
    private ArrayList<String>showingTimeShowTL;
    private ArrayList<String> distanceShowTL;
    private ArrayList<Site> sitesToShow;
    public static ArrayList<Site> liveSites;

    private com.mapbox.mapboxsdk.views.MapView mv;
    private Marker mark;
    private ViewPager viewLivePager;
    private ViewLivePagerAdapter viewLivePagerAdapter;

    private static final int FIRST_LIVE_PAGE=0;
    private static final int SECOND_LIVE_PAGE=1;
    private static int counting=0;



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
        LinearLayoutManager lManager=new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(lManager);
        //0807
//        RecyclerAdapter recAdapter=new RecyclerAdapter(idsFromShowTL);
        RecyclerAdapter recAdapter=new RecyclerAdapter(idsFromShowTL,showingTimeShowTL,distanceShowTL);


//        RecyclerAdapter recAdapter=new RecyclerAdapter(liveSites);
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

            }
        });



    }//fine onCreate

    private static class ViewLivePagerAdapter extends FragmentStatePagerAdapter {

        public ViewLivePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }




        @Override
        public Fragment getItem(int position) {


            final Bundle bundle = new Bundle();
            final LivePagerFragment fragment = new LivePagerFragment();

            bundle.putString("title", liveSites.get(position).name);
            bundle.putString("description",liveSites.get(position).address);
            bundle.putString("type",liveSites.get(position).typeOfSite);

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

}


