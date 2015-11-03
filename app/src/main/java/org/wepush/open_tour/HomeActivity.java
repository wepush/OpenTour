package org.wepush.open_tour;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;




import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.io.File;


public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
//        Fabric.with(this, new Crashlytics());
//        Fabric.with(this, new Crashlytics());





        destroyTourPreferences(this);

//Since just Milan is available, the only choise to do at the start is if this is the first launch or not.
//if this is first launch after installation, then Walkthrough is launched alongside ReadFromJson
//else launch Setting activity since local db is already loaded

        if( !(TextUtils.equals(Repository.retrieve(this, Constants.WALKTHROUGH_SEEN, String.class),"yes"))
                )
        {

            startActivity(new Intent(this, WalkthroughActivity.class));
            finish();
        }else  {

            startActivity(new Intent(this,SettingTourActivity.class));
            finish();
        }



    }//fine onCreate










    public static void destroyTourPreferences(Context ctx)
    {
//        Log.d("miotag","Preferences DESTROYED");

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


