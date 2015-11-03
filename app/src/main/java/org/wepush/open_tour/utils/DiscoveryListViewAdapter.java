package org.wepush.open_tour.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.wepush.open_tour.DetailsActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by antoniocoppola on 28/10/15.
 */
public class DiscoveryListViewAdapter extends BaseAdapter {

    private ArrayList<Site>elementsToShow;
    private Context context;
    private boolean placeholderEverywhere=false;
    private LayoutInflater layoutInflater;
    private String placeholderName;
    private Intent intent;

    public DiscoveryListViewAdapter (Context ctx, ArrayList<Site> aListing){
        elementsToShow=aListing;
//        Log.d("miotag"," in discoveryListAdapter, array dimensioni: "+elementsToShow.size());
//        Log.d("miotag","il primo elemento dell'array è: "+elementsToShow.get(0).name);
//        Log.d("miotag","l'ultimo dell'array è: "+elementsToShow.get(elementsToShow.size()-1).name);
        context=ctx;
        layoutInflater = LayoutInflater.from(context);
        String packagesChosen=Repository.retrieve(context, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }
        intent=new Intent(context, DetailsActivity.class);


    }

    @Override
    public Object getItem(int position) {
//        Log.d("miotag","getting item: "+elementsToShow.get(position));
        return elementsToShow.get(position);
    }

    @Override
    public int getCount() {
//        Log.d("miotag"," ho chiamato getCount con il conto di: "+elementsToShow.size());
        return elementsToShow.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
//            Log.d("miotag","coverView è nullo");
            convertView = layoutInflater.inflate(R.layout.item_listview_discovery, null);
            holder = new ViewHolder();
            holder.vTitle = (TextView) convertView.findViewById(R.id.textTitleItemListview);
            holder.vAddress = (TextView) convertView.findViewById(R.id.textBodyItemListview);
//            holder.vTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.vPlaceHolder= (ImageView) convertView.findViewById(R.id.cvListViewElement);

            convertView.setTag(holder);
        } else {
//            Log.d("miotag","covertView non è nullo");
            holder = (ViewHolder) convertView.getTag();
        }

        holder.vTitle.setText(elementsToShow.get(position).name);
        holder.vAddress.setText(elementsToShow.get(position).address+", "+elementsToShow.get(position).addressCivic);

        Bitmap bitmap;
        //TODO cambiamento: invece di recuperare dal db il sito, utilizzo la lista di siti passato
//        Site site= DB1SqlHelper.getInstance(context).getSite(elementsToShow.get(position).id);
//        String imagePath=site.pictureUrl;
        String imagePath= elementsToShow.get(position).pictureUrl;
        String categoryOfThisSite=elementsToShow.get(position).typeOfSite;

        String currentCity=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);


        if (currentCity.equals(Constants.CITY_MILAN)) {

            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(categoryOfThisSite);
                } else {
                    chooseThemeColors(categoryOfThisSite);
                }
                imagePath=placeholderName;
                int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                holder.vPlaceHolder.setImageResource(drawableResource);

            } else {
                imagePath = imagePath.substring(79, imagePath.length() - 4);
                imagePath = "milano_images/" + imagePath + ".jpg";
//                Log.d("miotag", "immagine da mostrare: " + imagePath);
                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
                holder.vPlaceHolder.setImageDrawable(mDrawable);
            }
        } else { //Palermo
            if ((TextUtils.equals(imagePath, "placeholder"))|| (placeholderEverywhere)) {
                Log.d("miotag","sono nel caso placeholder");
                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {

                    convertLanguageTypeOfSite(categoryOfThisSite);
                } else {
                    chooseThemeColors(categoryOfThisSite);
                }

                imagePath=placeholderName;
                Log.d("miotag","placeholder: "+placeholderName);
                int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                holder.vPlaceHolder.setImageResource(drawableResource);
            } else {
                imagePath =imagePath.substring(79, imagePath.length() - 4);
                imagePath="palermo_images/"+imagePath+".jpg";
                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
                holder.vPlaceHolder.setImageDrawable(mDrawable);

            }

        }




        return convertView;
    }




    static class ViewHolder {
        TextView vTitle;
        TextView vAddress;
        ImageView vPlaceHolder;

    }






//    private void chooseThemeColors(Site site) {
//        Log.d("miotag","TimeLine ChooseTheme: "+site.typeOfSite);

//TODO check if there's a better solution to manage sitesType in languages
    private void chooseThemeColors(String s){
        switch (s) {
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


//    private void convertLanguageTypeOfSite(Site site) {

    private void convertLanguageTypeOfSite(String s){
//TODO anche qui passo direttamente la stringa e non l'intero sito
        Log.d("miotag","stringa da confrontare per ottenere il giusto placeholder "+s);
        switch (s) {
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

//        Log.d("miotag","PLACEHOLDER in inglese"+ placeholderName);
    }
}