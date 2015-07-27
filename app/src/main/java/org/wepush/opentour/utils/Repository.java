package org.wepush.opentour.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.wepush.opentour.structures.Constants;


/**
 * Created by antoniocoppola on 11/06/15.
 */
public class Repository {


    public static void save (Context context, String nameOfTheObjectToSave, Object object){

        SharedPreferences mPref=context.getSharedPreferences(Constants.SHARED_PREFERENCES_ROOT,Context.MODE_PRIVATE);

        if (object instanceof String){
            mPref.edit().putString(nameOfTheObjectToSave,(String)object).commit();
        } else if(object instanceof Float)
        {
            mPref.edit().putFloat(nameOfTheObjectToSave, (Float)object).commit();
        } else if (object instanceof Boolean){
            mPref.edit().putBoolean(nameOfTheObjectToSave,(Boolean)object).commit();
        }
    }



    public static <T> T retrieve (Context context, String nameOfTheObjectToRetrieve, Class<T> className) {

        SharedPreferences mPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES_ROOT, Context.MODE_PRIVATE);
        if (className.isAssignableFrom(String.class)) {

            return className.cast(mPref.getString(nameOfTheObjectToRetrieve, ""));

        } else if (className.isAssignableFrom(Float.class)){

            return className.cast(mPref.getFloat(nameOfTheObjectToRetrieve,-1));
        } else if (className.isAssignableFrom(Boolean.class)){

            return className.cast(mPref.getBoolean(nameOfTheObjectToRetrieve,false));
        }

        return null;
    }

    public static void removeEverything (Context context){
        SharedPreferences mPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES_ROOT, Context.MODE_PRIVATE);
//        mPref.edit().clear().commit();
        mPref.edit().remove(Constants.TIME_TO_START).commit();
        mPref.edit().remove(Constants.TIME_TO_SPEND).commit();
        mPref.edit().remove(Constants.HOW_SAVE).commit();
        mPref.edit().remove(Constants.WHAT_SAVE).commit();
        mPref.edit().remove(Constants.WHEN_SAVE).commit();
        mPref.edit().remove(Constants.WHERE_SAVE).commit();
        mPref.edit().remove(Constants.LATITUDE_STARTING_POINT).commit();
        mPref.edit().remove(Constants.LONGITUDE_STARTING_POINT).commit();
    }

}
