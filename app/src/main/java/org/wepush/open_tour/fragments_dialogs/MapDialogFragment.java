package org.wepush.open_tour.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;

import org.w3c.dom.Text;
import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;
import org.wepush.open_tour.services.LookUpIntentService;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;


/**
 * Created by antoniocoppola on 30/06/15.
 */
public class MapDialogFragment extends DialogFragment {


    //section for OSMdroidMap
    private final static int ZOOM=17;
    private org.osmdroid.views.MapView map;
    private MapEventsOverlay overlayEventos;
    private  MapEventsReceiver mapEventsReceiver;
    private IMapController mapController;



    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_map, null);

        builder.setView(view);

        // Click on OK
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                SettingTourActivity.customPositionIsSet=true;

//TODO 24Luglio: Since no LookUpService, LAT/LON on the map taken from user interaction, are all is needed
                Log.d("miotag", "from Dialog, torno la location : " + Repository.retrieve(getActivity(), Constants.LATITUDE_STARTING_POINT, String.class) + "," + Repository.retrieve(getActivity(), Constants.LONGITUDE_STARTING_POINT, String.class));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });


//          16/07 OsmDroid Map

        map = (org.osmdroid.views.MapView) view.findViewById(R.id.mapview);

        //TODO 1settembre

        if (TextUtils.equals(Repository.retrieve(getActivity(), Constants.ACTIVATE_ONLINE_CONNECTION, String.class), Constants.ONLINE_CONNECTION_OFF)) {

            Log.d("miotag","mostra mappa offline");
            map.setTileSource(new XYTileSource("MapQuest",
                    ResourceProxy.string.mapquest_osm, 17, 17, 300, ".jpg", new String[]{
                    "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                    "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
            map.setBuiltInZoomControls(false);
            map.setMultiTouchControls(true);
            map.setUseDataConnection(false);
        }


        mapController = map.getController();
        mapController.setZoom(ZOOM);
// TODO decommentare per consentire l'acquisizione dell'attuale (fisica) posizione
 GeoPoint startPoint = new GeoPoint(Double.valueOf(Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)),Double.valueOf(Repository.retrieve(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.class)));
//        GeoPoint startPoint = new GeoPoint(Double.valueOf(Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)),Double.valueOf(Repository.retrieve(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.class)));

        mapController.setCenter(startPoint);


        org.osmdroid.bonuspack.overlays.Marker startMarker=new org.osmdroid.bonuspack.overlays.Marker(map);
        startMarker.setIcon(getResources().getDrawable(R.drawable.pin_acqua));
        startMarker.setPosition(startPoint);
        startMarker.setTitle(getResources().getString(R.string.myPosition));
        startMarker.showInfoWindow();
        map.getOverlays().add(startMarker);



        mapEventsReceiver=new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                putMarker(geoPoint);
                return false;
            }
        };

        overlayEventos = new MapEventsOverlay(getActivity(), mapEventsReceiver);
        map.getOverlays().add(overlayEventos);




        return builder.create();
    }

    private void putMarker(GeoPoint geoPoint){
        map.invalidate();
        map.getOverlays().clear();

        overlayEventos = new MapEventsOverlay(getActivity(), mapEventsReceiver);
        map.getOverlays().add(overlayEventos);
        Log.d("miotagw","metto mark con posizione: "+geoPoint.getLatitude()+", "+geoPoint.getLongitude());
        org.osmdroid.bonuspack.overlays.Marker startMarker=new org.osmdroid.bonuspack.overlays.Marker(map);
        startMarker.setIcon(getResources().getDrawable(R.drawable.pin_acqua));
        startMarker.setPosition(geoPoint);
        startMarker.setTitle(getActivity().getResources().getString(R.string.your_position));
        startMarker.showInfoWindow();
        map.getOverlays().add(startMarker);
        mapController.setCenter(geoPoint);

        //saving new marker position
        Repository.save(getActivity(), Constants.LATITUDE_STARTING_POINT, String.valueOf(geoPoint.getLatitude()));
        Repository.save(getActivity(), Constants.LONGITUDE_STARTING_POINT, String.valueOf(geoPoint.getLongitude()));

        Log.d("miotag","coordinate salvate da MapFragment di Where: "+Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)+", "+Repository.retrieve(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.class));



    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("miotag", "onStop from MapDialogFragment");
        map.getOverlays().remove(overlayEventos);
        map.getOverlays().clear();
        map.getTileProvider().createTileCache();
        map.getTileProvider().detach();
        Location mapLocation=new Location("LOCATION_SERVICE");
        mapLocation.setLatitude(Double.valueOf((Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class))));
        mapLocation.setLongitude(Double.valueOf((Repository.retrieve(getActivity(), Constants.LONGITUDE_STARTING_POINT, String.class))));



        if (
                (Repository.retrieve(getActivity(),Constants.ACTIVATE_ONLINE_CONNECTION,String.class).equals(Constants.ONLINE_CONNECTION_ON)) ||
                        ( Repository.retrieve(getActivity(),Constants.ACTIVATE_ONLINE_CONNECTION,String.class).equals("")) ||
                        (Repository.retrieve(getActivity(),Constants.ACTIVATE_ONLINE_CONNECTION,String.class) == null)
                ){



                        Log.d("miotag", "connessione on da Fragment");
                        Intent i=new Intent(getActivity(), LookUpIntentService.class);
                        SettingTourActivity.AddressResultReceiver mResultReceiver=new SettingTourActivity.AddressResultReceiver(new Handler());
                        i.putExtra(Constants.RECEIVER,mResultReceiver);
                         i.putExtra(Constants.LOCATION_DATA_EXTRA,mapLocation);
                            getActivity().startService(i);
        }



    }





}