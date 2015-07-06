package org.wultimaproject.db2.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import org.wultimaproject.db2.R;
import org.wultimaproject.db2.structures.Site;

import java.util.ArrayList;

/**
 * Created by Antonio on 23/04/2015.
 */
public class TimeLineAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private ArrayList<Site>siteToShowInList=new ArrayList<>();

    public TimeLineAdapter(Context context,ArrayList<Site> showingListOfSite){

        this.siteToShowInList=showingListOfSite;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public Object getItem(int position) {
//        return listData.get(position);
        return siteToShowInList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
//        return listData.size();
        return siteToShowInList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_element_tm, null);
            holder = new ViewHolder();
            holder.vTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.vDescription = (TextView) convertView.findViewById(R.id.txtAddress);
            holder.vTime = (TextView) convertView.findViewById(R.id.txtTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.vTitle.setText(siteToShowInList.get(position).name);
        holder.vDescription.setText(siteToShowInList.get(position).address+","+siteToShowInList.get(position).addressCivic);
        holder.vTime.setText(String.valueOf(siteToShowInList.get(position).showingTime));

        return convertView;
    }

    static class ViewHolder {
        TextView vTitle;
        TextView vDescription;
        TextView vTime;

    }
}




