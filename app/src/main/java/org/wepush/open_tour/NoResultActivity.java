package org.wepush.open_tour;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import org.wepush.open_tour.structures.Constants;

/**
 * Created by antoniocoppola on 17/07/15.
 */
public class NoResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        Intent intent=getIntent();
        if (TextUtils.equals(intent.getAction(), Constants.INSUFFICIENT_SETTINS)) {

            setContentView(R.layout.noresult_settings_activity);
        }
         else {
            setContentView(R.layout.noresult_gps_activity);
        }
    }

    public void onBackToSettingActivity(View v){
        startActivity(new Intent(this, SettingTourActivity.class));
        finish();
    }

    public void onBackToGPSActivity(View v){
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        Button btn=(Button) findViewById(R.id.btnToSettings);
        btn.setVisibility(View.VISIBLE);
    }


}
