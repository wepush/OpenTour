package org.wultimaproject.db2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.angmarch.circledpicker.CircledPicker;

import org.wultimaproject.db2.fragments_dialogs.GPSDialogFragment;
import org.wultimaproject.db2.fragments_dialogs.HowFragment;
import org.wultimaproject.db2.fragments_dialogs.IntroPagerFragment;
import org.wultimaproject.db2.fragments_dialogs.MapDialogFragment;
import org.wultimaproject.db2.services.TourAlgorithmTask;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.structures.DB1SqlHelper;
import org.wultimaproject.db2.structures.FloatingActionButton;
import org.wultimaproject.db2.utils.GeoManager;
import org.wultimaproject.db2.utils.Repository;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;


/**
 * Created by Antonio on 13/04/2015.
 */
public class SettingTourActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private final static int BY_WALK=1;
    private final static int BY_BYKE=2;
    static final int CITY_REQUEST = 90;
    public static Context context;
    private View dialogHowView;
    private Type type;

    private final static double DUMMY_STARTING_LOCATION_LATITUDE=38.121243;
    private final static double DUMMY_STARTING_LOCATION_LONGITUDE=13.357887;

//    45.4655925
//    9.1838722

//    38.117059 13.363698




    private TextView txtWhen;
    public static TextView txtWhatToSee,txtHow;
    private static TextView txtWhere;



    private String[] monthNames;
    private String[] dayNames;
    private CircledPicker circledPicker;
    private TextView tSetTime;
    private  static String mAddressOutput;

    private AddressResultReceiver receiver;

//    private AddressResultReceiver mReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver=new AddressResultReceiver(new Handler());

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


//        mReceiver = new AddressResultReceiver(null);


//        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowNavigationSettings);
//        backArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });



        //initialization of settings with preset data

    //                             DATE / TIME
//        tSetTime=(TextView)findViewById(R.id.txtSetTime);
//
//        Calendar c=Calendar.getInstance();
//        c.get(Calendar.YEAR);
//        c.get(Calendar.MONTH);
//        c.get(Calendar.DAY_OF_MONTH);
//        Log.d("miotag", "DUMMY calendar: c" + c);

//        settingDate(c,c.get(Calendar.DAY_OF_WEEK),c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_WEEK_IN_MONTH));

////         Log.d("miotag","DATA ATTUALE: "+c+"day of week"+ c.get(Calendar.DAY_OF_WEEK)+",anno: "+c.get(Calendar.YEAR)+", month: "+(c.get(Calendar.MONTH)+1)+"; "+c.get(Calendar.DAY_OF_MONTH));
//        settingDate(c, c.get(Calendar.DAY_OF_WEEK), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
////        settingTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
//
//
////                          WHAT
//
        txtWhatToSee.setText("Tutti i siti");
        ArrayList<String> whatToSeeItems=new ArrayList<>();
        whatToSeeItems.add("all");
        Gson gson = new Gson();

        String json = gson.toJson(whatToSeeItems);
        Repository.save(this, Constants.WHAT_SAVE, json);
//
////                        HOW
//
        txtHow.setText("A piedi");
        Repository.save(this,Constants.HOW_SAVE,"walk");
//


