package org.wepush.open_tour.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by antoniocoppola on 29/05/15.
 */
public class ReadFromJson extends IntentService {

    private String stringFromJson;
    private Boolean updatingDb;
    public ReadFromJson(){
        super("ReadFromJson");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        updatingDb=intent.getBooleanExtra("updatingPackage",false);
        String cityToLoad = Repository.retrieve(this, Constants.KEY_CURRENT_CITY, String.class);
        try {


            if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {

                if (TextUtils.equals(Constants.CITY_MILAN, cityToLoad)) {

                    SQLiteDatabase sql=DB1SqlHelper.getInstance(this).getWritableDatabase();
                    sql.execSQL("DROP TABLE IF EXISTS " + DB1SqlHelper.SITES_TABLE);
                    sql.execSQL(DB1SqlHelper.CREATE_TABLE_SITES);

                    stringFromJson = jsonToStringFromAssetFolder("milandb_en", getApplication());
//                    Log.d("miotag","PARSING milan_en");
                } else {

                    SQLiteDatabase sql=DB1SqlHelper.getInstance(this).getWritableDatabase();
                    sql.execSQL("DROP TABLE IF EXISTS " + DB1SqlHelper.SITES_TABLE);
                    sql.execSQL(DB1SqlHelper.CREATE_TABLE_SITES);


                    stringFromJson = jsonToStringFromAssetFolder("palermodb_eng", getApplication());
//                    Log.d("miotag","PARSING palermo_eng");
                }
            }  else if (TextUtils.equals(Constants.CITY_MILAN, cityToLoad)) {

                SQLiteDatabase sql=DB1SqlHelper.getInstance(this).getWritableDatabase();
                sql.execSQL("DROP TABLE IF EXISTS " + DB1SqlHelper.SITES_TABLE);
                sql.execSQL(DB1SqlHelper.CREATE_TABLE_SITES);

                stringFromJson = jsonToStringFromAssetFolder("milandb_ita", getApplication());
//                Log.d("miotag","PARSING milandb_ita");
            } else {

                SQLiteDatabase sql=DB1SqlHelper.getInstance(this).getWritableDatabase();
                sql.execSQL("DROP TABLE IF EXISTS " + DB1SqlHelper.SITES_TABLE);
                sql.execSQL(DB1SqlHelper.CREATE_TABLE_SITES);


                stringFromJson = jsonToStringFromAssetFolder("palermodb_ita", getApplication());
//                Log.d("miotag","PARSING palermodb_ita");

            }


//            if (TextUtils.equals(intent.getStringExtra("languages"), "english")) {
//                Log.d("miotag","intent è inglese");
//                stringFromJson = jsonToStringFromAssetFolder("milandb_en", getApplication());
//
//            } else if (TextUtils.equals(intent.getStringExtra("languages"), "italian")) {
//                stringFromJson = jsonToStringFromAssetFolder("milandb_ita", getApplication());
//                Log.d("miotag","intent è italiano");
//            } else {
//                stringFromJson = jsonToStringFromAssetFolder("milandb_ita", getApplication());
//                Log.d("miotag","intent è predefinito");
//            }


            parseResult(new JSONObject(stringFromJson));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println(e.toString());

        }



    }


