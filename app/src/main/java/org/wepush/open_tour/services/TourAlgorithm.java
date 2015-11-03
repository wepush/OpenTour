
package org.wepush.open_tour.services;

import android.app.usage.ConfigurationStats;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Repository;
import org.wepush.open_tour.utils.SphericalMercator;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by antoniocoppola on 30/05/15.
 */
public class TourAlgorithm {

    //the idea is to shrink at the fastest rate the number of sites to work on. This is done by process subset of sites multiple times:
    //so we dismiss the bad behaviour of working with all DB sites, but we tradeoff that with processing multiple times small subsets of sites

    private final static int RANDOM_PATH_GENERATION=100;
//    private final static int NUMBER_OF_ADJACENCIES=3;

    private static Context context;
    private static ArrayList<Site> sitesInRange;
    private  static Calendar timeToStart;
    private static float timeLeft;
    private static float maxTravelDistance;
    private static Site userStartingSite;

    private static int howToMove;
    private static ArrayList<ArrayList<Site>> allPossiblePaths=new ArrayList<ArrayList<Site>>();



    public  static ArrayList<Site> showTour (Site startingSite, float timeToSpend,Context ctx, Calendar timeOfDay, String algoritmToUse){

        int indexToChoose=0;
        int indexActualSize=0;

        Log.d("miotag"," starting site DENTRO l'algoritmo: "+startingSite.latitude+", "+startingSite.longitude);
        timeLeft=timeToSpend;
        timeToStart=timeOfDay;//al momento è solo la data
        context=ctx;

       if( TextUtils.equals(Repository.retrieve(context, Constants.HOW_SAVE, String.class),"walk"))
        {
            Log.d("miotag","tour by walk");
            howToMove=3;
        } else{
           Log.d("miotag","tour by bike");
           howToMove=6;
       }


       try {
           timeToStart.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);

           timeToStart.set(Calendar.DAY_OF_WEEK, 1);
           timeToStart.set(Calendar.DAY_OF_MONTH, 1);
       } catch (NullPointerException e ){
           timeToStart=new GregorianCalendar(timeOfDay.get(Calendar.YEAR),timeOfDay.get(Calendar.MONTH),timeOfDay.get(Calendar.DAY_OF_WEEK),
                   timeOfDay.get(Calendar.HOUR_OF_DAY),timeOfDay.get(Calendar.MINUTE));
       }




        //since timeLeft and timeToStart have to keep their max values for every getRandomPaths iterations. To keep track of them, we use timeLeftGlobal
        // and timeToStartGlobal but their visibility is methode-scope, so for every new iteration their values will be re-initialized from timeLeft,timeToStart

        //TODO 24Luglio: Avoid crash initilizind timToStart
        Gson gson=new Gson();
        Type type = new TypeToken<Calendar>() {}.getType();
        String json=Repository.retrieve(context, Constants.TIME_TO_START, String.class);
        Calendar timeToStartFromUser=gson.fromJson(json, type);


        int minutes=timeToStart.get(Calendar.MINUTE)+timeToStartFromUser.get(Calendar.MINUTE);
        int hours=timeToStart.get(Calendar.HOUR_OF_DAY)+ timeToStartFromUser.get(Calendar.HOUR_OF_DAY);
        if (minutes > 59){
            int overMinutes=minutes/60;
            minutes= minutes - (minutes*overMinutes);
            hours=hours+overMinutes;
        }

        if (hours > 23){
            int overHours=hours/24;
            hours=hours-(hours*overHours);
        }
        timeToStart.set(Calendar.MINUTE,minutes);
        timeToStart.set(Calendar.HOUR_OF_DAY,hours);

        //TODO check if sum of minutes from minutes exceeds current day

        userStartingSite=startingSite;
        //find out how far the user can go knowing timeToSpend and vehicles
        //IF TO BE USED TO CHOOSE THE RIGHT SPEED
        maxTravelDistance=(howToMove*1000f/3600f)*timeLeft;

        //divided by 5 to keep the reality of a user moving by walk/bike
        maxTravelDistance=maxTravelDistance/5f;
        //and again divided by 1000 to obtain KMs
        maxTravelDistance=maxTravelDistance/1000f;

        sitesInRange=new ArrayList<Site>();
        //choose site in range taking actualSite,all sites in db,max travel distance

//todo se funziona, si può cancellare sitesInRange=chooseSitesInRange(startingSite, DB1SqlHelper.getInstance(context).getSites(),maxTravelDistance);
//
        sitesInRange=chooseSitesInRange(startingSite, DB1SqlHelper.getInstance(context).getSites(),maxTravelDistance);
//

        //localize the nearest site belonging to DB from the coords feeded by user
        Site siteToStart= new Site();
        siteToStart=findNearestFromDb(startingSite);

        allPossiblePaths.clear();


