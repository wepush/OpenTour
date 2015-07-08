package org.wultimaproject.db2.fragments_dialogs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.wultimaproject.db2.LiveMapActivity;
import org.wultimaproject.db2.R;


/**
 * Created by antoniocoppola on 07/07/15.
 */
public class LivePagerFragment extends Fragment {

//    public static final String KEY_STRING_TITLE_LIVE="org.wepush.opentour.string.livemap.title";
//    public static final String KEY_STRING_ADDRESS_LIVE="org.wepush.opentour.string.livemap.address";
//    public static final String KEY_STRING_TYPE_LIVE="org.wepush.opentour.string.livemap.address";
//
//    private static String titleDetails;
//    private static String addressDetails;
//    private static String typeOfSite;

//
//    public static LivePagerFragment newInstance(String title, String address,String type){
//        LivePagerFragment fragment = new LivePagerFragment();
//        fragment.
//        titleDetails=title;
//        addressDetails=address;
//        typeOfSite=type;
//        return fragment;
//    }



//    public static LivePagerFragment newInstance(String title, String address,String type){
//
//        final Bundle bundle = new Bundle();
//        final LivePagerFragment fragment = new LivePagerFragment();
//
//        bundle.putString(KEY_STRING_TITLE_LIVE, title);
//        bundle.putString(KEY_STRING_ADDRESS_LIVE, address);
//        bundle.putString(KEY_STRING_TYPE_LIVE, type);
//
//        fragment.setArguments(bundle);


//        return fragment;
//
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){

       Log.d("miotag","nuovo fragment");
        View view=inflater.inflate(R.layout.fragment_livepager,null);

//        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_livepager,null);
//
        final TextView bodyView=(TextView) view.findViewById(R.id.liveAddressSite);
        final TextView titleView = (TextView) view.findViewById(R.id.liveTitleSite);
//
        final Bundle args=getArguments();
//
//        titleView.setText(getResources().getString(args.getInt(KEY_STRING_TITLE_LIVE)));
//        bodyView.setText(getResources().getString(args.getInt(KEY_STRING_ADDRESS_LIVE)));

        titleView.setText(args.getString("title"));
        bodyView.setText(args.getString("description"));
        Log.d("miotag","tipo di sito: "+args.getString("type"));


        return view;
    }







}//fine classe