//First if on mAddressOuput is to check if it was created through mapDialog; if not, it is created now through GeoManager
//in the latter, mAddressOutput is created for the first time, or user already saw ShowTourTimeLine
        if (mAddressOutput == null || TextUtils.equals(mAddressOutput,"")) {
            Log.d("miotag","mAddressOutput is null");
            GeoManager geo=new GeoManager();
            geo=(GeoManager) getApplicationContext();
            geo.createClient();
            if (geo.isGpsOn()){
                Log.d("miotag"," LookupService launched");
                geo.connectClient();

            } else {
                Toast.makeText(context,R.string.turn_gps_on, Toast.LENGTH_SHORT).show();
            }

            //TODO qui devo lanciare GeoManager, non dopo

        } else {
            Log.d("miotag", "mAddressOutput is:" + mAddressOutput);
            txtWhere.setText(mAddressOutput);

        }

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
//todo checking for actual values from GPS. If not, launch dialog

                    String valuesFromGsp=Repository.retrieve(getBaseContext(),Constants.LATITUDE_STARTING_POINT,String.class);
                    if (TextUtils.equals(valuesFromGsp,"")){

                        Log.d("miotag", "NO GPS COORDINATES! ABORT!");


                        FragmentManager fm = getSupportFragmentManager();
                        GPSDialogFragment hf = new GPSDialogFragment();
                        hf.show(fm, "nogps_fragment");

                    }
                    else {

                        startActivity(new Intent(getBaseContext(), ShowTourTimeLineActivity.class));
                        finish();

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
                Log.d("miotag","valore di Calendar prima del picker. NOW: "+now);
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

                GeoManager geo=new GeoManager();
                geo=(GeoManager) getApplicationContext();
                Log.d("miotag","applicationContext: OK");
                geo.createClient();
                if (geo.isGpsOn()) {
                    Log.d("miotag","GPS on!");
                    geo.connectClient();


//Activate tap to launch fragment for new start location
//if LATITUDE_STARTING_POING is NOT null, then go ahead and show map
//else, if LATITUDE_STARTING_POINT is null (no internet connection)
                        if (!(TextUtils.equals(Repository.retrieve(getBaseContext(),Constants.LATITUDE_STARTING_POINT,String.class),"")))
                            {
                                //TODO localizzazione fittizia per debug
//                                Repository.save(getBaseContext(), Constants.LATITUDE_STARTING_POINT, String.valueOf(DUMMY_STARTING_LOCATION_LATITUDE));
//                                Repository.save(getBaseContext(),Constants.LONGITUDE_STARTING_POINT,String.valueOf(DUMMY_STARTING_LOCATION_LONGITUDE));
                                FragmentManager fm = getSupportFragmentManager();
                                MapDialogFragment hf = new MapDialogFragment();
                                hf.show(fm, "map_fragment");
                            } else {
                                    Repository.save(getBaseContext(), Constants.LATITUDE_STARTING_POINT, String.valueOf(DUMMY_STARTING_LOCATION_LATITUDE));
                                    Repository.save(getBaseContext(),Constants.LONGITUDE_STARTING_POINT,String.valueOf(DUMMY_STARTING_LOCATION_LONGITUDE));
                                        Log.d("miotag","DUMMY LOCATION");
                                            FragmentManager fm = getSupportFragmentManager();
                                            MapDialogFragment hf = new MapDialogFragment();
                                            hf.show(fm, "map_fragment");
                                }
                }
                else //if GPS is not ON
                 {
                    Toast.makeText(context,R.string.turn_gps_on,Toast.LENGTH_SHORT).show();

                }
            }
        });






    }//fine onCreate



    @Override
    public void onResume(){
        super.onResume();
        Log.d("miotag","onResume");
        GeoManager geo= new GeoManager();
        geo = (GeoManager) getApplicationContext();
     Log.d("miotag", "applicationContext: OK");
        geo.createClient();
        if (geo.isGpsOn()) {

            geo.connectClient();
        }
        else {
            Toast.makeText(context,R.string.turn_gps_on,Toast.LENGTH_SHORT).show();
        }

        if (mAddressOutput!= null){
            txtWhere.setText(mAddressOutput);
        }



    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
//        settingTime( hourOfDay,minute);
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

//  21/07      Repository.save(this,Constants.STARTING_TIME_READABLE_FORMAT,time);

        Gson gson = new Gson();
        String json = gson.toJson(calendarTime);
        Repository.save(this, Constants.TIME_TO_START, json);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayWeekMonth) {
       Calendar calendarToSave = new GregorianCalendar(year, monthOfYear, dayWeekMonth); // Note that Month value is 0-based. e.g., 0 for January.
//        Log.d("miotag","tornando dal picker: calendarToSave: "+calendarToSave);
        int result = calendarToSave.get(Calendar.DAY_OF_WEEK);

//        settingDate(calendarToSave,result,year,monthOfYear,dayWeekMonth);
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



//        private Receiver mReceiver;

//    23 Luglio
        public static class AddressResultReceiver extends ResultReceiver {

            public AddressResultReceiver(Handler handler) {
                super(handler);
            }
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {

                // Display the address string
                // or an error message sent from the intent service.
                mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                Repository.save(context, Constants.WHERE_SAVE, mAddressOutput);

                // Show a toast message if an address was found.
                if (resultCode == Constants.SUCCESS_RESULT) {
                    // 09/05

                    txtWhere.setText(mAddressOutput);

                }

            }
        }


//   public class AddressResultReceiver extends ResultReceiver {
//        public AddressResultReceiver(Handler handler) {
//            super(handler);
//        }
//
//
//        public interface Receiver {
//            public void onReceiveResult(int resultCode, Bundle resultData);
//
//        }
//
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//
//            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
//                Repository.save(context, Constants.WHERE_SAVE, mAddressOutput);
//
//                // Show a toast message if an address was found.
//                if (resultCode == Constants.SUCCESS_RESULT) {
//                    // 09/05
//
//                    txtWhere.setText(mAddressOutput);
//
//                }
//        }
//    }




    @Override
    public void onStop(){
        super.onStop();

    }





private boolean isAnySettingVoid(){
    if (
            TextUtils.equals(Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class ),"")||
            Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class )==null ||

            TextUtils.equals(Repository.retrieve(context, Constants.HOW_SAVE, String.class ),"")||
           Repository.retrieve(context, Constants.HOW_SAVE, String.class)==null||

            TextUtils.equals(Repository.retrieve(context, Constants.WHAT_SAVE, String.class),"")||
            Repository.retrieve(context, Constants.WHAT_SAVE, String.class )==null||

            Repository.retrieve(context, Constants.WHEN_SAVE, String.class)==null||

            Repository.retrieve(context, Constants.TIME_TO_START,String.class)==null||
                    TextUtils.equals("", Repository.retrieve(context, Constants.TIME_TO_START,String.class))




            )
    {

        return true;
    } else{

        return false;
    }

}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.showTutorial) {
         //   PreferencesHelper.save(this,Constants.WALKTHROUGH_SEEN,"no");
            startActivity(new Intent(this, WalkthroughActivity.class));
            finish();

        }

        if(id == R.id.cityChooser){
          startActivity(new Intent(this, CityChooserActivity.class));
            finish();

        }

        return super.onOptionsItemSelected(item);
    }


