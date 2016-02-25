package org.wepush.open_tour;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import org.wepush.open_tour.fragments_dialogs.MapDialogFragment;
import org.wepush.open_tour.fragments_dialogs.NoGpsDialog;
import org.wepush.open_tour.fragments_dialogs.NoPackageFragment;
import org.wepush.open_tour.fragments_dialogs.OutOfBoundsDialog;
import org.wepush.open_tour.fragments_dialogs.SettingTutorialFragment;
import org.wepush.open_tour.fragments_dialogs.WhatFragment;
import org.wepush.open_tour.services.LookUpIntentService;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


/**
 * Created by Antonio on 13/04/2015.
 */
public class SettingTourActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    public static Context context;

    private TextView txtWhen,txtChangeNetwork;
    public static TextView txtWhatToSee,txtHow;
    private static TextView txtWhere;
    private String[] monthNames;
    private String[] dayNames;
    private CircledPicker circledPicker;
    private TextView tSetTime,txtDrawerNetwork,txtManagePackage;
    private Menu menu;
    private Type type;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private android.support.design.widget.FloatingActionButton fabButton;
    private ImageView imgCircleChangeNetworkMenu,imgCircleCredits,imgCircleChangeCity,imgCircleDiscoveryMode,imgIconChangeNetwork,imgCircleExplorerMode,imgCircleHome;
    private LinearLayout llBlur,llClickWhen,llClickWhere,llClickHow,llClickWhat;
    private FrameLayout flParent;
    private RelativeLayout mRevealView;
    boolean hidden = true;
    private int cx,cy,radius;
    private SupportAnimator animator_reverse;
    private boolean mapNotFound=false;
    private boolean backCloseCircularMenu=false;
    public static boolean customPositionIsSet=false;
    private LocationManager serviceGPS;
    private ImageView btnCircularOptions;

