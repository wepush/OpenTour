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
import org.wepush.open_tour.utils.Repository;

import java.io.File;

/**
 * Created by antoniocoppola on 19/10/15.
 */
public class DownloadingCitiesService extends IntentService{


    public DownloadingCitiesService() {
        super("DownloadingCitiesService");
    }

    @Override
    public void onHandleIntent(Intent intent){

        String typeOfPackageToDownload=intent.getStringExtra(Constants.DOWNLOADING_BUNDLE_INTENT);
        String cityChosen= Repository.retrieve(this,Constants.KEY_CURRENT_CITY,String.class);
        Log.d("miotag","intent action da CityShowcaserActivity: "+typeOfPackageToDownload);

        switch(typeOfPackageToDownload){


            case Constants.DOWNLOADING_MAPS_IMAGES:
                switch(cityChosen){

                    case Constants.CITY_MILAN:
                        Log.d("miotag","downloading service deve SCARICARE MAPPE_IMAGES"+", per la città di MILANO");
                        downloadMapsAndImages(Constants.CITY_MILAN);
                        break;

                    case Constants.CITY_PALERMO:
                        Log.d("miotag","downloading service deve SCARICARE MAPPE_IMAGES"+", per la città di Palermo");
                        downloadMapsAndImages(Constants.CITY_PALERMO);
                        break;


                    case Constants.CITY_TURIN:
                        downloadMapsAndImages(Constants.CITY_TURIN);
                        break;

                }
                break;

            case Constants.DOWNLOADING_IMAGES_ONLY:

                switch(cityChosen){

                    case Constants.CITY_MILAN:
                        Log.d("miotag","downloading service deve SCARICARE SOLO IMMAGINI"+", per la città di MILANO");
                        downloadImagesOnly(Constants.CITY_MILAN);
                        break;

                    case Constants.CITY_PALERMO:
                        downloadImagesOnly(Constants.CITY_PALERMO);
                        Log.d("miotag","downloading service deve SCARICARE SOLO IMMAGINI"+", per la città di Palermo");
                        break;

                    case Constants.CITY_TURIN:
                        downloadImagesOnly(Constants.CITY_TURIN);
                        break;

                }
                break;

            case Constants.DOWNLOADING_MAPS_ONLY:

                switch(cityChosen){

                    case Constants.CITY_MILAN:
                        Log.d("miotag","downloading service deve SCARICARE SOLO MAPPE"+", per la città di MILANO");
                        downloadMapsOnly(Constants.CITY_MILAN);
                        break;

                    case Constants.CITY_PALERMO:
                        downloadMapsOnly(Constants.CITY_PALERMO);
                        Log.d("miotag","downloading service deve SCARICARE SOLO MAPPE"+", per la città di Palermo");
                        break;


                    case Constants.CITY_TURIN:
                        downloadMapsOnly(Constants.CITY_TURIN);
                        break;

                }
                break;





        }//fine switch




    }//fine onHandleIntent

    private void downloadMapsAndImages(String city){

        switch (city) {

            case Constants.CITY_MILAN:

                //download mappe
                DownloadManager dmMilan = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqMilanMap = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_MILAN));
                reqMilanMap.setTitle(getResources().getString(R.string.downloadingMapTitleMilan));
                reqMilanMap.setDescription(getResources().getString(R.string.downloadingMapDescriptionMilan));
                File dirMilanMap = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dirMilanMap.mkdirs();
                reqMilanMap.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_MILAN);
                dmMilan.enqueue(reqMilanMap);