//    private void settingDate(Calendar calendarToSave,int result,int year, int monthOfYear, int dayWeekMonth){
//        switch (result) {
//            case Calendar.MONDAY:
//                txtWhen.setText(getResources().getString(R.string.monday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);
//
//                break;
//            case Calendar.TUESDAY:
//                txtWhen.setText(getResources().getString(R.string.tuesday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);
//
//                break;
//
//            case Calendar.WEDNESDAY:
//                txtWhen.setText(getResources().getString(R.string.wednesday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);
//
//                break;
//            case Calendar.THURSDAY:
//                txtWhen.setText(getResources().getString(R.string.thursday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);
//
//                break;
//
//            case Calendar.FRIDAY:
//                txtWhen.setText(getResources().getString(R.string.friday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);
//
//                break;
//
//            case Calendar.SATURDAY:
//                txtWhen.setText(getResources().getString(R.string.saturday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);
//
//                break;
//
//            case Calendar.SUNDAY:
//                txtWhen.setText(getResources().getString(R.string.sunday)+", "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);
//
//                break;
//        }
//
//        Gson gson = new Gson();
//        type = new TypeToken<Calendar>() {}.getType();
//        String json = gson.toJson(calendarToSave);
//        Log.d("miotag","calendarToSave: "+calendarToSave);
//        Repository.save(this, Constants.WHEN_SAVE, json);
//
//        calendarToSave.clear();
//
//    }


//    private void settingTime( int hourOfDay, int minute){
//
//
//        Calendar calendarTime = Calendar.getInstance(Locale.getDefault());
//
//        calendarTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
//        calendarTime.set(Calendar.MINUTE,minute);
//
//
//
//        String mins,hours;
//        if (minute <10){
//            mins="0"+minute;
//        }else {
//            mins=String.valueOf(minute);
//        }
//
//        if (hourOfDay <10){
//            hours="0"+hourOfDay;
//        } else {
//            hours=String.valueOf(hourOfDay);
//        }
//        String time =hours+":"+mins;
//
//        tSetTime.setText(time);
//
//        Repository.save(this,Constants.STARTING_TIME_READABLE_FORMAT,time);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(calendarTime);
//        Repository.save(this, Constants.TIME_TO_START, json);
//
//    }





}//fine classe


