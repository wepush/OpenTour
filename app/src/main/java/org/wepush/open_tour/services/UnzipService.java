package org.wepush.open_tour.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.wepush.open_tour.utils.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by antoniocoppola on 23/09/15.
 */
public class UnzipService extends IntentService {

    private File zipFile, targetDirectory;
    private ZipInputStream zis;
    public UnzipService(){
        super("UnzipService");
    }

    @Override
    public void onHandleIntent(Intent intent){

        zipFile=new File(intent.getStringExtra("zipFilePath"),intent.getStringExtra("zipFileName"));
        targetDirectory=new File(intent.getStringExtra("targetDirectory"));

        try {
            zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));

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

                    }
                } catch (IOException e){e.printStackTrace();}

            try {
                zis.close();
            } catch (IOException e){
                e.printStackTrace();
            }
                    Intent i=new Intent();
                    i.setAction(Constants.UNZIP_DONE);

                    sendBroadcast(i);



    }//fine onHandleIntent


}//fine classe


