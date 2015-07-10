package org.wultimaproject.db2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//
//import com.google.gson.Gson;
//
//import org.wepush.opentour.models.Place;
//import org.wepush.opentour.models.Site;
//import org.wepush.opentour.workers.DetourAlgoritmTask;
//
//import java.util.ArrayList;
//
///**
// * Created by Antonio on 13/05/2015.
// */
public class SplashActivityTimeLine extends Activity {

    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivityTimeLine.this, SettingTourActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }//fine onCreate



    }//fine classe



