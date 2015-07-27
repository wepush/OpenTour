package org.wepush.opentour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.wepush.opentour.structures.Constants;
import org.wepush.opentour.utils.Repository;


public class HomeActivity extends AppCompatActivity {

    static final int CITY_REQUEST = 90;



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//       destroyTourPreferences(this);
//
//
//        if( !(TextUtils.equals(Repository.retrieve(this, Constants.WALKTHROUGH_SEEN,String.class),"yes"))
//                )
//        {
//            Log.d("miotag","Launch Walkthrough");
//            startActivity(new Intent(this, WalkthroughActivity.class));
//            finish();
//        }else if(
//                !(TextUtils.equals(Repository.retrieve(this, Constants.KEY_CURRENT_CITY,  String.class),"milano"))&&
//                !(TextUtils.equals(Repository.retrieve(this, Constants.KEY_CURRENT_CITY, String.class),"palermo") )
//            )
//        {
//            Log.d("miotag","CityChooserActivity");
//            startActivity(new Intent(this,CityChooserActivity.class));
//            finish();
//        } else {
//
//            startActivity(new Intent(this,SettingTourActivity.class));
//            finish();
//        }
//
//
//
//    }//fine onCreate


    //10 luglio 2015

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        destroyTourPreferences(this);

//Since just Milan is available, the only choise to do at the start is if this is the first launch or not.
//if this is first launch after installation, then Walkthrough is launched alongside ReadFromJson
//else launch Setting activity since local db is already loaded

        if( !(TextUtils.equals(Repository.retrieve(this, Constants.WALKTHROUGH_SEEN, String.class),"yes"))
                )
        {
            Log.d("miotag","Launch Walkthrough");
            startActivity(new Intent(this, WalkthroughActivity.class));
            finish();
        }else  {

            startActivity(new Intent(this,SettingTourActivity.class));
            finish();
        }



    }//fine onCreate










    public static void destroyTourPreferences(Context ctx)
    {
        Log.d("miotag","Preferences DESTROYED");

        Repository.save(ctx, Constants.TIME_TO_START, "");
        Repository.save(ctx,Constants.TIME_TO_SPEND,"");
        Repository.save(ctx,Constants.WHAT_SAVE,"");
        Repository.save(ctx,Constants.WHEN_SAVE,"");
        Repository.save(ctx,Constants.WHERE_SAVE,"");
        Repository.save(ctx,Constants.HOW_SAVE,"");
        Repository.save(ctx, Constants.LATITUDE_STARTING_POINT, "");
        Repository.save(ctx, Constants.LONGITUDE_STARTING_POINT,"");




    }

}//fine classe


