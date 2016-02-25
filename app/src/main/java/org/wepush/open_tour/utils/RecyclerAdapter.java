package org.wepush.open_tour.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wepush.open_tour.R;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<String> idLive;
    private ArrayList<String> timeLive;
    private ArrayList<String> distanceLive;
    private static Context context;
    private RecyclerViewHolder recHolder;

    public RecyclerAdapter(ArrayList<String> oldIdArrayList,ArrayList<String>oldTimeArrayList,ArrayList<String> oldDistanceArrayList,Context ctx){
        idLive=new ArrayList<>();
        timeLive=new ArrayList<>();
        distanceLive=new ArrayList<>();
        idLive=oldIdArrayList;
        timeLive=oldTimeArrayList;
        distanceLive=oldDistanceArrayList;
        context=ctx;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_discovery, parent, false);
        OpenTourUtils mOpenTourUtils=new OpenTourUtils(context,Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class));


        return RecyclerViewHolder.newInstance(view,context,mOpenTourUtils,Repository.retrieve(context,Constants.USER_LANGUAGE,String.class));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //cast between abstract RecyclerView.ViewHolder and ViewHolder
        recHolder=(RecyclerViewHolder) holder;
        //taking array strings identified by "position" and passing them to ViewHolder
        String tempIdString=idLive.get(position);
        //0807
        String tempTimeString=timeLive.get(position);
        String tempDistanceString=distanceLive.get(position);
        recHolder.setTextInViewHolder(tempIdString,tempTimeString,tempDistanceString);
        Log.d("miotag","return from onBindViewHolder");

    }

    @Override
    public int getItemCount() {


        if (idLive.size()==0){
            return 0;
        } else {
            return idLive.size();
        }
    }


}
