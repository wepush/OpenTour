package org.wepush.open_tour.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import org.wepush.open_tour.DetailsActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.util.Locale;

/**
 * Created by antoniocoppola on 16/11/15.
 */
public class SiteNotificationService extends IntentService{

    private String titleNotification,addressNotification,categoryNotification, imagePath,idNotification;
    private ImageView imageToSet;
    private Bitmap bitmap;
    private String placeholderName;
    private boolean placeholderEverywhere=false;


    public SiteNotificationService(){
        super("UnzipService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle=intent.getExtras();
        titleNotification= bundle.getString("titleNotification");
        addressNotification=bundle.getString("addressNotification");
        categoryNotification=bundle.getString("categoryNotification");
        imagePath=bundle.getString("pictureUrlNotification");
        idNotification=bundle.getString("idNotification");

        String packagesChosen=Repository.retrieve(this, Constants.WHAT_I_WANT_TO_DOWNLOAD, String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }


//        imagePath="palermo_images/teatro_politeama_garibaldi.jpg";

//        bitmap = BitmapFactory.decodeFile(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);

//        String currentCity= Repository.retrieve(this, Constants.KEY_CURRENT_CITY,String.class);

        String currentCity= Repository.retrieve(this, Constants.KEY_CURRENT_CITY, String.class);



        if (currentCity.equals(Constants.CITY_MILAN)) {

            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(categoryNotification);
                } else {
                    chooseThemeColors(categoryNotification);
                }
                imagePath=placeholderName;
                int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
//                placeHolder.setImageResource(drawableResource);
                bitmap=BitmapFactory.decodeResource(getResources(),drawableResource);



            } else {
                imagePath = imagePath.substring(79, imagePath.length() - 4);
                imagePath = "milano_images/" + imagePath + ".jpg";
                bitmap = BitmapFactory.decodeFile(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);


            }
        } else {
            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(categoryNotification);
                } else {
                    chooseThemeColors(categoryNotification);
                }

                imagePath=placeholderName;
//                bitmap = BitmapFactory.decodeFile(imagePath);
                int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
                bitmap=BitmapFactory.decodeResource(getResources(),drawableResource);


            } else {
                imagePath =imagePath.substring(79, imagePath.length() - 4);
                imagePath="palermo_images/"+imagePath+".jpg";
                bitmap = BitmapFactory.decodeFile(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);

            }

        }



        Intent intentForTap = new Intent(Intent.ACTION_MAIN);
        intentForTap.setClass(this, DetailsActivity.class);
        intentForTap.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentForTap.putExtra("siteId",idNotification);


        PendingIntent sender = PendingIntent.getActivity(this,1,intentForTap,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);



        long when = System.currentTimeMillis();
        NotificationCompat.Builder builder =  new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setContentTitle(titleNotification)
                .setContentText(addressNotification)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(sender)
                .setWhen(when);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());


    }


    private void chooseThemeColors(String s){
        switch (s) {
            case "Teatri":

                placeholderName=this.getResources().getString(R.string.placeholderTheaters);
                break;

            case "Palazzi e Castelli":

                placeholderName=this.getResources().getString(R.string.placeholderCastles);

                break;

            case "Ville, Giardini e Parchi":

                placeholderName=this.getResources().getString(R.string.placeholderVillas);

                break;

            case "Musei e Gallerie d'arte":

                placeholderName=this.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statue e Fontane":


                placeholderName=this.getResources().getString(R.string.placeholderStatues);

                break;

            case "Piazze e Strade":

                placeholderName=this.getString(R.string.placeholderSquares);


                break;

            case "Archi, Porte e Mura":

                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fiere e Mercati":

                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cimiteri e Memoriali":

                placeholderName=this.getResources().getString(R.string.placeholderCemeteries);

                break;


            case "Edifici":

                placeholderName=this.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Ponti":

                placeholderName=this.getResources().getString(R.string.placeholderBridges);

                break;

            case "Chiese, Oratori e Luoghi di culto":

                placeholderName=this.getResources().getString(R.string.placeholderChurches);

                break;

            case "Altri monumenti e Luoghi di interesse":

                placeholderName=this.getResources().getString(R.string.placeholderOtherSites);
                break;

        }

    }


//    private void convertLanguageTypeOfSite(Site site) {

    private void convertLanguageTypeOfSite(String s){

        switch (s) {
            case "Theaters":

                placeholderName=this.getResources().getString(R.string.placeholderTheaters);

                break;

            case "Palaces and Castles":

                placeholderName=this.getResources().getString(R.string.placeholderCastles);

                break;

            case "Villas, Gardens and Parks":

                placeholderName=this.getResources().getString(R.string.placeholderVillas);

                break;

            case "Museums and Art galleries":

                placeholderName=this.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statues and Fountains":

                placeholderName=this.getResources().getString(R.string.placeholderStatues);

                break;

            case "Squares and Streets":

                placeholderName=this.getResources().getString(R.string.placeholderSquares);


                break;

            case "Arches, Gates and Walls":

                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fairs and Markets":

                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cemeteries and Memorials":

                placeholderName=this.getResources().getString(R.string.placeholderCemeteries);
                break;

            case "Buildings":

                placeholderName=this.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Bridges":

                placeholderName=this.getResources().getString(R.string.placeholderBridges);

                break;

            case "Churches, Oratories and Places of worship":

                placeholderName=this.getResources().getString(R.string.placeholderChurches);

                break;

            case "Other monuments and Places of interest":

                placeholderName=this.getResources().getString(R.string.placeholderOtherSites);

                break;

        }

    }
}
