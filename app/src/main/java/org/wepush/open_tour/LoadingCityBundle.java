////package org.wepush.open_tour;
////
////import android.app.DownloadManager;
////import android.content.BroadcastReceiver;
////import android.content.Context;
////import android.content.Intent;
////import android.content.IntentFilter;
////import android.database.Cursor;
////import android.net.Uri;
////import android.os.Bundle;
////import android.os.Environment;
////import android.support.v4.content.LocalBroadcastManager;
////import android.support.v7.app.AppCompatActivity;
////import android.text.TextUtils;
////import android.util.Log;
////
////import org.wepush.open_tour.utils.Constants;
////import org.wepush.open_tour.utils.Repository;
////
////import java.io.BufferedInputStream;
////import java.io.File;
////import java.io.FileInputStream;
////import java.io.FileNotFoundException;
////import java.io.FileOutputStream;
////import java.io.IOException;
////import java.util.zip.ZipEntry;
////import java.util.zip.ZipInputStream;
////
/////**
//// * Created by antoniocoppola on 21/09/15.
//// */
////public class LoadingCityBundle extends AppCompatActivity {
////
////    private DownloadManager dm;
////    private long idDownload;
////    private BroadcastReceiver receiver;
////    private static Context context;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState){
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.loading_citybundle);
////        context=this;
////
////        final String actualCity= Repository.retrieve(this, Constants.KEY_CURRENT_CITY,String.class);
////
////        if (TextUtils.equals(actualCity,Constants.CITY_MILAN)){
////            //TODO 22 aggiungere il download delle mappe offline discriminando con intent Action
//////            launchAppropriateDownload(Constants.ZIPPED_MAP_MILAN,Constants.URL_ZIPPED_MAP_MILAN,"Mappe Milano","Download Mappe offline città di MIlano");
////            launchAppropriateDownload(Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD, Constants.URL_ZIPPED_IMAGES_MILAN, "Milano", "Download delle schede città di Milano");
////
////        } else {
////            launchAppropriateDownload(Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD,Constants.URL_ZIPPED_IMAGES_PALERMO,"Palermo","Download delle scehede città di Palermo");
////        }
////
////
////        receiver = new BroadcastReceiver() {
////            @Override
////            public void onReceive(Context context, Intent intent) {
////                String action = intent.getAction();
////                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
////                    Log.d("miotag","ACTION_DOWNLOAD_COMPLETE");
////                    idDownload = intent.getLongExtra(
////                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
////                    DownloadManager.Query query = new DownloadManager.Query();
////                    query.setFilterById(idDownload);
////                    Cursor c = dm.query(query);
////                    if (c.moveToFirst()) {
////                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
////                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
////
////                            if (TextUtils.equals(actualCity,Constants.CITY_MILAN)){
////                                File zipToUnzipMilanImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
////                                try {
////                                    Log.d("miotag","starting unzip con: "+zipToUnzipMilanImages.getPath()+", target"+getExternalFilesDir(Environment.DIRECTORY_PICTURES));
////
////                                    unzip(zipToUnzipMilanImages, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
////                                } catch(IOException e){
////                                    e.printStackTrace();
////                                }
////                            } else {
////                                File zipToUnzipPalermoImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
////                                try {
////                                    Log.d("miotag","starting unzip con: "+zipToUnzipPalermoImages.getPath()+", target"+getExternalFilesDir(Environment.DIRECTORY_PICTURES));
////
////                                    unzip(zipToUnzipPalermoImages, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
////                                } catch(IOException e){
////                                    e.printStackTrace();
////                                }
////                            }
////                        }
////                    }
////                }
////            }
////        };
////
////        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
////
////
////
////
////
////    }
////
////
////
////    private void launchAppropriateDownload(String nameFileToDownload,String urlFileToDownload,String titleDownloadNotification, String descriptionDownloadNotification)
////    {
////        dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
////        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(urlFileToDownload));
////        req.setTitle(titleDownloadNotification);
////        req.setDescription(descriptionDownloadNotification);
//////        req.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, nameFileToDownload);
////
////        if ( (TextUtils.equals(nameFileToDownload,Constants.ZIPPED_MAP_MILAN)) || (TextUtils.equals(nameFileToDownload,Constants.ZIPPED_MAP_PALERMO))) {
////            File dir = new File(Environment.getExternalStorageDirectory(), "osmdroid");
////            dir.mkdirs();
//////                Log.d("miotag", "download. " + Environment.getExternalStorageDirectory() + "/osmdroid/");
//////                req.setDestinationInExternalFilesDir(this, "MapQuest", SELECTED_CITY_ZIP_IMAGES);
//////                req.setDestinationInExternalFilesDir(this,Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),SELECTED_CITY_ZIP_IMAGES);
////            req.setDestinationInExternalPublicDir("osmdroid", nameFileToDownload);
////        } else {
//////            Log.d("miotag", "salvataggio in DIRECTORY_PICTURES");
////            req.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, nameFileToDownload);
////
////        }
////        idDownload = dm.enqueue(req);
////    }
////
////    @Override
////    public void onPause(){
////        super.onPause();
////        unregisterReceiver(receiver);
////
////    }
////
////    @Override
////    public void onStop(){
////        super.onStop();
////        finish();
////    }
////
////    @Override
////    public void onResume(){
////        super.onResume();
////       if (receiver!=null) {
////           registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
////       }
////    }
////
////
////    public static void unzip(File zipFile, File targetDirectory) throws IOException {
////        ZipInputStream zis = new ZipInputStream(
////                new BufferedInputStream(new FileInputStream(zipFile)));
////        try {
////            Log.d("miotag","zipFile: "+zipFile+", con percorso: "+zipFile.getPath()+", e con destinazione: "+targetDirectory);
////            ZipEntry ze;
////            int count;
////            byte[] buffer = new byte[8192];
////            while ((ze = zis.getNextEntry()) != null) {
////                File file = new File(targetDirectory, ze.getName());
////                File dir = ze.isDirectory() ? file : file.getParentFile();
////                if (!dir.isDirectory() && !dir.mkdirs())
////                    throw new FileNotFoundException("Failed to ensure directory: (scritto da me) " +
////                            dir.getAbsolutePath());
////                if (ze.isDirectory())
////                    continue;
////                FileOutputStream fout = new FileOutputStream(file);
////                try {
////                    while ((count = zis.read(buffer)) != -1)
////                        fout.write(buffer, 0, count);
////                } finally {
////                    fout.close();
////
////                }
////            /* if time should be restored as well
////            long time = ze.getTime();
////            if (time > 0)
////                file.setLastModified(time);
////            */
////            }
////        } finally {
////            zis.close();
////            Log.d("miotag", "UNZIP completato");
////            context.startActivity(new Intent(context, SettingTourActivity.class));
////
////        }
////    }
////
////}
////
//
//package org.wepush.open_tour;
//
//import android.app.DownloadManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import org.wepush.open_tour.services.DownloadingService;
//import org.wepush.open_tour.services.UnzipService;
//import org.wepush.open_tour.utils.Constants;
//import org.wepush.open_tour.utils.Repository;
//
//public class LoadingCityBundle extends AppCompatActivity{
//
//    private BroadcastReceiver receiver;
//    private IntentFilter intentFilter;
//    private DownloadManager dm;
//    private boolean secondDownload=false;
//    private TextView txt1,txt2;
//
////TODO DEPRECATED 19/10
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.loading_citybundle);
//
//        txt1=(TextView)findViewById(R.id.textWaiting1);
//        txt2=(TextView)findViewById(R.id.textWaiting2);
//
//        ProgressBar progressBar=(ProgressBar) findViewById(R.id.progressLoadingPackages);
//        progressBar.getIndeterminateDrawable().setColorFilter(
//                getResources().getColor(R.color.white),
//                android.graphics.PorterDuff.Mode.SRC_IN);
//
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                String action = intent.getAction();
//                String actualCity= Repository.retrieve(context, Constants.KEY_CURRENT_CITY, String.class);
//
//                Log.d("miotag", "azione di intent mappa: " + action + ", per la città di: " + actualCity);
//                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
//                    long downloadId = intent.getLongExtra(
//                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
//                    DownloadManager.Query query = new DownloadManager.Query();
//                    query.setFilterById(downloadId);
//                    Cursor c = dm.query(query);
//                    if (c.moveToFirst()) {
//                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
//                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
//
//                            if(secondDownload){
//                                Log.d("miotag","Unzipping mode: ON");
//                                Intent i=new Intent(context, UnzipService.class);
//                                i.putExtra("zipFilePath",context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
////                                if (Constants.KEY_CURRENT_CITY.equals(Constants.CITY_MILAN)){
//                                if (actualCity.equals(Constants.CITY_MILAN)){
//                                    Log.d("miotag","Unzip immagini città di milano");
//                                    i.putExtra("zipFileName",Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
//                                } else {
//                                    Log.d("miotag","Unzip immagini città di palermo");
//                                    i.putExtra("zipFileName",Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
//                                }
//                                i.putExtra("targetDirectory",context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
//                                context.startService(i);
//                            }
//                            else {//images.zip doesn't exist, so I download them
//                                Intent i=new Intent(context, DownloadingService.class);
//                                i.putExtra("city",Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class));
//                                i.putExtra("whatToDownload","images");
//                                context.startService(i);
//                                secondDownload=true;
//                            }
//                        }
//                    }
//                } else {
//
//                    if (TextUtils.equals(action, Constants.UNZIP_DONE)) {
//                        Log.d("miotag", "UNZIPPATO");
//                        context.startActivity(new Intent(context, SettingTourActivity.class));
//                        finish();
//                    }
//                }
//
//
//            }
//        };
//
//
//    }//fine onCreate
//
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        String chosenCity=Repository.retrieve(this, Constants.KEY_CURRENT_CITY,String.class);
//        String city="";
//
//        if (chosenCity.equals(Constants.CITY_MILAN)){
//            city="Milano";
//        } else {
//            city="Palermo";
//        }
//        txt1.setText(getResources().getString(R.string.waitingForPackages1)+" "+city);
//        txt2.setText(getResources().getString(R.string.waitingForPackages2)+" "+city);
//        intentFilter=new IntentFilter();
//        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        intentFilter.addAction(Constants.UNZIP_DONE);
//
////        registerReceiver(receiver, new IntentFilter(
////                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//
//        registerReceiver(receiver, intentFilter);
//    }
//
//
//    @Override
//    public void onPause(){
//        super.onPause();
//        unregisterReceiver(receiver);
//    }
//}//fine classe
//
