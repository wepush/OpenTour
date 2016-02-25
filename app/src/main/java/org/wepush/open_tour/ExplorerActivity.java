package org.wepush.open_tour;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.wepush.open_tour.fragments_dialogs.ExplorerTutorialFragment;
import org.wepush.open_tour.fragments_dialogs.NoPackageFragment;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by antoniocoppola on 05/11/15.
 */
public class ExplorerActivity extends AppCompatActivity{


    private final static int ZOOM=17;
    private MapView map;
    private IMapController mapController;
    private MapEventsOverlay overlayNoEventos;
    private MapEventsReceiver mapNoEventsReceiver;
    private org.osmdroid.bonuspack.overlays.Marker mark;
    private ArrayList<Site> arrayAllLocations;
    private String city;
    private Snackbar snackbar;
    private CoordinatorLayout cl;
    private View snackbarLayout;
    private DB1SqlHelper dbHelper;
    private Intent intent;
    private ArrayList<Site> popularSiteToChooseFrom;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private int rubs;
    private TextView number;
    private Site siteFromShaking;
    private String pinName;
    private String language;
    private boolean backCloseCircularMenu=false;


    private ImageView imgCircleDiscoveryModeExplorer,imgCircleChangeCityExplorer,imgCircleChangeNetworkMenuExplorer,imgCircleCreditsExplorer,imgCircleHomeExplorer;


    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {

            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 11) {
//               Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG).show();
                siteFromShaking=shakingOutSite(popularSiteToChooseFrom);
                moveMapToShakedSite(siteFromShaking);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    //reveal

    private RelativeLayout mRevealViewExplorer;
    int cx,cy,radius;
    boolean hidden;
    private SupportAnimator animator_reverse;
    private TextView txtChangeNetworkExplorer;
    private ImageView imgIconChangeNetwork,hamburgerMenuExplorer;
    private RelativeLayout rlBlurr;
    private boolean letTouch;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explorer_activity);

        dbHelper=DB1SqlHelper.getInstance(this);
        intent=new Intent(this, DetailsActivity.class);


