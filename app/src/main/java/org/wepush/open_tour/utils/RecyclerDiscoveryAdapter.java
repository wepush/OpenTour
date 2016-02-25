//package org.wepush.open_tour.utils;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import org.wepush.open_tour.R;
//import org.wepush.open_tour.structures.Site;
//
//import java.util.ArrayList;
//
///**
// * Created by antoniocoppola on 26/10/15.
// */
//public class RecyclerDiscoveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private  Context context;
//    private ArrayList<Site> sitesForRecycler;
//    private RecyclerDiscoveryViewHolder recyclerDiscoveryHolder;
//
//    public RecyclerDiscoveryAdapter(Context ctx,ArrayList<Site> sites){
//        context=ctx;
//        sitesForRecycler=sites;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_discovery, parent, false);
//        return RecyclerDiscoveryViewHolder.newInstance(view, context);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        recyclerDiscoveryHolder= (RecyclerDiscoveryViewHolder) holder;
//        String imageToShow=sitesForRecycler.get(position).pictureUrl;
//        String siteName=sitesForRecycler.get(position).name;
//        String siteCategory=sitesForRecycler.get(position).typeOfSite;
//        String siteAddress=sitesForRecycler.get(position).address+", "+sitesForRecycler.get(position).addressCivic;
//        recyclerDiscoveryHolder.showHolderElement(imageToShow,siteName,siteAddress,siteCategory);
//
//    }
//
//    @Override
//    public int getItemCount() {
//
//
//        if (sitesForRecycler.size()==0){
//            return 0;
//        } else {
//
//            return sitesForRecycler.size();
//        }
//    }
//
//
//
//}
