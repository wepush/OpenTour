package org.wepush.open_tour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;


import com.google.gson.Gson;

import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    public static ArrayList<Site> museums,churches,palaces,villas,arrayListMuseums,arrayListChurches,arrayListPalaces,arrayListVillas;
    private static Context context;
    private String cityToChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        destroyTourPreferences(this);
        context=this;

        cityToChoose=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);
        initAllaArraysForDiscovery(cityToChoose);


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
        Repository.save(ctx, Constants.TIME_TO_START, "");
        Repository.save(ctx,Constants.TIME_TO_SPEND,"");
        Repository.save(ctx,Constants.WHAT_SAVE,"");
        Repository.save(ctx,Constants.WHEN_SAVE,"");
        Repository.save(ctx,Constants.WHERE_SAVE,"");
        Repository.save(ctx, Constants.HOW_SAVE, "");
        Repository.save(ctx, Constants.LATITUDE_STARTING_POINT, "");
        Repository.save(ctx, Constants.LONGITUDE_STARTING_POINT, "");
    }

    public static void initAllaArraysForDiscovery(String s){




        museums=new ArrayList<>();
        churches=new ArrayList<>();
        villas=new ArrayList<>();
        palaces=new ArrayList<>();

        arrayListChurches=new ArrayList<>();
        arrayListMuseums=new ArrayList<>();
        arrayListPalaces=new ArrayList<>();
        arrayListVillas=new ArrayList<>();




        museums= DB1SqlHelper.getInstance(context).getSameCategorySite(context.getResources().getString(R.string.museums_and_art_galleries));
        churches=DB1SqlHelper.getInstance(context).getSameCategorySite(context.getResources().getString(R.string.churchs_oratories_worship));
        villas=DB1SqlHelper.getInstance(context).getSameCategorySite(context.getString(R.string.villas_gardens_parks));
        palaces = DB1SqlHelper.getInstance(context).getSameCategorySite(context.getResources().getString(R.string.palaces_and_castels));




        switch(s){

            case Constants.CITY_PALERMO:

                //MUSEUMS:

                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31ae0000"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b319d0200"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31960000"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31b90200"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31980200"));


                //VILLAS

                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31560000"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31060100"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31920000"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31230100"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31f50000"));

                //CASTLES

                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31760000"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31140000"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31660000"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31a50000"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("5506ae77694d610b31b10300"));

                //CHURCHES

                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31220000"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31820000"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31ed0000"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b313c0000"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b315b0100"));
            break;

            case Constants.CITY_MILAN:

                //MUSEUMS

                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5551cf11694d6103fa040100"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5551fe27694d6103fa2b0100"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("55530f60694d6103fac80100"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5551cc00694d6103faf10000"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("5550be56694d6103fa020000"));

                //VILLAS

                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5550cbaa694d6103fa4c0000"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5552156f694d6103faa30100"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("55531d6c694d6103faff0100"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5552208e694d6103fabd0100"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("554b2c3a694d61267ec90000"));

                //CASTLES

                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("554b7461694d61267e9b0100"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("5549f352694d61267e880000"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("554b3360694d61267ef90000"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("554b3a3e694d61267e020100"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("5549e224694d61267e2b0000"));

                //CHURCH

                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("554b78f9694d61267ea70100"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("554b6d88694d61267e5f0100"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("554b8a6a694d6147f85e0000"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("555c9def694d6103faf70200"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("554b8b24694d6147f8670000"));

            break;


            case Constants.CITY_TURIN:

                //MUSEUMS

                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("565dd3ef32733015650001a4"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("565f0a9a3273301565000216"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("565f11203273301565000224"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("565da6d33273301565000123"));
                arrayListMuseums.add(DB1SqlHelper.getInstance(context).getSite("56938dee32733015650004b6"));

                //VILLAS

                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5693bbdc32733015650004ed"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("569616c832733015650005e6"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("566832aa3273301565000295"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("5697d78c3273301565000644"));
                arrayListVillas.add(DB1SqlHelper.getInstance(context).getSite("565f151a3273301565000230"));

                //CHURCH

                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("565ed2c132733015650001eb"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("56977b663273301565000617"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("5694def93273301565000592"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("56682ecf3273301565000285"));
                arrayListChurches.add(DB1SqlHelper.getInstance(context).getSite("5673ec20327330156500033e"));

                //CASTLES

                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("565dbe1b3273301565000166"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("565ebba332733015650001b2"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("565daed03273301565000136"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("565dcec83273301565000193"));
                arrayListPalaces.add(DB1SqlHelper.getInstance(context).getSite("56991e4e327330156500069e"));




        }


        Gson gsonMuseums=new Gson();
        String jsonMuseum = gsonMuseums.toJson(museums);
        Repository.save(context, Constants.JSON_MUSEUMS, jsonMuseum);

        Gson gsonVillas=new Gson();
        String jsonVillas = gsonVillas.toJson(villas);
        Repository.save(context, Constants.JSON_VILLAS, jsonVillas);

        Gson gsonChurches=new Gson();
        String jsonChurches = gsonChurches.toJson(churches);
        Repository.save(context, Constants.JSON_CHURCHES, jsonChurches);

        Gson gsonPalaces=new Gson();
        String jsonPalaces = gsonPalaces.toJson(palaces);
        Repository.save(context, Constants.JSON_PALACE, jsonPalaces);



        Gson gsonArrayMuseums=new Gson();
        String jsonArrayMuseums=gsonArrayMuseums.toJson(arrayListMuseums);
        Repository.save(context, Constants.JSON_ARRAYLIST_MUSEUMS, jsonArrayMuseums);

        Gson gsonArrayVillas=new Gson();
        String jsonArrayVillas = gsonArrayVillas.toJson(arrayListVillas);
        Repository.save(context, Constants.JSON_ARRAYLIST_VILLAS, jsonArrayVillas);

        Gson gsonArrayChurches=new Gson();
        String jsonArrayChurches = gsonArrayChurches.toJson(arrayListChurches);
        Repository.save(context, Constants.JSON_ARRAYLIST_CHURCHES, jsonArrayChurches);

        Gson gsonArrayPalaces=new Gson();
        String jsonArrayPalaces = gsonArrayPalaces.toJson(arrayListPalaces);
        Repository.save(context, Constants.JSON_ARRAYLIST_PALACES, jsonArrayPalaces);



    }

}//fine classe


