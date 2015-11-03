//package org.wepush.open_tour;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import org.wepush.open_tour.structures.DB1SqlHelper;
//import org.wepush.open_tour.structures.Site;
//import org.wepush.open_tour.utils.DiscoveryPreviewListAdapter;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
///**
// * Created by antoniocoppola on 02/10/15.
// */
//public class DiscoveryPreviewActivity extends AppCompatActivity implements View.OnClickListener{
//
//    private Toolbar toolbar;
//    private ArrayList<Site> arrayList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.discoverypreview_activity);
//
//        toolbar=(Toolbar)findViewById(R.id.toolbarDiscoveryPreview);
//        toolbar.setTitle("Discovery Preview");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        ImageView mainImage=(ImageView)findViewById(R.id.discoveryPreviewMainImage);
//        ImageView image1=(ImageView)findViewById(R.id.discoverPreviewImageNl);
//        ImageView image2=(ImageView)findViewById(R.id.discoverPreviewImageNr);
//        ImageView image3=(ImageView)findViewById(R.id.discoverPreviewImageSl);
//        ImageView image4=(ImageView)findViewById(R.id.discoverPreviewImageSr);
//
//        ListView lw=(ListView)findViewById(R.id.listViewDiscoveryPreview);
//        arrayList=new ArrayList<>();
//
//        //teatro Massimo
//        //cattedrale
//        //palazzo dei normanni
//        //fontana pretoria
//        //quattrocanti
//
////        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
////
////
////        } else {
//            arrayList.add(DB1SqlHelper.getInstance(this).getSite("5506ae75694d610b31000000"));
//            arrayList.add(DB1SqlHelper.getInstance(this).getSite("5506ae76694d610b31220000"));
//            arrayList.add(DB1SqlHelper.getInstance(this).getSite("5506ae76694d610b31760000"));
//            arrayList.add(DB1SqlHelper.getInstance(this).getSite("5506ae76694d610b31460000"));
//            arrayList.add(DB1SqlHelper.getInstance(this).getSite("5506ae76694d610b31490000"));
////        }
//
//
//        DiscoveryPreviewListAdapter discoveryPreviewListAdapter=new DiscoveryPreviewListAdapter(this,arrayList);
//
//        lw.setAdapter(discoveryPreviewListAdapter);
//
//
//        mainImage.setOnClickListener(this);
//        image1.setOnClickListener(this);
//        image2.setOnClickListener(this);
//        image3.setOnClickListener(this);
//        image4.setOnClickListener(this);
//
//
//
//
//
//
//
//
//    }//fine onCreate
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d("miotag", "back");
//        HomeActivity.destroyTourPreferences(getBaseContext());
//        startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
//        finish();
//    }
//
//    @Override
//    public void onClick(View v){
//
//        switch (v.getId()){
//            case R.id.discoveryPreviewMainImage:
//                Log.d("miotag","mainImageClicked");
//                break;
//
//            case R.id.discoverPreviewImageNl:
//                Log.d("miotag","North Left");
//                break;
//
//            case R.id.discoverPreviewImageNr:
//                Log.d("miotag","North Right");
//                break;
//
//            case R.id.discoverPreviewImageSr:
//                Log.d("miotag","South Right");
//                break;
//
//            case R.id.discoverPreviewImageSl:
//                Log.d("miotag","South Left");
//                break;
//
//        }
//
//
//
//    }
//}
