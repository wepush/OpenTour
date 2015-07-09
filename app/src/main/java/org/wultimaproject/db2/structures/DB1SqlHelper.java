package org.wultimaproject.db2.structures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 29/05/15.
 */
public class DB1SqlHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME="db1";
    private final static int DATABASE_VERSION=1;

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
    //always_open field is INTEGER as SQLite standard requires

    private final static String SITES_TABLE="sites_table";
    private final static String CREATE_TABLE_SITES= "CREATE TABLE IF NOT EXISTS " + SITES_TABLE
            + " ("
            + " id VARCHAR(30) PRIMARY KEY, "
            + " name VARCHAR(255), "
            + " latitude REAL,"
            + " longitude REAL,"
            + " description TEXT,"
            + " tips TEXT,"
            + " address TEXT,"
            + " address_civic TEXT,"
            + " always_open INTEGER,"
            + " priority TEXT,"
            + " picture TEXT,"
            + " openings TEXT,"
            + " tickets TEXT,"
            + " contacts TEXT,"
            + " visit_time TEXT,"
            + " type TEXT"
            + ");";




//    private final static String SITES_NEAREST="nearest_sites";
//    private final static String CREATE_TABLE_OPENINGS="CREATE TABLE IF NOT EXISTS " +  SITES_NEAREST
//
//            + " ("
//            + " id VARCHAR(30) PRIMARY KEY, "
//            + " name VARCHAR(255), "
//            + " latitude REAL,"
//            + " longitude REAL,"
//            + " description TEXT,"
//            + " tips TEXT,"
//            + " address TEXT,"
//            + " address_civic TEXT,"
//            + " always_open INTEGER,"
//            + " priority TEXT,"
//            + " picture TEXT,"
//            + " openings TEXT,"
//            + " tickets TEXT,"
//            + " contacts TEXT,"
//            + " visit_time TEXT,"
//            + " type TEXT"
//            + ");";

    public DB1SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_TABLE_SITES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + SITES_TABLE);
        onCreate(database);

    }

    public void deleteDb(Context ctx){
        ctx.deleteDatabase(DATABASE_NAME);

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
                siteToGive.add(site);
            } while (cursor.moveToNext());
        }
        sqlDb.close();
        return siteToGive;
    }




    public Site getSite(String id) {

        SQLiteDatabase database = getReadableDatabase();
        Site site = new Site();
        //commentato il 06/05
//         Cursor cursor = null, adjacencyCursor = null;

        Cursor cursor = null;

        try {
            // columns, where, selectionArgs?, groupby,
            cursor = database.query(SITES_TABLE, null, "id='" + id + "'", null, null, null, null);

            if (cursor.moveToFirst()) {
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
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return site;
    }




    private static DB1SqlHelper db1SqlHelper;

    public static DB1SqlHelper getInstance(Context context){
        if (db1SqlHelper == null) {
            db1SqlHelper = new DB1SqlHelper(context);
        }
        return db1SqlHelper;
    }


}
