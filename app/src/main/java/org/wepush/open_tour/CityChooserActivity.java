//package org.wepush.open_tour;
//
//import android.app.DownloadManager;
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//
//import org.wepush.open_tour.fragments_dialogs.CityFragment;
//import org.wepush.open_tour.services.CityPackagesBroadcastReceiver;
//import org.wepush.open_tour.services.DownloadingService;
//import org.wepush.open_tour.services.ReadFromJson;
//import org.wepush.open_tour.services.UnzipService;
//import org.wepush.open_tour.utils.Constants;
//import org.wepush.open_tour.utils.Repository;
//
//import java.io.File;
//
//
//public class CityChooserActivity extends AppCompatActivity {
//
//    //TODO DEPRECATED
//
//
//    int mCityIndex;
//    private CityPagerAdapter mPagerAdapter;
//    private ViewPager viewPager;
////    private ImageView cityImage;
//
////    private DownloadManager dm;
////    private BroadcastReceiver receiver;
////    private IntentFilter intentFilter;
//
//
//    public static final int FIRST_PAGE = 0;
//
//    public static final int SECOND_PAGE = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        HomeActivity.destroyTourPreferences(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_city_chooser);
//
//        deleteMapsAndImages();
//
////        receiver=new CityPackagesBroadcastReceiver();
//
//
//
//        viewPager = (ViewPager) findViewById(R.id.view_pager_activity_city_chooser);
//        mPagerAdapter=new CityPagerAdapter(getSupportFragmentManager());
//
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.VilleDark));
//
//        }
//
//        viewPager.setAdapter(mPagerAdapter);
//        Button button = (Button) findViewById(R.id.btnSkipCityChooser);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                mCityIndex=viewPager.getCurrentItem();
//                if (mCityIndex==0){
//                    Log.d("miotag","il db da installare è MILANO");
//
//
////                    if(Constants.KEY_CURRENT_CITY.equals(Constants.CITY_PALERMO)){
////                        deleteMapsAndImages(Constants.CITY_PALERMO);
////                    } else {
////                        deleteMapsAndImages(Constants.CITY_MILAN);
////                    }
//                    Repository.save(getApplicationContext(), Constants.KEY_CURRENT_CITY, Constants.CITY_MILAN);
//                    startService(new Intent(getBaseContext(), ReadFromJson.class));
//                    startActivity(new Intent(getBaseContext(), LoadingCityBundle.class));
//                    Intent i=new Intent(getBaseContext(), DownloadingService.class);
//                    i.putExtra("city",Constants.CITY_MILAN);
//                    i.putExtra("whatToDownload","map");
//                    startService(i);
//                    finish();
//
//                } else if (
//                        mCityIndex==1
//                        ){
//                    Log.d("miotag","il db da installare è PALERMO");
//
//
//                    //TODO 23settembre
//                    Repository.save(getApplicationContext(), Constants.KEY_CURRENT_CITY, Constants.CITY_PALERMO);
//                    startService(new Intent(getBaseContext(), ReadFromJson.class));
//                    startActivity(new Intent(getBaseContext(), LoadingCityBundle.class));
//                    Intent i=new Intent(getBaseContext(), DownloadingService.class);
//                    i.putExtra("city",Constants.CITY_PALERMO);
//                    i.putExtra("whatToDownload", "map");
//                    startService(i);
//                    finish();
//                }
//
//
//
//
//            }
//        });
//    }
//
//
//
//    private class CityPagerAdapter extends FragmentStatePagerAdapter {
//
//        public CityPagerAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//
//                switch (position) {
//                    case FIRST_PAGE:
//                        return CityFragment.newInstance("Milano");
//
//
//                    case SECOND_PAGE:
//                        return CityFragment.newInstance("Palermo");
//
//                }
//            return new CityFragment();
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }
//
//
//
//
//    @Override
//    public void onResume(){
//        super.onResume();
////        intentFilter=new IntentFilter();
////        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
////        intentFilter.addAction(Constants.UNZIP_DONE);
////
//////        registerReceiver(receiver, new IntentFilter(
//////                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
////
////        registerReceiver(receiver, intentFilter);
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
////        unregisterReceiver(receiver);
//    }
//
//
//    private void deleteMapsAndImages(){
//
//
//        File dirOsmDroid = new File(Environment.getExternalStorageDirectory().getPath()+"/osmdroid");
//        if (dirOsmDroid.isDirectory())
//        {
//
//            String[] children = dirOsmDroid.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dirOsmDroid, children[i]).delete();
//            }
//        }
//
//        File dirMACOSX=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/_MACOSX");
//        if (dirMACOSX.isDirectory())
//        {
//
//            String[] children = dirMACOSX.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dirMACOSX, children[i]).delete();
//            }
//        }
//
//        File dirMilanImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.UNZIPPED_IMAGES_MILAN_DOWNLOAD);
//            if (dirMilanImages.isDirectory())
//            {
//                String[] children = dirMilanImages.list();
//                for (int i = 0; i < children.length; i++)
//                {
//                    new File(dirMilanImages, children[i]).delete();
//                }
//            }
//
//
//        File dirPalermoImages=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.UNZIPPED_IMAGES_PALERMO_DOWNLOAD);
//
//        if (dirPalermoImages.isDirectory())
//        {
//            String[] children = dirPalermoImages.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dirPalermoImages, children[i]).delete();
//            }
//        }
//
//
//
//
//        File milanImagesZip=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.ZIPPED_IMAGES_MILANO_DOWNLOAD);
//        File palermoImagesZip=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+Constants.ZIPPED_IMAGES_PALERMO_DOWNLOAD);
//
//        dirMACOSX.delete();
//        dirOsmDroid.delete();
//        dirMilanImages.delete();
//        dirPalermoImages.delete();
//        milanImagesZip.delete();
//        palermoImagesZip.delete();
//    }
//
//
////    private void deleteMapsAndImages(String s){
////
////        File dirOsmDroid = new File(Environment.getExternalStorageDirectory().getPath()+"/osmdroid");
////        String[] children=dirOsmDroid.list();
////
////        for (String fileName: children){
////            if (!(fileName.equals(s+"_map.zip"))){
////                new File(dirOsmDroid.getPath()+fileName).delete();
////            }
////        }
////
////
////
////
////    }
//
//
//}
