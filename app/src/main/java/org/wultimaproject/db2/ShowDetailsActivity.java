package org.wultimaproject.db2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wultimaproject.db2.structures.DB1SqlHelper;
import org.wultimaproject.db2.structures.Opening;
import org.wultimaproject.db2.structures.Site;


import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShowDetailsActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {
    private ImageView mImageDetail;
    //    private View mToolbarView;
    private LinearLayout llToMove;


    private String imagePath;

    private Toolbar mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private int mFlexibleSpaceImageHeight,mActionBarSize;
    private TextView txtForTitle,txtTitleToolbar,txtToDisappear1,txtToDisappear2,txtOpeningsDays,txtTickets,txtContacts,txtOpeningsTime;
    private DocumentView txtDescription;
    private DocumentView txtTips;
    private  int cardBackground;
    private  int toolbarBackground;
    private ImageView imgArrowNavigation;

    private Site siteToShow;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdetails_activity);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = 56;
        mImageDetail = (ImageView)findViewById(R.id.imageDetails);

        llToMove=(LinearLayout)findViewById(R.id.llForText);
        //colore dello sfondo della scheda dettaglio in base al tipo di monumento
        // "amber300" va sostituito con un ciclo SWITCH per ottenere il colore giusto

        Intent intent=getIntent();

        Site siteToShow=DB1SqlHelper.getInstance(this).getSite(intent.getStringExtra("siteId"));

        chooseThemeColors(siteToShow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }


        llToMove.setBackground(getResources().getDrawable(cardBackground));



       mToolbarView = (Toolbar)findViewById(R.id.toolbaring);
       mToolbarView.setTitle("");

        setSupportActionBar(mToolbarView);

        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(toolbarBackground)));



        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        txtForTitle=(TextView) findViewById(R.id.txtForTitle);
        txtTitleToolbar=(TextView) findViewById(R.id.txtTitleToolbar);
        txtDescription=(DocumentView) findViewById(R.id.txtShowDetailsDescription);
        txtTips=(DocumentView)findViewById(R.id.txtShowDetailsTips);
        txtToDisappear1=(TextView) findViewById(R.id.txtToDisappear1);
        txtToDisappear2=(TextView)findViewById(R.id.txtToDisappear2);
        txtOpeningsDays=(TextView)findViewById(R.id.txtOpeningsDays);
        txtOpeningsTime=(TextView)findViewById(R.id.txtOpeningsTime);
        txtTickets=(TextView)findViewById(R.id.txtTicketsDetails);
        txtContacts=(TextView)findViewById(R.id.txtContactsDetails);


        txtTitleToolbar.setTextColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.amber400)));

        mParallaxImageHeight=100;





        if(TextUtils.equals(siteToShow.pictureUrl,"placeholder")){
            imagePath="header_milan";
        } else {
            Log.d("miotag","SiteToShow PICTURE: "+siteToShow.pictureUrl);
            imagePath = siteToShow.pictureUrl.substring(79, siteToShow.pictureUrl.length()-4);
            Log.d("miotag","imagePAth: "+imagePath);
        }

