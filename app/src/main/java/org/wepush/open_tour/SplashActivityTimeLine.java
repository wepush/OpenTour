package org.wepush.open_tour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;




public class SplashActivityTimeLine extends Activity {

    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        ProgressBar progressBar=(ProgressBar) findViewById(R.id.progressPreSettings);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashActivityTimeLine.this, SettingTourActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            HomeActivity.destroyTourPreferences(this);
            startActivity(new Intent(this,SettingTourActivity.class));
            finish();
    }




}