        for (int i=0; i<RANDOM_PATH_GENERATION; i++){

                allPossiblePaths.add(getRandomPaths(siteToStart));
//                allPossiblePaths.add(getPriorityPaths(siteToStart));

//re inizializzazione di timeToStart dopo ciascun ciclo a partire dalle impostazioni utente
                timeToStart.set(Calendar.HOUR_OF_DAY, timeToStartFromUser.get(Calendar.HOUR_OF_DAY));
                timeToStart.set(Calendar.MINUTE, timeToStartFromUser.get(Calendar.MINUTE));
//re inizializzazione di timeToSpend dopo ciascun ciclo a partire dalle impostazioni utente

                timeLeft = Float.valueOf(Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class));
                siteToStart = new Site();

                siteToStart = findNearestFromDb(startingSite);
                siteToStart.visitTime = 0.0f;

        }


        //TODO 27agosto2015 scelta dell'arrayList con maggior numero di hop al suo interno
//        for (int i=0; i<allPossiblePaths.size();i++){
//            if(indexActualSize<allPossiblePaths.get(i).size()){
//                indexActualSize=allPossiblePaths.size();
//                indexToChoose=i;
//            }
//        }

//        ArrayList<Site> sitesToShowing=allPossiblePaths.get(indexToChoose);

        ArrayList<Site> sitesToShowing=allPossiblePaths.get(pathToChoose(allPossiblePaths));
        allPossiblePaths.clear();

        return sitesToShowing;


    }

    //select the path with more popularity weight
    private static int pathToChoose(ArrayList<ArrayList<Site>> paths){
        int indexToChoose=0;
        int actualPopolarity=0;
        int totalPopolarity=0;
        for (int i=0; i<paths.size();i++){
            for (int j=0; j<paths.get(i).size(); j++){
//                Log.d("miotag","path "+i+", sito "+j+", priorità "+paths.get(i).get(j).priority);
                actualPopolarity=actualPopolarity+paths.get(i).get(j).priority;

            }
//            Log.d("miotag","path "+i+" con priorità: "+actualPopolarity);

            if (totalPopolarity<actualPopolarity){
                totalPopolarity=actualPopolarity;
//                Log.d("miotag","nuova priorità: "+totalPopolarity);
                indexToChoose=i;
            }
            actualPopolarity=0;
        }
        Log.d("miotag","l'indice scelto è: "+indexToChoose+", corrispondente a priorità: "+totalPopolarity);
        return indexToChoose;
    }


    private static Site findNearestFromDb(Site fromSite){
        double minDistance=Double.MAX_VALUE;
        Site siteToReturn=new Site();
        if (sitesInRange!=null){
            for (Site site: sitesInRange){
                double actualDistance= SphericalMercator.getDistanceFromLatLonInKm(site, fromSite);
                if(actualDistance<minDistance){

                    minDistance=actualDistance;
                    siteToReturn=site;
                }
            }
            return siteToReturn;
        }
        return null;
    }

    private static ArrayList<Site> chooseSitesInRange(Site siteToStart, ArrayList<Site> siteArray,float d){
        //choose which sites are in range for the user to walk/bike


        ArrayList<Site> siteInRangeToReturn=new ArrayList<Site>();


        Type type2=new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson2=new Gson();
        ArrayList<String>typeOfSiteChosen=gson2.fromJson(Repository.retrieve(context,Constants.WHAT_SAVE,String.class),type2);

        for (Site site: siteArray){
            if (
                    (isSiteTypeChosen(site,typeOfSiteChosen))
                ) {
                float w = ((float) SphericalMercator.getDistanceFromLatLonInKm(site, siteToStart));

                if (w < d) {
                    siteInRangeToReturn.add(site);
                }
            }
        }

        return siteInRangeToReturn;
    }


    //this method will modify real distance between actualSite and other site through popularity measure belongin to each db site
    //right now is set to 0 so the process can keep going without this parameter


    private static ArrayList<Site> getRandomPaths (Site startSite){

        //general initialization for variables in the method
        ArrayList<Site> chosenRandomPath=new ArrayList<Site>();

        int iterations=0;
//        double maxDist=Double.MAX_VALUE;
//        double minDistanceToTravelInMeters;
        Site siteToAdd=new Site();
        //timeLeft and all the times in this method MUST BE in minutes
        timeLeft=timeLeft/60f;

       try {
//this section is only for startSite sake, that is processed as first and give start to the follow method
           if (checkIfSiteIsOpen(startSite, (SphericalMercator.getDistanceFromLatLonInKm(startSite, userStartingSite)) * 1000f)) {
               startSite.alreadyTaken = true;



//TODO the first site is added to the returning arraylist. If there is any error in the timestamp (startSite.showingSite), it's here.

               chosenRandomPath.add(startSite);
               //following instructions tracking only the movement between actual position and first site found in db
               timeLeft = timeLeft - startSite.visitTime;

               timeToStart.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startSite.showingTime.substring(0, 2)));
               timeToStart.set(Calendar.MINUTE, Integer.valueOf(startSite.showingTime.substring(3)));
           }