       language=Locale.getDefault().getDisplayLanguage();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarExplorerActivity);
        cl=(CoordinatorLayout)findViewById(R.id.coordinatorLayoutExplorer);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        city=Repository.retrieve(this, Constants.KEY_CURRENT_CITY,String.class);

        imgCircleDiscoveryModeExplorer=(ImageView)findViewById(R.id.imageViewRevealIconDiscoveryModeExplorer);
        imgCircleChangeCityExplorer=(ImageView)findViewById(R.id.imageViewRevealIconChangeCityExplorer);
        imgCircleChangeNetworkMenuExplorer=(ImageView)findViewById(R.id.imageViewRevealIconChangeNetworkExplorer);
        imgCircleCreditsExplorer=(ImageView) findViewById(R.id.imageViewRevealIconCreditsExplorer);
        imgCircleHomeExplorer=(ImageView)findViewById(R.id.imageViewRevealIconHomeExplorer);

        rlBlurr=(RelativeLayout) findViewById(R.id.rlBlurrExplorer);
        hamburgerMenuExplorer=(ImageView) findViewById(R.id.hamburgerNavigationExplorer);

        hamburgerMenuExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRevalMenu();
            }
        });


        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
            language="english";
        } else {
            language="italian";
        }

            //TODO inizializzare array da db, completare i metodi mancanti


        arrayAllLocations= DB1SqlHelper.getInstance(this).getSites();
        popularSiteToChooseFrom=randomicPopularSite(arrayAllLocations);
        showDiscoveryMap();
        pinPointAllLocations();

        ImageView btnRefreshMap=(ImageView) findViewById(R.id.btnRefreshMap);
        btnRefreshMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscoveryMap();
            }
        });

        //reveal

        mRevealViewExplorer=(RelativeLayout)findViewById(R.id.revealItemsExplorerActivity);
        mRevealViewExplorer.setVisibility(View.INVISIBLE);
        imgIconChangeNetwork=(ImageView)findViewById(R.id.imageViewRevealIconChangeNetworkExplorer);


        txtChangeNetworkExplorer=(TextView)findViewById(R.id.textChangeNetworkExplorer);



        hidden=true;
        letTouch=true;
        cx=0;
        cy = mRevealViewExplorer.getTop();
        radius = Math.max(mRevealViewExplorer.getWidth(), mRevealViewExplorer.getHeight());

        setActualNetworkTextInRevealMenu();



        imgCircleDiscoveryModeExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDiscoverySafe()) {
                    startActivity(new Intent(getBaseContext(), DiscoveryPreviewPagerActivity.class));
                    finish();
                } else {
                    FragmentManager fmMissingPackages=getSupportFragmentManager();
                    NoPackageFragment nPackage=new NoPackageFragment();
                    nPackage.show(fmMissingPackages, "nopackage_fragment");
                }
            }
        });


        imgCircleChangeCityExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getBaseContext(), ChooseCityActivity.class);
                i.putExtra("fromSettingTourActivity", true);

                startActivity(i);
                finish();
            }
        });

        imgCircleChangeNetworkMenuExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                    SupportAnimator animator =
                            ViewAnimationUtils.createCircularReveal(mRevealViewExplorer, cx, cy, 0, radius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(800);

                    animator_reverse = animator.reverse();



                    if (TextUtils.equals(txtChangeNetworkExplorer.getText(),getResources().getString(R.string.onlineToActivate))) {
                        Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_ON);

                        animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                mRevealViewExplorer.setVisibility(View.INVISIBLE);
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
                        rlBlurr.setAlpha(1f);
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
                                mRevealViewExplorer.setVisibility(View.INVISIBLE);
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
                        rlBlurr.setAlpha(1f);
                        touchModeWithCircularMenu(true);
                        Toast.makeText(getBaseContext(),R.string.internetOnLineOff,Toast.LENGTH_SHORT).show();

                    }

                } //versione > Lollipop

                else {
                    Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealViewExplorer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mRevealViewExplorer.setVisibility(View.INVISIBLE);
                            hidden = true;
                        }
                    });
                    anim.start();
                    rlBlurr.setAlpha(1f);
                    touchModeWithCircularMenu(true);

                    if (TextUtils.equals(txtChangeNetworkExplorer.getText(), getResources().getString(R.string.onlineToActivate))) {
                        Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_ON);
                        txtChangeNetworkExplorer.setText(R.string.offlineToActivate);
                        imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_offline));
                        Toast.makeText(getBaseContext(), R.string.internetOnLineOn, Toast.LENGTH_SHORT).show();
                    } else {
                        Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_OFF);
                        txtChangeNetworkExplorer.setText(R.string.onlineToActivate);
                        imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_online));
                        Toast.makeText(getBaseContext(), R.string.internetOnLineOff, Toast.LENGTH_SHORT).show();
                    }
                }


            }});

        imgCircleCreditsExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), R.string.soon, Toast.LENGTH_SHORT).show();
            }
        });

        imgCircleHomeExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
                finish();
            }
        });




    }//fine onCreate

    @Override
    public void onResume(){
        super.onResume();
        boolean tutorialNotSeen=Repository.retrieve(this, Constants.TUTORIAL_EXPLORER_ACTIVITY_NOT_SEEN,Boolean.class);
//    boolean tutorialNotSeen=true;
        if (tutorialNotSeen) {

            FragmentManager fmExplorerTutorial=getSupportFragmentManager();
            ExplorerTutorialFragment sExplorer=new ExplorerTutorialFragment();
            sExplorer.show(fmExplorerTutorial,"setting_tutorial_fragment");

            Repository.save(this, Constants.TUTORIAL_EXPLORER_ACTIVITY_NOT_SEEN, false);
        }

    }





    private void showDiscoveryMap() {

        map = (org.osmdroid.views.MapView) findViewById(R.id.mapLive);
        map.getTileProvider().clearTileCache();


        if (TextUtils.equals(Repository.retrieve(this, Constants.ACTIVATE_ONLINE_CONNECTION, String.class), Constants.ONLINE_CONNECTION_OFF)) {
            map.setTileSource(new XYTileSource("MapQuest",
                    ResourceProxy.string.mapquest_osm, 17, 17, 300, ".jpg", new String[]{
                    "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
            map.setBuiltInZoomControls(false);
            map.setMultiTouchControls(false);
            map.setUseDataConnection(false);
        } else {
            map.setBuiltInZoomControls(false);
            map.setMultiTouchControls(true);
            map.setUseDataConnection(true);
        }


        mapController = map.getController();
        mapController.setZoom(ZOOM);


        switch (city){
            case Constants.CITY_MILAN:
                mapController.setCenter(new GeoPoint(45.465086, 9.188110));
                break;

            case Constants.CITY_PALERMO:
                mapController.setCenter(new GeoPoint(38.116560, 13.363621));
                break;

            case Constants.CITY_TURIN:
                mapController.setCenter(new GeoPoint(45.066943, 7.665698));
                break;
        }
        mapNoEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                return false;
            }
        };

        overlayNoEventos = new MapEventsOverlay(this, mapNoEventsReceiver);
        map.getOverlays().add(overlayNoEventos);
    }

    private void pinPointAllLocations(){

        for (Site site: arrayAllLocations) {

            GeoPoint gp = new GeoPoint(site.latitude, site.longitude);
            mark = new org.osmdroid.bonuspack.overlays.Marker(map);

            mark.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {

                    if (letTouch) {
                        initSnackBar(marker.getTitle());
                    }
                    return false;
                }
            });
            mark.setPosition(gp);

            if (language.equals("english")){
                convertLanguageTypeOfSite(site);
            } else {
                chooseThemeColors(site);
            }
            mark.setIcon(getResources().getDrawable(getResources().getIdentifier(pinName, "drawable", this.getPackageName())));
//            mark.setIcon(getResources().getDrawable(R.drawable.pin_blue));
            mark.setTitle(site.name);
            map.getOverlays().add(mark);
            map.invalidate();

        }

    }

    @Override
    public void onBackPressed() {

        if (backCloseCircularMenu) {
            hamburgerMenuExplorer.performClick();
            backCloseCircularMenu = false;
        } else {
            backCloseCircularMenu=true;
            HomeActivity.destroyTourPreferences(getBaseContext());
            startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
            finish();
        }
    }



    private void initSnackBar(String s){
        final String ss=s;
        snackbar = Snackbar.make(cl, s, Snackbar.LENGTH_LONG)
                .setAction(R.string.textActionSnackbar, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("siteId",findMarkerId(ss));
                        startActivity(intent);
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
        snackbarLayout=snackbar.getView();
        TextView tv = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getResources().getColor(R.color.white));
        snackbarLayout.setBackgroundColor(getResources().getColor(R.color.blue400));
        snackbar.show();
    }

    private String findMarkerId(String s){
        String idToReturn="";
        for (Site site: arrayAllLocations){
            if (site.name.equals(s)){
                idToReturn=site.id;
            }
        }

        return idToReturn;
    }


    private ArrayList<Site> randomicPopularSite(ArrayList<Site> arrayList){

        ArrayList<Site> arrayToReturn=new ArrayList<>();

        for (Site site: arrayList){
            if (site.priority==2){
                arrayToReturn.add(site);
            }
        }

        return arrayToReturn;
    }

    private Site shakingOutSite(ArrayList<Site> sites){

        Random r = new Random();
        int position = r.nextInt(sites.size()-1 - 0);
        return sites.get(position);


    }

    private void moveMapToShakedSite(Site site){

        final String name=site.name;
        GeoPoint center=new GeoPoint(site.latitude,site.longitude);
        org.osmdroid.bonuspack.overlays.Marker mark = new org.osmdroid.bonuspack.overlays.Marker(map);
        mapController.setCenter(new GeoPoint(center));

        mark.setPosition(center);
        mark.setIcon(getResources().getDrawable(R.drawable.pin_red));
        mark.setTitle(site.name);
        map.getOverlays().add(mark);
        map.invalidate();

        snackbar = Snackbar.make(cl, name, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.textActionSnackbar, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("siteId",findMarkerId(name));
                        startActivity(intent);
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
        snackbarLayout=snackbar.getView();
        TextView tv = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getResources().getColor(R.color.white));
        snackbarLayout.setBackgroundColor(getResources().getColor(R.color.blue400));
        snackbar.show();
    }

    private void showRevalMenu(){
        cx=0;
        cy = mRevealViewExplorer.getTop();
        radius = Math.max(mRevealViewExplorer.getWidth(), mRevealViewExplorer.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(mRevealViewExplorer, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(800);

            animator_reverse = animator.reverse();

            if (hidden) {
                mRevealViewExplorer.setVisibility(View.VISIBLE);
                animator.start();
                rlBlurr.setAlpha(0.5f);
                cl.setBackgroundColor(getResources().getColor(R.color.black));

                touchModeWithCircularMenu(false);
                backCloseCircularMenu = true;
                hidden = false;
            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        mRevealViewExplorer.setVisibility(View.INVISIBLE);
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
                rlBlurr.setAlpha(1f);
                cl.setBackgroundColor(getResources().getColor(R.color.white));
                touchModeWithCircularMenu(true);
                backCloseCircularMenu = false;



            }
        } else {
            if (hidden) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealViewExplorer, cx, cy, 0, radius);
                mRevealViewExplorer.setVisibility(View.VISIBLE);
                anim.start();
                rlBlurr.setAlpha(0.5f);
                cl.setBackgroundColor(getResources().getColor(R.color.black));
                touchModeWithCircularMenu(false);
//                        mRevealView.setElevation(25f);
                hidden = false;
                backCloseCircularMenu = true;

            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealViewExplorer, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealViewExplorer.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }
                });
                anim.start();
                rlBlurr.setAlpha(1f);
                touchModeWithCircularMenu(true);
                backCloseCircularMenu = false;
            }
        }
    }

    private void setActualNetworkTextInRevealMenu(){

        String actualMode= Repository.retrieve(this, Constants.ACTIVATE_ONLINE_CONNECTION, String.class);
        if (TextUtils.equals(actualMode,Constants.ONLINE_CONNECTION_OFF))
        {
            txtChangeNetworkExplorer.setText(R.string.onlineToActivate);
            imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_online));
        } else

        {
            txtChangeNetworkExplorer.setText(R.string.offlineToActivate);
            imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_offline));


        }

    }

    private boolean isDiscoverySafe(){
        File dirMilanImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_MILAN_DOWNLOAD);
        File dirPalermoImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_PALERMO_DOWNLOAD);
        File dirTurinImages = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + Constants.UNZIPPED_IMAGES_TURIN_DOWNLOAD);

        String city=Repository.retrieve(this, Constants.KEY_CURRENT_CITY,String.class);
        switch (city){

            case Constants.CITY_MILAN:
                if (!(dirMilanImages.isDirectory())) {
//                    Log.d("miotag","controllo pacchetti per immagini di milano: NON presenti");
                    return false;
                } else {
//                    Log.d("miotag","controllo pacchetti per immagini di milano:presenti");
                    return true;
                }


            case Constants.CITY_PALERMO:
                if (!(dirPalermoImages.isDirectory())) {
//                    Log.d("miotag","controllo pacchetti per immagini di Palermo: NON presenti");
                    return false;
                } else {
//                    Log.d("miotag","controllo pacchetti per immagini di Palermo: presenti");
                    return true;
                }

            case Constants.CITY_TURIN:
                if (!(dirTurinImages.isDirectory())) {
//                    Log.d("miotag","controllo pacchetti per immagini di Palermo: NON presenti");
                    return false;
                } else {
//                    Log.d("miotag","controllo pacchetti per immagini di Palermo: presenti");
                    return true;
                }

        }
        return false;
    }


    private void touchModeWithCircularMenu(boolean flag){
        if (flag){

            letTouch=true;

        } else {

            letTouch=false;
        }

    }


    private void convertLanguageTypeOfSite(Site site) {

        switch (site.typeOfSite) {
            case "Theaters":

                pinName="ic_teatri";

                break;

            case "Palaces and Castles":

                pinName="ic_palazzi_castelli";

                break;

            case "Villas, Gardens and Parks":

                pinName="ic_ville_giardini";

                break;

            case "Museums and Art galleries":

                pinName="ic_musei";

                break;

            case "Statues and Fountains":

                pinName="ic_statue";

                break;

            case "Squares and Streets":

                pinName="ic_piazze";


                break;

            case "Arches, Gates and Walls":

                pinName="ic_archi";

                break;

            case "Fairs and Markets":

                pinName="ic_mercati";

                break;

            case "Cemeteries and Memorials":

                pinName="ic_cimiteri";
                break;

            case "Buildings":

                pinName="ic_edifici";

                break;

            case "Bridges":

                pinName="ic_ponti";

                break;

            case "Churches, Oratories and Places of worship":

                pinName="ic_chiese";

                break;

            case "Other monuments and Places of interest":

                pinName="ic_altri";

                break;

        }


    }

    private void chooseThemeColors(Site site) {

        switch (site.typeOfSite) {

            case "Teatri":
                pinName="ic_teatri";
                break;

            case "Palazzi e Castelli":
                pinName="ic_palazzi_castelli";

                break;

            case "Ville, Giardini e Parchi":
                pinName="ic_ville_giardini";

                break;

            case "Musei e Gallerie d'arte":
                pinName="ic_musei";

                break;

            case "Statue e Fontane":
                pinName="ic_statue";

                break;

            case "Piazze e Strade":
                pinName="ic_piazze";


                break;

            case "Archi, Porte e Mura":
                pinName="ic_archi";

                break;

            case "Fiere e Mercati":
                pinName="ic_mercati";

                break;

            case "Cimiteri e Memoriali":
                pinName="ic_cimiteri";

                break;


            case "Edifici":
                pinName="ic_edifici";

                break;

            case "Ponti":
                pinName="ic_ponti";

                break;

            case "Chiese, Oratori e Luoghi di culto":
                pinName="ic_chiese";

                break;

            case "Altri monumenti e Luoghi di interesse":
                pinName="ic_altri";
                break;

        }

    }
}