//                DownloadManager.Request reqMilanImages = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_MILAN));
//                reqMilanImages.setTitle(getResources().getString(R.string.downloadingImagesTitleMilan));
//                reqMilanImages.setDescription(getResources().getString(R.string.downloadingImagesDescriptionMilan));
//                reqMilanImages.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
//                dmMilan.enqueue(reqMilanImages);


            break;

            case Constants.CITY_PALERMO:

                Log.d("miotag","scaricando città di Palermo");

                DownloadManager dmPalermo = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqPalermoMap = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_PALERMO));
                reqPalermoMap.setTitle(getResources().getString(R.string.downloadingMapTitlePalermo));
                reqPalermoMap.setDescription(getResources().getString(R.string.downloadingMapDescriptionPalermo));
                File dirPalermoMap = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dirPalermoMap.mkdirs();
                reqPalermoMap.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_PALERMO);
                dmPalermo.enqueue(reqPalermoMap);

                //download images
                DownloadManager.Request reqPalermoImages = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_PALERMO));
                reqPalermoImages.setTitle(getResources().getString(R.string.downloadingImagesTitlePalermo));
                reqPalermoImages.setDescription(getResources().getString(R.string.downloadingImagesDescriptionPalermo));
                reqPalermoImages.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                dmPalermo.enqueue(reqPalermoImages);
            break;

            case Constants.CITY_TURIN:

                Log.d("miotag","download città di torino");

                DownloadManager dmTurin = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqTurinMap = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_TURIN));
                reqTurinMap.setTitle(getResources().getString(R.string.downloadingMapTitleTurin));
                reqTurinMap.setDescription(getResources().getString(R.string.downloadingMapDescriptionTurin));
                File dirTorinoMap = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dirTorinoMap.mkdirs();
                reqTurinMap.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_TURIN);
                dmTurin.enqueue(reqTurinMap);

                //download images
                DownloadManager.Request reqTurinImages = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_TURIN));
                reqTurinImages.setTitle(getResources().getString(R.string.downloadingImagesTitleTurin));
                reqTurinImages.setDescription(getResources().getString(R.string.downloadingImagesDescriptionTurin));
                reqTurinImages.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_TURIN_DOWNLOAD);
                dmTurin.enqueue(reqTurinImages);
                break;



        }


    }//fine downloadMapAndImages


    private void downloadMapsOnly(String s) {

        switch (s) {

            case Constants.CITY_MILAN:
                DownloadManager dmMilanMap = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqMilanMap = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_MILAN));
                reqMilanMap.setTitle(getResources().getString(R.string.downloadingMapTitleMilan));
                reqMilanMap.setDescription(getResources().getString(R.string.downloadingMapDescriptionMilan));
                File dirMilanMap = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dirMilanMap.mkdirs();
                reqMilanMap.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_MILAN);
                dmMilanMap.enqueue(reqMilanMap);
                break;

            case Constants.CITY_PALERMO:
                DownloadManager dmPalermoMap = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqPalermoMap = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_PALERMO));
                reqPalermoMap.setTitle(getResources().getString(R.string.downloadingMapTitlePalermo));
                reqPalermoMap.setDescription(getResources().getString(R.string.downloadingMapDescriptionPalermo));
                File dirPalermoMap = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dirPalermoMap.mkdirs();
                reqPalermoMap.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_PALERMO);
                dmPalermoMap.enqueue(reqPalermoMap);
                break;


            case Constants.CITY_TURIN:
                DownloadManager dmTurinMap = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqTurinMap = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_MAP_TURIN));
                reqTurinMap.setTitle(getResources().getString(R.string.downloadingMapTitleTurin));
                reqTurinMap.setDescription(getResources().getString(R.string.downloadingMapDescriptionTurin));
                File dirTurinMap = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dirTurinMap.mkdirs();
                reqTurinMap.setDestinationInExternalPublicDir("osmdroid", Constants.ZIPPED_MAP_TURIN);
                dmTurinMap.enqueue(reqTurinMap);
                break;

        }
    } //fine downloadMapOnly


    private void downloadImagesOnly(String s){

        switch (s){

            case Constants.CITY_MILAN:

                DownloadManager dmMilanImages = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqMilanImages = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_MILAN));
                reqMilanImages.setTitle(getResources().getString(R.string.downloadingImagesTitleMilan));
                reqMilanImages.setDescription(getResources().getString(R.string.downloadingImagesDescriptionMilan));
                reqMilanImages.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);

                dmMilanImages.enqueue(reqMilanImages);
            break;


            case Constants.CITY_PALERMO:

                DownloadManager dmPalermoImages = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqPalermoImages = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_PALERMO));
                reqPalermoImages.setTitle(getResources().getString(R.string.downloadingImagesTitlePalermo));
                reqPalermoImages.setDescription(getResources().getString(R.string.downloadingImagesDescriptionPalermo));
                reqPalermoImages.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                dmPalermoImages.enqueue(reqPalermoImages);

            break;


            case Constants.CITY_TURIN:

                DownloadManager dmTurinImages = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request reqTurinImages = new DownloadManager.Request(Uri.parse(Constants.URL_ZIPPED_IMAGES_TURIN));
                reqTurinImages.setTitle(getResources().getString(R.string.downloadingImagesTitleTurin));
                reqTurinImages.setDescription(getResources().getString(R.string.downloadingImagesDescriptionPalermo));
                reqTurinImages.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, Constants.ZIPPED_IMAGES_TURIN_DOWNLOAD);
                dmTurinImages.enqueue(reqTurinImages);

                break;

        }

    }










}
