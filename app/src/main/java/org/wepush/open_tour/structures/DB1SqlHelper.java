package org.wepush.open_tour.structures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import org.wepush.open_tour.R;
import org.wepush.open_tour.utils.Constants;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by antoniocoppola on 29/05/15.
 */
public class DB1SqlHelper extends SQLiteOpenHelper {

//    private final static String DATABASE_NAME="db1";
//    private final static int DATABASE_VERSION=1;

    private final static int COLUMN_ID=0;
    private final static int COLUMN_NAME=1;
    private final static int COLUMN_LATITUDE=2;
    private final static int COLUMN_LONGITUDE=3;
    private final static int COLUMN_DESCRIPTION=4;
    private final static int COLUMN_TIPS=5;
    private final static int COLUMN_ADDRESS=6;
    private final static int COLUMN_ADDRESS_CIVIC=7;
    private final static int COLUMN_ALWAYSOPEN=8;
    private final static int COLUMN_PRIORITY=9;
    private final static int COLUMN_PICTURE=10;
    private final static int COLUMN_OPENINGS=11;
    private final static int COLUMN_TICKETS=12;
    private final static int COLUMN_CONTACTS=13;
    private final static int COLUMN_VISIT_TIME=14;
    private final static int COLUMN_TYPE_OF_SITE=15;
    private final static int COLUMN_ALL_ADJACENCY=16;




    private final static String KEY_ID="id";
    private final static String KEY_NAME="name";
    private final static String KEY_LATITUDE="latitude";
    private final static String KEY_LONGITUDE="longitude";
    private final static String KEY_DESCRIPTION ="description";
    private final static String KEY_TIPS="tips";
    private final static String KEY_ADDRESS="address";
    private final static String KEY_ADDRESSCIVIC="address_civic";
    private final static String KEY_ALWAYSOPEN="always_open";
    private final static String KEY_PRIORITY="priority";
    private final static String KEY_PICTURE="picture";
    private final static String KEY_OPENINGS="openings";
    private final static String KEY_TICKETS="tickets";
    private final static String KEY_CONTACTS="contacts";
    private final static String KEY_VISIT_TIME="visit_time";
    private final static String KEY_TYPE_OF_SITE="type";
    private final static String KEY_ALL_ADJACENCY="all_adjacency";
    //always_open field is INTEGER as SQLite standard requires

    public final static String SITES_TABLE="sites_table";
    public final static String CREATE_TABLE_SITES= "CREATE TABLE IF NOT EXISTS " + SITES_TABLE
            + " ("
            + " id VARCHAR(30) PRIMARY KEY, "
//            + " id VARCHAR(30)"
            + " name VARCHAR(255), "
            + " latitude REAL,"
            + " longitude REAL,"
            + " description TEXT,"
            + " tips TEXT,"
            + " address TEXT,"
            + " address_civic TEXT,"
            + " always_open INTEGER,"
            + " priority INTEGER,"
            + " picture TEXT,"
            + " openings TEXT,"
            + " tickets TEXT,"
            + " contacts TEXT,"
            + " visit_time TEXT,"
            + " type TEXT,"
            + " all_adjacency TEXT"
            + ");";





    public DB1SqlHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_TABLE_SITES);
//        Log.d("miotag", "onCreate da Database");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//        Log.d("miotag","onUpgrade da Database");
        database.execSQL("DROP TABLE IF EXISTS " + SITES_TABLE);
        onCreate(database);

    }
