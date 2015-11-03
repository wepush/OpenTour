package org.wepush.open_tour.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.wepush.open_tour.R;
import org.wepush.open_tour.utils.Constants;

import java.io.File;

/**
 * Created by antoniocoppola on 23/09/15.
 */
public class DownloadingService extends IntentService {



//TODO DEPRECATED 19/10


//    private long idDownload;

    public DownloadingService() {
        super("DownloadingService");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        Log.d("miotag", "Downloading intent");
        if (intent.getStringExtra("city").equals(Constants.CITY_MILAN)) {
            Log.d("miotag","città di milano");
            if (intent.getStringExtra("whatToDownload").equals("map")) {
                Log.d("miotag","download mappa città di milano");
                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_MILAN));
                req.setTitle(getResources().getString(R.string.downloadingMapTitleMilan));
                req.setDescription(getResources().getString(R.string.downloadingMapDescriptionMilan));
                File dir = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dir.mkdirs();
                req.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_MILAN);
                 dm.enqueue(req);
            } else {
                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_MILAN));
                req.setTitle(getResources().getString(R.string.downloadingImagesTitleMilan));
                req.setDescription(getResources().getString(R.string.downloadingImagesDescriptionMilan));
                req.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
                dm.enqueue(req);

            }
        } else {
//PALERMO
            if (intent.getStringExtra("whatToDownload").equals("map")) {
                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_PALERMO));
                req.setTitle(getResources().getString(R.string.downloadingMapTitlePalermo));
                req.setDescription(getResources().getString(R.string.downloadingMapDescriptionPalermo));
                File dir = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dir.mkdirs();
                req.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_PALERMO);
                 dm.enqueue(req);
            } else {
                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_PALERMO));
                req.setTitle(getResources().getString(R.string.downloadingImagesTitlePalermo));
                req.setDescription(getResources().getString(R.string.downloadingImagesDescriptionPalermo));
                req.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                dm.enqueue(req);
            }

        }
    }
}

