package org.wepush.open_tour;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.DiscoveryPreviewPagerAdapter;
import org.wepush.open_tour.utils.Repository;

import java.util.ArrayList;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by antoniocoppola on 05/10/15.
 */
public class DiscoveryPreviewPagerActivity extends AppCompatActivity implements View.OnClickListener{

    private DiscoveryPreviewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private int positionTab;

    public static ArrayList<Site>dummy;
    private ImageView backArrow;
    private RelativeLayout mRevealView;
    private TextView txtChangeNetwork;

    private boolean hidden = true;
    private boolean swipeAndTap=true;
    private int cx,cy,radius;
    private SupportAnimator animator_reverse;
    private ImageView imgIconChangeNetwork,imgIconHomeDiscovery,imgIconChangeCity,imgIconExplorer;
    private ImageView imgCircularToolbarMenu;
    private RelativeLayout rlDiscovery;
    private Toolbar toolbar;
    private boolean backCloseCircularMenu;





    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discoverypreviewpager_activity);


        txtChangeNetwork=(TextView)findViewById(R.id.textChangeNetworkDiscovery);
        imgIconChangeNetwork=(ImageView)findViewById(R.id.imageViewRevealIconChangeNetworkDiscovery);
        imgIconHomeDiscovery=(ImageView)findViewById(R.id.imageViewRevealIconHomeDiscovery);
        imgIconChangeCity=(ImageView) findViewById(R.id.imageViewRevealIconChangeCityDiscovery);
        imgIconExplorer=(ImageView) findViewById(R.id.imageViewRevealIconExplorerDiscovery);
        rlDiscovery=(RelativeLayout)findViewById(R.id.rlDiscovery);




        mRevealView = (RelativeLayout) findViewById(R.id.revealDiscovery);
        mRevealView.setVisibility(View.INVISIBLE);


        //set the right label for imgCircleChangeNetworkMenu
        setActualNetworkTextInRevealMenu();
        String titleCity= "";
        titleCity=Repository.retrieve(getBaseContext(),Constants.KEY_CURRENT_CITY,String.class);

        toolbar = (Toolbar) findViewById(R.id.toolDiscoveryPreviewPager);
        TextView txtTitleBar=(TextView) findViewById(R.id.txtTitleToolbarDiscoveryPreviewPager);

        //TORINO 10/02
//        if (titleCity.equals(Constants.CITY_MILAN)){
//            titleCity=getResources().getString(R.string.milan);
//        } else {
//            titleCity=getResources().getString(R.string.palermo);
//        }

        switch(titleCity){

            case Constants.CITY_MILAN:
                titleCity=getResources().getString(R.string.milan);
            break;

            case Constants.CITY_TURIN:
                titleCity=getResources().getString(R.string.turin);
                break;

            case Constants.CITY_PALERMO:
                titleCity=getResources().getString(R.string.palermo);
                break;


        }

        setSupportActionBar(toolbar);
        txtTitleBar.setText(titleCity);


        imgCircularToolbarMenu=(ImageView) findViewById(R.id.imgMenuFromToolbarDiscovery);
        imgCircularToolbarMenu.setOnClickListener(this);
        imgIconHomeDiscovery.setOnClickListener(this);
        imgIconChangeCity.setOnClickListener(this);
        imgIconExplorer.setOnClickListener(this);
        imgIconChangeNetwork.setOnClickListener(this);


        viewPager=(ViewPager) findViewById(R.id.viewPagerDiscoveryPreviewPagerAdapter);
        pagerAdapter= new DiscoveryPreviewPagerAdapter(getSupportFragmentManager(),getBaseContext());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionTab = position;
