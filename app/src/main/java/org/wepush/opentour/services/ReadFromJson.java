package org.wepush.opentour.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wepush.opentour.structures.DB1SqlHelper;
import org.wepush.opentour.structures.Site;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by antoniocoppola on 29/05/15.
 */
public class ReadFromJson extends IntentService {

    private String stringFromJson;
    public ReadFromJson(){
        super("ReadFromJson");
    }

    @Override
    protected void onHandleIntent(Intent intent){


        try {



//            if(TextUtils.equals(Repository.retrieve(this,Constants.KEY_CURRENT_CITY,String.class),"milano")) {
//                Log.d("miotag","Loading Milan");
//                stringFromJson = jsonToStringFromAssetFolder("milandb", getApplication());
//            } else {
//                Log.d("miotag","Loading Palermo");
//                stringFromJson=jsonToStringFromAssetFolder("palermodb",getApplication());
//            }

//10 Luglio: since Milan is the only city available, if/else is useless
//but! choose right json based on system language
            Log.d("miotag","lingua di sistema: "+Locale.getDefault().getDisplayLanguage());

            if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")){
                //TODO associare il giusto json in lingua inglese
                Log.d("miotag","INGLESE selezionato");
                stringFromJson = jsonToStringFromAssetFolder("milandb", getApplication());
            } else {

                stringFromJson = jsonToStringFromAssetFolder("milandb", getApplication());
            }

            parseResult(new JSONObject(stringFromJson));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println(e.toString());

        }



    }


    private void parseResult(JSONObject obj){

        DB1SqlHelper.getInstance(this).deleteDb(this);

        try {
            Log.d("miotag"," Parse Result");
            ArrayList<Site> siteToReturn = new ArrayList<Site>();
            JSONArray arrayFromJson = obj.getJSONArray("sites");

            for (int i = 0; i < arrayFromJson.length(); i++) {

                Site site = new Site();

                JSONObject singleSite = arrayFromJson.getJSONObject(i);
                site.id = singleSite.getString("id");
                site.name = singleSite.getString("name");
                site.description = singleSite.getString("description");
                site.tips = singleSite.getString("tips");
//                site.alwaysOpen = singleSite.getInt("alwaysOpen");

//                site.priority = singleSite.getInt("priority");

                JSONObject locationInfo = singleSite.getJSONObject("location");
                site.address = locationInfo.getString("street_name");
                site.addressCivic = locationInfo.getString("street_number");
                JSONArray coordinates = locationInfo.getJSONArray("coordinates");
                // 0 is array position for latitude
                // 1 is array position for Longitude
                site.latitude = coordinates.getDouble(0);
                site.longitude = coordinates.getDouble(1);

                //PICTURES
                JSONArray jsonArrayPicture=singleSite.getJSONArray("pictures");
                JSONObject pictureJson=new JSONObject();
                try {
                    pictureJson = jsonArrayPicture.getJSONObject(0);
                } catch (JSONException e){
                    pictureJson=null;
                }
                if (pictureJson!=null){
                    site.pictureUrl=pictureJson.getString("picture");
                }else {
                    site.pictureUrl="placeholder";
                }




                //sezione tickets
                JSONArray jsonTickets=singleSite.getJSONArray("tickets");
                //sezione contacts
                JSONArray jsonContacts=singleSite.getJSONArray("contacts");
                //sezione openings
                JSONArray jsonOpenings=singleSite.getJSONArray("openings");
                site.openings=jsonOpenings.toString();
                site.tickets=jsonTickets.toString();
                site.contacts=jsonContacts.toString();
                site.visitTime=singleSite.getInt("visit_time");
                Log.d("miotag"," VISIT TIME acquired: "+site.visitTime);
//sezione TYPE OF SITE
                site.typeOfSite=singleSite.getString("type");

                try {
                       if (TextUtils.equals(singleSite.getString("always_open"),"true")) {
                        site.alwaysOpen = 1;
                        } else {
                            site.alwaysOpen=0;
                            }
                    } catch(JSONException e){
                        e.printStackTrace();
                        }

//sezione nearest sites





            siteToReturn.add(site);


            }



            DB1SqlHelper.getInstance(this).addSites(siteToReturn);

             }  catch (JSONException e){
                 System.out.println(e.toString());
        }
    }

    public static String jsonToStringFromAssetFolder(String fileName,Context context) throws IOException {
        AssetManager manager = context.getAssets();

        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];

        file.read(data);
        file.close();
        return new String(data);
    }
}
