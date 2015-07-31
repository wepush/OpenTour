package org.wepush.open_tour.structures;

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
    public static final String RECEIVER=PACKAGE_NAME+"_RECEIVER";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME+"_LOCATION_DATA";
    public static final String STARTING_TIME_READABLE_FORMAT=PACKAGE_NAME+"_TIME_READABLE";

    //intent actions

    public final static String INTENT_FROM_SHOWTOURTL=PACKAGE_NAME+"_FROM_SHOWTOURTL";
    public final static String INTENT_FROM_SHOWDETAILS=PACKAGE_NAME+"_FROM_SHOWDETAILS";
    public final static String INTENT_FROM_LIVEMAP=PACKAGE_NAME+"_FROM_LIVEMAP";

    public final static GeoPoint NORTH_WEST=new GeoPoint(45.511999, 9.093887);
    public final static GeoPoint NORTH_EAST=new GeoPoint(45.513735, 9.250724);
    public final static GeoPoint SOUTH_EAST=new GeoPoint(45.426890, 9.244499);




}