//    public static ArrayList<Site> museums,churches,palaces,villas;
    private  File dirOsmDroid,dirMACOSX,dirMilanImages,dirPalermoImages, dirTurinImages;



    //for lookup purpouse (online mode only)
    public static GoogleApiClient mGoogleApiClient;
    private  static String mAddressOutput,actualMode;
    public SettingTourActivity.AddressResultReceiver mResultReceiver;

    String city;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("miotag","onCreate");

        city=Repository.retrieve(this, Constants.KEY_CURRENT_CITY,String.class);

        dirOsmDroid = new File(Environment.getExternalStorageDirectory().getPath() + "/osmdroid");
        dirMACOSX = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/_MACOSX");
        dirMilanImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_MILAN_DOWNLOAD);
        dirPalermoImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_PALERMO_DOWNLOAD);
        dirTurinImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_TURIN_DOWNLOAD);

        if (Repository.retrieve(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class).equals(Constants.DOWNLOADING_IMAGES_ONLY)){
            mapNotFound=true;
        }

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
        tSetTime=(TextView)findViewById(R.id.txtSetTime);

        txtChangeNetwork=(TextView)findViewById(R.id.textChangeNetwork);
        actualMode= Repository.retrieve(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, String.class);

        imgCircleChangeNetworkMenu=(ImageView) findViewById(R.id.imageViewRevealIconChangeNetwork);
        imgCircleChangeCity=(ImageView)findViewById(R.id.imageViewRevealIconChangeCity);
        imgCircleDiscoveryMode=(ImageView)findViewById(R.id.imageViewRevealIconDiscoveryMode);
        imgCircleHome=(ImageView)findViewById(R.id.imageViewRevealIconHome);
        imgCircleExplorerMode=(ImageView) findViewById(R.id.imageViewRevealIconExplorer);
        imgIconChangeNetwork=(ImageView)findViewById(R.id.imageViewRevealIconChangeNetwork);

        imgCircleCredits=(ImageView)findViewById(R.id.imageViewRevealIconCredits);


        toolbar=(Toolbar) findViewById(R.id.toolbarSettingTour);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);


        //animation Menu
        //TODO 12 Settembre

        mRevealView = (RelativeLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.INVISIBLE);


        //set the right label for imgCircleChangeNetworkMenu
        setActualNetworkTextInRevealMenu();

        btnCircularOptions=(ImageView) findViewById(R.id.imgMenuFromToolbar);
        btnCircularOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        flParent=(FrameLayout)findViewById(R.id.frameLayoutParent);

        llBlur=(LinearLayout)findViewById(R.id.llToBlur);
        cx=0;
        cy = mRevealView.getTop();
        radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                    SupportAnimator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(800);

                    animator_reverse = animator.reverse();

                    if (hidden) {
                        mRevealView.setVisibility(View.VISIBLE);
                        animator.start();
                        llBlur.setBackgroundColor(getResources().getColor(R.color.white));
                        llBlur.setAlpha(0.5f);
                        flParent.setBackgroundColor(getResources().getColor(R.color.black));

                        touchModeWithCircularMenu(false);
                        hidden = false;
                    } else {
                        animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                mRevealView.setVisibility(View.INVISIBLE);
                                hidden = true;

                            }

                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationRepeat() {

                            }
                        });
                        animator_reverse.start();
                        llBlur.setAlpha(1f);
                        flParent.setBackgroundColor(getResources().getColor(R.color.white));
                        touchModeWithCircularMenu(true);
                    }
                } else {        // <=LOLLIPOP
                    if (hidden) {
                        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                        mRevealView.setVisibility(View.VISIBLE);
                        anim.start();
                        llBlur.setAlpha(0.5f);
                        llBlur.setBackgroundColor(getResources().getColor(R.color.white));
                        flParent.setBackgroundColor(getResources().getColor(R.color.black));
                        touchModeWithCircularMenu(false);
                        hidden = false;

                    } else {
                        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mRevealView.setVisibility(View.INVISIBLE);
                                hidden = true;
                            }
                        });
                        anim.start();
                        llBlur.setAlpha(1f);
                        flParent.setBackgroundColor(getResources().getColor(R.color.white));
                        touchModeWithCircularMenu(true);
                    }
                }





            }
        });

      //                     WHAT
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


                if (event.getAction() == 0) {
                    return true;
                } else if (event.getAction() == 2) {
                    Repository.save(context, Constants.TIME_TO_SPEND, String.valueOf(circledPicker.getValue() * 60));
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

        //Instantiate actual data/time

        Calendar now=Calendar.getInstance();
        Calendar calendarTemp = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
        showCurrentDate(calendarTemp);
        showCurrentTime(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE));

         fabButton=(android.support.design.widget.FloatingActionButton)findViewById(R.id.floatButton);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (backCloseCircularMenu){
                    btnCircularOptions.performClick();
                    backCloseCircularMenu=false;
                }

                else if (isAnySettingVoid()) {
                    Toast.makeText(context, R.string.complete_all_fields, Toast.LENGTH_SHORT).show();
                } else {

                    if (!(serviceGPS.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

                        showAppropriateDialog(Constants.SHOW_NO_GPS_DIALOG);
                    } else {

                        if (isInMapBounds()) {
                            customPositionIsSet = false;
                            if (checkIfPackagesArePresent()) {
                                startActivity(new Intent(getBaseContext(), ShowTourTimeLineActivity.class));
                                finish();
                            } else {
//                                            HomeActivity.destroyTourPreferences(getBaseContext());
                                showAppropriateDialog(Constants.SHOW_NO_PACKAGES_DIALOG);
                            }
                        } else {

                            showAppropriateDialog(Constants.SHOW_OUT_OF_BOUNDS_DIALOG);
                        }

                    }

                }
            }
        });


        llClickWhen = (LinearLayout)findViewById(R.id.whenLayout);
        llClickWhere = (LinearLayout)findViewById(R.id.whereLayout);
        llClickWhat = (LinearLayout)findViewById(R.id.whatLayout);
        llClickHow = (LinearLayout)findViewById(R.id.howLayout);

        tSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (backCloseCircularMenu) {
                    btnCircularOptions.performClick();
                    backCloseCircularMenu = false;
                } else {


                    Calendar now = Calendar.getInstance();
                    TimePickerDialog dpd = TimePickerDialog.newInstance(
                            SettingTourActivity.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    );
                    dpd.show(getFragmentManager(), "Timepickerdialog");

                }
            }
        });


        llClickWhen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (backCloseCircularMenu){
                    btnCircularOptions.performClick();
                    backCloseCircularMenu=false;
                }

                else {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            SettingTourActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)

                    );

                    dpd.setMinDate(now);//this instruction let calendar to exclude past dates
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }

            }

        });

        llClickHow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (backCloseCircularMenu){
                    btnCircularOptions.performClick();
                    backCloseCircularMenu=false;
                }

                else {

                    showAppropriateDialog(Constants.SHOW_HOW_DIALOG);
                }
            }
        });

        llClickWhat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (backCloseCircularMenu){
                    btnCircularOptions.performClick();
                    backCloseCircularMenu=false;
                }

                else {

                    showAppropriateDialog(Constants.SHOW_WHAT_DIALOG);
                }
            }
        });

        llClickWhere.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if (backCloseCircularMenu){
                    btnCircularOptions.performClick();
                    backCloseCircularMenu=false;
                }

                else

                if (serviceGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

//                    Log.d("miotag","servizio GPS on");


                        if (!(TextUtils.equals(Repository.retrieve(getBaseContext(),Constants.LATITUDE_STARTING_POINT,String.class),"")))
                            {
                                //TODO 02ottobre saltata verifica
//                                if (isInMapBounds()) {
//                                    showAppropriateDialog(Constants.SHOW_WHERE_DIALOG);
//                                } else {
//                                    showAppropriateDialog(Constants.SHOW_OUT_OF_BOUNDS_DIALOG);
//                                }
                                //TODO fine
                                showAppropriateDialog(Constants.SHOW_WHERE_DIALOG);
                            } else {

                                      showAppropriateDialog(Constants.SHOW_NO_GPS_DIALOG);
                                }
                }
                else //if GPS is not ON
                 {
                    Toast.makeText(context, R.string.turn_gps_on,Toast.LENGTH_SHORT).show();

                }
            }
        });


        imgCircleDiscoveryMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isDiscoverySafe()) {
                        startActivity(new Intent(getBaseContext(), DiscoveryPreviewPagerActivity.class));
                        finish();
                    } else {
                        showAppropriateDialog(Constants.SHOW_NO_PACKAGES_DIALOG);
                    }
                }
            });


        imgCircleChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getBaseContext(), ChooseCityActivity.class);
                i.putExtra("fromSettingTourActivity", true);

                startActivity(i);
                finish();
            }
        });

        imgCircleChangeNetworkMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                    SupportAnimator animator =
                            ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(800);

                    animator_reverse = animator.reverse();



                    if (TextUtils.equals(txtChangeNetwork.getText(),getResources().getString(R.string.onlineToActivate))) {
                    Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_ON);

                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {

                        }

                        @Override
                        public void onAnimationEnd() {
                            mRevealView.setVisibility(View.INVISIBLE);
                            hidden = true;

                        }

                        @Override
                        public void onAnimationCancel() {

                        }

                        @Override
                        public void onAnimationRepeat() {

                        }
                    });
                    animator_reverse.start();
                    llBlur.setAlpha(1f);
                    touchModeWithCircularMenu(true);
                    Toast.makeText(getBaseContext(), R.string.internetOnLineOn, Toast.LENGTH_SHORT).show();

                } else {
                    Repository.save(getBaseContext(),Constants.ACTIVATE_ONLINE_CONNECTION,Constants.ONLINE_CONNECTION_OFF);
                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {

                        }

                        @Override
                        public void onAnimationEnd() {
                            mRevealView.setVisibility(View.INVISIBLE);
                            hidden = true;

                        }

                        @Override
                        public void onAnimationCancel() {

                        }

                        @Override
                        public void onAnimationRepeat() {

                        }
                    });
                    animator_reverse.start();
                    llBlur.setAlpha(1f);
                    touchModeWithCircularMenu(true);
                    Toast.makeText(getBaseContext(),R.string.internetOnLineOff,Toast.LENGTH_SHORT).show();

                }

            } //versione > Lollipop

            else {
                    Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mRevealView.setVisibility(View.INVISIBLE);
                            hidden = true;
                        }
                    });
                    anim.start();
                    llBlur.setAlpha(1f);
                    touchModeWithCircularMenu(true);

                    if (TextUtils.equals(txtChangeNetwork.getText(), getResources().getString(R.string.onlineToActivate))) {
//                        Log.d("miotag", "online connection: ON");
                        Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_ON);
                        txtChangeNetwork.setText(R.string.offlineToActivate);
                        imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_offline));
                        Toast.makeText(getBaseContext(), R.string.internetOnLineOn, Toast.LENGTH_SHORT).show();
                    } else {
//                        Log.d("miotag", "online connection: ON");
                        Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_OFF);
                        txtChangeNetwork.setText(R.string.onlineToActivate);
                        imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_online));
                        Toast.makeText(getBaseContext(), R.string.internetOnLineOff, Toast.LENGTH_SHORT).show();
                    }
                }


        }});



