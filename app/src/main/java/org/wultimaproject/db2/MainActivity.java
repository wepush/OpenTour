//package org.wultimaproject.db2;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//
//import org.wultimaproject.db2.services.ReadFromJson;
//import org.wultimaproject.db2.services.TourAlgorithmTask;
//import org.wultimaproject.db2.structures.Constants;
//import org.wultimaproject.db2.structures.DB1SqlHelper;
//import org.wultimaproject.db2.utils.Repository;
//import org.wultimaproject.db2.structures.Site;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//
//public class MainActivity extends Activity {
//
//    public static Context context;
//    public ArrayList<Site> siteToStamp=new ArrayList<Site>();
//    TextView txtShowShort;
//    TextView txtShowRandom;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        context=this;
//        txtShowShort=(TextView)findViewById(R.id.txtForShortResult);
//        txtShowRandom=(TextView)findViewById(R.id.txtForRandomResult);
//
//        //riempimento dati iniziali per l'esecuzione dell'algoritmo
//
//        //data:
//
//        Calendar cal=Calendar.getInstance();
//        Gson gson = new Gson();
//        String json = gson.toJson(cal);
//        Repository.save(this, Constants.WHEN_SAVE, json);
//
//        //sito di partenza
//        Site site=new Site();
////        site.latitude=38.114778;
////        site.longitude=13.365013;
//        site.latitude=45.4677527;
//        site.longitude=9.1809509;
//
//        site.name="Studio";
//        Gson gsonSite=new Gson();
//        String jsonSite=gson.toJson(site);
//        Repository.save(this,Constants.STARTING_SITE,jsonSite);
//
//        //durata del tour
//        float timeToSpend=14400;
//        Repository.save(this, Constants.TIME_TO_SPEND, timeToSpend);
//
//
//    }
//
//
//
//
////    public void onShowDb(View v){
////        siteToStamp=DB1SqlHelper.getInstance(getBaseContext()).getSites();
////        String results="";
////
////        for (Site actualSite: siteToStamp){
////            results= results+"\n"+writeDown(actualSite);
////        }
////
////        txtShow.setText(results);
////
////    }
//
//    public void onEraseDb(View v){
//        DB1SqlHelper.getInstance(this).deleteDb(this);
//    }
//
//
//    public void onLoadMilan(View v){
//        Intent intent=new Intent(this, ReadFromJson.class);
//        intent.putExtra("dbToLoad", "milano");
//        startService(intent);
//    }
//
//
//    public void onLoadPalermo(View v){
//        Intent intent=new Intent(this, ReadFromJson.class);
//        Log.d("miotag","sto selezionando palermo");
//        intent.putExtra("dbToLoad","palermo");
//        startService(intent);
//    }
//
//
//
//
//
//    public void onLaunchAlgorithmShortest(View v){
//        new TourAlgorithmTask(this, "shortest").execute();
//
//    }
//
//    public void onLaunchAlgorithmRandom(View v){
//        new TourAlgorithmTask(this, "random").execute();
//
//    }
//
//    public void showResultFromAlgoritm(){
//        String list="";
//        txtShowShort.setText("");
//        Log.d("miotag","Sites List: ");
//        for (Site site: siteToStamp) {
//            list =list+ " "+site.name + ", al tempo: " + site.showingTime + ".";
//        }
//           txtShowShort.setText(list);
//
//    }
//
//
//}
