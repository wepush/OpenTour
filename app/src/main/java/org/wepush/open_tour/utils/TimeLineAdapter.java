package org.wepush.open_tour.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Antonio on 23/04/2015.
 */
public class TimeLineAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private ArrayList<Site>siteToShowInList=new ArrayList<>();
    private Context context;
    private String currentCity;
    private String placeholderName;
    private boolean placeholderEverywhere=false;

    public TimeLineAdapter(Context ctx,ArrayList<Site> showingListOfSite){

        context=ctx;
        siteToShowInList=showingListOfSite;
        layoutInflater = LayoutInflater.from(context);
        String packagesChosen=Repository.retrieve(context, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }

    }

    @Override
    public Object getItem(int position) {
        return siteToShowInList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
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
            holder.vPlaceHolder= (ImageView) convertView.findViewById(R.id.imgPlaceHolder);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.vTitle.setText(siteToShowInList.get(position).name);
        holder.vDescription.setText(siteToShowInList.get(position).address+","+siteToShowInList.get(position).addressCivic);
        holder.vTime.setText(String.valueOf(siteToShowInList.get(position).showingTime));
        Bitmap bitmap;
        Site site=DB1SqlHelper.getInstance(context).getSite(siteToShowInList.get(position).id);
        String imagePath=site.pictureUrl;
        currentCity=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);


        switch(currentCity){

            case Constants.CITY_MILAN:
                    if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                            convertLanguageTypeOfSite(site);
                        } else {
                            chooseThemeColors(site);
                        }
                        imagePath=placeholderName;
                        int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
        //                holder.vPlaceHolder.setImageResource(drawableResource);

                        Glide.with(context)
                                .load(drawableResource)
                                .into(holder.vPlaceHolder);

                    } else {
                        imagePath = imagePath.substring(79, imagePath.length() - 4);
                        imagePath = "milano_images/" + imagePath + ".jpg";
        //                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
        //                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
        //                holder.vPlaceHolder.setImageDrawable(mDrawable);

                        Glide.with(context)
                                .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
                                .into(holder.vPlaceHolder);

                    }
            break;



            case Constants.CITY_PALERMO:

                        if ((TextUtils.equals(imagePath, "placeholder"))|| (placeholderEverywhere)) {

                            if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                                convertLanguageTypeOfSite(site);
                            } else {
                                chooseThemeColors(site);
                            }

                            imagePath=placeholderName;

                            int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
            //                holder.vPlaceHolder.setImageResource(drawableResource);
                            Glide.with(context)
                                    .load(drawableResource)
                                    .into(holder.vPlaceHolder);

                        } else {
                            imagePath =imagePath.substring(79, imagePath.length() - 4);
                            imagePath="palermo_images/"+imagePath+".jpg";
            //                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
            //                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
            //                holder.vPlaceHolder.setImageDrawable(mDrawable);
                            Glide.with(context)
                                    .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
            //                        .placeholder(R.drawable.placeholder_glide)
                                    .into(holder.vPlaceHolder);

                        }

            break;


            case Constants.CITY_TURIN:

                if ((TextUtils.equals(imagePath, "placeholder"))|| (placeholderEverywhere)) {

                    if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                        convertLanguageTypeOfSite(site);
                    } else {
                        chooseThemeColors(site);
                    }

                    imagePath=placeholderName;

                    int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                    Glide.with(context)
                            .load(drawableResource)
                            .into(holder.vPlaceHolder);

                } else {
                    imagePath =imagePath.substring(79, imagePath.length() - 4);
                    imagePath="turin_images/"+imagePath+".jpg";
                    Glide.with(context)
                            .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
                                    //                        .placeholder(R.drawable.placeholder_glide)
                            .into(holder.vPlaceHolder);

                }

                break;





        }



//        if (currentCity.equals(Constants.CITY_MILAN)) {
//
//            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//                imagePath=placeholderName;
//                int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
////                holder.vPlaceHolder.setImageResource(drawableResource);
//
//                Glide.with(context)
//                        .load(drawableResource)
//                        .into(holder.vPlaceHolder);
//
//            } else {
//                imagePath = imagePath.substring(79, imagePath.length() - 4);
//                imagePath = "milano_images/" + imagePath + ".jpg";
////                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
////                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
////                holder.vPlaceHolder.setImageDrawable(mDrawable);
//
//                Glide.with(context)
//                        .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
//                        .into(holder.vPlaceHolder);
//
//            }
//        } else { //Palermo
//            if ((TextUtils.equals(imagePath, "placeholder"))|| (placeholderEverywhere)) {
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
////                holder.vPlaceHolder.setImageResource(drawableResource);
//                Glide.with(context)
//                        .load(drawableResource)
//                        .into(holder.vPlaceHolder);
//
//            } else {
//                imagePath =imagePath.substring(79, imagePath.length() - 4);
//                imagePath="palermo_images/"+imagePath+".jpg";
////                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
////                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
////                holder.vPlaceHolder.setImageDrawable(mDrawable);
//                Glide.with(context)
//                        .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath)
////                        .placeholder(R.drawable.placeholder_glide)
//                        .into(holder.vPlaceHolder);
//
//            }
//
//        }


        return convertView;
    }

    static class ViewHolder {
        TextView vTitle;
        TextView vDescription;
        TextView vTime;
        ImageView vPlaceHolder;

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




