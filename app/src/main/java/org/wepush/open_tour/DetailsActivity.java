package org.wepush.open_tour;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by antoniocoppola on 30/09/15.
 */
public class DetailsActivity extends AppCompatActivity {

    private Intent intentThatGeneratedThisActivity;
    private String currentCity,placeholderName,imagePath;
    private int toolbarBackground,cardBackground;
    private AppBarLayout appBarLayout;
    private TextView txtTitleToolbar,txtToDisappear1,txtToDisappear2,txtOpeningsDays,txtOpeningsTime,txtTickets,txtContacts;
    private DocumentView txtDescription,txtTips;
    private ImageView mImageDetail,imgArrowNavigation;
    private LinearLayout llForText,llInternalTips,llTickets,llContacts,llOpenings,llOpeningsToAdd;
    private Bitmap bitmap;
    private boolean placeholderEverywhere=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        intentThatGeneratedThisActivity = getIntent();
        Site siteToShow = DB1SqlHelper.getInstance(this).getSite(intentThatGeneratedThisActivity.getStringExtra("siteId"));

        currentCity= Repository.retrieve(this, Constants.KEY_CURRENT_CITY, String.class);
        String packagesChosen=Repository.retrieve(this, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }
        chooseThemeColors(siteToShow);
        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
            convertLanguageTypeOfSite(siteToShow);
        } else {
            chooseThemeColors(siteToShow);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(siteToShow.name);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));

        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(toolbarBackground));

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(cardBackground));


        txtTitleToolbar = (TextView) findViewById(R.id.txtTitleToolbar);
        txtDescription = (DocumentView) findViewById(R.id.txtShowDetailsDescription);
        txtTips = (DocumentView) findViewById(R.id.txtShowDetailsTips);
        txtToDisappear1 = (TextView) findViewById(R.id.txtToDisappear1);
        txtToDisappear2 = (TextView) findViewById(R.id.txtToDisappear2);
        txtOpeningsDays = (TextView) findViewById(R.id.txtOpeningsDays);
        txtOpeningsTime = (TextView) findViewById(R.id.txtOpeningsTime);
        txtTickets = (TextView) findViewById(R.id.txtTicketsDetails1);
        txtContacts = (TextView) findViewById(R.id.txtContactsDetails1);
        mImageDetail = (ImageView)findViewById(R.id.imageDetail);
        llForText=(LinearLayout) findViewById(R.id.llForText);
        llInternalTips=(LinearLayout)findViewById(R.id.llInternalTips);
        llTickets = (LinearLayout) findViewById(R.id.llTickets);
        llContacts = (LinearLayout) findViewById(R.id.llTickets);
        llOpenings = (LinearLayout) findViewById(R.id.llOpenings);
        llOpeningsToAdd=(LinearLayout)findViewById(R.id.llToAddOpenings);

        llForText.setBackgroundColor(getResources().getColor(toolbarBackground));

        imgArrowNavigation = (ImageView) findViewById(R.id.imageArrowNavigation);
        imgArrowNavigation.setColorFilter(getResources().getColor(R.color.white));
        imgArrowNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(intentThatGeneratedThisActivity.getAction(), Constants.INTENT_FROM_LIVEMAP)) {
                    Intent i = new Intent(getBaseContext(), LiveMapActivity.class);
                    i.setAction(Constants.INTENT_FROM_SHOWDETAILS);
                    startActivity(i);
                    finish();
                } else {
                    finish();
                }
            }
        });

        switch(currentCity){

            case Constants.CITY_MILAN:
                if ((TextUtils.equals(siteToShow.pictureUrl, "placeholder")) || (placeholderEverywhere) ){

                imagePath=placeholderName;
                int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
                mImageDetail.setImageResource(drawableResource);
                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);

            } else {
                imagePath = siteToShow.pictureUrl.substring(79, siteToShow.pictureUrl.length() - 4);
                imagePath = "milano_images/" + imagePath + ".jpg";
                bitmap = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
                mImageDetail.setImageDrawable(mDrawable);
                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            break;


            case Constants.CITY_PALERMO:
                if ((TextUtils.equals(siteToShow.pictureUrl, "placeholder")) || (placeholderEverywhere)) {

                imagePath=placeholderName;
                int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
                mImageDetail.setImageResource(drawableResource);
                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);

            } else {
                imagePath = siteToShow.pictureUrl.substring(79, siteToShow.pictureUrl.length() - 4);
                imagePath="palermo_images/"+imagePath+".jpg";
                bitmap = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
                mImageDetail.setImageDrawable(mDrawable);
                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            break;


            case Constants.CITY_TURIN:
                if ((TextUtils.equals(siteToShow.pictureUrl, "placeholder")) || (placeholderEverywhere)) {

                    imagePath=placeholderName;
                    int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
                    mImageDetail.setImageResource(drawableResource);
                    mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);

                } else {
                    imagePath = siteToShow.pictureUrl.substring(79, siteToShow.pictureUrl.length() - 4);
                    imagePath="turin_images/"+imagePath+".jpg";
                    bitmap = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                    Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
                    mImageDetail.setImageDrawable(mDrawable);
                    mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
                }

                break;






        }

