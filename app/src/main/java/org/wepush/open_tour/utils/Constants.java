package org.wepush.open_tour.utils;

import com.google.android.gms.location.internal.ParcelableGeofence;

import org.osmdroid.util.GeoPoint;

/**
 * Created by antoniocoppola on 11/06/15.
 */
public class Constants {

    public final static String PACKAGE_NAME="org.wultimaproject.org";
    public final static String SHARED_PREFERENCES_ROOT=PACKAGE_NAME+".sharedPreferences_preferences";
    public final static String WHEN_SAVE=PACKAGE_NAME+"_WHEN_SAVE";
    public final static String TIME_TO_SPEND=PACKAGE_NAME+"_TIME_TO_SPEND";
    public final static String WALKTHROUGH_SEEN=PACKAGE_NAME+"_WALKTHROUGH_SEEN";
    public final static String KEY_CURRENT_CITY=PACKAGE_NAME+"_CURRENT_CITY";
    public final static String TIME_TO_START=PACKAGE_NAME+"_TIME_TO_START";
    public final static String WHAT_SAVE=PACKAGE_NAME+"_WHAT_SAVE";
    public final static String WHERE_SAVE=PACKAGE_NAME+"_WHERE_SAVE";
    public final static String HOW_SAVE=PACKAGE_NAME+"_HOW_SAVE";
    public static final String LATITUDE_STARTING_POINT=PACKAGE_NAME+"_LATITUDE_STARTING_POINT";
    public static final String LONGITUDE_STARTING_POINT=PACKAGE_NAME+"_LONGITUDE_STARTING_POINT";
    public static final String RESULT_DATA_KEY=PACKAGE_NAME+"RESULT_DATA_KEY";
    public static final int SUCCESS_RESULT = 0;
    public static final int NUMBER_OF_ADJACENCIES=3;
    public static final String RECEIVER=PACKAGE_NAME+"_RECEIVER";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME+"_LOCATION_DATA";
    public static final String STARTING_TIME_READABLE_FORMAT=PACKAGE_NAME+"_TIME_READABLE";
    public static final String ACTIVATE_ONLINE_CONNECTION=PACKAGE_NAME+"_ONLINE_CONNECTION";
    public static final String ONLINE_CONNECTION_ON=PACKAGE_NAME+"_ONLINE_CONNECTION_ON";
    public static final String ONLINE_CONNECTION_OFF=PACKAGE_NAME+"_ONLINE_CONNECTION_OFF";
    public static final String CITY_MILAN=PACKAGE_NAME+"_MILAN";
    public static final String CITY_PALERMO=PACKAGE_NAME+"_PALERMO";
    public static final String CATEGORY_VIEW_PAGER_CHURCH=PACKAGE_NAME+"_CHURCH";
    public static final String CATEGORY_VIEW_PAGER_MUSEUM=PACKAGE_NAME+"_MUSEUM";
    public static final String CATEGORY_VIEW_PAGER_CASTLES=PACKAGE_NAME+"_CASTLES";
    public static final String CATEGORY_VIEW_PAGER_VILLAS=PACKAGE_NAME+"_VILLAS";
//    public static final String CITY_MILAN="milan";
//    public static final String CITY_PALERMO="palermo";

    //intent actions

    public final static String INTENT_FROM_SHOWTOURTL=PACKAGE_NAME+"_FROM_SHOWTOURTL";
    public final static String INTENT_FROM_SHOWDETAILS=PACKAGE_NAME+"_FROM_SHOWDETAILS";
    public final static String INTENT_FROM_LIVEMAP=PACKAGE_NAME+"_FROM_LIVEMAP";

    public final static GeoPoint MILAN_NORTH_WEST=new GeoPoint(45.511999, 9.093887);
    public final static GeoPoint MILAN_NORTH_EAST=new GeoPoint(45.513735, 9.250724);
    public final static GeoPoint MILAN_SOUTH_EAST=new GeoPoint(45.426890, 9.244499);




    public final static GeoPoint PALERMO_NORTH_WEST=new GeoPoint(38.172798, 13.298042);
    public final static GeoPoint PALERMO_NORTH_EAST=new GeoPoint(38.171060, 13.376573);
    public final static GeoPoint PALERMO_SOUTH_EAST=new GeoPoint(38.088531, 13.392027);

    // dialogs names (for SettingActivities)

    public final static String SHOW_WHAT_DIALOG=PACKAGE_NAME+"_WHAT_DIALOG";
    public final static String SHOW_WHERE_DIALOG=PACKAGE_NAME+"_WHERE_DIALOG";
    public final static String SHOW_HOW_DIALOG=PACKAGE_NAME+"_HOW_DIALOG";
    public final static String SHOW_NO_GPS_DIALOG= PACKAGE_NAME+"_NO_DIALOG";
    public final static String SHOW_OUT_OF_BOUNDS_DIALOG=PACKAGE_NAME+"_OUT_OF_BOUNDS";
    public final static String SHOW_NO_PACKAGES_DIALOG=PACKAGE_NAME+"_NO_PACKAGES_DOWNLOADED";

    //City bundle management

    public final static String URL_ZIPPED_IMAGES_PALERMO="http://www.open-tour.org/assets/palermo_images.zip";
    public final static String URL_ZIPPED_MAP_PALERMO="http://www.open-tour.org/assets/palermo_map.zip";
    public final static String URL_ZIPPED_MAP_MILAN="http://www.open-tour.org/assets/milano_map.zip";
    public final static String URL_ZIPPED_IMAGES_MILAN="http://www.open-tour.org/assets/milano_images.zip";
    public final static String ZIPPED_IMAGES_MILANO_DOWNLOAD="milano_images.zip";
    public final static String ZIPPED_IMAGES_PALERMO_DOWNLOAD="palermo_images.zip";
    public final static String UNZIPPED_IMAGES_PALERMO_DOWNLOAD="palermo_images";
    public final static String UNZIPPED_IMAGES_MILAN_DOWNLOAD="milano_images";
    public final static String ZIPPED_MAP_PALERMO="palermo_map.zip";
    public final static String ZIPPED_MAP_MILAN="milan_map.zip";
    public final static String UNZIP_DONE=PACKAGE_NAME+"_unzip_done";


    //constants to manage downloading city packages
    //name of the intent field (putExtra)
    public final static String DOWNLOADING_BUNDLE_INTENT=PACKAGE_NAME+"_downloading_bundle_intent";
    //downloading both maps and images
    public final static String DOWNLOADING_MAPS_IMAGES=PACKAGE_NAME+"_downloading_maps_images";
    //maps only
    public final static String DOWNLOADING_MAPS_ONLY=PACKAGE_NAME+"_downloading_maps_only";
    //images only
    public final static String DOWNLOADING_IMAGES_ONLY=PACKAGE_NAME+"_downloading_images_only";

    public final static String WHAT_I_WANT_TO_DOWNLOAD=PACKAGE_NAME+"_what_ive_downloaded";







    public final static String DATABASE_NAME="db1";
    public final static int DATABASE_VERSION=1;



}