//        Ottengo la giusta immagine del monumento da impostare nella toolbar
        int drawableResource=this.getResources().getIdentifier(imagePath, "drawable", this.getPackageName());

        mImageDetail.setBackground(getResources().getDrawable(drawableResource));
        txtTitleToolbar.setText(siteToShow.name);
        txtForTitle.setText(siteToShow.name);
        txtDescription.setText(siteToShow.description);
        txtTips.setText(siteToShow.tips);

        if(TextUtils.equals(siteToShow.addressCivic,"null") || siteToShow.addressCivic.length()>4){
            txtToDisappear1.setText(siteToShow.address);
        } else{
            txtToDisappear1.setText(siteToShow.address+", "+siteToShow.addressCivic);

        }


        imgArrowNavigation=(ImageView) findViewById(R.id.imageArrowNavigation);
        imgArrowNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //check for opening/tickets/contacts: if empty, then set View.GONE for their Layouts
       if (siteToShow.alwaysOpen==1){
           RelativeLayout rlOpening=(RelativeLayout)findViewById(R.id.rlOpenings);
           rlOpening.setVisibility(View.GONE);
           View viewOpening=(View)findViewById(R.id.viewOpenings);
           viewOpening.setVisibility(View.GONE);
           txtToDisappear2.setText("Sempre Aperto");

       }


        Log.d("miotag","TICKETS outside IF "+siteToShow.tickets);
        Log.d("miotag","TICKETS outside IF with ToString"+ siteToShow.tickets.toString());

        if (TextUtils.equals(siteToShow.tickets.toString(),"[]")){
            Log.d("miotag","TICKETS ASSENTE");
            RelativeLayout rlTickets=(RelativeLayout)findViewById(R.id.rlTickets);
            rlTickets.setVisibility(View.GONE);

        }

        if (TextUtils.equals(siteToShow.contacts.toString(),"[]")){

            RelativeLayout rlContacts=(RelativeLayout)findViewById(R.id.rlTickets);
            rlContacts.setVisibility(View.GONE);
            ImageView pinContact=(ImageView)findViewById(R.id.pinPhone);
            pinContact.setVisibility(View.INVISIBLE);
        }


//decision based on siteType

// Section for Openings/Contacts/Tickets


        showOpenings(siteToShow);
        showTickets(siteToShow);
        showContacts(siteToShow);



    }//fine onCreate


    @Override
    public void onStop(){
        super.onStop();
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {


        //this is to parallax the image

        ViewHelper.setTranslationY(mImageDetail, scrollY / 1.2f);
        //this is to change toolbar's alpha



        int baseColor=getResources().getColor(toolbarBackground);
        int cardColor = getResources().getColor(cardBackground);


        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);

        int scrollYY=scrollY-250;
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        float beta = Math.min(1, (float) scrollYY / mParallaxImageHeight);


        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor)); //pre 09/06 = baseColor, post 09/06 toolbarBackground
        ViewHelper.setPivotX(txtForTitle, txtForTitle.getLeft());
        ViewHelper.setPivotY(txtForTitle, txtForTitle.getTop());


        //trasparenza per la scheda riassuntiva: parte da scrolly=250
        if (scrollY>250) {
            llToMove.setBackgroundColor(ScrollUtils.getColorWithAlpha(1 - beta, cardColor));//pre 09/06 = cardColor, post -> cardBackground
//            txtTitleToolbar.setTextColor(ScrollUtils.getColorWithAlpha(1-beta, getResources().getColor(R.color.white)));

            if (scrollY >  270){

                txtTitleToolbar.setTextColor(ScrollUtils.getColorWithAlpha(beta-0.3f, getResources().getColor(R.color.white)));

            } else {
                txtTitleToolbar.setTextColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.white)));
            }

        } else {
            llToMove.setBackgroundColor(cardColor);//pre 09/06 = cardColor, post -> cardBackground
//           txtTitleToolbar.setTextColor(ScrollUtils.getColorWithAlpha(0,getResources().getColor(R.color.amber400)));
        }


