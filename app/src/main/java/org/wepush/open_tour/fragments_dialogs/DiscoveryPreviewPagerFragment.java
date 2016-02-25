package org.wepush.open_tour.fragments_dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wepush.open_tour.DetailsActivity;
import org.wepush.open_tour.HomeActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.DiscoveryListViewAdapter;
import org.wepush.open_tour.utils.OpenTourUtils;
import org.wepush.open_tour.utils.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by antoniocoppola on 05/10/15.
 */
public class DiscoveryPreviewPagerFragment extends Fragment implements View.OnClickListener{

    private ImageView previewMainImage,previewNlImage,previewNrImage,previewSlImage,previewSrImage,circleImage0,circleImage1,circleImage2,circleImage3,circleImage4;
    private TextView txtMain,txtNl,txtNr,txtSl,txtSr;
    private ArrayList<Site> arrayList,arra;
    private ArrayList<ImageView>arrayImageReferencesList,arrayCircleList;
    private ArrayList<TextView> arrayTextViewList,arrayTextBodyList;
    private static Context context;
    private String placeholderName="";
    private String city="";
    private String tabTypeOfSiteSelected="";
    private Intent i;
    private static int tabPosition;
//    private CardView cd0,cd1,cd2,cd3,cd4;
    private boolean placeholderEverywhere=false;
    private String categoryForListView,language;

    private  Intent intent;

    private ListView lwDiscovery;

    private ArrayList<Site> museumsForList,churchesForList,villasForList,palacesForList,arrayListVillas,arrayListMuseums,arrayListChurches,arrayListPalaces;



    public static DiscoveryPreviewPagerFragment newInstance(Context ctx,String  category, int position) {
        context=ctx;
        DiscoveryPreviewPagerFragment fragment = new DiscoveryPreviewPagerFragment();
        Bundle bundle = new Bundle();
        tabPosition=position;

        switch (category){
            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_MUSEUM);
                break;

            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_CASTLES);
                break;

            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_CHURCH);
                break;

            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_VILLAS);
                break;

        }

        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discoverypreviewadapter, null);
        city=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);


        language=Repository.retrieve(context,Constants.USER_LANGUAGE,String.class);


        //TODO 04novembre section for ListView
        museumsForList=new ArrayList<>();
        palacesForList=new ArrayList<>();
        villasForList=new ArrayList<>();
        churchesForList=new ArrayList<>();

        Gson gson=new Gson();
        Type type = new TypeToken<ArrayList<Site>>() {}.getType();
        String jsonMuseums=Repository.retrieve(context, Constants.JSON_MUSEUMS, String.class);
        museumsForList=gson.fromJson(jsonMuseums, type);

        gson=new Gson();
        String jsonChurches=Repository.retrieve(context, Constants.JSON_CHURCHES, String.class);
        churchesForList=gson.fromJson(jsonChurches, type);

        gson=new Gson();
        String jsonPalaces=Repository.retrieve(context, Constants.JSON_PALACE, String.class);
        palacesForList=gson.fromJson(jsonPalaces, type);

        gson=new Gson();
        String jsonVillas=Repository.retrieve(context, Constants.JSON_VILLAS, String.class);
        villasForList=gson.fromJson(jsonVillas, type);

        //TODO fine

        //Deserialization for showcaserPictures

        gson=new Gson();
        String jsonArrayVillas=Repository.retrieve(context, Constants.JSON_ARRAYLIST_VILLAS, String.class);
        arrayListVillas=gson.fromJson(jsonArrayVillas, type);

        gson=new Gson();
        String jsonArrayMuseums=Repository.retrieve(context, Constants.JSON_ARRAYLIST_MUSEUMS, String.class);
        arrayListMuseums=gson.fromJson(jsonArrayMuseums, type);

        gson=new Gson();
        String jsonArrayChurches=Repository.retrieve(context, Constants.JSON_ARRAYLIST_CHURCHES, String.class);
        arrayListChurches=gson.fromJson(jsonArrayChurches, type);

        gson=new Gson();
        String jsonArrayPalaces=Repository.retrieve(context, Constants.JSON_ARRAYLIST_PALACES, String.class);
        arrayListPalaces=gson.fromJson(jsonArrayPalaces, type);






        lwDiscovery=(ListView) view.findViewById(R.id.lwDiscoveryActivity);
        lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, HomeActivity.churches));
        setListViewHeightBasedOnChildren(lwDiscovery);
        lwDiscovery.setFocusable(false);
