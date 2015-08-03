package org.wepush.open_tour.services;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;
import org.wepush.open_tour.ShowTourTimeLineActivity;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Repository;


import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by antoniocoppola on 11/06/15.
 */
public class TourAlgorithmTask extends AsyncTask<Void, Void, ArrayList<Site>> {

//TODO COMMMENT before release


    ShowTourTimeLineActivity showTourTimeLineActivityInstance;
    static WeakReference<ShowTourTimeLineActivity> showTourTimeLineActivityWeakReference;
    String algToUse="";
    Site newSite;



    Context context= SettingTourActivity.context;

    Gson gsonCalendar=new Gson();
    Type typeCalendar=new TypeToken<Calendar>(){}.getType();
    String jSonCalendar= Repository.retrieve(context, Constants.WHEN_SAVE, String.class);
    Calendar actualDate=gsonCalendar.fromJson(jSonCalendar,typeCalendar);


    public TourAlgorithmTask(ShowTourTimeLineActivity wShowTourTimeLineActivity, String algortithmToUse){
        showTourTimeLineActivityWeakReference=new WeakReference<ShowTourTimeLineActivity>(wShowTourTimeLineActivity);
        algToUse=algortithmToUse;
        newSite= new Site();
        newSite.latitude=Double.valueOf(Repository.retrieve(context,Constants.LATITUDE_STARTING_POINT,String.class));
        newSite.longitude=Double.valueOf(Repository.retrieve(context,Constants.LONGITUDE_STARTING_POINT,String.class));


        newSite.name="this actual position";

    }

    @Override
    protected ArrayList<Site> doInBackground(Void... params){

        return TourAlgorithm.showTour(newSite, Float.valueOf(Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class)), context, actualDate, algToUse);
    }

    @Override
    protected void onPostExecute(ArrayList<Site> siteReturning){


        showTourTimeLineActivityInstance=showTourTimeLineActivityWeakReference.get();


            if ((siteReturning.isEmpty()) || (TextUtils.equals(siteReturning.get(0).name,"dummy"))   ){
                 if (showTourTimeLineActivityInstance != null){
                     showTourTimeLineActivityInstance.showDummyActivity();
                 }

            } else if (showTourTimeLineActivityInstance != null){
            showTourTimeLineActivityInstance.siteToStamp=siteReturning;
            showTourTimeLineActivityInstance.progressBar=(ProgressBar)  showTourTimeLineActivityInstance.findViewById(R.id.progressBarTourTimeLine);
            showTourTimeLineActivityInstance.progressBar.setVisibility(View.GONE);
            showTourTimeLineActivityInstance.showResultFromAlgorithm();
        }else {
            showTourTimeLineActivityWeakReference = new WeakReference<>(showTourTimeLineActivityInstance);

        }

    }

}



