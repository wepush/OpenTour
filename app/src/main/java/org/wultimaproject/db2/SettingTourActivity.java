package org.wultimaproject.db2;

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

import org.wultimaproject.db2.fragments_dialogs.HowFragment;
import org.wultimaproject.db2.fragments_dialogs.IntroPagerFragment;
import org.wultimaproject.db2.fragments_dialogs.MapDialogFragment;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.structures.DB1SqlHelper;
import org.wultimaproject.db2.structures.FloatingActionButton;
import org.wultimaproject.db2.utils.GeoManager;
import org.wultimaproject.db2.utils.Repository;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


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




    private TextView txtWhen;
    public static TextView txtWhatToSee,txtHow;
    private static TextView txtWhere;
    public Calendar calendar;
    private String[] monthNames= {"0","Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio","Giugno","Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"};
    private String[] dayNames = {"Lunedì","Martedì","Mercoledì","Giovedì","Venerdì","Sabato","Domenica"};
    private CircledPicker circledPicker;
    private TextView tSetTime;
    private  static String mAddressOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        context=this;

        setContentView(R.layout.setting_tour_activity);
        txtWhen=(TextView)findViewById(R.id.txtWhen);
        txtWhere=(TextView)findViewById(R.id.txtWhere);
        txtWhatToSee=(TextView)findViewById(R.id.txtWhatToVisit);
        txtHow=(TextView)findViewById(R.id.txtHow);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbarSettingTour);
       toolbar.setTitle("");

        setSupportActionBar(toolbar);

        ImageView backArrow=(ImageView) findViewById(R.id.imageArrowNavigationSettings);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//First if on mAddressOuput is to check if it was created through mapDialog; if not, it is created now through GeoManager
//in the latter, mAddressOutput is created for the first time, or user already saw ShowTourTimeLine
        if (mAddressOutput == null || TextUtils.equals(mAddressOutput,"")) {
            Log.d("miotag","mAddressOutput is null");
            GeoManager geo=new GeoManager();
            geo=(GeoManager) getApplicationContext();
            geo.createClient();
            if (geo.isGpsOn()){
                Log.d("miotag","ho lanciato LookupSerivce");
                geo.connectClient();

            } else {
                Toast.makeText(context,"Accendere GPS alta precisione", Toast.LENGTH_SHORT).show();
            }

            //TODO qui devo lanciare GeoManager, non dopo

        } else {
            Log.d("miotag", "mAddressOutput vale:" + mAddressOutput);
            txtWhere.setText(mAddressOutput);

        }

        circledPicker=(CircledPicker)findViewById(R.id.circledPicker);
        circledPicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d("miotag","L'azione che sto compiendo è: "+event.getAction()+"\n");

                if (event.getAction()==0){
                    return true;
                } else if (event.getAction()==2){
                    Log.d("miotag", "posso salvare questo valore: "+circledPicker.getValue()*60);
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
                    Toast.makeText(context, "Completare correttamente tutti i campi", Toast.LENGTH_SHORT).show();
                } else {



                    startActivity(new Intent(getBaseContext(), ShowTourTimeLineActivity.class));
               finish();

              }
            }})
            ;


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

;


                GeoManager geo=new GeoManager();
                geo=(GeoManager) getApplicationContext();
                Log.d("miotag","applicationContext: OK");
                geo.createClient();
                if (geo.isGpsOn()) {
                    Log.d("miotag","GPS acceso!");
                    geo.connectClient();


//Activate tap to launch fragment for new start location

                    FragmentManager fm = getSupportFragmentManager();
                    MapDialogFragment hf=new MapDialogFragment();
                    hf.show(fm, "map_fragment");
                }
                else {
                    Toast.makeText(context,"Accendere GPS alta precisione",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "Acquisizione dato",Toast.LENGTH_SHORT).show();
            }
        });






    }//fine onCreate

    @Override
    public void onStop(){
        super.onStop();
        Log.d("miotag","OnStop");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("miotag","OnPause");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("miotag","onResume");
        GeoManager geo=new GeoManager();
        geo=(GeoManager) getApplicationContext();
     Log.d("miotag","applicationContext: OK");
        geo.createClient();
        if (geo.isGpsOn()) {

            geo.connectClient();
        }
        else {
            Toast.makeText(context,"Accendere GPS alta precisione",Toast.LENGTH_SHORT).show();
        }

        if (mAddressOutput!= null){
            txtWhere.setText(mAddressOutput);
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
                txtWhen.setText("Lunedì, "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;
            case Calendar.TUESDAY:
                txtWhen.setText("Martedì, "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.WEDNESDAY:
                txtWhen.setText("Mercoledì, "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;
            case Calendar.THURSDAY:
                txtWhen.setText("Giovedì, "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.FRIDAY:
                txtWhen.setText("Venerdì, "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.SATURDAY:
                txtWhen.setText("Sabato, "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;

            case Calendar.SUNDAY:
                txtWhen.setText("Domenica, "+dayWeekMonth+" "+(monthNames[monthOfYear+1])+" "+year);

                break;
        }

        Gson gson = new Gson();
        type = new TypeToken<Calendar>() {}.getType();
        String json = gson.toJson(calendarToSave);
        Repository.save(this, Constants.WHEN_SAVE, json);


        calendarToSave.clear();



    }


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
          Toast.makeText(this,"Prossimamente!",Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }



}//fine classe

