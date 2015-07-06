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


    public static RecyclerViewHolder newInstance (View view){
        TextView txtItem=(TextView)view.findViewById(R.id.itemToPut);

        return new RecyclerViewHolder(view,txtItem);


    }


    public RecyclerViewHolder(final View parent, TextView newTextToSet) {
        super(parent);

            textToSet=newTextToSet;

    }

    public void setTextInViewHolder(String s){
        textToSet.setText(s);
    }


}
