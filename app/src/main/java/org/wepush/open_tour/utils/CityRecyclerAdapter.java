package org.wepush.open_tour.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.wepush.open_tour.ChooseCityActivity;
import org.wepush.open_tour.CityShowcaserActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;

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
//        img=(ImageView) view.findViewById(R.id.imgShowcaseChooseCity);
//        img.setOnClickListener(this);
//        final ImageView imageToTransition=(ImageView)view.findViewById(R.id.imgShowcaseChooseCity);
//
////        final Activity activity=new ChooseCityActivity();
//
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("miotag", "position: " + result.getAdapterPosition());
                Intent intent = new Intent(context,CityShowcaserActivity.class);

                String actualCity=cities.get(result.getPosition());

                switch(actualCity){
                    case Constants.CITY_MILAN:
                        intent.putExtra("city",Constants.CITY_MILAN);
                        break;
                    case Constants.CITY_PALERMO:
                        intent.putExtra("city",Constants.CITY_PALERMO);
                        break;
                }


                intent.putExtra("city",actualCity);
                if (Build.VERSION.SDK_INT >= 21) {
                    activity.getWindow().setExitTransition(new Explode());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
//                    activity.finish();

//                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                } else {
                    context.startActivity(intent);
//                    activity.finish();


                }
            }
        });
        return result;






//        return RecyclerViewHolderCityChooser.newInstance(view, context, cities);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holderCityChooser=(RecyclerViewHolderCityChooser) holder;
////        holderCityChooser.onClick(img);
        holderCityChooser.setShowcaseImage(cities.get(position));




    }


    @Override
    public int getItemCount() {
        return cities.size();
    }



//    @Override
//    public void onClick(View v){
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            // Call some material design APIs here
//
//            // Implement this feature without material design
////            int actualPosition=result.getLayoutPosition();
//            int actualPosition=result.getPosition();
//
//            Log.d("miotag","result position:"+actualPosition);
//            String actualCity=cities.get(actualPosition);
//            intent.putExtra("city",actualCity);
//            activity.getWindow().setExitTransition(new Explode());
//            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
////            context.startActivity(new Intent(context,CityShowcaserActivity.class));
////            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, (View) img, transitionName);
////            ActivityCompat.startActivity(activity, intent, options.toBundle());
//        } else {
//            context.startActivity(new Intent (context, CityShowcaserActivity.class));
//        }
//
//    }

//    @Override
//    public void onClick(View v) {
//        intent.putExtra("city",cities.get(holderCityChooser.getAdapterPosition()));
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            activity.getWindow().setExitTransition(new Explode());
//            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
//        } else {
//            //TODO < Lollipop
//        }
//    }

}