//        imgCircleHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(),R.string.soon, Toast.LENGTH_SHORT).show();
//            }
//        });

        imgCircleExplorerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ExplorerActivity.class));
                finish();
            }
        });

        imgCircleCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),R.string.soon, Toast.LENGTH_SHORT).show();
            }
        });


        HomeActivity.initAllaArraysForDiscovery(city);



    }//fine onCreate



    @Override
    public void onResume(){
        super.onResume();
        Log.d("miotag","onResume");

        if (serviceGPS!= null) {
            if (!(serviceGPS.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

                Toast.makeText(context, R.string.turn_gps_on, Toast.LENGTH_SHORT).show();
            }
        }

//       mGoogleApiClient.connect();
        acquiringUserPosition();

        //TODO disabled for 13-11 release
        // section for alertdialog-tutorial
        boolean tutorialNotSeen=Repository.retrieve(this, Constants.TUTORIAL_SETTING_ACTIVITY_NOT_SEEN,Boolean.class);
//        boolean tutorialNotSeen=true;
        if (tutorialNotSeen) {

            //decommentare per riattivare

            showAppropriateDialog(Constants.SHOW_TUTORIAL_SETTING_DIALOG);
            Repository.save(this, Constants.TUTORIAL_SETTING_ACTIVITY_NOT_SEEN, false);
        }

        //TODO fine

//check to show/hide functions based on the presence/absence of images/map packages
        if (mapNotFound){
            imgCircleChangeNetworkMenu.setClickable(false);
            imgCircleChangeNetworkMenu.setAlpha(0.5f);
        }

        if (!(isMapPresent()) && TextUtils.equals(txtChangeNetwork.getText(),getResources().getString(R.string.offlineToActivate))){
            imgCircleExplorerMode.setClickable(false);
            imgCircleExplorerMode.setAlpha(0.5f);
            //this is the case when map ISNOT present, AND connection is OFFLINE
        }

        if (!(arePicturesPresent())){
            imgCircleDiscoveryMode.setClickable(false);
            imgCircleDiscoveryMode.setAlpha(0.5f);
        }


    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("miotag", "onStop");
//        mGoogleApiClient.disconnect();
        LocationServices.FusedLocationApi.flushLocations(mGoogleApiClient);
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,)

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

    }

    public void onConnectionFailed(ConnectionResult connResult){
        Toast.makeText(this, R.string.connectionFailed, Toast.LENGTH_LONG).show();
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

            showAppropriateDialog(Constants.SHOW_NO_GPS_DIALOG);
        }
            if (actualUserPosition!=null) {


                switch (Repository.retrieve(this,Constants.KEY_CURRENT_CITY,String.class)){

                    case Constants.CITY_MILAN:

                        if ( ((actualUserPosition.getLongitude() < Constants.MILAN_NORTH_EAST.getLongitude()) &&
                                (actualUserPosition.getLongitude() > Constants.MILAN_NORTH_WEST.getLongitude()) &&
                                (actualUserPosition.getLatitude() > Constants.MILAN_SOUTH_EAST.getLatitude()) &&
                                (actualUserPosition.getLatitude() < Constants.MILAN_NORTH_EAST.getLatitude())))
                        {
                            return true;
                        } else {
                            return false;
                        }


                    case Constants.CITY_PALERMO:

                        if ((actualUserPosition.getLongitude() < Constants.PALERMO_NORTH_EAST.getLongitude()) &&
                                        (actualUserPosition.getLongitude() > Constants.PALERMO_NORTH_WEST.getLongitude()) &&
                                        (actualUserPosition.getLatitude() > Constants.PALERMO_SOUTH_EAST.getLatitude()) &&
                                        (actualUserPosition.getLatitude() < Constants.PALERMO_NORTH_EAST.getLatitude()))
                        {
                            return true;
                        }
                         else {
                            return false;
                        }

                    case Constants.CITY_TURIN:

                        if ((actualUserPosition.getLongitude() < Constants.TURIN_NORTH_EAST.getLongitude()) &&
                                (actualUserPosition.getLongitude() > Constants.TURIN_NORTH_WEST.getLongitude()) &&
                                (actualUserPosition.getLatitude() > Constants.TURIN_SOUTH_EAST.getLatitude()) &&
                                (actualUserPosition.getLatitude() < Constants.TURIN_NORTH_EAST.getLatitude()))
                        {
                            return true;
                        }
                        else {
                            return false;
                        }


                }


//                if (
//
//                        ((actualUserPosition.getLongitude() < Constants.MILAN_NORTH_EAST.getLongitude()) &&
//                                (actualUserPosition.getLongitude() > Constants.MILAN_NORTH_WEST.getLongitude()) &&
//                                (actualUserPosition.getLatitude() > Constants.MILAN_SOUTH_EAST.getLatitude()) &&
//                                (actualUserPosition.getLatitude() < Constants.MILAN_NORTH_EAST.getLatitude()))
//                    ||
//
//                                ((actualUserPosition.getLongitude() < Constants.PALERMO_NORTH_EAST.getLongitude()) &&
//                                        (actualUserPosition.getLongitude() > Constants.PALERMO_NORTH_WEST.getLongitude()) &&
//                                        (actualUserPosition.getLatitude() > Constants.PALERMO_SOUTH_EAST.getLatitude()) &&
//                                        (actualUserPosition.getLatitude() < Constants.PALERMO_NORTH_EAST.getLatitude()))
//
//
//
//
//                        ) {
//                    return true;
//                } else {
//                    return false;
//                }
            } else {

                showAppropriateDialog(Constants.SHOW_NO_GPS_DIALOG);
            }
        return false;
    }

    private void establishGoogleConnection(){
        if (mGoogleApiClient.isConnected())
        {
            acquiringUserPosition();
        } else {
            mGoogleApiClient.connect();
        }

    }

    private void acquiringUserPosition(){
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null){
            //this is need to be done only if LATITUDE_STARTING_POINT /LONGITUDE_STARTING_POINT aren't set

                if (!(customPositionIsSet)){
                double mLastLocationLatitude = mLastLocation.getLatitude();
                double mLastLocationLongitude = mLastLocation.getLongitude();

                    if (
                            (Repository.retrieve(this,Constants.ACTIVATE_ONLINE_CONNECTION,String.class).equals(Constants.ONLINE_CONNECTION_ON)) ||
                                    ( Repository.retrieve(this,Constants.ACTIVATE_ONLINE_CONNECTION,String.class).equals("")) ||
                                    (Repository.retrieve(this,Constants.ACTIVATE_ONLINE_CONNECTION,String.class) == null)
                            ){
                        Intent i=new Intent(this, LookUpIntentService.class);
                        mResultReceiver=new SettingTourActivity.AddressResultReceiver(new Handler());
                        i.putExtra(Constants.RECEIVER,mResultReceiver);
                        i.putExtra(Constants.LOCATION_DATA_EXTRA,mLastLocation);
                        startService(i);
                    }


                Repository.save(this, Constants.LATITUDE_STARTING_POINT, String.valueOf(mLastLocationLatitude));
                Repository.save(this, Constants.LONGITUDE_STARTING_POINT, String.valueOf(mLastLocationLongitude));
            }



        }

    }

    private void showAppropriateDialog(String s){

        switch(s){

            case Constants.SHOW_NO_GPS_DIALOG:
                FragmentManager fmNoGps = getSupportFragmentManager();
                NoGpsDialog hfNoGps = new NoGpsDialog();
                hfNoGps.show(fmNoGps, "nogps_dialog");
                break;

            case Constants.SHOW_OUT_OF_BOUNDS_DIALOG:
                FragmentManager fm = getSupportFragmentManager();
                OutOfBoundsDialog hf = new OutOfBoundsDialog();
                hf.show(fm,"out_ofBonds");
                break;

            case Constants.SHOW_WHERE_DIALOG:
                FragmentManager fmMap = getSupportFragmentManager();
                MapDialogFragment hfMap = new MapDialogFragment();
                hfMap.show(fmMap, "map_fragment");
                break;

            case Constants.SHOW_WHAT_DIALOG:
                FragmentManager fmWhat = getSupportFragmentManager();
                WhatFragment wfWhat=new WhatFragment();
                wfWhat.show(fmWhat,"what_fragment");
                break;

            case Constants.SHOW_HOW_DIALOG:
                FragmentManager fmHow = getSupportFragmentManager();
                HowFragment hfHow=new HowFragment();
                hfHow.show(fmHow,"how_fragment");
                break;

            case Constants.SHOW_NO_PACKAGES_DIALOG:
                FragmentManager fmMissingPackages=getSupportFragmentManager();
                NoPackageFragment nPackage=new NoPackageFragment();
                nPackage.show(fmMissingPackages,"nopackage_fragment");

            case Constants.SHOW_TUTORIAL_SETTING_DIALOG:
                FragmentManager fmSettingTutorial=getSupportFragmentManager();
                SettingTutorialFragment sTutorial=new SettingTutorialFragment();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                sTutorial.show(fmSettingTutorial,"setting_tutorial_fragment");
            break;

        }

    }




    private void showCurrentDate(Calendar calendar){

       int result = calendar.get(Calendar.DAY_OF_WEEK);
        String nameDay="";

        switch (result) {
            case Calendar.MONDAY:
                    nameDay="Lunedì";
                break;
            case Calendar.TUESDAY:
                nameDay="Martedì";
                break;

            case Calendar.WEDNESDAY:
                nameDay="Mercoledì";
                break;
            case Calendar.THURSDAY:
                nameDay="Giovedì";
                break;

            case Calendar.FRIDAY:
                nameDay="Venerdì";
                break;

            case Calendar.SATURDAY:
                nameDay="Sabato";
                break;

            case Calendar.SUNDAY:
                nameDay="Domenica";

                break;
        }

        txtWhen.setText(nameDay+", "+calendar.get(Calendar.DAY_OF_MONTH) + " " + (monthNames[calendar.get(Calendar.MONTH) + 1]) + " " + calendar.get(Calendar.YEAR));

        Gson gson = new Gson();
        type = new TypeToken<Calendar>() {}.getType();
        String json = gson.toJson(calendar);
        Repository.save(this, Constants.WHEN_SAVE, json);
        calendar.clear();




    }

    private void showCurrentTime(int hourOfDay,int minute) {

        Calendar calendarTime = Calendar.getInstance(Locale.getDefault());

        calendarTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendarTime.set(Calendar.MINUTE,minute);

        String mins, hours;
        if (minute < 10) {
            mins = "0" + minute;
        } else {
            mins = String.valueOf(minute);
        }

        if (hourOfDay < 10) {
            hours = "0" + hourOfDay;
        } else {
            hours = String.valueOf(hourOfDay);
        }
        String time = hours + ":" + mins;

        tSetTime.setText(time);

        Gson gson = new Gson();
        String json = gson.toJson(calendarTime);
        Repository.save(this, Constants.TIME_TO_START, json);

    }


    //receiver waiting for lookup intent to identify user current address (ONLINE mode only)

    public static class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Repository.save(context,Constants.WHERE_SAVE,String.class);

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                // 09/05
                txtWhere.setText(mAddressOutput);

            }

        }
    }


    private boolean checkIfPackagesArePresent() {



        String packagePreferences = Repository.retrieve(this, Constants.WHAT_I_WANT_TO_DOWNLOAD, String.class);


        switch (packagePreferences) {

            case Constants.DOWNLOADING_MAPS_IMAGES:


                switch(city){
                        case Constants.CITY_PALERMO:
                            if ((!(dirOsmDroid.isDirectory())) || (!(dirPalermoImages.isDirectory()))) {
                                return false;
                                } else {
                                    return true;
                                }

                    case Constants.CITY_MILAN:

                        if ((!(dirOsmDroid.isDirectory())) || (!(dirMilanImages.isDirectory()))) {
                             return false;
                            } else {
                                return true;
                            }


                    case Constants.CITY_TURIN:

                        if ((!(dirOsmDroid.isDirectory())) || (!(dirTurinImages.isDirectory()))) {
                            return false;
                        } else {
                            return true;
                        }



                }


            case Constants.DOWNLOADING_IMAGES_ONLY:




                switch(city){

                    case Constants.CITY_PALERMO:

                        if (!(dirPalermoImages.isDirectory())) {
                                    return false;
                                } else {
                                    return true;
                            }

                    case Constants.CITY_MILAN:
                        if (!(dirMilanImages.isDirectory())) {
                            return false;
                                } else {
                                return true;
                            }



                    case Constants.CITY_TURIN:

                        if (!(dirTurinImages.isDirectory())) {
                            return false;
                        } else {
                            return true;
                        }



                }

            case Constants.DOWNLOADING_MAPS_ONLY:


                if (!(dirOsmDroid.isDirectory())) {
                    return false;
                } else {

                    return true;

                }


            default:
                return false;

        }
    }



    private void touchModeWithCircularMenu(boolean flag){
        if (flag){
            llClickHow.setClickable(true);
            llClickWhat.setClickable(true);
            llClickWhere.setClickable(true);
            llClickWhen.setClickable(true);
            tSetTime.setClickable(true);
            fabButton.setClickable(true);
            backCloseCircularMenu=false;

        } else {

//            llClickHow.setClickable(false);
//            llClickWhat.setClickable(false);
//            llClickWhere.setClickable(false);
//            llClickWhen.setClickable(false);
//            tSetTime.setClickable(false);
//            fabButton.setClickable(false);

            backCloseCircularMenu=true;

        }

    }

    private void setActualNetworkTextInRevealMenu(){

        String actualMode= Repository.retrieve(this, Constants.ACTIVATE_ONLINE_CONNECTION, String.class);
        if (TextUtils.equals(actualMode,Constants.ONLINE_CONNECTION_OFF))
        {
            txtChangeNetwork.setText(R.string.onlineToActivate);
            imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_online));
        } else

        {
            txtChangeNetwork.setText(R.string.offlineToActivate);
            imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_offline));


        }

    }


