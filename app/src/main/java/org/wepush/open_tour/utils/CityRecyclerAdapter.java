package org.wepush.open_tour.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.wepush.open_tour.CityShowcaserActivity;
import org.wepush.open_tour.R;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 14/10/15.
 */
public class CityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<String> cities;
    private RecyclerViewHolderCityChooser holderCityChooser;
    private ImageView img;
    private Intent intent;
    private String transitionName;
    private Activity activity;



    public CityRecyclerAdapter(Context ctx,ArrayList<String> citiesInShowcase) {
        context=ctx;
        intent = new Intent(ctx,CityShowcaserActivity.class);
        transitionName = ctx.getString(R.string.transition_album_cover);
        cities=citiesInShowcase;
        activity=(Activity)context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_citychooser, parent, false);
       final RecyclerViewHolderCityChooser result=RecyclerViewHolderCityChooser.newInstance(view, context, cities);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,CityShowcaserActivity.class);

                String actualCity=cities.get(result.getPosition());

                switch(actualCity){
                    case Constants.CITY_MILAN:
                        intent.putExtra("city",Constants.CITY_MILAN);
                        break;
                    case Constants.CITY_PALERMO:
                        intent.putExtra("city",Constants.CITY_PALERMO);
                        break;


                    //TORINO 10/02
                    case Constants.CITY_TURIN:
                        intent.putExtra("city",Constants.CITY_TURIN);
                }


                intent.putExtra("city",actualCity);
                if (Build.VERSION.SDK_INT >= 21) {
                    activity.getWindow().setExitTransition(new Explode());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });
        return result;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holderCityChooser=(RecyclerViewHolderCityChooser) holder;
        holderCityChooser.setShowcaseImage(cities.get(position));
    }


    @Override
    public int getItemCount() {
        return cities.size();
    }

}

