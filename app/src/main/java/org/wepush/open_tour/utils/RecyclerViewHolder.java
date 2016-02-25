package org.wepush.open_tour.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.wepush.open_tour.DetailsActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by antoniocoppola on 02/07/15.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    //TODO below txtToSet should be ImageView
    private CircleImageView imageToSet;
    private TextView timeTextToSet;
    private TextView distanceTextToSet;
    private Bitmap bitmap;
    private OpenTourUtils openTourUtils;

    private String idToPassFromCLick,currentCity,placeholderName,language;
    private static Context context;

    private boolean placeholderEverywhere=false;


    public static RecyclerViewHolder newInstance (View view, Context ctx, OpenTourUtils opUtls,String lan){
        CircleImageView imageItem=(CircleImageView)view.findViewById(R.id.imgToPut);
        TextView txtTime=(TextView)view.findViewById(R.id.itemTimeToPut);
        TextView txtDistance=(TextView)view.findViewById(R.id.itemDistanceToPut);
        context=ctx;

        return new RecyclerViewHolder(view,imageItem,txtTime,txtDistance, opUtls,lan);


    }


    public RecyclerViewHolder(final View parent, CircleImageView newImageToSet,TextView newTimeToSet,TextView newDistanceToSet,OpenTourUtils opUt,String lan) {
        super(parent);

        openTourUtils=opUt;
        language=lan;
        String packagesChosen=Repository.retrieve(context, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }


            imageToSet=newImageToSet;
            timeTextToSet=newTimeToSet;
            distanceTextToSet=newDistanceToSet;


        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context, DetailsActivity.class);
                i.putExtra("siteId",idToPassFromCLick);
                i.setAction(Constants.INTENT_FROM_LIVEMAP);
                context.startActivity(i);


            }
        });

    }

    public void setTextInViewHolder(String id,String time,String distance){

        idToPassFromCLick=id;


        Site site=DB1SqlHelper.getInstance(context).getSite(idToPassFromCLick);
        String imagePath=site.pictureUrl;

        currentCity=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);

//TODO 30 novembre: modifiche per introduzione OpenTourUtils

//        openTourUtils.setImage(site.pictureUrl,language,imageToSet,site.typeOfSite,false);
//            OpenTourUtils.setImage(site.pictureUrl,language,imageToSet,site.typeOfSite,false);



        switch (currentCity){


            case Constants.CITY_MILAN:

            if ((TextUtils.equals(imagePath, "placeholder"))|| (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(site);
                } else {
                    chooseThemeColors(site);
                }
                imagePath=placeholderName;
                int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
//                imageToSet.setImageResource(drawableResource);
                Glide.with(context)
                        .load(drawableResource)
                        .into(imageToSet);


            } else {
                imagePath = imagePath.substring(79, imagePath.length() - 4);
                imagePath = "milano_images/" + imagePath + ".jpg";
                     Glide.with(context)
                        .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
                        .into(imageToSet);
            }

                break;

            case Constants.CITY_PALERMO:

                        if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                            convertLanguageTypeOfSite(site);
                        } else {
                            chooseThemeColors(site);
                        }

                        imagePath=placeholderName;

                        int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                        Glide.with(context)
                                .load(drawableResource)
                                .into(imageToSet);
                    } else {
                        imagePath =imagePath.substring(79, imagePath.length() - 4);
                        imagePath="palermo_images/"+imagePath+".jpg";
                        Glide.with(context)
                                .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
                                .into(imageToSet);
                    }

            break;


            case Constants.CITY_TURIN:

                if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                    if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                        convertLanguageTypeOfSite(site);
                    } else {
                        chooseThemeColors(site);
                    }

                    imagePath=placeholderName;

                    int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                    Glide.with(context)
                            .load(drawableResource)
                            .into(imageToSet);
                } else {
                    imagePath =imagePath.substring(79, imagePath.length() - 4);
                    imagePath="turin_images/"+imagePath+".jpg";
                    Glide.with(context)
                            .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
                            .into(imageToSet);
                }

                break;






        }


