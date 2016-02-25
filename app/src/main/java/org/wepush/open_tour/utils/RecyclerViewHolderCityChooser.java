package org.wepush.open_tour.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
        txtOverlay=(TextView) view.findViewById(R.id.textCityOverlay);
        return new RecyclerViewHolderCityChooser(view,context);

    }

    public RecyclerViewHolderCityChooser(final View parent, Context ctx){
        super(parent);
        context=ctx;

    }

    public void setShowcaseImage(String s){
        switch (s){
            case Constants.CITY_MILAN:

                imgShowcase.setImageDrawable(context.getResources().getDrawable(R.drawable.milano));
                txtOverlay.setText(R.string.milan);

                break;

            case Constants.CITY_PALERMO:

                imgShowcase.setImageDrawable(context.getResources().getDrawable(R.drawable.palermo2));
                     txtOverlay.setText(R.string.palermo);
                break;

            case Constants.CITY_TURIN:

                imgShowcase.setImageDrawable(context.getResources().getDrawable(R.drawable.piazza_san_carlo));
                txtOverlay.setText(R.string.turin);
                break;
        }

    }


}