//TODO 22settembre
    public void deleteDb(Context ctx){
        ctx.deleteDatabase(Constants.DATABASE_NAME);


//        SQLiteDatabase sqlDb=this.getWritableDatabase();
//        onUpgrade(sqlDb,1,2);

    }



    public void addSites(ArrayList<Site> sitesToAdd){


        SQLiteDatabase sqlDb=this.getWritableDatabase();
        ContentValues value=new ContentValues();

        for (Site site: sitesToAdd){
            value.put(KEY_ID,site.id);
            value.put(KEY_NAME,site.name);
            value.put(KEY_DESCRIPTION,site.description);
            value.put(KEY_TIPS,site.tips);
            value.put(KEY_ADDRESS,site.address);
            value.put(KEY_ADDRESSCIVIC,site.addressCivic);
            value.put(KEY_LATITUDE,site.latitude);
            value.put(KEY_LONGITUDE,site.longitude);
            value.put(KEY_ALWAYSOPEN,site.alwaysOpen);
            value.put(KEY_PRIORITY,site.priority);
            value.put(KEY_PICTURE,site.pictureUrl);
            value.put(KEY_OPENINGS,site.openings);
            value.put(KEY_TICKETS, site.tickets);
            value.put(KEY_CONTACTS,site.contacts);
            value.put(KEY_VISIT_TIME,site.visitTime);
            value.put(KEY_TYPE_OF_SITE,site.typeOfSite);
            value.put(KEY_ALL_ADJACENCY,site.allAdjacency);

            sqlDb.insert(SITES_TABLE,null,value);

        }
        sqlDb.close();

     }

    public void addUpdatedSites(ArrayList<Site> sitesToAdd){

        SQLiteDatabase sqlDb=this.getWritableDatabase();
        ContentValues value=new ContentValues();

        for (Site site: sitesToAdd){
//            value.put(KEY_ID,site.id);
            value.put(KEY_NAME,site.name);
            value.put(KEY_DESCRIPTION,site.description);
            value.put(KEY_TIPS,site.tips);
            value.put(KEY_ADDRESS,site.address);
            value.put(KEY_ADDRESSCIVIC,site.addressCivic);
            value.put(KEY_LATITUDE,site.latitude);
            value.put(KEY_LONGITUDE,site.longitude);
            value.put(KEY_ALWAYSOPEN,site.alwaysOpen);
            value.put(KEY_PRIORITY,site.priority);
            value.put(KEY_PICTURE,site.pictureUrl);
            value.put(KEY_OPENINGS,site.openings);
            value.put(KEY_TICKETS, site.tickets);
            value.put(KEY_CONTACTS,site.contacts);
            value.put(KEY_VISIT_TIME,site.visitTime);
            value.put(KEY_TYPE_OF_SITE,site.typeOfSite);
            value.put(KEY_ALL_ADJACENCY,site.allAdjacency);

            sqlDb.insert(SITES_TABLE,null,value);

        }
        sqlDb.close();

    }


    public ArrayList<Site> getSites(){

        SQLiteDatabase sqlDb=this.getReadableDatabase();
        ArrayList<Site> siteToGive=new ArrayList<Site>();

        Cursor cursor=sqlDb.query(SITES_TABLE,null,null,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                Site site=new Site();
                site.id=cursor.getString(COLUMN_ID);
                site.name=cursor.getString(COLUMN_NAME);
                site.description=cursor.getString(COLUMN_DESCRIPTION);
                site.tips=cursor.getString(COLUMN_TIPS);
                site.longitude=cursor.getDouble(COLUMN_LONGITUDE);
                site.latitude=cursor.getDouble(COLUMN_LATITUDE);
                site.address=cursor.getString(COLUMN_ADDRESS);
                site.addressCivic=cursor.getString(COLUMN_ADDRESS_CIVIC);
                site.alwaysOpen=cursor.getInt(COLUMN_ALWAYSOPEN);
                site.priority=cursor.getInt(COLUMN_PRIORITY);
                site.pictureUrl=cursor.getString(COLUMN_PICTURE);
                site.openings=cursor.getString(COLUMN_OPENINGS);
                site.tickets=cursor.getString(COLUMN_TICKETS);
                site.contacts=cursor.getString(COLUMN_CONTACTS);
                site.visitTime=cursor.getInt(COLUMN_VISIT_TIME);
                site.typeOfSite=cursor.getString(COLUMN_TYPE_OF_SITE);
                site.allAdjacency=cursor.getString(COLUMN_ALL_ADJACENCY);
                siteToGive.add(site);
            } while (cursor.moveToNext());
        }
        sqlDb.close();
        return siteToGive;
    }

    public ArrayList<Site> getSameCategorySite(String categoryToRetrieve){

//        String categoryDB=convertFromAppTypeToDBType(categoryToRetrieve);
        String museumToConfront;
        ArrayList<Site>siteToReturn=new ArrayList<>();

        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")){
            museumToConfront="Museums and Art galleries";
        } else {
            museumToConfront="Musei e Gallerie d'arte";
        }
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor;

        try {
//            Log.d("miotag","categoryToRetrieve: "+categoryToRetrieve);
//            Log.d("miotag","museumToConfront: "+museumToConfront);
            if (categoryToRetrieve.equals(museumToConfront)) {

                cursor=database.query(SITES_TABLE,null,"type LIKE '"+"Muse%"+"'",null,null,null,null);
//                Log.d("miotag","cursor.tostring with museumToConfront: "+cursor.getCount());
            } else {

                cursor = database.query(SITES_TABLE, null, "type='" + categoryToRetrieve + "'", null, null, null, null);
//                Log.d("miotag","cursor.tostring with NO museum-> "+cursor.getCount());
            }


            if (cursor.moveToFirst()) {
//                Log.d("miotag","moveToFirst");
                do {
                    Site site=new Site();
                    site.id = cursor.getString(COLUMN_ID);
                    site.name = cursor.getString(COLUMN_NAME);
//                    site.description = cursor.getString(COLUMN_DESCRIPTION);
                    site.typeOfSite = cursor.getString(COLUMN_TYPE_OF_SITE);
//                    site.tips = cursor.getString(COLUMN_TIPS);
                    site.address = cursor.getString(COLUMN_ADDRESS);
                    site.addressCivic = cursor.getString(COLUMN_ADDRESS_CIVIC);
//                    site.alwaysOpen = cursor.getInt(COLUMN_ALWAYSOPEN);
                    site.pictureUrl = cursor.getString(COLUMN_PICTURE);
//                    site.tickets = cursor.getString(COLUMN_TICKETS);
//                    site.contacts = cursor.getString(COLUMN_CONTACTS);
//                    site.openings = cursor.getString(COLUMN_OPENINGS);
//                    site.latitude = cursor.getDouble(COLUMN_LATITUDE);
//                    site.longitude = cursor.getDouble(COLUMN_LONGITUDE);
//                    site.priority = cursor.getInt(COLUMN_PRIORITY);
//                    site.allAdjacency = cursor.getString(COLUMN_ALL_ADJACENCY);

//                    Log.d("miotag","da db getSameCategory: "+site.name+"; "+site.typeOfSite+", "+site.typeOfSite+", "+site.pictureUrl);
                    siteToReturn.add(site);
                } while (cursor.moveToNext() && siteToReturn.size()<11);
            }

        }catch (Exception e){
//            Log.d("miotag", "getSite: " + e.toString());
        }

        database.close();
        return siteToReturn;


    }



    public String getSiteByName(String name){
        SQLiteDatabase database=getReadableDatabase();
        Site site=null;
        String s="";
        Cursor cursor;
        cursor=database.query(SITES_TABLE, null, "name='" +  name + "'", null, null, null, null);


        try{

            if (cursor.moveToFirst()) {
//                Log.d("miotag","moveToFirst");
                do {
                    site = new Site();
                    site.id = cursor.getString(COLUMN_ID);
//                    site.name = cursor.getString(COLUMN_NAME);
                } while (cursor.moveToNext());
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return site.id;
    }




    public Site getSite(String id) {

        SQLiteDatabase database = getReadableDatabase();
        Site site = new Site();

        Cursor cursor = null;
//        Log.d("miotag","getSite from DB is ON");
        try {
            // columns, where, selectionArgs?, groupby,
            cursor = database.query(SITES_TABLE, null, "id='" + id + "'", null, null, null, null);

            if (cursor.moveToFirst()) {
                site.id=cursor.getString(COLUMN_ID);
                site.name = cursor.getString(COLUMN_NAME);
                site.description = cursor.getString(COLUMN_DESCRIPTION);
                site.typeOfSite = cursor.getString(COLUMN_TYPE_OF_SITE);
                site.tips = cursor.getString(COLUMN_TIPS);
                site.address = cursor.getString(COLUMN_ADDRESS);
                site.addressCivic = cursor.getString(COLUMN_ADDRESS_CIVIC);
                site.alwaysOpen = cursor.getInt(COLUMN_ALWAYSOPEN);
                site.pictureUrl = cursor.getString(COLUMN_PICTURE);
                site.tickets = cursor.getString(COLUMN_TICKETS);
                site.contacts = cursor.getString(COLUMN_CONTACTS);
                site.openings=cursor.getString(COLUMN_OPENINGS);
                site.latitude=cursor.getDouble(COLUMN_LATITUDE);
                site.longitude=cursor.getDouble(COLUMN_LONGITUDE);
                site.priority=cursor.getInt(COLUMN_PRIORITY);
                site.allAdjacency=cursor.getString(COLUMN_ALL_ADJACENCY);
            }
        } catch (Exception e){
//            Log.d("miotag", "getSite: " + e.toString());
        }
//            Log.d("miotag","returning site from DB: "+site.name);
        return site;
    }


    private String convertFromDBTypeToAppType(String s){

        String stringToReturn="";

        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")){

            switch (s) {
                case "theaters":

                    stringToReturn="Theaters";

                    break;

                case "palaces":

                    stringToReturn="Palaces and Castles";
                    break;

                case "villas":

                    stringToReturn="Villas, Gardens and Parks";
                    break;

                case "museums":

                    stringToReturn="Museums and Art galleries";
                    break;

                case "statues":

                    stringToReturn="Statues and Fountains";
                    break;

                case "squares":

                    stringToReturn="Squares and Streets";

                    break;

                case "arches":

                    stringToReturn="Arches, Gates and Walls";
                    break;

                case "fairs":

                    stringToReturn="Fairs and Markets";
                    break;

                case "cemeteries":

                    stringToReturn="Cemeteries and Memorials";
                    break;

                case "buildings":

                    stringToReturn="Buildings";
                    break;

                case "bridges":

                    stringToReturn="Bridges";
                    break;

                case "churches":

                    stringToReturn="Churches, Oratories and Places of worship";
                    break;

                case "other":

                    stringToReturn="Other monuments and Places of interest";
                    break;

            }

            return stringToReturn;


        } else {
            switch (s) {

                case "teatri":

                    stringToReturn="Teatri";
                    break;

                case "palazzi":

                    stringToReturn="Palazzi e Castelli";

                    break;

                case "ville":

                    stringToReturn="Ville, Giardini e Parchi";

                    break;

                case "musei":

                    stringToReturn="Musei e Gallerie d'arte";

                    break;

                case "statue":


                    stringToReturn="Statue e Fontane";

                    break;

                case "piazze":

                    stringToReturn="Piazze e Strade";


                    break;

                case "archi":

                    stringToReturn="Archi, Porte e Mura";

                    break;

                case "fiere":

                    stringToReturn="Fiere e Mercati";

                    break;

                case "cimiteri":

                    stringToReturn="Cimiteri e Memoriali";

                    break;


                case "edifici":

                    stringToReturn="Edifici";

                    break;

                case "ponti":

                    stringToReturn="Ponti";

                    break;

                case "chiese":

                    stringToReturn="Chiese, Oratori e Luoghi di culto";

                    break;

                case "altri":

                    stringToReturn="Altri monumenti e Luoghi di interesse";
                    break;

            }
                return stringToReturn;
            }

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

//    public String getPictureSite(String id){
//        SQLiteDatabase database = getReadableDatabase();
//        Site site = new Site();
//        Cursor cursor = null;
//
//        try {
//            // columns, where, selectionArgs?, groupby,
//            cursor = database.query(SITES_TABLE, null, "id='" + id + "'", null, null, null, null);
//
//            if (cursor.moveToFirst()) {
//
//                site.pictureUrl = cursor.getString(COLUMN_PICTURE);
//
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return site.pictureUrl;
//
//
//    }




    private static DB1SqlHelper db1SqlHelper;

    public static DB1SqlHelper getInstance(Context context){
        if (db1SqlHelper == null) {
            db1SqlHelper = new DB1SqlHelper(context);
        }
        return db1SqlHelper;
    }





}
