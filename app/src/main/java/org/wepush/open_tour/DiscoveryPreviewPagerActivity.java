package org.wepush.open_tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.wepush.open_tour.fragments_dialogs.DiscoveryPreviewPagerFragment;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.DiscoveryListViewAdapter;
import org.wepush.open_tour.utils.DiscoveryPreviewPagerAdapter;
import org.wepush.open_tour.utils.Repository;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 05/10/15.
 */
public class DiscoveryPreviewPagerActivity extends AppCompatActivity{

    private DiscoveryPreviewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private int positionTab;
//    private CardView cd0,cd1,cd2,cd3;
//    public static ArrayList<Site> museums,churches,villas,palaces;
    public static ArrayList<Site>dummy;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discoverypreviewpager_activity);

        //        museums=new ArrayList<>();
//        churches=new ArrayList<>();
//        villas=new ArrayList<>();
//        palaces=new ArrayList<>();
//
//        museums= DB1SqlHelper.getInstance(this).getSameCategorySite(this.getResources().getString(R.string.museums_and_art_galleries));
//        churches=DB1SqlHelper.getInstance(this).getSameCategorySite(this.getResources().getString(R.string.churchs_oratories_worship));
//        villas=DB1SqlHelper.getInstance(this).getSameCategorySite(this.getString(R.string.villas_gardens_parks));
//        palaces = DB1SqlHelper.getInstance(this).getSameCategorySite(this.getResources().getString(R.string.palaces_and_castels));






        String titleCity= "";
        titleCity=Repository.retrieve(getBaseContext(),Constants.KEY_CURRENT_CITY,String.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolDiscoveryPreviewPager);
        TextView txtTitleBar=(TextView) findViewById(R.id.txtTitleToolbarDiscoveryPreviewPager);
        if (titleCity.equals(Constants.CITY_MILAN)){
            titleCity=getResources().getString(R.string.milan);
        } else {
            titleCity=getResources().getString(R.string.palermo);
        }
        setSupportActionBar(toolbar);
        txtTitleBar.setText(titleCity);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowDiscoveryPreviewActivity);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.destroyTourPreferences(getBaseContext());
                startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
                finish();
            }
        });




        viewPager=(ViewPager) findViewById(R.id.viewPagerDiscoveryPreviewPagerAdapter);
        pagerAdapter= new DiscoveryPreviewPagerAdapter(getSupportFragmentManager(),this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               positionTab=position;
//               activateImageListeners();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        cd0=(CardView)findViewById(R.id.cdTouch0);
//        cd1=(CardView)findViewById(R.id.cdTouch1);
//        cd2=(CardView)findViewById(R.id.cdTouch2);
//        cd3=(CardView)findViewById(R.id.cdTouch3);
//
//        cd0.setOnClickListener(this);
//        cd1.setOnClickListener(this);
//        cd2.setOnClickListener(this);
//        cd3.setOnClickListener(this);





    } //fine onCreate






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeActivity.destroyTourPreferences(getBaseContext());
        startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
        finish();
    }

}