//        if (currentCity.equals(Constants.CITY_MILAN)) {
//
//            if ((TextUtils.equals(imagePath, "placeholder"))|| (placeholderEverywhere)) {
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//                imagePath=placeholderName;
//                int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
////                imageToSet.setImageResource(drawableResource);
//                Glide.with(context)
//                        .load(drawableResource)
//                        .into(imageToSet);
//
//
//            } else {
//                imagePath = imagePath.substring(79, imagePath.length() - 4);
//                imagePath = "milano_images/" + imagePath + ".jpg";
//                     Glide.with(context)
//                        .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
//                        .into(imageToSet);
//            }
//        } else {
//            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//
//                imagePath=placeholderName;
//
//                int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
//                Glide.with(context)
//                        .load(drawableResource)
//                        .into(imageToSet);
//            } else {
//                imagePath =imagePath.substring(79, imagePath.length() - 4);
//                imagePath="palermo_images/"+imagePath+".jpg";
//                Glide.with(context)
//                        .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
//                        .into(imageToSet);
//            }
//
//        }
    //TODO fine

        timeTextToSet.setText(time);
        distanceTextToSet.setText(distance.substring(0,4)+" km");
    }


    public void setBackgroundAsHighlighted(){
        imageToSet.setBackground(context.getResources().getDrawable(R.drawable.highlighted_placeholders));
    }



    private void chooseThemeColors(Site site) {

//TODO check if there's a better solution to manage sitesType in languages
        switch (site.typeOfSite) {
            case "Teatri":

                placeholderName=context.getResources().getString(R.string.placeholderTheaters);
                break;

            case "Palazzi e Castelli":

                placeholderName=context.getResources().getString(R.string.placeholderCastles);

                break;

            case "Ville, Giardini e Parchi":

                placeholderName=context.getResources().getString(R.string.placeholderVillas);

                break;

            case "Musei e Gallerie d'arte":

                placeholderName=context.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statue e Fontane":


                placeholderName=context.getResources().getString(R.string.placeholderStatues);

                break;

            case "Piazze e Strade":

                placeholderName=context.getString(R.string.placeholderSquares);


                break;

            case "Archi, Porte e Mura":

                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fiere e Mercati":

                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cimiteri e Memoriali":

                placeholderName=context.getResources().getString(R.string.placeholderCemeteries);

                break;


            case "Edifici":

                placeholderName=context.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Ponti":

                placeholderName=context.getResources().getString(R.string.placeholderBridges);

                break;

            case "Chiese, Oratori e Luoghi di culto":

                placeholderName=context.getResources().getString(R.string.placeholderChurches);

                break;

            case "Altri monumenti e Luoghi di interesse":

                placeholderName=context.getResources().getString(R.string.placeholderOtherSites);
                break;

        }

    }


    private void convertLanguageTypeOfSite(Site site) {


        switch (site.typeOfSite) {
            case "Theaters":

                placeholderName=context.getResources().getString(R.string.placeholderTheaters);

                break;

            case "Palaces and Castles":

                placeholderName=context.getResources().getString(R.string.placeholderCastles);

                break;

            case "Villas, Gardens and Parks":

                placeholderName=context.getResources().getString(R.string.placeholderVillas);

                break;

            case "Museums and Art galleries":

                placeholderName=context.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statues and Fountains":

                placeholderName=context.getResources().getString(R.string.placeholderStatues);

                break;

            case "Squares and Streets":

                placeholderName=context.getResources().getString(R.string.placeholderSquares);


                break;

            case "Arches, Gates and Walls":

                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fairs and Markets":

                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cemeteries and Memorials":

                placeholderName=context.getResources().getString(R.string.placeholderCemeteries);
                break;

            case "Buildings":

                placeholderName=context.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Bridges":

                placeholderName=context.getResources().getString(R.string.placeholderBridges);

                break;

            case "Churches, Oratories and Places of worship":

                placeholderName=context.getResources().getString(R.string.placeholderChurches);

                break;

            case "Other monuments and Places of interest":

                placeholderName=context.getResources().getString(R.string.placeholderOtherSites);

                break;

        }


    }

}