//        if (currentCity.equals(Constants.CITY_MILAN)) {
//
//            if ((TextUtils.equals(siteToShow.pictureUrl, "placeholder")) || (placeholderEverywhere) ){
//
//                imagePath=placeholderName;
//                int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
//                mImageDetail.setImageResource(drawableResource);
//                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
//
//            } else {
//                imagePath = siteToShow.pictureUrl.substring(79, siteToShow.pictureUrl.length() - 4);
//                imagePath = "milano_images/" + imagePath + ".jpg";
//                bitmap = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
//                Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
//                mImageDetail.setImageDrawable(mDrawable);
//                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
//            }
//        } else { //palermo
//
//            if ((TextUtils.equals(siteToShow.pictureUrl, "placeholder")) || (placeholderEverywhere)) {
//
//                imagePath=placeholderName;
//                int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());
//                mImageDetail.setImageResource(drawableResource);
//                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
//
//            } else {
//                imagePath = siteToShow.pictureUrl.substring(79, siteToShow.pictureUrl.length() - 4);
//                imagePath="palermo_images/"+imagePath+".jpg";
//                bitmap = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
//                Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
//                mImageDetail.setImageDrawable(mDrawable);
//                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
//            }
//
//        }




        //scheda riassuntiva

        if (siteToShow.alwaysOpen == 1) {

            llOpenings.setVisibility(View.GONE);
            View viewOpening = findViewById(R.id.viewOpenings);
            viewOpening.setVisibility(View.GONE);
            txtToDisappear2.setText("Sempre Aperto");

        }




        //sezione testuale
        txtDescription.setText(siteToShow.description);
        txtTips.setText(siteToShow.tips);

        if (TextUtils.equals(siteToShow.addressCivic, "null") || siteToShow.addressCivic.length() > 4) {
            txtToDisappear1.setText(siteToShow.address);
        } else {
            txtToDisappear1.setText(siteToShow.address + ", " + siteToShow.addressCivic);

        }

        //openings, tickets, contacts

        if(TextUtils.equals(siteToShow.tips.toString(),"")){
            llInternalTips.setVisibility(View.GONE);
        }

        if (TextUtils.equals(siteToShow.tickets.toString(), "[]")) {

//TODO
            llTickets.setVisibility(View.GONE);

        }

        if (TextUtils.equals(siteToShow.contacts.toString(), "[]")) {



            llContacts.setVisibility(View.GONE);

            ImageView pinContact = (ImageView) findViewById(R.id.pinMail);
            pinContact.setVisibility(View.INVISIBLE);
        }


        showOpenings(siteToShow);
        showTickets(siteToShow);
        showContacts(siteToShow);








    }//fine onCreate


    private void chooseThemeColors(Site site) {

//TODO check if there's a better solution to manage sitesType in languages
        switch (site.typeOfSite) {
            case "Teatri":
                cardBackground = this.getResources().getIdentifier("TeatriLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("TeatriDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderTheaters);
                break;

            case "Palazzi e Castelli":
                cardBackground = this.getResources().getIdentifier("PalazziLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("PalazziDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderCastles);

                break;

            case "Ville, Giardini e Parchi":
                cardBackground = this.getResources().getIdentifier("VilleLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("VilleDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderVillas);

                break;

            case "Musei e Gallerie d'arte":
                cardBackground = this.getResources().getIdentifier("MuseiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("MuseiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statue e Fontane":

                cardBackground = this.getResources().getIdentifier("StatueLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("StatueDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderStatues);

                break;

            case "Piazze e Strade":
                Log.d("miotag", "Piazza e Strade selezionate");
                cardBackground = getResources().getIdentifier("PiazzeLight", "color", this.getPackageName());
                toolbarBackground = getResources().getIdentifier("PiazzeDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderSquares);


                break;

            case "Archi, Porte e Mura":
                cardBackground = this.getResources().getIdentifier("ArchiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("ArchiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fiere e Mercati":
                cardBackground = this.getResources().getIdentifier("MercatiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("MercatiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cimiteri e Memoriali":
                cardBackground = this.getResources().getIdentifier("CimiteriLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("CimiteriDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderCemeteries);

                break;


            case "Edifici":
                cardBackground = this.getResources().getIdentifier("EdificiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("EdificiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Ponti":
                cardBackground = this.getResources().getIdentifier("PontiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("PontiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderBridges);

                break;

            case "Chiese, Oratori e Luoghi di culto":
                cardBackground = this.getResources().getIdentifier("ChieseLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("ChieseDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderChurches);

                break;

            case "Altri monumenti e Luoghi di interesse":
                cardBackground = this.getResources().getIdentifier("AltriMonumentiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("AltriMonumentiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderOtherSites);
                break;

        }

    }

    private ArrayList<String> translatingDays(ArrayList<String> array) {
        ArrayList<String> sToReturn = new ArrayList<String>();
        for (String s : array) {
            switch (s) {
                case "mo":
                    sToReturn.add("Lun");
                    break;
                case "tu":
                    sToReturn.add("Mar");
                    break;
                case "we":
                    sToReturn.add("Mer");
                    break;

                case "th":
                    sToReturn.add("Giov");
                    break;
                case "fr":
                    sToReturn.add("Ven");
                    break;
                case "sa":
                    sToReturn.add("Sab");
                    break;
                case "su":
                    sToReturn.add("Dom");
                    break;
            }

        }
        return sToReturn;
    }


    private void convertLanguageTypeOfSite(Site site) {


        switch (site.typeOfSite) {
            case "Theaters":
                cardBackground = this.getResources().getIdentifier("TeatriLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("TeatriDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderTheaters);

                break;

            case "Palaces and Castles":
                cardBackground = this.getResources().getIdentifier("PalazziLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("PalazziDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderCastles);

                break;

            case "Villas, Gardens and Parks":
                cardBackground = this.getResources().getIdentifier("VilleLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("VilleDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderVillas);

                break;

            case "Museums and Art galleries":
                cardBackground = this.getResources().getIdentifier("MuseiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("MuseiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statues and Fountains":

                cardBackground = this.getResources().getIdentifier("StatueLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("StatueDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderStatues);

                break;

            case "Squares and Streets":
                Log.d("miotag", "Piazza e Strade selezionate");
                cardBackground = getResources().getIdentifier("PiazzeLight", "color", this.getPackageName());
                toolbarBackground = getResources().getIdentifier("PiazzeDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderSquares);


                break;

            case "Arches, Gates and Walls":
                cardBackground = this.getResources().getIdentifier("ArchiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("ArchiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fairs and Markets":
                cardBackground = this.getResources().getIdentifier("MercatiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("MercatiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cemeteries and Memorials":
                cardBackground = this.getResources().getIdentifier("CimiteriLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("CimiteriDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderCemeteries);
                break;

            case "Buildings":
                cardBackground = this.getResources().getIdentifier("EdificiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("EdificiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Bridges":
                cardBackground = this.getResources().getIdentifier("PontiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("PontiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderBridges);

                break;

            case "Churches, Oratories and Places of worship":
                cardBackground = this.getResources().getIdentifier("ChieseLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("ChieseDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderChurches);

                break;

            case "Other monuments and Places of interest":
                cardBackground = this.getResources().getIdentifier("AltriMonumentiLight", "color", this.getPackageName());
                toolbarBackground = this.getResources().getIdentifier("AltriMonumentiDark", "color", this.getPackageName());
                placeholderName=this.getResources().getString(R.string.placeholderOtherSites);

                break;

        }


    }




    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    private void showOpenings(Site site) {

        String jsonOpeningsFromSite = site.openings;
        JSONArray jsonOpenings = new JSONArray();
        String dateFrom, dateTo, timeFrom, timeTo;
        JSONObject singleOpeningFromObject = new JSONObject();
        JSONArray daysFromOpenings = new JSONArray();
        ArrayList<String> daysArray = new ArrayList<>();

        String openings = "";

        try {
            jsonOpenings = new JSONArray(jsonOpeningsFromSite);

            if (jsonOpenings != null && jsonOpenings.length() > 0) {
                for (int i = 0; i < jsonOpenings.length(); i++) {
                    if (i==0) {
                        singleOpeningFromObject = jsonOpenings.getJSONObject(i);
//                        dateFrom = singleOpeningFromObject.getString("date_from");
//                        dateTo = singleOpeningFromObject.getString("date_to");
                        timeFrom = singleOpeningFromObject.getString("time_from");
                        timeTo = singleOpeningFromObject.getString("time_to")+"\n";
                        daysFromOpenings = singleOpeningFromObject.getJSONArray("days");
                        for (int j = 0; j < daysFromOpenings.length(); j++) {
                            daysArray.add(daysFromOpenings.getString(j));
                        }

                        ArrayList<String> translatedDays = translatingDays(daysArray);
                        if (translatedDays.size() == 7) {
                            txtOpeningsDays.setText("Tutti i giorni");
                        } else {
                            for (String d : translatedDays) {
                                openings = openings + d + " ";

                            }

                            txtOpeningsDays.setText(openings);

                            //TODO 6ottobre
                            openings = "";
                        }

                        //da qui gli orari
                        String openingTime = getResources().getString(R.string.open_from) + " " + timeFrom + " " + getResources().getString(R.string.open_to) + " " + timeTo;
                        txtOpeningsTime.setText(openingTime);
                        txtToDisappear2.setText(timeFrom + " - " + timeTo);
                    } else {
                        daysArray.clear();
                        //more then 1 opening
                        TextView txtViewToAddForMoreOpening=new TextView(this);
                        TextView txtViewToAddForMoreOpeningTime=new TextView(this);
                        singleOpeningFromObject = jsonOpenings.getJSONObject(i);
                        timeFrom = singleOpeningFromObject.getString("time_from");
                        timeTo = singleOpeningFromObject.getString("time_to")+"\n";
                        daysFromOpenings = singleOpeningFromObject.getJSONArray("days");
                        for (int j = 0; j < daysFromOpenings.length(); j++) {
                            daysArray.add(daysFromOpenings.getString(j));
                        }

                        ArrayList<String> translatedDays = translatingDays(daysArray);
                        if (translatedDays.size() == 7) {
                                  txtViewToAddForMoreOpening.setText("Tutti i giorni");
                                    llOpeningsToAdd.addView(txtViewToAddForMoreOpening);
                        } else {
                            for (String d : translatedDays) {
                                openings = openings + d + " ";

                            }

                            txtViewToAddForMoreOpening.setText(openings);
                            llOpeningsToAdd.addView(txtViewToAddForMoreOpening);


                            //TODO 6ottobre
                            openings = "";
                        }

                        //da qui gli orari
                        String openingTime = getResources().getString(R.string.open_from) + " " + timeFrom + " " + getResources().getString(R.string.open_to) + " " + timeTo;
                        txtViewToAddForMoreOpeningTime.setText(openingTime);
                        llOpeningsToAdd.addView(txtViewToAddForMoreOpeningTime);
                        txtViewToAddForMoreOpeningTime.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;

                    }// fine more then 1 opening




                }
                //insert others TextView programmatically if jsonOpenings has more then 1 element

            }


        } catch (JSONException e) {
            e.printStackTrace();

        }

    }


    private void showTickets(Site site) {

        JSONArray jsonTickets = new JSONArray();
        String typeTickets, descriptionTickets, priceTickets;
        ArrayList<String> stringInRecipients = new ArrayList<String>();
        String ticketsJson = site.tickets;
        ArrayList<ArrayList<String>> allTickets = new ArrayList<ArrayList<String>>();
        ArrayList<String> singleTicketsInstance = new ArrayList<String>();

        String resultTickets = "";


        try {
            jsonTickets = new JSONArray(ticketsJson);

            if (jsonTickets != null && jsonTickets.length() > 0) {
                for (int i = 0; i < jsonTickets.length(); i++) {
                    JSONObject jsonTick = jsonTickets.getJSONObject(i); //got the i-position of ticket array as Object
                    typeTickets = jsonTick.getString("type");
                    descriptionTickets = jsonTick.getString("description");
                    priceTickets = jsonTick.getString("price");


                    if (TextUtils.equals(descriptionTickets, "")) {
                        resultTickets = resultTickets + typeTickets + ": " + priceTickets + "€" + "\n\n";
                    } else {
                        resultTickets = resultTickets + typeTickets + ": " + priceTickets + "€" + "\n" + descriptionTickets + "\n\n";

                    }


                }//every tickets is in allTickets that is now printed


            }

            txtTickets.setText(resultTickets);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }


    private void showContacts(Site site) {

        String resultContacts = "";
        JSONArray jsonContact;
        String contactJson = site.contacts;
        String typeContacts, descriptionContacts, valueContacts;


        try {
            jsonContact = new JSONArray(contactJson);

            if (jsonContact != null && jsonContact.length() > 0) {
                for (int i = 0; i < jsonContact.length(); i++) {
                    JSONObject jsonTick = jsonContact.getJSONObject(i); //got the i-position of ticket array as Object
                    typeContacts = jsonTick.getString("type");
                    descriptionContacts = jsonTick.getString("description");
                    valueContacts = jsonTick.getString("value");


                    if (TextUtils.equals(descriptionContacts, "")) {
                        resultContacts = resultContacts + typeContacts + ": " + valueContacts + "\n\n";
                    } else {
                        resultContacts = resultContacts + typeContacts + ": " + valueContacts + "\n" + "\n" + descriptionContacts + "\n\n";

                    }


                }
            }

            txtContacts.setText(resultContacts);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}