//After startSite is being processed, we take in consideration the first adiacences, choosing randomly one of them; then the global variables
//are update; the new found site is added to the arraylist and a new iteration is started taking in consideration the new startingSite

           Random r = new Random();
           while (timeLeft > 0) {

               fillInAdjacency(startSite);

               int randPosition = r.nextInt(Constants.NUMBER_OF_ADJACENCIES);
               Site site = startSite.adjacencySite.get(randPosition);
               if (site != null) {
                   //from here on, we referring to the adiacent site

                   if (!site.alreadyTaken) {
                       site.alreadyTaken = true;

                       if (checkIfSiteIsOpen(site, (SphericalMercator.getDistanceFromLatLonInKm(startSite, site)) * 1000f)) {
                           iterations = 0;
                           siteToAdd = site;
                           siteToAdd.alreadyTaken = true;
                           timeLeft = timeLeft - siteToAdd.visitTime;
                           timeToStart.set(Calendar.HOUR_OF_DAY, Integer.valueOf(siteToAdd.showingTime.substring(0, 2)));
                           timeToStart.set(Calendar.MINUTE, Integer.valueOf(siteToAdd.showingTime.substring(3)));

                           startSite = siteToAdd;
                           chosenRandomPath.add(siteToAdd);
                           //                        }

                       }

                   } else {

                       iterations++;
                       if (iterations > 21) {
//                           Log.d("miotag","iterazioni > 21!!!!");
//                           Log.d("miotag","chosenRandomPath  iteration dimensione: "+chosenRandomPath.size());
                           //TODO riportare i siti con alreadyTaken=false in maniera da potere essere nuovamente scelti nella successiva iterazione
//                           resetAlreadyTakenSites(chosenRandomPath);
                           return chosenRandomPath;
                       }
                   }


               } //fine if(site != null)

           }//fine while
       } catch(NullPointerException e){
           e.printStackTrace();
//           Log.d("miotag",e.toString());
           startSite.isItDummy=true;
           ArrayList<Site> dummyArrayToReturn=new ArrayList<Site>();
           dummyArrayToReturn.add(startSite);
           Log.d("miotag","dummyy!!!!");
           return dummyArrayToReturn;

       }
        Log.d("miotag","chosenRandomPath dimensione: "+chosenRandomPath.size());
