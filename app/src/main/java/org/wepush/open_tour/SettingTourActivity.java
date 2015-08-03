package org.wepush.open_tour;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.angmarch.circledpicker.CircledPicker;
import org.osmdroid.util.GeoPoint;
import org.wepush.open_tour.fragments_dialogs.HowFragment;
import org.wepush.open_tour.fragments_dialogs.IntroPagerFragment;
import org.wepush.open_tour.fragments_dialogs.MapDialogFragment;
import org.wepush.open_tour.fragments_dialogs.NoGpsDialog;
import org.wepush.open_tour.fragments_dialogs.OutOfBoundsDialog;
import org.wepush.open_tour.structures.Constants;
import org.wepush.open_tour.structures.FloatingActionButton;
import org.wepush.open_tour.utils.Repository;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by Antonio on 13/04/2015.
 */
public class SettingTourActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    public static Context context;
    private Type type;

    private final static double DUMMY_STARTING_LOCATION_LATITUDE=45.468994;
    private final static double DUMMY_STARTING_LOCATION_LONGITUDE=9.182067;

    private TextView txtWhen;
    public static TextView txtWhatToSee,txtHow;
    private static TextView txtWhere;



    private String[] monthNames;
    private String[] dayNames;
    private CircledPicker circledPicker;
    private TextView tSetTime;
    private  static String mAddressOutput;

//    private AddressResultReceiver receiver;

    private LocationManager serviceGPS;
    private GoogleApiClient mGoogleApiClient;

//    private AddressResultReceiver mReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        monthNames=new String []{"", getResources().getString(R.string.january),
                getResources().getString(R.string.february),
                getResources().getString(R.string.march),getResources().getString(R.string.april),
                getResources().getString(R.string.may),getResources().getString(R.string.june),
                getResources().getString(R.string.july),getResources().getString(R.string.august),
                getResources().getString(R.string.september),getResources().getString(R.string.october),
                getResources().getString(R.string.november),getResources().getString(R.string.december)};


        dayNames=new String[] {getResources().getString(R.string.monday),getResources().getString(R.string.tuesday),
            getResources().getString(R.string.wednesday),getResources().getString(R.string.thursday),
            getResources().getString(R.string.friday),getResources().getString(R.string.saturday),getResources().getString(R.string.sunday)};


        context=this;

        setContentView(R.layout.setting_tour_activity);
        txtWhen=(TextView)findViewById(R.id.txtWhen);
        txtWhere=(TextView)findViewById(R.id.txtWhere);
        txtWhatToSee=(TextView)findViewById(R.id.txtWhatToVisit);
        txtHow=(TextView)findViewById(R.id.txtHow);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbarSettingTour);
       toolbar.setTitle("");

        setSupportActionBar(toolbar);


////                          WHAT
//
        txtWhatToSee.setText(R.string.all_sites);
        ArrayList<String> whatToSeeItems=new ArrayList<>();
        whatToSeeItems.add("all");
        Gson gson = new Gson();

        String json = gson.toJson(whatToSeeItems);
        Repository.save(this, Constants.WHAT_SAVE, json);

//                       HOW
        txtHow.setText(R.string.by_walk);
        String thisWalk=getResources().getString(R.string.by_walk);
        Repository.save(this, Constants.HOW_SAVE, thisWalk);
//


//First if on mAddressOuput is to check if it was created through mapDialog; if not, it is created now through GeoManager
//in the latter, mAddressOutput is created for the first time, or user already saw ShowTourTimeLine



        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        serviceGPS = (LocationManager) getSystemService(LOCATION_SERVICE);
        actualUserPosition();



        circledPicker=(CircledPicker)findViewById(R.id.circledPicker);
        circledPicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d("miotag","Aciont performing is: "+event.getAction()+"\n");

                if (event.getAction()==0){
                    return true;
                } else if (event.getAction()==2){
                    Log.d("miotag", "saving this value: "+circledPicker.getValue()*60);
                    Repository.save(context,Constants.TIME_TO_SPEND,String.valueOf(circledPicker.getValue()*60));
                }

                return false;
            }
