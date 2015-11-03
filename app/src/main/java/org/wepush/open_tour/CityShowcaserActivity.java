package org.wepush.open_tour;

import android.app.usage.ConfigurationStats;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.wepush.open_tour.services.DownloadingCitiesService;
import org.wepush.open_tour.services.ReadFromJson;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.io.File;

/**
 * Created by antoniocoppola on 15/10/15.
 */
public class CityShowcaserActivity extends AppCompatActivity implements  View.OnClickListener{

    private ViewGroup rlFab,llBody;
    private ImageView imgArrowNavigation;
    private Toolbar toolbar;
    private CheckBox cbxMap,cbxImages;
    private TextView txtToolbar,txtIntroductionCity;
    private FloatingActionButton fab;
    private Intent intentForDownloading;
    private String packagesToDownload;
    private RelativeLayout rlShadowingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cityshowcaser_activity);



        Intent i=getIntent();
        String cityToShow=i.getStringExtra("city");


        cbxImages=(CheckBox)findViewById(R.id.cbxDownloadImages);
        cbxMap=(CheckBox)findViewById(R.id.cbxDownloadMaps);
        fab=(FloatingActionButton)findViewById(R.id.floatButtonInCityShowcaseActivity);


        toolbar=(Toolbar) findViewById(R.id.toolbarCityShowcaserActivity);
        rlShadowingToolbar=(RelativeLayout) findViewById(R.id.rlToolbarShadowing);

        imgArrowNavigation=(ImageView)findViewById(R.id.imageArrowNavigationShowcaserActivity);
        imgArrowNavigation.setOnClickListener(this);
        fab.setOnClickListener(this);

        setSupportActionBar(toolbar);

        txtToolbar=(TextView)findViewById(R.id.txtToolbarShowcaser);


//        rlMain=(ViewGroup)findViewById(R.id.rlLayoutCityShowcaserActivity);
        llBody=(ViewGroup) findViewById(R.id.llBody);
        rlFab=(ViewGroup)findViewById(R.id.rlFabCityShowcaseActivity);
        intentForDownloading=new Intent(this, DownloadingCitiesService.class);
        txtIntroductionCity=(TextView)findViewById(R.id.textIntroductionCity);


//        ImageView img=(ImageView)findViewById(R.id.imgShowcaseDetail);

        switch (cityToShow){
            case Constants.CITY_MILAN:
//                img.setImageDrawable(getResources().getDrawable(R.drawable.duomo));
                toolbar.setBackground(getResources().getDrawable(R.drawable.milano));
                txtToolbar.setText("Milano");
                txtIntroductionCity.setText(R.string.cityChooserIntroductionMilan);

                //saving current city as city chosen by the user
                Repository.save(getBaseContext(),Constants.KEY_CURRENT_CITY,Constants.CITY_MILAN);
//                packagesToDownload=Constants.CITY_MILAN;
                break;

            case Constants.CITY_PALERMO:
//                img.setImageDrawable(getResources().getDrawable(R.drawable.palermo_overview));
                toolbar.setBackground(getResources().getDrawable(R.drawable.palermo2));
                txtToolbar.setText("Palermo");
                txtIntroductionCity.setText(R.string.cityChooserIntroductionPalermo);

                Repository.save(getBaseContext(), Constants.KEY_CURRENT_CITY, Constants.CITY_PALERMO);
//                packagesToDownload=Constants.CITY_PALERMO;
                break;

        }