    private void parseResult(JSONObject obj){

//        TODO 22Settembre: modifiche per il db
//        DB1SqlHelper.getInstance(this).deleteDb(this);


//        sql.needUpgrade(sql.getVersion()+1);
//        sql.getPath()
//        this.deleteDatabase("db1");

//        SQLiteDatabase.deleteDatabase(new File(myPath));
        try {
            ArrayList<Site> siteToReturn = new ArrayList<Site>();
            JSONArray arrayFromJson = obj.getJSONArray("sites");

            for (int i = 0; i < arrayFromJson.length(); i++) {

                Site site = new Site();

                JSONObject singleSite = arrayFromJson.getJSONObject(i);
                site.id = singleSite.getString("id");
                site.name = singleSite.getString("name");
                site.description = singleSite.getString("description");
                site.tips = singleSite.getString("tips");
                site.priority=convertPriority(singleSite.getInt("priority"));


                JSONObject locationInfo = singleSite.getJSONObject("location");
                site.address = locationInfo.getString("street_name");
                site.addressCivic = locationInfo.getString("street_number");
                JSONArray coordinates = locationInfo.getJSONArray("coordinates");

                site.latitude = coordinates.getDouble(0);
                site.longitude = coordinates.getDouble(1);

//                //adjacency
//                JSONArray adjacentsArray=singleSite.getJSONArray("nearest_sites");
////                for (int j=0; j< adjacentsArray.length(); j++){
////                    JSONObject singleAdjacency=adjacentsArray.getJSONObject(j);
////                    site.allAdjacency.add(singleAdjacency.getString("id"));
////                    Log.d("miotag", "adiacenze acquisita per  " + site.name + " -> "+ singleAdjacency.getString("id") );
////                }
//
//                site.allAdjacency=adjacentsArray.toString();
//                Log.d("miotag","allAdjacency: "+site.allAdjacency);

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
//                Log.d("miotag"," VISIT TIME acquired: "+site.visitTime);
//                Log.d("miotag","PRIORITY acquired: "+site.priority);
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


//            Log.d("miotag","returning site"+ ", "+site.name);
            siteToReturn.add(site);


            }

            if (updatingDb){
                DB1SqlHelper.getInstance(this).addUpdatedSites(siteToReturn);
            } else {

//                Log.d("miotag", "Prima della creazione del DB");
                DB1SqlHelper.getInstance(this).addSites(siteToReturn);
//                Log.d("miotag", "Dopo la creazione del db");
            }

             }  catch (JSONException e){
                 System.out.println(e.toString());
        }
    }

    private int convertPriority(int i){

        switch (i)
        {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;

        }

        return -1;
    }

    public static String jsonToStringFromAssetFolder(String fileName,Context context) throws IOException {
        AssetManager manager = context.getAssets();

        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];

        file.read(data);
        file.close();
        return new String(data);
    }



    private String convertFromAppTypeToDBType(String s){
        String stringToReturn="";

        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")){

            switch (s) {
                case "Theaters":

                    stringToReturn="theaters";

                    break;

                case "Palaces and Castles":

                    stringToReturn="palaces";
                    break;

                case "Villas, Gardens and Parks":

                    stringToReturn="villas";
                    break;

                case "Museums and Art galleries":

                    stringToReturn="museums";
                    break;

                case "Statues and Fountains":

                    stringToReturn="statues";
                    break;

                case "Squares and Streets":

                    stringToReturn="squares";

                    break;

                case "Arches, Gates and Walls":

                    stringToReturn="arches";
                    break;

                case "Fairs and Markets":

                    stringToReturn="fairs";
                    break;

                case "Cemeteries and Memorials":

                    stringToReturn="cemeteries";
                    break;

                case "Buildings":

                    stringToReturn="buildings";
                    break;

                case "Bridges":

                    stringToReturn="bridges";
                    break;

                case "Churches, Oratories and Places of worship":

                    stringToReturn="churches";
                    break;

                case "Other monuments and Places of interest":

                    stringToReturn="other";
                    break;

            }

            return stringToReturn;



        } else {
            switch (s) {

                case "Teatri":

                    stringToReturn="teatri";
                    break;

                case "Palazzi e Castelli":

                    stringToReturn="palazzi";

                    break;

                case "Ville, Giardini e Parchi":

                    stringToReturn="ville";

                    break;

                case "Musei e Gallerie d'arte":

                    stringToReturn="musei";

                    break;

                case "Statue e Fontane":


                    stringToReturn="statue";

                    break;

                case "Piazze e Strade":

                    stringToReturn="piazze";


                    break;

                case "Archi, Porte e Mura":

                    stringToReturn="archi";

                    break;

                case "Fiere e Mercati":

                    stringToReturn="fiere";

                    break;

                case "Cimiteri e Memoriali":

                    stringToReturn="cimiteri";

                    break;


                case "Edifici":

                    stringToReturn="edifici";

                    break;

                case "Ponti":

                    stringToReturn="ponti";

                    break;

                case "Chiese, Oratori e Luoghi di culto":

                    stringToReturn="chiese";

                    break;

                case "Altri monumenti e Luoghi di interesse":

                    stringToReturn="altri";
                    break;

            }
        }


        return stringToReturn;

    }

}
