package org.wepush.open_tour.structures;

import org.wepush.open_tour.utils.Constants;

import java.util.ArrayList;

/**
 * Created by antoniocoppola on 29/05/15.
 */
public class Site {

    public String id;
    public String name;
    public String description;
    public String tips;
    public Double latitude;
    public Double longitude;
    public String address;
    public String addressCivic;
    public int alwaysOpen;
    public int priority;
    public String pictureUrl;
    public float visitTime; //this keep track of distance to reach AND the time to visit the site
    public boolean alreadyTaken;
    public boolean isItDummy=false;
    public String showingTime;
    public String typeOfSite;

    public ArrayList<Site> adjacencySite=new ArrayList<Site>(Constants.NUMBER_OF_ADJACENCIES);
    public String allAdjacency;
    public String openings;
    public String tickets;
    public String contacts;




    public Site(){

    }

}


//id: unique identifier for db purpouses
//name: name of the site
//description: field with info on the site
//tips: tips for the site
//lat/lon: geo coords of the site;
//address/addressCivic: street and street number of the streets
//alwaysOpen: the site is always open to visit
//priority: the turistic weight of the site
//weight: sum of priority and distance from the headSite (redundant/not used)
//picture: picture of the site
//visitTime: keep tracks of the distance between this site and the headsite
//adjacencySite: array with the N sites immediatly closer to the site
//opening/tickets/contacts: keeps the json string for the 3 records
//showingTime: the time for the user to arrive at this site
