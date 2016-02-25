package org.wepush.open_tour;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private String packagesToDownload, city,cityToShow;
    private  String milanToDownload,palermoToDownload,turinToDownload;
    private RelativeLayout rlShadowingToolbar;
    private File dirMACOSX,dirOsmDroid,dirMilanImages,dirPalermoImages,milanImagesZip,palermoImagesZip,dirTurinImages,turinImagesZip;
    private boolean mapIsNotPresent=true;
    private boolean imagesAreNotPresent=true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cityshowcaser_activity);


        city=Repository.retrieve(this, Constants.KEY_CURRENT_CITY, String.class);

        Intent i=getIntent();
        cityToShow=i.getStringExtra("city");

//TODO CAMBIAMENTI 10/02
        milanToDownload = getResources().getString(R.string.milan);
        palermoToDownload=getResources().getString(R.string.palermo);
        //TORINO 10/02
        turinToDownload=getResources().getString(R.string.turin);



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

        llBody=(ViewGroup) findViewById(R.id.llBody);
        rlFab=(ViewGroup)findViewById(R.id.rlFabCityShowcaseActivity);
        intentForDownloading=new Intent(this, DownloadingCitiesService.class);
        txtIntroductionCity=(TextView)findViewById(R.id.textIntroductionCity);


        switch (cityToShow){
            case Constants.CITY_MILAN:
                toolbar.setBackground(getResources().getDrawable(R.drawable.milano));
                txtToolbar.setText(getResources().getString(R.string.milan));
                txtIntroductionCity.setText(R.string.cityChooserIntroductionMilan);


                break;

            case Constants.CITY_PALERMO:
                toolbar.setBackground(getResources().getDrawable(R.drawable.palermo2));
                txtToolbar.setText(getResources().getString(R.string.palermo));
                txtIntroductionCity.setText(R.string.cityChooserIntroductionPalermo);

                break;

            //TORINO 10/02

            case Constants.CITY_TURIN:
                toolbar.setBackground(getResources().getDrawable(R.drawable.piazza_san_carlo));
                txtToolbar.setText(getResources().getString(R.string.turin));
                txtIntroductionCity.setText(R.string.cityChooserIntroductionTurin);

                break;

        }




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

        dirOsmDroid = new File(Environment.getExternalStorageDirectory().getPath()+"/osmdroid");
        dirMACOSX=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/_MACOSX");
        dirMilanImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.UNZIPPED_IMAGES_MILAN_DOWNLOAD);
        dirPalermoImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.UNZIPPED_IMAGES_PALERMO_DOWNLOAD);
        dirTurinImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.UNZIPPED_IMAGES_TURIN_DOWNLOAD);

        milanImagesZip=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
        palermoImagesZip=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
        turinImagesZip=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.ZIPPED_IMAGES_TURIN_DOWNLOAD);


        checkIfAnyPackageIsPresent();

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
                    intentForDownloading.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT, Constants.DOWNLOADING_MAPS_ONLY);
                    Repository.save(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,Constants.DOWNLOADING_MAPS_IMAGES);
                    //TODO cambiamenti 10/02
                    String txtToolbarInString=txtToolbar.getText().toString();
