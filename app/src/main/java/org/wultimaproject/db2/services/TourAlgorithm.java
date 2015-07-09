
package org.wultimaproject.db2.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.structures.DB1SqlHelper;
import org.wultimaproject.db2.structures.Site;
import org.wultimaproject.db2.utils.Repository;
import org.wultimaproject.db2.utils.SphericalMercator;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by antoniocoppola on 30/05/15.
 */
public class TourAlgorithm {

    //the idea is to shrink at the fastest rate the number of sites to work on. This is done by process subset of sites multiple times:
    //so we dismiss the bad behaviour of working with all DB sites, but we tradeoff that with processing multiple times small subsets of sites

//    private static final int MOVE_BY_WALK=3;
//    private static final int MOVE_BY_BIKE=6;
    private static Context context;
    private static ArrayList<Site> sitesInRange;
    private  static Calendar timeToStart;
    private static float timeLeft;
    private static float maxTravelDistance;
    private static Site userStartingSite;

//    private static final int VISIT_TIME=30; //minutes
    private static int howToMove;
    private static ArrayList<ArrayList<Site>> allPossiblePaths=new ArrayList<ArrayList<Site>>();



    public  static ArrayList<Site> showTour (Site startingSite, float timeToSpend,Context ctx, Calendar timeOfDay, String algoritmToUse){

//            Log.d("miotag","startingSite: "+startingSite.name);
//            Log.d("miotag","timeToSpend: "+timeToSpend);
        int indexToChoose=0;
        int indexActualSize=0;

        timeLeft=timeToSpend;
        timeToStart=timeOfDay;//al momento è solo la data
//             timeLeftGlobal=timeLeft;//quanto dura il tour
        context=ctx;

       if( TextUtils.equals(Repository.retrieve(context,Constants.HOW_SAVE,String.class),"walk"))
        {
//            Log.d("miotag","HOW TO MOVE: walk");
            howToMove=3;
        } else{
//           Log.d("miotag","HOW TO MOVE: bike");
           howToMove=6;
       }





        //since timeLeft and timeToStart have to keep their max values for every getRandomPaths iterations. To keep track of them, we use timeLeftGlobal
        // and timeToStartGlobal but their visibility is methode-scope, so for every new iteration their values will be re-initialized from timeLeft,timeToStart
        Gson gson=new Gson();
        Type type = new TypeToken<Calendar>() {}.getType();
        String json=Repository.retrieve(context, Constants.TIME_TO_START, String.class);
        Calendar timeToStartFromUser=gson.fromJson(json, type);
        timeToStart.add(Calendar.HOUR_OF_DAY,timeToStartFromUser.get(Calendar.HOUR_OF_DAY));
        timeToStart.add(Calendar.MINUTE,timeToStartFromUser.get(Calendar.MINUTE));

        userStartingSite=startingSite;
        //find out how far the user can go knowing timeToSpend and vehicles
        //IF TO BE USED TO CHOOSE THE RIGHT SPEED
        maxTravelDistance=(howToMove*1000f/3600f)*timeLeft;
//            Log.d("miotag","maxTravelDistance: "+maxTravelDistance);
        //divided by 5 to keep the reality of a user moving by walk/bike
        maxTravelDistance=maxTravelDistance/5f;
        //and again divided by 1000 to obtain KMs
        maxTravelDistance=maxTravelDistance/1000f;

//            Log.d("miotag","SHOWTOUR: maxDistance="+maxTravelDistance);
        sitesInRange=new ArrayList<Site>();
        //choose site in range taking actualSite,all sites in db,max travel distance

//todo se funziona, si può cancellare sitesInRange=chooseSitesInRange(startingSite, DB1SqlHelper.getInstance(context).getSites(),maxTravelDistance);
//
        sitesInRange=chooseSitesInRange(startingSite, DB1SqlHelper.getInstance(context).getSites(),maxTravelDistance);
//

        //localize the nearest site belonging to DB from the coords feeded by user
        Site siteToStart= new Site();
        //todo se funziona, è da cancellare           siteToStart=findNearestFromDb(startingSite);
        siteToStart=findNearestFromDb(startingSite);
//            Log.d("miotag","Algoritmo scelto: "+algoritmToUse);

//            if (TextUtils.equals(algoritmToUse,"shortest")) {
//                Log.d("miotag","l'algoritmo che utilizzo è SHORTEST");
//             allPossiblePaths.add(getShortestPath(siteToStart));
//            } else {
//                Log.d("miotag"," l'algoritmo che utilizzo è RANDOM");
//                allPossiblePaths.add(getRandomPaths(siteToStart));
//            }



        allPossiblePaths.clear();


//           allPossiblePaths.add(getRandomPaths(siteToStart));
//TODO riempimento di allpossibilePaths con 10 iterazioni di getRandomPaths
        for (int i=0; i<512; i++){

//            Log.d("miotag","NUOVA ITERAZIONE con siteToStart="+siteToStart.toString());

            Log.d("miotag","NEW ITERATION with siteToStart="+siteToStart.toString());
                allPossiblePaths.add(getRandomPaths(siteToStart));
//re inizializzazione di timeToStart dopo ciascun ciclo a partire dalle impostazioni utente
                timeToStart.set(Calendar.HOUR_OF_DAY, timeToStartFromUser.get(Calendar.HOUR_OF_DAY));
                timeToStart.set(Calendar.MINUTE, timeToStartFromUser.get(Calendar.MINUTE));
//re inizializzazione di timeToSpend dopo ciascun ciclo a partire dalle impostazioni utente

                timeLeft = Float.valueOf(Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class));
                siteToStart = new Site();

//                Log.d("miotag2", "startingSite=" + startingSite.name);
//                Log.d("miotag2", "startingSite.visitTime=" + startingSite.visitTime);
                siteToStart = findNearestFromDb(startingSite);
                siteToStart.visitTime = 0.0f;
//                Log.d("miotag2", "siteToStart.name=" + siteToStart.name);
//                Log.d("miotag2", "siteToStart.visitTime=" + siteToStart.visitTime);


        }
//            allPossiblePaths.add(getRandomPaths(siteToStart));
//                    timeLeft=timeLeftGlobal;
//                    timeToStart=timeToStartGlobal;
//            allPossiblePaths.add(getRandomPaths(siteToStart));
//                    timeLeft=timeLeftGlobal;
//                    timeToStart=timeToStartGlobal;
//            allPossiblePaths.add(getRandomPaths(siteToStart));
//                    timeLeft=timeLeftGlobal;
//                    timeToStart=timeToStartGlobal;

