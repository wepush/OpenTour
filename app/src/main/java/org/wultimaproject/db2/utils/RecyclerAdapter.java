package org.wultimaproject.db2.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wultimaproject.db2.R;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> myList;


    public RecyclerAdapter(ArrayList<String> oldArrayList){
       myList=new ArrayList<>();
        myList=oldArrayList;



    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_discovery, parent, false);

        return RecyclerViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //cast between abstract RecyclerView.ViewHolder and ViewHolder
        RecyclerViewHolder recHolder=(RecyclerViewHolder) holder;
        //taking array strings identified by "position" and passing them to ViewHolder
        String tempString=myList.get(position);
        recHolder.setTextInViewHolder(tempString);

    }

    @Override
    public int getItemCount() {
        if (myList.size()==0){
            return 0;
        } else {
            return myList.size();
        }
    }
}
