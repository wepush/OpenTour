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

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class LiveMapActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<String> idsFromShowTL;
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
        idsFromShowTL=intent.getStringArrayListExtra("id");
        //retrieve sites from db through db

        liveSites=new ArrayList<>();
        for (String s: idsFromShowTL){
           liveSites.add(DB1SqlHelper.getInstance(this).getSite(s));
        }

//        for (int i=0; i<liveSites.size(); i++){
//            Log.d("miotag","siti in arrivo: "+liveSites.get(i).name+" ,con posizione: "+i);
//        }
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


    //ViewPager for LiveSiteDetail

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
            Log.d("miotag", "ViewLivePager torna");

        }




        @Override
        public Fragment getItem(int position) {

        Log.d("miotag", "prima di LivePage, position: " + position);

            //LivePagerFragment.newInstance(liveSites.get(position).name, liveSites.get(position).address, liveSites.get(position).typeOfSite);

            Log.d("miotag", "dopo LivePage, position: " + position);

            //LivePagerFragment fragment = new LivePagerFragment();
            final Bundle bundle = new Bundle();
            final LivePagerFragment fragment = new LivePagerFragment();

            bundle.putString("title", liveSites.get(position).name);
            bundle.putString("description",liveSites.get(position).address);
            bundle.putString("type",liveSites.get(position).typeOfSite);

            fragment.setArguments(bundle);
            return fragment;

            //return new LivePagerFragment();
        }




//        @Override
//        public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);
//        }

        @Override
        public int getCount() {


            return (liveSites.size());
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            Log.d("miotag", "destroy!");
//        }


    }//fine ViewLivePagerAdapter

}