private boolean isDiscoverySafe() {
    File dirMilanImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_MILAN_DOWNLOAD);
    File dirPalermoImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_PALERMO_DOWNLOAD);
    File dirTurinImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_TURIN_DOWNLOAD);

    switch (city) {

        case Constants.CITY_MILAN:
            if (!(dirMilanImages.isDirectory())) {
                return false;
            } else {
                return true;
            }


        case Constants.CITY_PALERMO:
            if (!(dirPalermoImages.isDirectory())) {
                return false;
            } else {
                return true;
            }


        case Constants.CITY_TURIN:
            if (!(dirTurinImages.isDirectory())) {
                return false;
            } else {
                return true;
            }

    }
    return false;
}

    private boolean isMapPresent(){

        if (dirOsmDroid.isDirectory()){
            return true;
        } else {
            return false;
        }

    }

    private boolean arePicturesPresent(){
        boolean flag=false;
        switch (city){
            case Constants.CITY_MILAN:
                if (dirMilanImages.isDirectory()){
                    flag= true;
                } else {
                    flag= false;
                }
         break;

            case Constants.CITY_PALERMO:
                if (dirPalermoImages.isDirectory()){
                    flag= true;
                } else {
                    flag= false;
                }
            break;

            case Constants.CITY_TURIN:
                if (dirTurinImages.isDirectory()){
                    flag= true;
                } else {
                    flag= false;
                }

        }

        return flag;
    }

    @Override
    public void onBackPressed(){
        if (backCloseCircularMenu){
            btnCircularOptions.performClick();
            backCloseCircularMenu=false;
        }else {
           finish();
        }

    }



}


