package org.wultimaproject.db2.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.wultimaproject.db2.R;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView textToSet;
    private TextView timeTextToSet;
    private TextView distanceTextToSet;


    public static RecyclerViewHolder newInstance (View view){
        TextView txtItem=(TextView)view.findViewById(R.id.itemToPut);
        TextView txtTime=(TextView)view.findViewById(R.id.itemTimeToPut);
        TextView txtDistance=(TextView)view.findViewById(R.id.itemDistanceToPut);
        return new RecyclerViewHolder(view,txtItem,txtTime,txtDistance);


    }


    public RecyclerViewHolder(final View parent, TextView newTextToSet,TextView newTimeToSet,TextView newDistanceToSet) {
        super(parent);

            textToSet=newTextToSet;
            timeTextToSet=newTimeToSet;
            distanceTextToSet=newDistanceToSet;

    }

    public void setTextInViewHolder(String id,String time,String distance){
        textToSet.setText(id);
        timeTextToSet.setText(time);
        distanceTextToSet.setText(distance);
    }


}
