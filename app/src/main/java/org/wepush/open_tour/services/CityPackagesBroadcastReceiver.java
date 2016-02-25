package org.wepush.open_tour.services;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.wepush.open_tour.SettingTourActivity;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.io.File;

/**
 * Created by antoniocoppola on 23/09/15.
 */
public class CityPackagesBroadcastReceiver extends BroadcastReceiver {

    private DownloadManager dm;
//    private File imagesAlreadyDownloaded;
    private boolean secondDownload=false;

    @Override
    public void onReceive(Context context, Intent intent){

        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        String action = intent.getAction();
        String actualCity=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor c = dm.query(query);
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                        if(secondDownload){
                        Intent i=new Intent(context, UnzipService.class);
                        i.putExtra("zipFilePath",context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
//                                if (Constants.KEY_CURRENT_CITY.equals(Constants.CITY_MILAN)){
                            if (actualCity.equals(Constants.CITY_MILAN)){
                                    i.putExtra("zipFileName",Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
                                } else {
                                    i.putExtra("zipFileName",Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                                }
                        i.putExtra("targetDirectory",context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
                        context.startService(i);
                    }
                    else {//images.zip doesn't exist, so I download them
//                        Intent i=new Intent(context, DownloadingService.class);
                            Intent i=new Intent(context, DownloadingCitiesService.class);
                        i.putExtra("city",Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class));
                        i.putExtra("whatToDownload","images");
                        context.startService(i);
                            secondDownload=true;
                    }
                }
            }
        } else {

            if (TextUtils.equals(action, Constants.UNZIP_DONE)) {
                context.startActivity(new Intent(context, SettingTourActivity.class));
            }
        }


        }
    }




