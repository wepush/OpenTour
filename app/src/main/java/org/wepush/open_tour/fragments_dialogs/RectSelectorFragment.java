package org.wepush.open_tour.fragments_dialogs;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.wepush.open_tour.R;


/**
 * Created by Antonio on 13/04/2015.
 */
public class RectSelectorFragment extends Fragment {

    private ViewGroup viewGroup;
    private LinearLayout ll0,ll1,ll2,ll3;
    private RelativeLayout rlFloating;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rect_selector, null);

        ll0 = (LinearLayout) view.findViewById(R.id.whenLayout);
        ll1 = (LinearLayout) view.findViewById(R.id.whereLayout);
        ll2 = (LinearLayout) view.findViewById(R.id.whatLayout);
        ll3 = (LinearLayout) view.findViewById(R.id.howLayout);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewGroup = (ViewGroup) view.findViewById(R.id.linearLayoutSectorToSlide);


//handler to animate setting records
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    TransitionManager.beginDelayedTransition(viewGroup, new Slide());
                    showLayouts(ll0, ll1, ll2, ll3);
                }
            }, 700);





        } else {
            showLayouts(ll0,ll1,ll2,ll3);
        }





        return view;
    }

    private void showLayouts(LinearLayout... llViews){
        for (LinearLayout llView: llViews){
            if (llView.getVisibility()==View.VISIBLE){
                llView.setVisibility(View.INVISIBLE);
            } else {

                llView.setVisibility(View.VISIBLE);
            }

        }
    }

    private void showFloat(RelativeLayout rl){
        if (rl.getVisibility()==View.VISIBLE){
            rl.setVisibility(View.INVISIBLE);
        } else {
            rl.setVisibility(View.VISIBLE);
        }
    }


}