        //TODO scelta dell'arrayList con maggior numero di hope al suo interno
        for (int i=0; i<allPossiblePaths.size();i++){
            if(indexActualSize<allPossiblePaths.get(i).size()){
                indexActualSize=allPossiblePaths.size();
                indexToChoose=i;
            }
        }
//            Log.d("miotag","allpossiblePaths ha misura"+allPossiblePaths.size());
//          Log.d("miotag"," il numero random scelto è : "+ meshRandomPosition);

        ArrayList<Site> sitesToShowing=allPossiblePaths.get(indexToChoose);

        allPossiblePaths.clear();
//            sitesInRange.clear();
        return sitesToShowing;
//            return allPossiblePaths.get(indexToChoose);

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

        //d: maxDistance the user can use based on timeToSpend and vehicle chosen
        //w: distance between startSite and the i-site of DB
//        Log.d("miotag"," l'array di tutti i siti dal db ha dimensioni: "+siteArray.size());
//        Log.d("miotag","siteToStart: "+siteToStart.name);
        ArrayList<Site> siteInRangeToReturn=new ArrayList<Site>();


        Type type2=new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson2=new Gson();
        ArrayList<String>typeOfSiteChosen=gson2.fromJson(Repository.retrieve(context,Constants.WHAT_SAVE,String.class),type2);

//        Log.d("miotag","max distanza fissata a: "+d);
        for (Site site: siteArray){
            if (
                    (isSiteTypeChosen(site,typeOfSiteChosen))
                ) {
                float w = ((float) SphericalMercator.getDistanceFromLatLonInKm(site, siteToStart));

//            Log.d("miotag","distanza attuale tra il sito in lav. "+site.name+", ed il sito iniziale "+siteToStart.name+": "+w);


                if (w < d) {
//                Log.d("miotag","attuale distanza: "+w);
//                Log.d("miotag","sito in range aggiunto : "+site.name);
                    siteInRangeToReturn.add(site);
                }
            }
        }
//        Log.d("miotag","dimensione siti in range: "+siteInRangeToReturn.size());




