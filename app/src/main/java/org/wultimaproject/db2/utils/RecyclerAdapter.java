package org.wultimaproject.db2.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wultimaproject.db2.R;
import org.wultimaproject.db2.structures.Site;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<String> idLive;
    private ArrayList<String> timeLive;
    private ArrayList<String> distanceLive;
    private static Context context;
//    private ArrayList<Double> distanceLive;

    public RecyclerAdapter(ArrayList<String> oldIdArrayList,ArrayList<String>oldTimeArrayList,ArrayList<String> oldDistanceArrayList,Context ctx){
       idLive=new ArrayList<>();
        timeLive=new ArrayList<>();
        distanceLive=new ArrayList<>();
        idLive=oldIdArrayList;
        timeLive=oldTimeArrayList;
        distanceLive=oldDistanceArrayList;
        context=ctx;

    }

//    public RecyclerAdapter(ArrayList<Site> sitesFromShowTour){
//
//        timeLive=new ArrayList<>();
//        ArrayList<Site> sitesForViewHolder=new ArrayList<>();
//        sitesForViewHolder=sitesFromShowTour;
////        for (int i=0; i<sitesForViewHolder.size(); i++){
////            Log.d("miotag","siti in arrivo: "+sitesForViewHolder.get(i).name+" ,con posizione: "+i);
////        }
//        for (Site s: sitesForViewHolder){
//
//            timeLive.add(s.name);
//
//        }
//    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_discovery, parent, false);

        return RecyclerViewHolder.newInstance(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //cast between abstract RecyclerView.ViewHolder and ViewHolder
        RecyclerViewHolder recHolder=(RecyclerViewHolder) holder;
        //taking array strings identified by "position" and passing them to ViewHolder
//        String tempString=myList.get(position);
        String tempIdString=idLive.get(position);
        //0807
        String tempTimeString=timeLive.get(position);

        String tempDistanceString=distanceLive.get(position);
        Log.d("miotag", " la stringa di id di onBind è : " + tempIdString);
        Log.d("miotag", " la stringa di time di onBind è : " + tempTimeString);
        Log.d("miotag", " la stringa di distance di onBind è : " + tempDistanceString);

        recHolder.setTextInViewHolder(tempIdString,tempTimeString,tempDistanceString);

    }

    @Override
    public int getItemCount() {
//        if (myList.size()==0){
//            return 0;
//        } else {
//            return myList.size();
//        }

        if (idLive.size()==0){
            return 0;
        } else {
            return idLive.size();
        }
    }
}
