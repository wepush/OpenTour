package org.wepush.open_tour;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.wepush.open_tour.services.DownloadingCitiesService;
import org.wepush.open_tour.services.UnzipService;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

/**
 * Created by antoniocoppola on 19/10/15.
 */
public class LoadingCityPackages extends AppCompatActivity {

    private Intent intentFromCityShowcaserActivity;
    private Intent intentToDownloadImages,intentToUnzipImages;
    private DownloadManager dm;
    private BroadcastReceiver receiver;
    private String actualCity;
    private IntentFilter intentFilter;
    private boolean mapAlreadyDownloaded=false;

    private TextView txtDownloadData,txtInstallData;


    @Override
    protected void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.loading_citybundle);

        ImageView gifLoading=(ImageView)findViewById(R.id.splashGifAnimation);
        AnimationDrawable animDrawable=(AnimationDrawable) gifLoading.getBackground();
        animDrawable.start();

//        txtDownloadData=(TextView)findViewById(R.id.textWaiting1);
//        txtInstallData=(TextView)findViewById(R.id.textWaiting2);

      actualCity= Repository.retrieve(this,Constants.KEY_CURRENT_CITY,String.class);

      //TODO AZZERARE il layout relativo a LoadingCityBundle
//      ProgressBar progressBar=(ProgressBar) findViewById(R.id.progressLoadingPackages);
//      progressBar.getIndeterminateDrawable().setColorFilter(
//              getResources().getColor(R.color.white),
//              android.graphics.PorterDuff.Mode.SRC_IN);


//      intentFromCityShowcaserActivity=getIntent();
//      final String packagesPreferences=intentFromCityShowcaserActivity.getStringExtra(Constants.DOWNLOADING_BUNDLE_INTENT);
//        Log.d("miotag","intent from showcaserActivity: "+packagesPreferences);
        final String packagesPreferences=Repository.retrieve(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);

        intentToDownloadImages=new Intent(this, DownloadingCitiesService.class);
        intentToDownloadImages.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT,Constants.DOWNLOADING_IMAGES_ONLY);

        intentToUnzipImages=new Intent(this, UnzipService.class);
        intentToUnzipImages.putExtra("zipFilePath", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());

        receiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {

              Log.d("miotag","onReceive di LoadingCityPackages");
              dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
              String action = intent.getAction();
              Log.d("mitoag", "in onReceive, l'action è: "+action);
              if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                  long downloadId = intent.getLongExtra(
                          DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                  DownloadManager.Query query = new DownloadManager.Query();
                  query.setFilterById(downloadId);
                  Cursor c = dm.query(query);
                  if (c.moveToFirst()) {
                      int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                      if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                          switch (packagesPreferences) {

                              case Constants.DOWNLOADING_MAPS_IMAGES :

                                  if (!mapAlreadyDownloaded){
                                      //l'intent arrivato è il primo, le mappe sono presenti quindi devo lanciare l'intent per scaricare le immagini
                                      mapAlreadyDownloaded=true;
                                      startService(intentToDownloadImages);

                                  } else {

                                      switch(actualCity){
                                          case Constants.CITY_MILAN:
                                              Log.d("miotag", "Unzip immagini città di milano");
                                              intentToUnzipImages.putExtra("zipFileName",Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
                                          break;


                                          case Constants.CITY_PALERMO:
                                              intentToUnzipImages.putExtra("zipFileName",Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                                          break;

                                      }
                                         intentToUnzipImages.putExtra("targetDirectory",context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
                                        startService(intentToUnzipImages);

                                  }
                                break;

                              case Constants.DOWNLOADING_MAPS_ONLY:

                                      startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
                                      finish();

                              break;

                              case Constants.DOWNLOADING_IMAGES_ONLY:
                                  Intent intentToUnzipImagesOnly=new Intent(context, UnzipService.class);
                                  intentToUnzipImagesOnly.putExtra("zipFilePath", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
//                                if (Constants.KEY_CURRENT_CITY.equals(Constants.CITY_MILAN)){

                                  switch(actualCity){
                                      case Constants.CITY_MILAN:
                                          Log.d("miotag", "Unzip immagini città di milano");
                                          intentToUnzipImagesOnly.putExtra("zipFileName",Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
                                          break;


                                      case Constants.CITY_PALERMO:
                                          intentToUnzipImagesOnly.putExtra("zipFileName",Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                                          break;

                                  }
                                  intentToUnzipImagesOnly.putExtra("targetDirectory", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
                                  context.startService(intentToUnzipImagesOnly);
                              break;


                          }//fine switch on packages
                      }
                  }
              }//fine ACTION_DOWNLOAD_COMPLETE
               else {
                  //intent contiene l'action UNZIP DONE

                  startActivity(new Intent(getBaseContext(),SettingTourActivity.class));
                  finish();
              }


          }

        };//fine onReceive



    }//fine onCreate

    @Override
    public void onResume(){
        super.onResume();
        intentFilter=new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(Constants.UNZIP_DONE);

        registerReceiver(receiver, intentFilter);




    }

    @Override
    public void onPause(){

        super.onPause();
        unregisterReceiver(receiver);

    }


}//fine classe