//        lwDiscovery.setOnItemClickListener(this);

        intent = new Intent(context,DetailsActivity.class);


        lwDiscovery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (categoryForListView != null){
                    switch (categoryForListView){
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            intent.putExtra("siteId",SettingTourActivity.museums.get(position).id);
                            intent.putExtra("siteId",museumsForList.get(position).id);
                            startActivity(intent);
                        break;


                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            intent.putExtra("siteId", SettingTourActivity.villas.get(position).id);
                            intent.putExtra("siteId",villasForList.get(position).id);
                            startActivity(intent);
                            break;


                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            intent.putExtra("siteId",SettingTourActivity.churches.get(position).id);
                            intent.putExtra("siteId",churchesForList.get(position).id);
                            startActivity(intent);
                            break;


                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            intent.putExtra("siteId",SettingTourActivity.palaces.get(position).id);
                            intent.putExtra("siteId",palacesForList.get(position).id);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });







//TODO semplificazione
        arrayList=new ArrayList<>();
        arrayImageReferencesList=new ArrayList<ImageView>();
        arrayTextViewList=new ArrayList<>();

        previewMainImage=(ImageView) view.findViewById(R.id.discoveryPreviewMainImage);
        previewNlImage=(ImageView) view.findViewById(R.id.discoverPreviewImageNl);
        previewNrImage=(ImageView) view.findViewById(R.id.discoverPreviewImageNr);
        previewSlImage=(ImageView) view.findViewById(R.id.discoverPreviewImageSl);
        previewSrImage=(ImageView) view.findViewById(R.id.discoverPreviewImageSr);

        txtMain=(TextView) view.findViewById(R.id.txtMainImage);
        txtNl=(TextView) view.findViewById(R.id.txtNlImage);
        txtNr=(TextView) view.findViewById(R.id.txtNrImage);
        txtSl=(TextView) view.findViewById(R.id.txtSlImage);
        txtSr=(TextView) view.findViewById(R.id.txtSrImage);


        arrayImageReferencesList.add(previewMainImage);
        arrayImageReferencesList.add(previewNlImage);
        arrayImageReferencesList.add(previewNrImage);
        arrayImageReferencesList.add(previewSlImage);
        arrayImageReferencesList.add(previewSrImage);

        arrayTextViewList.add(txtMain);
        arrayTextViewList.add(txtNl);
        arrayTextViewList.add(txtNr);
        arrayTextViewList.add(txtSl);
        arrayTextViewList.add(txtSr);

        Bundle bundle=getArguments();
        categoryForListView=bundle.getString("category");


        switch (categoryForListView){
            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_VILLAS;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_VILLAS);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, HomeActivity.villas));

                break;

            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_CHURCH;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_CHURCH);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, HomeActivity.churches));

                break;

            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_CASTLES;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_CASTLES);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, HomeActivity.palaces));


                break;

            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_MUSEUM;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_MUSEUM);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, HomeActivity.museums));


                break;
        }


        return view;
    }


    private void selectAppropriateSite(String s){

        switch (s){

            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                setLayoutInPlace(arrayListMuseums);
            break;

            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                setLayoutInPlace(arrayListPalaces);
            break;

            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                setLayoutInPlace(arrayListChurches);
            break;

            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                setLayoutInPlace(arrayListVillas);
            break;

        }



    }

    private void setLayoutInPlace(ArrayList<Site> arrayList) {
//        String city=Repository.retrieve(context, Constants.KEY_CURRENT_CITY, String.class);
        OpenTourUtils mOpenTourUtils=new OpenTourUtils(context,city);
//        String currentCity = "";

//        Bitmap bitmap;
        for (int i=0; i<arrayList.size();i++) {

            Site site=arrayList.get(i);
            ImageView imageToSet=arrayImageReferencesList.get(i);

            mOpenTourUtils.setImage(site.pictureUrl,language,imageToSet,site.typeOfSite,true);
            TextView textOver=arrayTextViewList.get(i);
            textOver.setText(arrayList.get(i).name);
            textOver.setAllCaps(true);
            imageToSet.setOnClickListener(this);


        }//Fine for
    }//Fine setLayout

    @Override
    public void onClick(View v){

        switch(city){

            case Constants.CITY_PALERMO:
                switch (v.getId()){

                    case R.id.discoveryPreviewMainImage:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31ae0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31760000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31220000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31560000");
                            startActivity(i);
                            break;
                    }
                    break;

                case R.id.discoverPreviewImageNl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b319d0200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31140000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31820000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31060100");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageNr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31960000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31660000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31ed0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31920000");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageSl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31b90200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31a50000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b313c0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31230100");
                            startActivity(i);
                            break;

                    }
                    break;


                case R.id.discoverPreviewImageSr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31980200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae77694d610b31b10300");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b315b0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31f50000");
                            startActivity(i);
                            break;

                    }
                    break;

                }
            break;


            case Constants.CITY_MILAN:

            switch (v.getId()) {


                case R.id.discoveryPreviewMainImage:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5551cf11694d6103fa040100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b7461694d61267e9b0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b78f9694d61267ea70100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5550cbaa694d6103fa4c0000");
                            startActivity(i);
                            break;
                    }
                    break;

                case R.id.discoverPreviewImageNl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5551fe27694d6103fa2b0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5549f352694d61267e880000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b6d88694d61267e5f0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5552156f694d6103faa30100");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageNr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "55530f60694d6103fac80100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b3360694d61267ef90000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b8a6a694d6147f85e0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "55531d6c694d6103faff0100");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageSl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5551cc00694d6103faf10000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b3a3e694d61267e020100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "555c9def694d6103faf70200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5552208e694d6103fabd0100");
                            startActivity(i);
                            break;

                    }
                    break;


                case R.id.discoverPreviewImageSr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5550be56694d6103fa020000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5549e224694d61267e2b0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b8b24694d6147f8670000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b2c3a694d61267ec90000");
                            startActivity(i);
                            break;

                    }
                    break;
             }
            break;


            case Constants.CITY_TURIN:

                switch (v.getId()) {


                    case R.id.discoveryPreviewMainImage:

                        switch (tabTypeOfSiteSelected) {
                            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565dd3ef32733015650001a4");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565dbe1b3273301565000166");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565ed2c132733015650001eb");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "5693bbdc32733015650004ed");
                                startActivity(i);
                                break;
                        }
                        break;

                    case R.id.discoverPreviewImageNl:
                        switch (tabTypeOfSiteSelected) {
                            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565f0a9a3273301565000216");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565ebba332733015650001b2");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "56977b663273301565000617");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "569616c832733015650005e6");
                                startActivity(i);
                                break;

                        }
                        break;

                    case R.id.discoverPreviewImageNr:

                        switch (tabTypeOfSiteSelected) {
                            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565f11203273301565000224");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565daed03273301565000136");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "5694def93273301565000592");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "566832aa3273301565000295");
                                startActivity(i);
                                break;

                        }
                        break;

                    case R.id.discoverPreviewImageSl:
                        switch (tabTypeOfSiteSelected) {
                            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565da6d33273301565000123");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565dcec83273301565000193");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "56682ecf3273301565000285");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "5697d78c3273301565000644");
                                startActivity(i);
                                break;

                        }
                        break;


                    case R.id.discoverPreviewImageSr:

                        switch (tabTypeOfSiteSelected) {
                            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "56938dee32733015650004b6");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "56991e4e327330156500069e");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "5673ec20327330156500033e");
                                startActivity(i);
                                break;

                            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                                i = new Intent(context, DetailsActivity.class);
                                i.putExtra("siteId", "565f151a3273301565000230");
                                startActivity(i);
                                break;

                        }
                        break;
                }





        }


    }




    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



}//fine classe