//        img.setScaleType(ImageView.ScaleType.FIT_CENTER);







        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    TransitionManager.beginDelayedTransition(toolbar, new Slide());
                    showSingleLayout(toolbar);
                }
            }, 300);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    TransitionManager.beginDelayedTransition(rlShadowingToolbar, new Slide());
                    showSingleLayout(rlShadowingToolbar);
                }
            }, 300);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    TransitionManager.beginDelayedTransition(llBody, new Slide());
                    showSingleLayout(llBody);
                }
            }, 500);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    TransitionManager.beginDelayedTransition(rlFab, new Slide());
                    showSingleLayout(rlFab);
                }
            }, 800);



        } else {

            toolbar.setVisibility(View.VISIBLE);
            llBody.setVisibility(View.VISIBLE);
            rlFab.setVisibility(View.VISIBLE);

        }


    }//fine onCreate


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, ChooseCityActivity.class);
        i.putExtra("fromSettingTourActivity", true);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v){
       int id=v.getId();

        switch(id){

            case R.id.imageArrowNavigationShowcaserActivity:
                Intent i=new Intent(this, ChooseCityActivity.class);
                i.putExtra("fromSettingTourActivity",true);
                startActivity(i);
                finish();

                //TODO animazione di back da CityShowcaserActivity a ChooseCityActivity
                //TODO CAMBIARE LA STRING HARDCODED per il toast del no paccketto selezionato
            break;

            case R.id.floatButtonInCityShowcaseActivity:

                if (cbxMap.isChecked() && cbxImages.isChecked()){
                    deleteMapsAndImages();
//                    intentForDownloading.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT, Constants.DOWNLOADING_MAPS_IMAGES);
//                    Log.d("miotag","in Showcaser, putExtra per intentForDownloading: "+intentForDownloading.getStringExtra(Constants.DOWNLOADING_BUNDLE_INTENT));
                    intentForDownloading.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT,Constants.DOWNLOADING_MAPS_ONLY);
                    Repository.save(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,Constants.DOWNLOADING_MAPS_IMAGES);
                    startService(intentForDownloading);
                    startActivity(new Intent(this, LoadingCityPackages.class));
                    finish();

//                    Log.d("miotag","scarica mappa ed immagini");


                } else if (!(cbxMap.isChecked()) && !(cbxImages.isChecked())){
//                    Log.d("miotag", "seleziona almeno un pacchetto da scaricare con campo intent: "+Constants.DOWNLOADING_BUNDLE_INTENT+", e contenuto:"+packagesToDownload);
                    Toast.makeText(this, "Seleziona almeno un pacchetto", Toast.LENGTH_SHORT).show();


                } else if ( cbxMap.isChecked() && (!(cbxImages.isChecked())) ){
//                    Log.d("miotag","scarica solo le mappe");
                    deleteMapsAndImages();
                    intentForDownloading.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT, Constants.DOWNLOADING_MAPS_ONLY);
                    Repository.save(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,Constants.DOWNLOADING_MAPS_ONLY);
                    startService(intentForDownloading);
                    startActivity(new Intent(this, LoadingCityPackages.class));
                    finish();
                } else if ( !(cbxMap.isChecked() && (cbxImages.isChecked()))){
                    deleteMapsAndImages();
                    intentForDownloading.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT, Constants.DOWNLOADING_IMAGES_ONLY);
                    Repository.save(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,Constants.DOWNLOADING_IMAGES_ONLY);
                    startService(intentForDownloading);
                    startActivity(new Intent(this, LoadingCityPackages.class));
                    finish();
//                    Log.d("miotag","scarica solo le immagini");

                }

                startService(new Intent(this, ReadFromJson.class));
            break;



        }
//        startActivity(new Intent(this, LoadingCityBundle.class));

    }


    private void showSingleLayout(ViewGroup v){
        if (v.getVisibility() == View.VISIBLE){
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }


//    private void showLayouts(LinearLayout... llViews){
//        for (LinearLayout llView: llViews){
//            if (llView.getVisibility()== View.VISIBLE){
//                llView.setVisibility(View.INVISIBLE);
//            } else {
//
//                llView.setVisibility(View.VISIBLE);
//            }
//
//        }
//    }



    private void deleteMapsAndImages(){

        Log.d("miotag","STO CANCELLANDO TUTTI I PACCHETTI!");

        File dirOsmDroid = new File(Environment.getExternalStorageDirectory().getPath()+"/osmdroid");
        if (dirOsmDroid.isDirectory())
        {

            String[] children = dirOsmDroid.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirOsmDroid, children[i]).delete();
            }
        }

        File dirMACOSX=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/_MACOSX");
        if (dirMACOSX.isDirectory())
        {

            String[] children = dirMACOSX.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirMACOSX, children[i]).delete();
            }
        }

        File dirMilanImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.UNZIPPED_IMAGES_MILAN_DOWNLOAD);
        if (dirMilanImages.isDirectory())
        {
            String[] children = dirMilanImages.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirMilanImages, children[i]).delete();
            }
        }


        File dirPalermoImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.UNZIPPED_IMAGES_PALERMO_DOWNLOAD);

        if (dirPalermoImages.isDirectory())
        {
            String[] children = dirPalermoImages.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirPalermoImages, children[i]).delete();
            }
        }




        File milanImagesZip=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
        File palermoImagesZip=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);

        dirMACOSX.delete();
        dirOsmDroid.delete();
        dirMilanImages.delete();
        dirPalermoImages.delete();
        milanImagesZip.delete();
        palermoImagesZip.delete();
    }

}
