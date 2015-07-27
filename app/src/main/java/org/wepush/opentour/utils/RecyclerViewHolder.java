package org.wepush.opentour.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.wepush.opentour.R;
import org.wepush.opentour.ShowDetailsActivity;
import org.wepush.opentour.structures.DB1SqlHelper;


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
        //TODO below txtItem should be ImageView
        CircleImageView imageItem=(CircleImageView)view.findViewById(R.id.imgToPut);
        TextView txtTime=(TextView)view.findViewById(R.id.itemTimeToPut);
        TextView txtDistance=(TextView)view.findViewById(R.id.itemDistanceToPut);
        context=ctx;
        return new RecyclerViewHolder(view,imageItem,txtTime,txtDistance);


    }


    public RecyclerViewHolder(final View parent, CircleImageView newImageToSet,TextView newTimeToSet,TextView newDistanceToSet) {
        super(parent);

        //TODO below txtItem should be ImageView
            imageToSet=newImageToSet;
            timeTextToSet=newTimeToSet;
            distanceTextToSet=newDistanceToSet;

        //listener sul click dell'elemento recyclerView
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, ShowDetailsActivity.class);
                i.putExtra("siteId",idToPassFromCLick);
                context.startActivity(i);
            }
        });

    }

    public void setTextInViewHolder(String id,String time,String distance){
        //TODO the above textToSet should take the id and through the DB method getPictureSite, set the correct image
//        textToSet.setText(id);
        idToPassFromCLick=id;
       String imagePath= DB1SqlHelper.getInstance(context).getPictureSite(id);
        String imagePathToUse="";

        if(TextUtils.equals(imagePath, "placeholder")){
            imagePathToUse="header_milan";
        } else {
            Log.d("miotag", "SiteToShow PICTURE: " + imagePath);
            imagePathToUse = imagePath.substring(79, imagePath.length()-4);
            Log.d("miotag","imagePAth: "+imagePathToUse);
        }

//        Ottengo la giusta immagine del monumento da impostare nella toolbar

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


}
