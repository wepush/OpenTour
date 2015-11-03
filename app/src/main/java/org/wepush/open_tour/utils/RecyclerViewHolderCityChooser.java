package org.wepush.open_tour.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.wepush.open_tour.CityShowcaserActivity;
import org.wepush.open_tour.R;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 14/10/15.
 */
public class RecyclerViewHolderCityChooser extends RecyclerView.ViewHolder  {

    private static Context context;
    private static ArrayList<String> citiesToShow;
    private static ImageView imgShowcase;
    private String actualCity;
    private static Activity activity;
    private static TextView txtOverlay;
    private static FrameLayout fl;


    public static RecyclerViewHolderCityChooser newInstance (View view, Context ctx, ArrayList<String> cities){
        context=ctx;
        activity=(Activity)context;
        citiesToShow=cities;
        imgShowcase=(ImageView) view.findViewById(R.id.imgShowcaseChooseCity);
//        fl=(FrameLayout)view.findViewById(R.id.flRecyclerCityChooserParent);
        txtOverlay=(TextView) view.findViewById(R.id.textCityOverlay);
//        View v = (View) view.findViewById(R.id.viewRecyclerCityChooser);
//        RelativeLayout rl=(RelativeLayout) view.findViewById(R.id.rlItemRecyclerCityChooser);

//        v.setBackground(Color.parseColor("4d4d4d"));
//        v.setAlpha(0.3f);
////        rl.addView(v);

        return new RecyclerViewHolderCityChooser(view,context);

    }

    public RecyclerViewHolderCityChooser(final View parent, Context ctx){
        super(parent);
//        parent.setOnClickListener(this);
        context=ctx;




    }

    public void setShowcaseImage(String s){
        Log.d("miotag","in Set Show Case: "+s);
        switch (s){
            case Constants.CITY_MILAN:
//                actualCity="milano";
                Log.d("miotag", "sono in milano");
//                Repository.save(context,Constants.KEY_CURRENT_CITY,Constants.CITY_MILAN);
                imgShowcase.setImageDrawable(context.getResources().getDrawable(R.drawable.milano));
                txtOverlay.setText(R.string.milan);
//                fl.removeView(txtOverlay);
//                fl.addView(txtOverlay);
//                imgShowcase.setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case Constants.CITY_PALERMO:
                Log.d("miotag","sono in palermo");
//                actualCity="palermo";
//                Repository.save(context,Constants.KEY_CURRENT_CITY,Constants.CITY_PALERMO);
                imgShowcase.setImageDrawable(context.getResources().getDrawable(R.drawable.palermo2));
                     txtOverlay.setText(R.string.palermo);
//                fl.removeView(txtOverlay);
//                fl.addView(txtOverlay);

                break;
        }

    }



//    @Override
//    public void onClick(View v) {
//        Intent intent=new Intent(context, CityShowcaserActivity.class);
//        actualCity=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);
//        Log.d("miotag","dal viewholder la città scelta è: "+actualCity);
//        intent.putExtra("city", actualCity);
//        if (Build.VERSION.SDK_INT >= 21) {
//            activity.getWindow().setExitTransition(new Explode());
//            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
//        } else {
//
//            context.startActivity(intent);
//        }
//    }







}
