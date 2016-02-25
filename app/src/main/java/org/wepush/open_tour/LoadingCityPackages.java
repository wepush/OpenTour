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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wepush.open_tour.services.DownloadingCitiesService;
import org.wepush.open_tour.services.UnzipService;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

/**
 * Created by antoniocoppola on 19/10/15.
 */
public class LoadingCityPackages extends AppCompatActivity {

    private Intent intentToDownloadImages,intentToUnzipImages;
    private DownloadManager dm;
    private BroadcastReceiver receiver;
    private String actualCity;
    private IntentFilter intentFilter;
    private boolean mapAlreadyDownloaded=false;
    private TextView txtSplashScreen1,txtSplashScreen2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.loading_citybundle);

        txtSplashScreen1=(TextView)findViewById(R.id.txtSplashScreen1);
        txtSplashScreen2=(TextView) findViewById(R.id.txtSplashScreen2);

        ImageView gifLoading=(ImageView)findViewById(R.id.splashGifAnimation);
        AnimationDrawable animDrawable=(AnimationDrawable) gifLoading.getBackground();
        animDrawable.start();


      actualCity= Repository.retrieve(this,Constants.KEY_CURRENT_CITY,String.class);

        final String packagesPreferences=Repository.retrieve(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);

        intentToDownloadImages=new Intent(this, DownloadingCitiesService.class);
        intentToDownloadImages.putExtra(Constants.DOWNLOADING_BUNDLE_INTENT,Constants.DOWNLOADING_IMAGES_ONLY);

        intentToUnzipImages=new Intent(this, UnzipService.class);
        intentToUnzipImages.putExtra("zipFilePath", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());

        receiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {

              dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
              String action = intent.getAction();
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
                                      mapAlreadyDownloaded=true;
                                      startService(intentToDownloadImages);

                                  } else {

                                    txtSplashScreen2.setText(R.string.downloadAndUnzipImages);
                                    txtSplashScreen2.setVisibility(View.VISIBLE);

                                      switch(actualCity){
                                          case Constants.CITY_MILAN:
                                              intentToUnzipImages.putExtra("zipFileName",Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
                                          break;


                                          case Constants.CITY_PALERMO:
                                              intentToUnzipImages.putExtra("zipFileName",Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                                          break;


                                          case Constants.CITY_TURIN:
                                              intentToUnzipImages.putExtra("zipFileName",Constants.ZIPPED_IMAGES_TURIN_DOWNLOAD);

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

                                  txtSplashScreen2.setText(R.string.downloadAndUnzipImages);
                                  txtSplashScreen2.setVisibility(View.VISIBLE);


                                  switch(actualCity){
                                      case Constants.CITY_MILAN:
                                          intentToUnzipImagesOnly.putExtra("zipFileName",Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
                                          break;


                                      case Constants.CITY_PALERMO:
                                          intentToUnzipImagesOnly.putExtra("zipFileName",Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                                          break;


                                      case Constants.CITY_TURIN:
                                          intentToUnzipImagesOnly.putExtra("zipFileName",Constants.ZIPPED_IMAGES_TURIN_DOWNLOAD);
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
