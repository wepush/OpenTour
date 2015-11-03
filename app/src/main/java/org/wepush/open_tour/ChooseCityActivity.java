package org.wepush.open_tour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.wepush.open_tour.utils.CityRecyclerAdapter;
import org.wepush.open_tour.utils.Constants;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 14/10/15.
 */
public class ChooseCityActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private Activity activity;
    private CityRecyclerAdapter adapter;
    private Intent i;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citychoose_activity);



        i=getIntent();

        if(i.getBooleanExtra("fromSettingTourActivity",false)) {
            ImageView arrowBack = (ImageView) findViewById(R.id.imageArrowCityChooser);
            arrowBack.setVisibility(View.VISIBLE);
            arrowBack.setClickable(true);
            arrowBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getBaseContext(),SettingTourActivity.class));
                }
            });
        }


        activity=new ChooseCityActivity();


        ArrayList<String> s=new ArrayList<String>();
//TODO le stringhe saranno gli URL delle immagini delle città da mostrare
        s.add(Constants.CITY_MILAN);
        s.add(Constants.CITY_PALERMO);

        recycler=(RecyclerView)findViewById(R.id.recyclerViewCityChooserActivity);

        final LinearLayoutManager ll=new LinearLayoutManager(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        recycler.setLayoutManager(ll);
        adapter=new CityRecyclerAdapter(this,s);


    }//fine onCreate

    @Override
    public void onBackPressed(){
        if(i.getBooleanExtra("fromSettingTourActivity",false)) {
                    startActivity(new Intent(getBaseContext(),SettingTourActivity.class));
                } else {
            finish();
        }


    }


    @Override
    public void onResume(){
        super.onResume();
        recycler.setAdapter(adapter);

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }






}//fine classe