        return siteInRangeToReturn;
    }


    //this method will modify real distance between actualSite and other site through popularity measure belongin to each db site
    //right now is set to 0 so the process can keep going without this parameter

    public int addingPopularity(Site site){
        return 0;
    }

    public static void reachingTimeForEverySite(ArrayList <Site> sitesToReach){

        for (Site site: sitesToReach){


        }

    }




    private static ArrayList<Site> getRandomPaths (Site startSite){
//        Log.d("miotag","GET RANDOM PATHS to return");

        //general initialization for variables in the method
        ArrayList<Site> chosenRandomPath=new ArrayList<Site>();

        int iterations=0;
        double maxDist=Double.MAX_VALUE;
        double minDistanceToTravelInMeters;
        Site siteToAdd=new Site();
        //timeLeft and all the times in this method MUST BE in minutes
        timeLeft=timeLeft/60f;

       try {
//        startSite.alreadyTaken = true;
//this section is only for startSite sake, that is processed as first and give start to the follow method
           if (checkIfSiteIsOpen(startSite, (SphericalMercator.getDistanceFromLatLonInKm(startSite, userStartingSite)) * 1000f)) {
               startSite.alreadyTaken = true;



//TODO the first site is added to the returning arraylist. If there is any error in the timestamp (startSite.showingSite), it's here.

               chosenRandomPath.add(startSite);
               //following instructions tracking only the movement between actual position and first site found in db
//               Log.d("miotag", "TimeLeft before being modified(first site outside while): " + timeLeft);
//               Log.d("miotag", "startSite.visitTime after the sum between visit time and move time " + startSite.visitTime);

               timeLeft = timeLeft - startSite.visitTime;

//               Log.d("miotag", "TimeLeft after substract the first site outside while " + timeLeft);

               //DA QUI MODIFICHE DEL 22 GIUGNODA RIVEDERE
               //TODO: LE DUE VARIABILI QUI CALCOLATE SONO

//            String futureTimeFromSite= ""+siteToAdd.showingTime;
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            Date  futureTimeSite=new Date();
//            try {
//
//                futureTimeSite = sdf.parse(futureTimeFromSite);
//            } catch(ParseException e){
//                Log.d("miotag", "fail in conversione data di startSite (iniziale)");
//                e.printStackTrace();
//            }


//            timeToStart.add(Calendar.HOUR_OF_DAY,futureTimeSite.getDate());
//            timeToStart.add(Calendar.MINUTE,futureTimeSite.getMinutes());
               //CAMBIAMENTO DI IDEA: IL TEMPO GLOBALE (QUELLO DI OROLOGIO) VIENE PRESO DA site.showingTime
//               Log.d("miotag", "startSite.showingTime (before While): " + startSite.showingTime);
               timeToStart.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startSite.showingTime.substring(0, 2)));
               timeToStart.set(Calendar.MINUTE, Integer.valueOf(startSite.showingTime.substring(3)));
//               Log.d("miotag", "startSite.showingTime BEFORE del while: " + startSite.showingTime.substring(0, 2));
//               Log.d("miotag", "startSite.showingTime BEFORE del while: " + startSite.showingTime.substring(3));
//               Log.d("miotag", "TimeToStart modified after the first site before while is being processed: " + timeToStart.get(Calendar.HOUR_OF_DAY) + ":" + timeToStart.get(Calendar.MINUTE));

               //FIN QUI MODIFICHE DEL 22 GIUGNO
//            timeToStart.add(Calendar.MINUTE,((int)startSite.visitTime));
           }


//After startSite is being processed, we take in consideration the first adiacences, choosing randomly one of them; then the global variables
//are update; the new found site is added to the arraylist and a new iteration is started taking in consideration the new startingSite

           Random r = new Random();
           while (timeLeft > 0) {

               fillInAdjacency(startSite);
               int randPosition = r.nextInt(3);


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

                           //FIN QUI MODIFICHE DEL 22 GIUGNO
                           //                            timeToStart.add(Calendar.MINUTE, ((int) siteToAdd.visitTime));
                           startSite = siteToAdd;
                           chosenRandomPath.add(siteToAdd);
                           //                        }


                       }
                       //            if(isTimeAlwaysTheSame()){
                       //                iterations=iterations++;
                       //                    if(iterations>10){
                       //                        return chosenRandomPath;
                       //                    }
                       //            } else {
                       //                iterations=0;
                   } else {
                       //                   Log.d("miotag","iterazione numero: "+iterations);
                       iterations++;
                       if (iterations > 21) {

                           Log.d("miotag", "ATTENTION: iterations > 11");
                           return chosenRandomPath;
                       }
                   }

               } //fine if(site != null)

           }//fine while
       } catch(NullPointerException e){
           e.printStackTrace();
//           Log.d("miotag",e.toString());
           startSite.name="dummy";
           ArrayList<Site> dummyArrayToReturn=new ArrayList<Site>();
           dummyArrayToReturn.add(startSite);
           return dummyArrayToReturn;

       }

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
//        site.showingTime=hourOfDay+":"+minuteOfDay;
        if (site.alwaysOpen==1){
            site.visitTime=destinationTime;
            //il valore site.visitTime è dato dal JSON, non da calcoli
            // site.visitTime is from JSON, not from the process
            //SI è AGGIORNATO IL CONTO DEL TEMPO GLOBALE DA DIMINUIRE?
    //Is global time being update?

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
//        Log.d("miotag","(CompareTime) inTime: "+inTime);
//        Log.d("miotag","(CompareTime) outTime: "+outTime);
        String thisTime=String.valueOf(hour)+":"+String.valueOf(min);
//        Log.d("miotag","(CompareTime): String thisTime: "+thisTime);
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

    //    private static boolean isTimeAlwaysTheSame(){
//        if(timing==timeLeft){
//            return true;
//        }
//        return false;
//    }
    private static boolean compareDate(String dateIn, String dateOut, Calendar whenDate){
        //Calendar whenDate is from user Settings
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");

        if (TextUtils.equals(dateIn,"")){
//            Log.d("miotag","(compareDate) DateIN ->torno TRUE");
            return true;
        }

        if (TextUtils.equals(dateOut,"")){
//            Log.d("miotag","(compareDate) DateOUT ->torno TRUE");
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
//                Log.d("miotag","TOS: "+site.typeOfSite+"->"+s);
                return true;
            }
        }
        return false;
    }


}//fine classe

