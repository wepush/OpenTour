package org.wepush.open_tour;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.wepush.open_tour.fragments_dialogs.IntroPagerFragment;
import org.wepush.open_tour.fragments_dialogs.LaunchingSettingFragment;
import org.wepush.open_tour.services.ReadFromJson;
import org.wepush.open_tour.structures.Constants;
import org.wepush.open_tour.utils.AnimationHelper;
import org.wepush.open_tour.utils.Repository;


/**
 * Created by Antonio on 10/04/2015.
 */
public class WalkthroughActivity extends AppCompatActivity {

    private static ViewPager introViewPager;
    private View[] mTransitions = new View[5];

    private final static int NUM_PAGES=5;

    public final static int NO_ALPHA=0;

    public static final int FIRST_PAGE = 0;

    public static final int SECOND_PAGE = 1;

    private static final int THIRD_PAGE = 2;

    private static final int FOURTH_PAGE=3;

    private static final int FIFTH_PAGE=4;

    private Button nextButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough_pager_layout);

        Repository.save(this, Constants.WALKTHROUGH_SEEN, "yes");


        final View centerBackgroundView = findViewById(R.id.view_activity_intro_center_background);
        final View leftBackgroundView = findViewById(R.id.view_activity_intro_left_background);
        final View rightBackgroundView = findViewById(R.id.view_activity_intro_right_background);
        final View walkthroughIndicators=findViewById(R.id.layout_activity_walkthrought_transition_indicators);
        centerBackgroundView.setBackgroundResource(R.color.indigo300);
        rightBackgroundView.setBackgroundResource(R.color.green300);

        final Button skipButton = (Button) findViewById(R.id.btnSkipWalkthrough);
//       Button nextButton = (Button) findViewById(R.id.btnNextWalkthrough);


        mTransitions[0] = findViewById(R.id.view_transition_one);
        mTransitions[1] = findViewById(R.id.view_transition_two);
        mTransitions[2] = findViewById(R.id.view_transition_three);
        mTransitions[3] = findViewById(R.id.view_transition_four);
        mTransitions[4] = findViewById(R.id.view_transition_four);

        final int[] resourcesIds = {R.color.indigo300, R.color.green300, R.color.cyan300, R.color.eggplant300, R.color.white};

        ((TransitionDrawable) mTransitions[0].getBackground()).startTransition(0);

        introViewPager=(ViewPager) findViewById(R.id.introPager);
        introViewPager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));



        introViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int mLastPosition = 0;
            int mLastPage = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                float diff = position - mLastPage;

                // moving right
                if (diff == 0) {
                    rightBackgroundView.setAlpha(positionOffset);

                }
                // moving left
                else {
                    leftBackgroundView.setAlpha(1 - positionOffset);
                }


            }

            @Override
            public void onPageSelected(int position) {

                if (position < 5) {

                    if (position == mLastPosition) {
                        AnimationHelper.alphaIn(walkthroughIndicators);
                        AnimationHelper.alphaIn(skipButton);
                    } else {
                        ((TransitionDrawable) mTransitions[position].getBackground()).startTransition(300);
                        ((TransitionDrawable) mTransitions[mLastPosition].getBackground()).reverseTransition(300);
                    }
                    mLastPosition = position;
                } else {
                    AnimationHelper.alphaOut(walkthroughIndicators);
                    AnimationHelper.alphaOut(skipButton);
                    mLastPosition = FIFTH_PAGE;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == 0) {
                    mLastPage = mLastPosition;
                    int lower = Math.max(0, mLastPage - 1);
                    int higher = Math.min(resourcesIds.length - 1, mLastPage + 1);

                    centerBackgroundView.setBackgroundResource(resourcesIds[mLastPage]);
                    rightBackgroundView.setBackgroundResource(resourcesIds[higher]);
                    leftBackgroundView.setBackgroundResource(resourcesIds[lower]);


                    leftBackgroundView.setAlpha(NO_ALPHA);
                    rightBackgroundView.setAlpha(NO_ALPHA);
                }

                if (mLastPosition == FIFTH_PAGE) {
//
//                    if(
//                            !(TextUtils.equals(Repository.retrieve(getBaseContext(), Constants.KEY_CURRENT_CITY, String.class), "milano"))&&
//                                    !(TextUtils.equals(Repository.retrieve(getBaseContext(), Constants.KEY_CURRENT_CITY, String.class),"palermo") )
//                            )
//                    {
//                        startActivity(new Intent(getBaseContext(),CityChooserActivity.class));
//                        finish();
//                    }else {
//                        startService(new Intent(getBaseContext(),ReadFromJson.class));
//                        Intent intent = new Intent(getBaseContext(), SettingTourActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }

    // 10 Luglio 2015
// Since Milan is the only city, at the end of the walkthrough the only thing left to do is launch SplashScreenActivity (that in turns will launch SettingActivity AND ReadJson
                    startService(new Intent(getBaseContext(), ReadFromJson.class));
                    startActivity(new Intent(getBaseContext(), SplashActivityTimeLine.class));
                    finish();


                }


            }
        });


        nextButton =(Button)findViewById(R.id.btnNextWalkthrough);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introViewPager.setCurrentItem(introViewPager.getCurrentItem()+1);
            }
        });

    }//fine onCreate


    @Override
    public void onBackPressed() {
        if (introViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            introViewPager.setCurrentItem(introViewPager.getCurrentItem() - 1);
        }
    }








    public class IntroPagerAdapter extends FragmentPagerAdapter {


        public IntroPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {



            switch (position) {
                case FIRST_PAGE:
                    return IntroPagerFragment
                            .newInstance(R.drawable.primowalk, R.string.firstTitleWalk, R.string.firstBodyWalk);

                case SECOND_PAGE:

                    return IntroPagerFragment.newInstance(R.drawable.secondowalk, R.string.secondTitleWalk, R.string.secondBodyWalk);

                case THIRD_PAGE:

                    return IntroPagerFragment.newInstance(R.drawable.terzowalk,  R.string.thirdTitleWalk, R.string.thirdBodyWalk);

                case FOURTH_PAGE:

                    return IntroPagerFragment.newInstance(R.drawable.quartowalk,R.string.fourthTitleWalk, R.string.fourthBodyWalk);



                default:
                    return new LaunchingSettingFragment();
            }
        }

        @Override
        public int getCount() {
        //  return 4;
            return NUM_PAGES;
        }
    }

    public void onSettingActivity (View v){

//        if(
//                !(TextUtils.equals(Repository.retrieve(this, Constants.KEY_CURRENT_CITY,String.class), "milano"))&&
//                        !(TextUtils.equals(Repository.retrieve(this, Constants.KEY_CURRENT_CITY, String.class),"palermo") )
//                )
//        {
//            startActivity(new Intent(this,CityChooserActivity.class));
//            finish();
//        }else {
//            startService(new Intent(getBaseContext(),ReadFromJson.class));
//            Intent intent = new Intent(this, SettingTourActivity.class);
//            startActivity(intent);
//            finish();
//        }


// 10 Luglio 2015:
//since Milan is the oncly city availabele, etc

        startService(new Intent(getBaseContext(),ReadFromJson.class));
            Intent intent = new Intent(this, SplashActivityTimeLine.class);
        startActivity(intent);
            finish();
    }





}//fine Classe
