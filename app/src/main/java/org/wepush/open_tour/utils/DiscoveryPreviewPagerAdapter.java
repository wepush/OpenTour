//package org.wepush.open_tour.utils;
//
//import android.content.Context;
//import android.media.Image;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//
//import org.wepush.open_tour.R;
//import org.wepush.open_tour.fragments_dialogs.DiscoveryPreviewPagerFragment;
//import org.wepush.open_tour.structures.DB1SqlHelper;
//import org.wepush.open_tour.structures.Site;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
///**
// * Created by antoniocoppola on 05/10/15.
// */
//public class DiscoveryPreviewPagerAdapter extends FragmentStatePagerAdapter {
//    public static final int FIRST_PAGE = 0;
//    public static final int SECOND_PAGE = 1;
//    public static final int THIRD_PAGE=2;
//    public static final int FOURTH_PAGE=3;
//
//    private Context context;
//    private Image mainImage,nlImage,nrImage,slImage,srImage;
//    private RecyclerView mRecyclerDiscovery;
//    private String categoryToShow;
//
//    public DiscoveryPreviewPagerAdapter(FragmentManager fragmentManager, Context ctx) {
//        super(fragmentManager);
//        context=ctx;
//
//
//
//
//    }
//
//
//    @Override
//    public Fragment getItem(int position) {
//
//        switch(position){
//
//            case FIRST_PAGE:
////                activateImageListeners(FIRST_PAGE);
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    categoryToShow="Churches, Oratories and Places of worship";
//                } else {
//                    categoryToShow="Chiese, Oratori e Luoghi di culto";
//                }
//
//               return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_CHURCH, FIRST_PAGE, categoryToShow);
//
//            case SECOND_PAGE:
////                activateImageListeners(SECOND_PAGE);
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    categoryToShow="Museums and Art galleries";
//                } else {
//                    categoryToShow="Musei e Gallerie d'arte";
//                }
//                return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_MUSEUM,SECOND_PAGE, categoryToShow);
//
//            case THIRD_PAGE:
////                activateImageListeners(THIRD_PAGE);
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    categoryToShow="Palaces and Castles";
//                } else {
//                    categoryToShow="Palazzi e Castelli";
//                }
//
//                return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_CASTLES,THIRD_PAGE, categoryToShow);
//
//            case FOURTH_PAGE:
////                activateImageListeners(FOURTH_PAGE);
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    categoryToShow="Palaces and Castles";
//                } else {
//                    categoryToShow="Villas, Gardens and Parks";
//                }
//                return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_VILLAS,FOURTH_PAGE, categoryToShow);
//
//
//
//        }
//
//
//        return new DiscoveryPreviewPagerFragment();
//    }
//
//    @Override
//    public int getCount() {
//
//        return 4;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        CharSequence title="";
//        switch(position){
//            case 0:
//                title= context.getResources().getString(R.string.church);
//                break;
//            case 1:
//                title= context.getResources().getString(R.string.museums);
//                break;
//            case 2:
//               title= context.getResources().getString(R.string.palaces_in_tabs);
//                break;
//            case 3:
//                title=context.getResources().getString(R.string.tabParks);
//                break;
//
//        }
//
//
//        return title;
//
////        return "OBJECT " + (position + 1);
//    }
//
//
//
//
//
//
////    private void activateImageListeners(int page){
////
////        switch(page){
////
////            case FIRST_PAGE:
////                break;
////            case SECOND_PAGE:
////                break;
////
////            case THIRD_PAGE:
////                break;
////
////            case FOURTH_PAGE:
////                break;
////
////
////
////
////        }
////
////    }
//}
//
//



package org.wepush.open_tour.utils;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.wepush.open_tour.DiscoveryPreviewPagerActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;
import org.wepush.open_tour.fragments_dialogs.DiscoveryFragment;
import org.wepush.open_tour.fragments_dialogs.DiscoveryPreviewPagerFragment;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 05/10/15.
 */
public class DiscoveryPreviewPagerAdapter extends FragmentStatePagerAdapter {
    public static final int FIRST_PAGE = 0;
    public static final int SECOND_PAGE = 1;
    public static final int THIRD_PAGE=2;
    public static final int FOURTH_PAGE=3;

    private Context context;
    private Image mainImage,nlImage,nrImage,slImage,srImage;
//    private ArrayList<Site>  museums,churches,villas,palaces;

    public DiscoveryPreviewPagerAdapter(FragmentManager fragmentManager, Context ctx){
        super(fragmentManager);
        context=ctx;





    }


    @Override
    public Fragment getItem(int position) {

        switch(position){

            case FIRST_PAGE:
//                activateImageListeners(FIRST_PAGE);

                return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_CHURCH, FIRST_PAGE);

//                return DiscoveryFragment.newInstance(context, SettingTourActivity.churches);

            case SECOND_PAGE:
//                activateImageListeners(SECOND_PAGE);
                return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_MUSEUM,SECOND_PAGE);

//                return DiscoveryFragment.newInstance(context, SettingTourActivity.museums);

            case THIRD_PAGE:
//                activateImageListeners(THIRD_PAGE);
                return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_CASTLES,THIRD_PAGE);
//                return DiscoveryFragment.newInstance(context, SettingTourActivity.palaces);


            case FOURTH_PAGE:
//                activateImageListeners(FOURTH_PAGE);
                return DiscoveryPreviewPagerFragment.newInstance(context,Constants.CATEGORY_VIEW_PAGER_VILLAS,FOURTH_PAGE);
//                return DiscoveryFragment.newInstance(context, SettingTourActivity.villas);



        }



//        return new DiscoveryPreviewPagerFragment();
        return new DiscoveryFragment();
    }

    @Override
    public int getCount() {

        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        CharSequence title="";
        switch(position){
            case 0:
                title= context.getResources().getString(R.string.church);
                break;
            case 1:
                title= context.getResources().getString(R.string.museums);
                break;
            case 2:
                title= context.getResources().getString(R.string.palaces_in_tabs);
                break;
            case 3:
                title=context.getResources().getString(R.string.tabParks);
                break;

        }


        return title;

//        return "OBJECT " + (position + 1);
    }


//    private void activateImageListeners(int page){
//
//        switch(page){
//
//            case FIRST_PAGE:
//                break;
//            case SECOND_PAGE:
//                break;
//
//            case THIRD_PAGE:
//                break;
//
//            case FOURTH_PAGE:
//                break;
//
//
//
//        }
//
//    }
}


