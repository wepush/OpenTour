package org.wepush.open_tour.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.wepush.open_tour.R;
import org.wepush.open_tour.ShowDetailsActivity;
import org.wepush.open_tour.structures.DB1SqlHelper;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    //TODO below txtToSet should be ImageView
    private CircleImageView imageToSet;
    private TextView timeTextToSet;
    private TextView distanceTextToSet;

    private String idToPassFromCLick;
    private static Context context;


    public static RecyclerViewHolder newInstance (View view, Context ctx){
        CircleImageView imageItem=(CircleImageView)view.findViewById(R.id.imgToPut);
        TextView txtTime=(TextView)view.findViewById(R.id.itemTimeToPut);
        TextView txtDistance=(TextView)view.findViewById(R.id.itemDistanceToPut);
        context=ctx;
        return new RecyclerViewHolder(view,imageItem,txtTime,txtDistance);


    }


    public RecyclerViewHolder(final View parent, CircleImageView newImageToSet,TextView newTimeToSet,TextView newDistanceToSet) {
        super(parent);


            imageToSet=newImageToSet;
            timeTextToSet=newTimeToSet;
            distanceTextToSet=newDistanceToSet;


        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, ShowDetailsActivity.class);
                i.putExtra("siteId",idToPassFromCLick);
                i.setAction(Constants.INTENT_FROM_LIVEMAP);
                context.startActivity(i);


            }
        });

    }

    public void setTextInViewHolder(String id,String time,String distance){

        idToPassFromCLick=id;
       String imagePath= DB1SqlHelper.getInstance(context).getPictureSite(id);
        String imagePathToUse="";

        if(TextUtils.equals(imagePath, "placeholder")){
            imagePathToUse="header_milan";
        } else {
            imagePathToUse = imagePath.substring(79, imagePath.length()-4);
        }

        int drawableResource=context.getResources().getIdentifier(imagePathToUse, "drawable", context.getPackageName());

        try {
            imageToSet.setImageDrawable(context.getResources().getDrawable(drawableResource));
        } catch (Resources.NotFoundException e){
            Log.d("miotag","eccezione!!!!! immagine mancante: "+imagePathToUse);
            imagePathToUse="header_milan";
            int drawableResourceNoPicture=context.getResources().getIdentifier(imagePathToUse, "drawable", context.getPackageName());
            imageToSet.setImageDrawable(context.getResources().getDrawable(drawableResourceNoPicture));

        }



        timeTextToSet.setText(time);


        distanceTextToSet.setText(distance.substring(0,4)+" km");
    }

//    public CircleImageView returnCircleImageFromHolder(){
//        return imageToSet;
//    }


}