//                    //TORINO 10/02
                                if (txtToolbarInString.equals(milanToDownload)) {
                                    Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_MILAN);
                                } else if (txtToolbarInString.equals(palermoToDownload)){
                                        Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_PALERMO);

                                } else if (txtToolbarInString.equals(turinToDownload)){
                                    Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_TURIN);

                                }

                                startService(intentForDownloading);
                                startActivity(new Intent(this, LoadingCityPackages.class));
                                finish();



                } else if (!(cbxMap.isChecked()) && !(cbxImages.isChecked())){
                    Toast.makeText(this, R.string.chooseAtLeastOnePackage, Toast.LENGTH_SHORT).show();


                } else if ( cbxMap.isChecked() && (!(cbxImages.isChecked())) ){

//                    deleteMapsAndImages();

                    if (!(city.equals(cityToShow))){

                        deleteMapsAndImages();
                    } // ELSE : user selected the same city he already choose, thus he want to download another package (map) thus no need to delete the other package


                    //TODO 19novembre
//                    if (mapIsNotPresent) {
                        intentForDownloading.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT, Constants.DOWNLOADING_MAPS_ONLY);
                        Repository.save(this, Constants.WHAT_I_WANT_TO_DOWNLOAD, Constants.DOWNLOADING_MAPS_ONLY);
                        String txtToolbarInString = txtToolbar.getText().toString();
                        if (txtToolbarInString.equals(milanToDownload)) {
                            Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_MILAN);
                        } else if (txtToolbarInString.equals(palermoToDownload)) {
                            Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_PALERMO);

                        } else if (txtToolbarInString.equals(turinToDownload)) {
                            Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_TURIN);

                        }
                        startService(intentForDownloading);
                        startActivity(new Intent(this, LoadingCityPackages.class));
                        finish();

                } else if ( !(cbxMap.isChecked() && (cbxImages.isChecked()))){

//                    deleteMapsAndImages();

                    if (!(city.equals(cityToShow))){

                        deleteMapsAndImages();
                    }// ELSE : as previous if

//                    if (imagesAreNotPresent) {
                        intentForDownloading.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT, Constants.DOWNLOADING_IMAGES_ONLY);
                        Repository.save(this, Constants.WHAT_I_WANT_TO_DOWNLOAD, Constants.DOWNLOADING_IMAGES_ONLY);
                        switch (txtToolbar.getText().toString()) {
                            case "Milano":
                                Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_MILAN);
                                break;

                            case "Palermo":
                                Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_PALERMO);
                            break;

                            case "Torino":
                                Repository.save(this, Constants.KEY_CURRENT_CITY, Constants.CITY_TURIN);
                            break;

                        }
                        startService(intentForDownloading);
                        startActivity(new Intent(this, LoadingCityPackages.class));
                        finish();
                }

                startService(new Intent(this, ReadFromJson.class));
            break;


        }
    }


    private void showSingleLayout(ViewGroup v){
        if (v.getVisibility() == View.VISIBLE){
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }



    private void deleteMapsAndImages(){
        if (dirOsmDroid.isDirectory())
        {

            String[] children = dirOsmDroid.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirOsmDroid, children[i]).delete();
            }
        }

        if (dirMACOSX.isDirectory())
        {

            String[] children = dirMACOSX.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirMACOSX, children[i]).delete();
            }
        }

        if (dirMilanImages.isDirectory())
        {
            String[] children = dirMilanImages.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirMilanImages, children[i]).delete();
            }
        }


        if (dirPalermoImages.isDirectory())
        {
            String[] children = dirPalermoImages.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirPalermoImages, children[i]).delete();
            }
        }

        if (dirTurinImages.isDirectory())
        {
            String[] children = dirTurinImages.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dirTurinImages, children[i]).delete();
            }
        }

        dirMACOSX.delete();
        dirOsmDroid.delete();
        dirMilanImages.delete();
        dirPalermoImages.delete();
        dirTurinImages.delete();
        milanImagesZip.delete();
        palermoImagesZip.delete();
        turinImagesZip.delete();
    }

    private void checkIfAnyPackageIsPresent(){

        if (city.equals(cityToShow)){
            switch(city){

                case Constants.CITY_MILAN:
                    if (dirMilanImages.isDirectory()){
                        cbxImages.setChecked(true);
                        imagesAreNotPresent=false;
                    }

                    if (dirOsmDroid.isDirectory()){
                        cbxMap.setChecked(true);
                        mapIsNotPresent=false;
                    }
                break;

                case Constants.CITY_PALERMO:
                    if (dirPalermoImages.isDirectory()){
                        cbxImages.setChecked(true);
                        imagesAreNotPresent=false;
                    }

                    if (dirOsmDroid.isDirectory()){
                        cbxMap.setChecked(true);
                        mapIsNotPresent=false;
                    }
                break;


                case Constants.CITY_TURIN:
                    if (dirTurinImages.isDirectory()){
                        cbxImages.setChecked(true);
                        imagesAreNotPresent=false;
                    }

                    if (dirOsmDroid.isDirectory()){
                        cbxMap.setChecked(true);
                        mapIsNotPresent=false;
                    }
                    break;

            }
        }

    }

}
