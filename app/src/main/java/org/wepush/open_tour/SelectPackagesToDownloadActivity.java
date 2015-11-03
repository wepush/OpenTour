package org.wepush.open_tour;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by antoniocoppola on 18/09/15.
 */
public class SelectPackagesToDownloadActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String URL_ZIPPED_IMAGES_PALERMO="http://wultima.altervista.org/palermo_images.zip";
    private final static String URL_ZIPPED_MAP_PALERMO="http://wultima.altervista.org/palermo_map.zip";
    private final static String URL_ZIPPED_MAP_MILAN="http://wultima.altervista.org/milano_map.zip";
    private final static String URL_ZIPPED_IMAGES_MILAN="http://wultima.altervista.org/milano_images.zip";


    private final static String ZIPPED_IMAGES_MILANO_DOWNLOAD="milano_images.zip";
    private final static String ZIPPED_IMAGES_PALERMO_DOWNLOAD="palermo_images.zip";
    private final static String ZIPPED_MAP_PALERMO="palermo_map.zip";
    private final static String ZIPPED_MAP_MILAN="milano_map.zip";



    private DownloadManager dm;
    private BroadcastReceiver receiver;
    private long idDownload;
    private Button btnDownloadMapMilan,btnDownloadMapPalermo,btnDownloadImagesMilan,btnDownloadImagesPalermo, btnUnzipMilan, btnUnzipPalermo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_packages);

        btnDownloadImagesMilan=(Button)findViewById(R.id.btnImageMilano);
        btnDownloadImagesPalermo=(Button)findViewById(R.id.btnImagePalermo);
        btnDownloadMapMilan=(Button)findViewById(R.id.btnMapMilano);
        btnDownloadMapPalermo=(Button)findViewById(R.id.btnMapPalermo);

        btnUnzipMilan=(Button) findViewById(R.id.btnUnzipMilan);
        btnUnzipPalermo=(Button) findViewById(R.id.btnUnzipPalermo);

        btnDownloadImagesMilan.setOnClickListener(this);
        btnDownloadImagesPalermo.setOnClickListener(this);
        btnDownloadMapPalermo.setOnClickListener(this);
        btnDownloadMapMilan.setOnClickListener(this);

        btnUnzipMilan.setOnClickListener(this);
        btnUnzipPalermo.setOnClickListener(this);




        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.d("miotag","ACTION_DOWNLOAD_COMPLETE");
                    idDownload = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(idDownload);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            Log.d("miotag", "Download successfull!");
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));










    }

    @Override
    public void onClick(View v){

        switch(v.getId()){

            case R.id.btnImageMilano :
                launchAppropriateDownload(ZIPPED_IMAGES_MILANO_DOWNLOAD, URL_ZIPPED_IMAGES_MILAN, "Milano", "Download delle schede città di Milano");

//                File zipToUnzip=new File(Environment.getExternalStorageDirectory() + "/osmdroid/",SELECTED_CITY_ZIP_IMAGES);)
                break;


            case R.id.btnMapMilano :
                 dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req = new DownloadManager.Request(Uri.parse(URL_ZIPPED_MAP_MILAN));
                req.setTitle("Download mappa Milano");
                req.setDescription("Download ed installazioni mappe offline Milano");
                File dir = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dir.mkdirs();
                req.setDestinationInExternalPublicDir("osmdroid",ZIPPED_MAP_MILAN);
                idDownload = dm.enqueue(req);

                break;


            case R.id.btnImagePalermo:
                launchAppropriateDownload(ZIPPED_IMAGES_PALERMO_DOWNLOAD, URL_ZIPPED_IMAGES_PALERMO, "Palermo", "Download delle schede città di Palermo");
                Toast.makeText(this,"Download completato, installazione", Toast.LENGTH_LONG).show();
                File zipToUnzipPalermoImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),ZIPPED_IMAGES_PALERMO_DOWNLOAD);
                try {
                    unzip(zipToUnzipPalermoImages, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                } catch(IOException e){
                    e.printStackTrace();
                }

                break;

            case R.id.btnMapPalermo:
                DownloadManager dm2 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req2 = new DownloadManager.Request(Uri.parse(URL_ZIPPED_MAP_PALERMO));
                req2.setTitle("Download mappa Milano");
                req2.setDescription("Download ed installazioni mappe offline Milano");
                File dir2 = new File(Environment.getExternalStorageDirectory(), "osmdroid");
                dir2.mkdirs();
                req2.setDestinationInExternalPublicDir("osmdroid",ZIPPED_MAP_PALERMO);
                idDownload = dm2.enqueue(req2);

                break;

            case R.id.btnDeleteData:
                if ( deleteDirectory(Environment.getExternalStoragePublicDirectory("osmdroid"))) {
                    Toast.makeText(this, "Mappe cancellate correttamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Errore cancellazione dati: Eseguire a mano l'operazione",Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.btnUnzipMilan:
                File zipToUnzipMilanImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),ZIPPED_IMAGES_MILANO_DOWNLOAD);
                try {
                    Log.d("miotag","starting unzip con: "+zipToUnzipMilanImages.getPath()+", target"+getExternalFilesDir(Environment.DIRECTORY_PICTURES));

                    unzip(zipToUnzipMilanImages, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                } catch(IOException e){
                    e.printStackTrace();
                }
                 break;



            case R.id.btnUnzipPalermo:
                Toast.makeText(this,"Download completato, installazione", Toast.LENGTH_LONG).show();
                File zipToUnzipPalermoImages2=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),ZIPPED_IMAGES_MILANO_DOWNLOAD);
                try {
                    Log.d("miotag","starting unzip con: "+zipToUnzipPalermoImages2.getPath()+", target"+getExternalFilesDir(Environment.DIRECTORY_PICTURES));

                    unzip(zipToUnzipPalermoImages2, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                } catch(IOException e){
                    e.printStackTrace();
                }






        }//switch off


    }//onCreate off


    private void launchAppropriateDownload(String nameFileToDownload,String urlFileToDownload,String titleDownloadNotification, String descriptionDownloadNotification)
    {
        dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(urlFileToDownload));
        req.setTitle(titleDownloadNotification);
        req.setDescription(descriptionDownloadNotification);
//        req.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, nameFileToDownload);

        if ( (TextUtils.equals(nameFileToDownload,ZIPPED_MAP_MILAN)) || (TextUtils.equals(nameFileToDownload,ZIPPED_MAP_PALERMO))) {
            File dir = new File(Environment.getExternalStorageDirectory(), "osmdroid");
            dir.mkdirs();
//                Log.d("miotag", "download. " + Environment.getExternalStorageDirectory() + "/osmdroid/");
//                req.setDestinationInExternalFilesDir(this, "MapQuest", SELECTED_CITY_ZIP_IMAGES);
//                req.setDestinationInExternalFilesDir(this,Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),SELECTED_CITY_ZIP_IMAGES);
            req.setDestinationInExternalPublicDir("osmdroid", nameFileToDownload);
        } else {
            Log.d("miotag", "salvataggio in DIRECTORY_PICTURES");
            req.setDestinationInExternalFilesDir(this,Environment.DIRECTORY_PICTURES,nameFileToDownload);

        }
        idDownload = dm.enqueue(req);
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            Log.d("miotag","zipFile: "+zipFile+", con percorso: "+zipFile.getPath()+", e con destinazione: "+targetDirectory);
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: (scritto da me) " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }


    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (receiver == null){
            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }





}//fine classe