//                if (
//                        !(event.getAction()==MotionEvent.ACTION_DOWN) && (event.getAction()==MotionEvent.ACTION_UP)
//
//                        )
//                {
//                    Log.d("miotag","ho preso il tempo : "+circledPicker.getValue()*60);
//                    Repository.save(context,Constants.TIME_TO_SPEND,String.valueOf(circledPicker.getValue() * 60));
//                }
//
//                return false;

        });



        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.mipmap.ic_floating_play))
                .withButtonColor(getResources().getColor(R.color.orange500))

                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAnySettingVoid()) {
                    Toast.makeText(context, R.string.complete_all_fields, Toast.LENGTH_SHORT).show();
                } else {

                       if(!(serviceGPS.isProviderEnabled(LocationManager.GPS_PROVIDER))){
                                FragmentManager fm = getSupportFragmentManager();
                                NoGpsDialog hf = new NoGpsDialog();
                                hf.show(fm, "nogps_dialog");
                       }
                         else {

                                    if (isInMapBounds()) {

                                        startActivity(new Intent(getBaseContext(), ShowTourTimeLineActivity.class));
                                        finish();
                                    } else {
                                        FragmentManager fm = getSupportFragmentManager();
                                        OutOfBoundsDialog hf = new OutOfBoundsDialog();
                                        hf.show(fm, "outofbounds_dialog");
                                    }

                            }

              }
            }});


        LinearLayout rlClickWhen = (LinearLayout)findViewById(R.id.whenLayout);
        LinearLayout llClickWhere = (LinearLayout)findViewById(R.id.whereLayout);
        LinearLayout llClickWhat = (LinearLayout)findViewById(R.id.whatLayout);
        LinearLayout llClickHow = (LinearLayout)findViewById(R.id.howLayout);
       tSetTime=(TextView)findViewById(R.id.txtSetTime);

        tSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar now=Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        SettingTourActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true


                );
                dpd.show(getFragmentManager(), "Timepickerdialog");

            }
        });


        rlClickWhen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar now=Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SettingTourActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)

                );
                dpd.setMinDate(now);//this instruction let calendar to exclude past dates
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }

        });

        llClickHow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                FragmentManager fm = getSupportFragmentManager();
                HowFragment hf=new HowFragment();
                hf.show(fm,"how_fragment");
            }
        });

        llClickWhat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                FragmentManager fm = getSupportFragmentManager();
                IntroPagerFragment.WhatFragment wf=new IntroPagerFragment.WhatFragment();
                wf.show(fm,"what_fragment");
            }
        });

        llClickWhere.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){



                if (serviceGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

//Activate tap to launch fragment for new start location
//if LATITUDE_STARTING_POING is NOT null, then go ahead and show map
//else, if LATITUDE_STARTING_POINT is null (no internet connection)
                        if (!(TextUtils.equals(Repository.retrieve(getBaseContext(),Constants.LATITUDE_STARTING_POINT,String.class),"")))
                            {
                                if (isInMapBounds()) {
                                    FragmentManager fm = getSupportFragmentManager();
                                    MapDialogFragment hf = new MapDialogFragment();
                                    hf.show(fm, "map_fragment");
                                } else {
                                    FragmentManager fm = getSupportFragmentManager();
                                    OutOfBoundsDialog hf = new OutOfBoundsDialog();
                                    hf.show(fm,"out_ofBonds");
                                    Toast.makeText(context,R.string.tooFarAway,Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                    Repository.save(getBaseContext(), Constants.LATITUDE_STARTING_POINT, String.valueOf(DUMMY_STARTING_LOCATION_LATITUDE));
                                    Repository.save(getBaseContext(),Constants.LONGITUDE_STARTING_POINT,String.valueOf(DUMMY_STARTING_LOCATION_LONGITUDE));
                                            FragmentManager fm = getSupportFragmentManager();
                                            MapDialogFragment hf = new MapDialogFragment();
                                            hf.show(fm, "map_fragment");
                                }
                }
                else //if GPS is not ON
                 {
                    Toast.makeText(context, R.string.turn_gps_on,Toast.LENGTH_SHORT).show();

                }
            }
        });

    }//fine onCreate



    @Override
    public void onResume(){
        super.onResume();

        if (serviceGPS!= null) {
            if (!(serviceGPS.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                Toast.makeText(context, R.string.turn_gps_on, Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Calendar calendarTime = Calendar.getInstance(Locale.getDefault());

        calendarTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendarTime.set(Calendar.MINUTE,minute);

        String mins,hours;
        if (minute <10){
            mins="0"+minute;
        }else {
            mins=String.valueOf(minute);
        }

        if (hourOfDay <10){
            hours="0"+hourOfDay;
        } else {
            hours=String.valueOf(hourOfDay);
        }
        String time =hours+":"+mins;

        tSetTime.setText(time);

        Gson gson = new Gson();
        String json = gson.toJson(calendarTime);
        Repository.save(this, Constants.TIME_TO_START, json);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayWeekMonth) {
       Calendar calendarToSave = new GregorianCalendar(year, monthOfYear, dayWeekMonth); // Note that Month value is 0-based. e.g., 0 for January.
        int result = calendarToSave.get(Calendar.DAY_OF_WEEK);

        switch (result) {
            case Calendar.MONDAY:
                txtWhen.setText(getResources().getString(R.string.monday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;
            case Calendar.TUESDAY:
                txtWhen.setText(getResources().getString(R.string.tuesday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.WEDNESDAY:
                txtWhen.setText(getResources().getString(R.string.wednesday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;
            case Calendar.THURSDAY:
                txtWhen.setText(getResources().getString(R.string.thursday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.FRIDAY:
                txtWhen.setText(getResources().getString(R.string.friday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.SATURDAY:
                txtWhen.setText(getResources().getString(R.string.saturday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.SUNDAY:
                txtWhen.setText(getResources().getString(R.string.sunday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;
        }

        Gson gson = new Gson();
        type = new TypeToken<Calendar>() {}.getType();
        String json = gson.toJson(calendarToSave);
        Repository.save(this, Constants.WHEN_SAVE, json);


        calendarToSave.clear();



    }

    @Override
    public void onStop(){
        super.onStop();
//        unregisterReceiver(null);
    }





private boolean isAnySettingVoid(){
    if (
            TextUtils.equals(Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class ),"")||
            Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class )==null ||

            TextUtils.equals(Repository.retrieve(context, Constants.HOW_SAVE, String.class ),"")||
            Repository.retrieve(context, Constants.HOW_SAVE, String.class)==null||

            TextUtils.equals(Repository.retrieve(context, Constants.WHAT_SAVE, String.class),"")||
            Repository.retrieve(context, Constants.WHAT_SAVE, String.class )==null||

            TextUtils.equals(Repository.retrieve(context, Constants.WHEN_SAVE, String.class),"")||
            Repository.retrieve(context, Constants.WHEN_SAVE, String.class)==null||

            Repository.retrieve(context, Constants.TIME_TO_START,String.class)==null||
            TextUtils.equals("", Repository.retrieve(context, Constants.TIME_TO_START,String.class))

            )
        {
            return true;
        } else  {
                return false;
            }

}

    public void onConnected(Bundle hintBundle){
            acquiringUserPosition();
    }//fine onConnected

    public void onConnectionSuspended(int i){
        mGoogleApiClient.disconnect();
        Log.d("miotag","client connection: INTERRUPTED");
        Toast.makeText(this, "Client Connection: SUSPENDED!", Toast.LENGTH_LONG).show();

    }

    public void onConnectionFailed(ConnectionResult connResult){
        Log.d("miotag","connection to LocalServices lost ");
        Toast.makeText(this, "Lost Connection: No Signal!", Toast.LENGTH_LONG).show();
    }


    private void actualUserPosition(){
        establishGoogleConnection();
    }



    private boolean isInMapBounds(){
        //acquires user GPS here is necessary because it could had not be done before (some previous weird stop/resume of the activity)
        establishGoogleConnection();
        GeoPoint actualUserPosition=null;
        try {
             actualUserPosition = new GeoPoint(Double.valueOf(Repository.retrieve(getBaseContext(), Constants.LATITUDE_STARTING_POINT, String.class)), Double.valueOf(Repository.retrieve(getBaseContext(), Constants.LONGITUDE_STARTING_POINT, String.class)));
        } catch (NumberFormatException e){
            FragmentManager fm = getSupportFragmentManager();
            NoGpsDialog hf = new NoGpsDialog();
            hf.show(fm, "nogps_dialog");
        }
            if (actualUserPosition!=null) {
                Log.d("miotag", "check ifInBound: " + actualUserPosition.getLatitude() + ", " + actualUserPosition.getLongitude());
                if (

                        (actualUserPosition.getLongitude() < Constants.NORTH_EAST.getLongitude()) &&
                                (actualUserPosition.getLongitude() > Constants.NORTH_WEST.getLongitude()) &&
                                (actualUserPosition.getLatitude() > Constants.SOUTH_EAST.getLatitude()) &&
                                (actualUserPosition.getLatitude() < Constants.NORTH_EAST.getLatitude())


                        ) {
                    return true;
                } else {
                    return false;
                }
            } else {
                FragmentManager fm = getSupportFragmentManager();
                NoGpsDialog hf = new NoGpsDialog();
                hf.show(fm, "nogps_dialog");
            }
        return false;
    }

    private void establishGoogleConnection(){
        if (mGoogleApiClient.isConnected())
        {
//            mGoogleApiClient.disconnect();
//            Log.d("miotag", " GP era connesso, adesso lancio");
//
//            mGoogleApiClient.connect();
            acquiringUserPosition();
        } else {
            Log.d("miotag","GP non era connesso, adesso lancio");
            mGoogleApiClient.connect();
        }
    }

    private void acquiringUserPosition(){
        Log.d("miotag","connessione con GP stabilita");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null){
            double mLastLocationLatitude=mLastLocation.getLatitude();
            double mLastLocationLongitude=mLastLocation.getLongitude();
            Log.d("miotag", "dopo connessione con Google i valori di mLastLocation: "+mLastLocationLatitude+", "+mLastLocationLongitude);
            Repository.save(this, Constants.LATITUDE_STARTING_POINT, String.valueOf(mLastLocationLatitude));
            Repository.save(this, Constants.LONGITUDE_STARTING_POINT, String.valueOf(mLastLocationLongitude));

//                   Repository.save(this,Constants.LATITUDE_STARTING_POINT,String.valueOf(45.468994) );
//                    Repository.save(this,Constants.LONGITUDE_STARTING_POINT,String.valueOf(9.182067));
        }
//        else {
//            FragmentManager fm = getSupportFragmentManager();
//            NoGpsDialog hf = new NoGpsDialog();
//            hf.show(fm, "nogps_dialog");
//        }
    }


}//fine classe


