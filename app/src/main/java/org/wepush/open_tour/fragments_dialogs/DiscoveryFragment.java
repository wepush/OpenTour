package org.wepush.open_tour.fragments_dialogs;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wepush.open_tour.DetailsActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.DiscoveryListViewAdapter;
import org.wepush.open_tour.utils.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by antoniocoppola on 30/10/15.
 */
public class DiscoveryFragment extends Fragment {

    private static Context context;
    private ArrayList<Site> elements;
    private Intent i;

    public static DiscoveryFragment newInstance (Context ctx, ArrayList<Site> elementsList){

        DiscoveryFragment f=new DiscoveryFragment();
        context=ctx;
        Gson gson = new Gson();
        String json = gson.toJson(elementsList);

        Bundle bundle=new Bundle();
        bundle.putString("elements", json);



        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview_cancellala, null);

        Bundle bundle=getArguments();
        String fromJson=bundle.getString("elements");

        Gson gson=new Gson();
        Type type = new TypeToken<ArrayList<Site>>() {}.getType();

        elements = gson.fromJson(fromJson, type);
        i=new Intent(context, DetailsActivity.class);

        ListView lw=(ListView) view.findViewById(R.id.listView1);
        lw.setAdapter(new DiscoveryListViewAdapter(context,elements));
        lw.setFocusable(false);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("miotag","posizione di click: "+position);
                i.putExtra("siteId",elements.get(position).id);
                startActivity(i);
            }
        });

        return view;
    }


}