//               activateImageListeners();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    } //fine onCreate



    private void setActualNetworkTextInRevealMenu(){

        String actualMode= Repository.retrieve(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, String.class);
        if (TextUtils.equals(actualMode, Constants.ONLINE_CONNECTION_OFF))
        {
            txtChangeNetwork.setText(R.string.onlineToActivate);
            imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_online));
        } else

        {
            txtChangeNetwork.setText(R.string.offlineToActivate);
            imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_offline));


        }

    }


    @Override
    public void onClick(View v){
        switch(v.getId()){

            case R.id.imageViewRevealIconChangeNetworkDiscovery:

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
                        viewPager.setAlpha(1f);
//                        touchModeWithCircularMenu(true);
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
                        viewPager.setAlpha(1f);
//                        touchModeWithCircularMenu(true);
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
                    viewPager.setAlpha(1f);
//                    touchModeWithCircularMenu(true);

                    if (TextUtils.equals(txtChangeNetwork.getText(), getResources().getString(R.string.onlineToActivate))) {
                        Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_ON);
                        txtChangeNetwork.setText(R.string.offlineToActivate);
                        imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_offline));
                        Toast.makeText(getBaseContext(), R.string.internetOnLineOn, Toast.LENGTH_SHORT).show();
                    } else {
                        Repository.save(getBaseContext(), Constants.ACTIVATE_ONLINE_CONNECTION, Constants.ONLINE_CONNECTION_OFF);
                        txtChangeNetwork.setText(R.string.onlineToActivate);
                        imgIconChangeNetwork.setBackground(getResources().getDrawable(R.mipmap.ic_online));
                        Toast.makeText(getBaseContext(), R.string.internetOnLineOff, Toast.LENGTH_SHORT).show();
                    }
                }
            break;


            case R.id.viewPagerDiscoveryPreviewPagerAdapter:
                break;

            case R.id.imageViewRevealIconExplorerDiscovery:
                startActivity(new Intent(getBaseContext(),ExplorerActivity.class));
                finish();
                break;

            case R.id.imageViewRevealIconHomeDiscovery:
                startActivity(new Intent(getBaseContext(),SettingTourActivity.class));
                finish();
                break;


            case R.id.imageViewRevealIconChangeCityDiscovery:

                Intent i=new Intent(getBaseContext(), ChooseCityActivity.class);
                i.putExtra("fromSettingTourActivity", true);
                startActivity(i);
                finish();
                break;

            case R.id.imgMenuFromToolbarDiscovery:

//                enableViewPager(false);

                cx = 0;
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
                        viewPager.setAlpha(0.5f);
                        rlDiscovery.setBackgroundColor(getResources().getColor(R.color.black));
                        hidden = false;
                        backCloseCircularMenu=true;

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
                        viewPager.setAlpha(1f);
                        rlDiscovery.setBackgroundColor(getResources().getColor(R.color.white));
                        backCloseCircularMenu=false;
//                        enableViewPager(true);
                    }
                } else {
                    if (hidden) {
                        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                        mRevealView.setVisibility(View.VISIBLE);
                        anim.start();
                        viewPager.setAlpha(0.5f);
                        rlDiscovery.setBackgroundColor(getResources().getColor(R.color.black));
                        hidden = false;
                        backCloseCircularMenu=true;

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
                        viewPager.setAlpha(1f);
                        rlDiscovery.setBackgroundColor(getResources().getColor(R.color.white));
                        backCloseCircularMenu=false;

                    }
                }
            break;







        } //end switch

    } // end onClick


    @Override
    public void onBackPressed() {
        if (backCloseCircularMenu) {
            imgCircularToolbarMenu.performClick();
            backCloseCircularMenu = false;
        } else {
            HomeActivity.destroyTourPreferences(getBaseContext());
            startActivity(new Intent(getBaseContext(), SettingTourActivity.class));
            finish();
        }
    }

//    private void enableViewPager(boolean f){
//
//        if (!f) {
//            viewPager.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    imgCircularToolbarMenu.performClick();
//                    backCloseCircularMenu = false;
//                }
//            });
//        } else {
//            viewPager.setOnClickListener(null);
//        }
//
//
//    }


}