//            resetAlreadyTakenSites(chosenRandomPath);
        return chosenRandomPath;

    }



    private static Boolean checkIfSiteIsOpen(Site site, double distance){

//in the following, .visitTime is reutilized to keep track NOT ONLY of the time spent to visite a site, but also of the time to move, so
//visit.Time will be the sum between proper visit time and move time. This new value is utilized in getRandomPath() to update global variables
        float destinationTime=(float)((distance/(howToMove*1000f/3600f)));//this are seconds
        destinationTime=destinationTime/60;//this are minutes
        //and this is the "travelTime" + visitTime (in mins)
        destinationTime=destinationTime+site.visitTime;

        int hourOfDay=timeToStart.get(Calendar.HOUR_OF_DAY);
        int minuteOfDay=timeToStart.get(Calendar.MINUTE);

        minuteOfDay=minuteOfDay+(int)destinationTime;

        if (minuteOfDay>60){

            hourOfDay=hourOfDay+(minuteOfDay/60);
            minuteOfDay=minuteOfDay-(60*(minuteOfDay/60));

            if(hourOfDay > 24){
                hourOfDay=hourOfDay-24;
            }
        }
        if (site.alwaysOpen==1){
            site.visitTime=destinationTime;
            site.showingTime=timeFormattingString(""+hourOfDay+":"+minuteOfDay,minuteOfDay);
            return true;
        }

        String openingsJson=site.openings;

        try {
            JSONArray jsonOpenings = new JSONArray(openingsJson);
            for (int i=0; i<jsonOpenings.length(); i++){
                JSONObject singleOpening=jsonOpenings.getJSONObject(i);
                if(singleOpening!=null) {


                    if (compareTime(hourOfDay, minuteOfDay, singleOpening.getString("time_from"), singleOpening.getString("time_to"))) {
                        if(compareDate(singleOpening.getString("date_from"),singleOpening.getString("date_to"),timeToStart)){

                            //if i can reach it and is open, then i add it as result, and then i have to update
                            //the time left

                            site.visitTime=destinationTime;

                            site.showingTime=timeFormattingString(""+hourOfDay+":"+minuteOfDay,minuteOfDay);

                            return true;
                        }
                    } else {
                        return false;
                    }
                }

            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return false;

    }




    private static boolean compareTime( int hour, int min, String inTime, String outTime){

        inTime=inTime.replace(".", ":");
        outTime=outTime.replace(".",":");
        Date actualTime=new Date();
        Date openTime=new Date();
        Date closeTime=new Date();
        String thisTime=String.valueOf(hour)+":"+String.valueOf(min);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {

            actualTime = sdf.parse(thisTime);
            openTime = sdf.parse(inTime);
            closeTime = sdf.parse(outTime);
        } catch (ParseException e){
            System.out.println(e);
        }

        if (actualTime.compareTo(openTime) > 0 && actualTime.compareTo(closeTime) < 0){
            return true;
        }

        return false;
    }


    private static boolean compareDate(String dateIn, String dateOut, Calendar whenDate){
        //Calendar whenDate is from user Settings

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");

        if (TextUtils.equals(dateIn,"")){
            return true;
        }

        if (TextUtils.equals(dateOut,"")){
            return true;
        }
        try{


            Date thisDate=convertCalendarToDate(whenDate);

            Date inDate = formatter.parse(dateIn);

            Date outDate = formatter.parse(dateOut);

            if (
                    (thisDate.compareTo(inDate)>0) &&
                            (thisDate.compareTo(outDate)<0)
                    )
            {
                return true;
            }

        }catch (ParseException e1){
            e1.printStackTrace();
        }

        return false;
    }

    private static Date convertCalendarToDate(Calendar calendar){
        calendar = Calendar.getInstance();

        return calendar.getTime();
    }

    private static void fillInAdjacency(Site thisSite){
        //every site belonging to siteInRange will have its arraylist adjacent filled with the closer 3 sites
        double closeDistance=Double.MAX_VALUE;
        double mediumDistance=Double.MAX_VALUE;
        double longDistance=Double.MAX_VALUE;
        Site siteCloser=new Site();
        Site siteMedium=new Site();
        Site siteLong=new Site();

            for(Site site: sitesInRange){
                if(!site.alreadyTaken) {
                    double distanceBetweenSite = SphericalMercator.getDistanceFromLatLonInKm(site, thisSite);
                    if (TextUtils.equals(site.name, thisSite.name)) {

                    } else if (distanceBetweenSite < closeDistance) {
                        closeDistance = distanceBetweenSite;
                        siteCloser = site;

                    } else if (distanceBetweenSite < mediumDistance) {
                        mediumDistance = distanceBetweenSite;
                        siteMedium = site;
                    } else if (distanceBetweenSite < longDistance) {
                        longDistance = distanceBetweenSite;
                        siteLong = site;
                    }
                }

            }
                thisSite.adjacencySite.add(siteCloser);
                thisSite.adjacencySite.add(siteMedium);
                thisSite.adjacencySite.add(siteLong);
    }

//    private static void fillInAdjacency(Site thisSite) {
//
//        String adjacencyId;
//        try {
//            JSONArray adjacencyArray = new JSONArray(thisSite.allAdjacency);
//
//                for (int i=0; i< 3; i++){
//                    thisSite.adjacencySite.add(DB1SqlHelper.getInstance(context).getSite((adjacencyArray.getJSONObject(i)).getString("id")));
//                    Log.d("miotag","adiacenza aggiunta: "+thisSite.adjacencySite.get(i).name);
//                }
//        } catch(JSONException e){
//            Log.d("miotag","FILLINADJACENCE exception");
//            e.printStackTrace();
//
//        }
//
////        for (int i=0; i<Constants.NUMBER_OF_ADJACENCIES; i++){
////            thisSite.adjacencySite.add(DB1SqlHelper.getInstance(context).getSite(thisSite.allAdjacency.get(i)));
////            Log.d("miotag","adiacenza aggiunta: "+thisSite.adjacencySite.get(i).name);
////        }
//        Log.d("miotag","rispedisco da fillInAdjacency");
//
//    }

    private static String timeFormattingString(String s,int minute){

        String minuteString=String.valueOf(minute);
        if (s.indexOf(":")!=2){
            s="0"+s;
        }
        if (s.length()<5){
            minuteString=":0"+minuteString;
            s=s.substring(0,2);
            s=s+minuteString;


        }
        return s;

    }



    private static boolean isSiteTypeChosen(Site site, ArrayList<String> sList){

        for (String s: sList){
            if (TextUtils.equals(s,"all")){
                return true;
            }
            if(TextUtils.equals(site.typeOfSite,s)){
                return true;
            }
        }
        return false;
    }

//    private static void resetAlreadyTakenSites(ArrayList<Site> sites){
//        for (Site site: sites){
//            for (Site adjacentSite: site.adjacencySite){
//                adjacentSite.alreadyTaken=false;
//            }
//        }
//
//    }



}//fine classe