//scale per l'allargamento del testo (titolo card riassuntiva)

        float scale= 1+ ((float)scrollY/700f);
        ViewHelper.setScaleX(txtForTitle,scale);
        ViewHelper.setScaleY(txtForTitle, scale);

        txtForTitle.setTextColor(ScrollUtils.mixColors(getResources().getColor(R.color.white), getResources().getColor(R.color.white), Math.min(1, alpha)));
        txtToDisappear1.setTextColor(ScrollUtils.getColorWithAlpha(1-alpha, getResources().getColor(R.color.white)));
        txtToDisappear2.setTextColor(ScrollUtils.getColorWithAlpha(1-alpha,getResources().getColor(R.color.white)));


    }
    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }




    private void showOpenings(Site site){

        String jsonOpeningsFromSite=site.openings;
        JSONArray jsonOpenings=new JSONArray();
        String dateFrom,dateTo, timeFrom,timeTo;
        JSONObject singleOpeningFromObject=new JSONObject();
        JSONArray daysFromOpenings=new JSONArray();
        ArrayList<String> daysArray=new ArrayList<>();

        String openings="";

        try {
            jsonOpenings = new JSONArray(jsonOpeningsFromSite);

            if (jsonOpenings != null && jsonOpenings.length() > 0){
                for (int i=0; i<jsonOpenings.length(); i++){
                    singleOpeningFromObject=jsonOpenings.getJSONObject(i);
                    Log.d("miotag","OPENINGS daysFromArray: "+singleOpeningFromObject);
                    dateFrom=singleOpeningFromObject.getString("date_from");
                    dateTo=singleOpeningFromObject.getString("date_to");
                    timeFrom=singleOpeningFromObject.getString("time_from");
                    timeTo=singleOpeningFromObject.getString("time_to");
                    daysFromOpenings=singleOpeningFromObject.getJSONArray("days");

                    for (int j=0; j<daysFromOpenings.length();j++) {
                            Log.d("miotag","I GIORNI CHE STO INSERENDO: "+daysFromOpenings.getString(j));
                            daysArray.add(daysFromOpenings.getString(j));
                        }

                    ArrayList<String> translatedDays=translatingDays(daysArray);
                    if (translatedDays.size()==7) {
                        txtOpeningsDays.setText("Tutti i giorni");
                    } else {
                        for (String d : translatedDays) {
                            openings = openings + d + " ";

                        }
//                        openings.lastIndexOf("-",0);
//                            openings=openings.substring(0,openings.length());
//                        Log.d("miotag"," ed infine i giorni openings: \n"+openings);
                            txtOpeningsDays.setText(openings);
//                        txtOpenings.setText(openings.substring(0,(openings.length()-3)));
                    }

                    //da qui gli orari
                    String openingTime="Apertura dalle ore "+timeFrom+" alle "+timeTo;
                    txtOpeningsTime.setText(openingTime);
                    txtToDisappear2.setText(timeFrom+" - "+timeTo);





                }
            }



        } catch(JSONException e){
            e.printStackTrace();

        }

    }


    private void showTickets(Site site){

        JSONArray jsonTickets=new JSONArray();
        String typeTickets,descriptionTickets,priceTickets;
        ArrayList<String> stringInRecipients=new ArrayList<String>();
        String ticketsJson=site.tickets;
        ArrayList<ArrayList<String>> allTickets=new ArrayList<ArrayList<String>>();
        ArrayList<String>singleTicketsInstance=new ArrayList<String>();

        String resultTickets="";


        try {
            jsonTickets = new JSONArray(ticketsJson);

           if(jsonTickets!=null && jsonTickets.length()>0) {
                for (int i = 0; i < jsonTickets.length(); i++) {
                    JSONObject jsonTick=jsonTickets.getJSONObject(i); //got the i-position of ticket array as Object
                    typeTickets=jsonTick.getString("type");
                    descriptionTickets=jsonTick.getString("description");
                    priceTickets=jsonTick.getString("price");
//                   recipientsTickets=jsonTick.getString("recipients").toString();

//                    Log.d("miotag", "da tickets: " + recipientsTickets);
//                    if (TextUtils.equals(typeTickets,"Gratuito")){
//                        singleTicketsInstance.add(typeTickets);
//                        singleTicketsInstance.add(descriptionTickets);
//                    } else {
//                        singleTicketsInstance.add(typeTickets);
//                        singleTicketsInstance.add(descriptionTickets);
//                        singleTicketsInstance.add(priceTickets);
//                    }

                    if (TextUtils.equals(descriptionTickets,"")) {
                        resultTickets = resultTickets + typeTickets  + ": " + priceTickets + "€" + "\n\n";
                    }
                        else {
                        resultTickets = resultTickets + typeTickets + ": " + priceTickets + "€"+"\n"+descriptionTickets+"\n\n";

                    }


                }//every tickets is in allTickets that is now printed
//
//                   Log.d("miotag","ALLTICKETS : "+allTickets.toString());
//
//
//               txtTickets.setText(printTicket(allTickets));
            }

            txtTickets.setText(resultTickets);
        } catch(JSONException e){
            e.printStackTrace();

        }

    }




    private void showContacts(Site site){

        String resultContacts="";
        JSONArray jsonContact;
        String contactJson=site.contacts;
        String typeContacts,descriptionContacts,valueContacts;



        try {
            jsonContact = new JSONArray(contactJson);

            if(jsonContact!=null && jsonContact.length()>0) {
                for (int i = 0; i < jsonContact.length(); i++) {
                    JSONObject jsonTick=jsonContact.getJSONObject(i); //got the i-position of ticket array as Object
                    typeContacts=jsonTick.getString("type");
                    descriptionContacts=jsonTick.getString("description");
                    valueContacts=jsonTick.getString("value");


                    if (TextUtils.equals(descriptionContacts,"")) {
                        resultContacts = resultContacts+ typeContacts  + ": " + valueContacts+  "\n\n";
                    }
                    else {
                        resultContacts= resultContacts + typeContacts+ ": " + valueContacts + "\n"+"\n"+descriptionContacts+"\n\n";

                    }


                }
            }

            txtContacts.setText(resultContacts);
        } catch(JSONException e){
            e.printStackTrace();

        }

    }

    private void chooseThemeColors(Site site){

        switch(site.typeOfSite){
            case "Teatri":
                cardBackground=getResources().getIdentifier("TeatriLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("TeatriDark", "color", this.getPackageName());
                break;

            case "Palazzi e Castelli":
                cardBackground=this.getResources().getIdentifier("PalazziLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("PalazziDark", "color", this.getPackageName());
                break;

            case "Ville, Giardini e Parchi":
                cardBackground=this.getResources().getIdentifier("VilleLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("VilleDark", "color", this.getPackageName());
                break;

            case "Musei e Gallerie d'arte":
                cardBackground=this.getResources().getIdentifier("MuseiLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("MuseiDark", "color", this.getPackageName());
                break;

            case "Statue e Fontane":

                cardBackground=this.getResources().getIdentifier("StatueLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("StatueDark", "color", this.getPackageName());
                break;

            case "Piazze e Strade":
                Log.d("miotag","Piazza e Strade selezionate");
                cardBackground=getResources().getIdentifier("PiazzeLight", "color", this.getPackageName());
                toolbarBackground=getResources().getIdentifier("PiazzeDark", "color", this.getPackageName());

                break;

            case "Archi, Porte e Mura":
                cardBackground=this.getResources().getIdentifier("ArchiLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("ArchiDark", "color", this.getPackageName());
                break;

            case "Fiere e Mercati":
                cardBackground=this.getResources().getIdentifier("MercatiLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("MercatiDark", "color", this.getPackageName());
                break;

            case "Cimiteri e Memoriali":
                cardBackground=this.getResources().getIdentifier("CimiteriLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("CimiteriDark", "color", this.getPackageName());

            case "Edifici":
                cardBackground=this.getResources().getIdentifier("EdificiLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("EdificiDark", "color", this.getPackageName());
                break;

            case "Ponti":
                cardBackground=this.getResources().getIdentifier("PontiLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("PontiDark", "color", this.getPackageName());
                break;

            case "Chiese, Oratori e Luoghi di culto":
                cardBackground=this.getResources().getIdentifier("ChieseLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("ChieseDark", "color", this.getPackageName());
                break;

            case "Altri monumenti e Luoghi di interesse":
                cardBackground=this.getResources().getIdentifier("AltriMonumentiLight", "color", this.getPackageName());
                toolbarBackground=this.getResources().getIdentifier("AltriMonumentiDark", "color", this.getPackageName());
                break;

        }

    }

    private ArrayList<String> translatingDays(ArrayList<String> array){
        ArrayList<String> sToReturn=new ArrayList<String>();
        for (String s : array){
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



}//fine classe
