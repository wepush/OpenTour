package org.wultimaproject.db2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import org.wultimaproject.db2.fragments_dialogs.CityFragment;
import org.wultimaproject.db2.services.ReadFromJson;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.utils.Repository;


public class CityChooserActivity extends AppCompatActivity {

    int mCityIndex;
    private CityPagerAdapter mPagerAdapter;
    private ViewPager viewPager;
    private ImageView cityImage;

    public static final int FIRST_PAGE = 0;

    public static final int SECOND_PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HomeActivity.destroyTourPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_chooser);



        viewPager = (ViewPager) findViewById(R.id.view_pager_activity_city_chooser);
        mPagerAdapter=new CityPagerAdapter(getSupportFragmentManager());




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.VilleDark));

        }

        viewPager.setAdapter(mPagerAdapter);
        Button button = (Button) findViewById(R.id.btnSkipCityChooser);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCityIndex=viewPager.getCurrentItem();
                if (mCityIndex==0){
                    Log.d("miotag","city saved: MILANO");
                   Repository.save(getApplicationContext(), Constants.KEY_CURRENT_CITY, "milano");
                    startService(new Intent(getBaseContext(), ReadFromJson.class));
                    startActivity(new Intent(getBaseContext(), SplashActivityTimeLine.class));
                    finish();
                } else if (
                        mCityIndex==1
                        ){
                    Log.d("miotag", "city saved: PALERMO");
                    Repository.save(getApplicationContext(),Constants.KEY_CURRENT_CITY,"palermo");
                    Toast.makeText(getBaseContext(),"Soon",Toast.LENGTH_SHORT).show();
                }




            }
        });
    }



    private class CityPagerAdapter extends FragmentStatePagerAdapter {

        public CityPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {


                switch (position) {
                    case FIRST_PAGE:
                        return CityFragment.newInstance("Milano");


                    case SECOND_PAGE:
                        return CityFragment.newInstance("Palermo");

                }
            return new CityFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }
}
