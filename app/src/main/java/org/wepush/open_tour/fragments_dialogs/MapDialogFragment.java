package org.wepush.open_tour.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.usage.ConfigurationStats;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;

import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.Constants;
import org.wepush.open_tour.utils.Repository;


/**
 * Created by antoniocoppola on 30/06/15.
 */
public class MapDialogFragment extends DialogFragment {

//    private static MapView mv;
//    private Marker mark;
//    private UserLocationOverlay myLocationOverlay;
//    private String currentMap = null;
//    private Type type;

    //section for OSMdroidMap
    private final static int ZOOM=17;
    private org.osmdroid.views.MapView map;
    private MapEventsOverlay overlayEventos;
    private  MapEventsReceiver mapEventsReceiver;
    private IMapController mapController;
//    private double mapLatitudeExample=45.482596;
//    private double mapLongitudeExample=9.200048;


    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_map, null);

            builder.setView(view);

        // Click on OK
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    //the lookup request for marker's coordinates on maps doesn't happen inside GeoMarker- too annoyin
                    //what is done is to instantiate a new Location object beefed up with user settings

//
//                    Location mDialogLocation = new Location(LocationManager.GPS_PROVIDER);
//
//
//                    LatLng thisLatLng = new LatLng
//                            (
//                                    Double.valueOf(Repository.retrieve(getActivity(), Constants.LATITUDE_STARTING_POINT, String.class)),
//                                    Double.valueOf(Repository.retrieve(getActivity(), Constants.LONGITUDE_STARTING_POINT, String.class))
//                            );
//
//                    mDialogLocation.setLatitude(thisLatLng.getLatitude());
//                    mDialogLocation.setLongitude(thisLatLng.getLongitude());
//
//                    Log.d("miotag", "MAPDIALOGFRAGMENT coordinates from dialog: " + mDialogLocation.getLatitude() + ", " + mDialogLocation.getLongitude());
//                    Intent intent = new Intent(getActivity(), LookUpIntentService.class);
//                    ResultReceiver mResultReceiver = new SettingTourActivity.AddressResultReceiver(new Handler());
//                    intent.putExtra(Constants.RECEIVER, mResultReceiver);
//                    intent.putExtra(Constants.LOCATION_DATA_EXTRA, mDialogLocation);
//                    getActivity().startService(intent);
//                    }

//TODO 24Luglio: Since no LookUpService, LAT/LON on the map taken from user interaction, are all is needed
                    Log.d("miotag","from Dialog, torno la location : "+Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)+","+ Repository.retrieve(getActivity(), Constants.LATITUDE_STARTING_POINT, String.class));
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                }
            });



        //Map with MapBox (commented out 16/07)

//        LatLng thisLatLng=new LatLng(Double.valueOf(Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)),
//                                Double.valueOf(Repository.retrieve(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.class)));
//
//        mark=new Marker(getResources().getString(R.string.im_here_marker), getResources().getString(R.string.description_marker),thisLatLng);
//        mark.setMarker(getResources().getDrawable(R.drawable.pin_blue));
//        mv = (MapView) view.findViewById(R.id.mapview);
//        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
//        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
//        mv.setCenter(thisLatLng);
//        mv.setZoom(17);
//        mv.addMarker(mark);
////        mv.setUserLocationEnabled(true);
//
//        mv.setMapViewListener(new MapViewListener() {
//            @Override
//            public void onShowMarker(MapView mapView, Marker marker) {
//                Log.d("miotag", "onShowMarker");
//
//            }
//
//            @Override
//            public void onHideMarker(MapView mapView, Marker marker) {
//
//                Log.d("miotag", "onHideMarker");
//
//
//            }
//
//            @Override
//            public void onTapMarker(MapView mapView, Marker marker) {
//                Log.d("miotag", "onTapMarker");
//            }
//
//            @Override
//            public void onLongPressMarker(MapView mapView, Marker marker) {
//                Log.d("miotag", "Coordinate: " + marker.getPosition().getLatitude() + ", " + marker.getPosition().getLongitude());
//            }
//
//            @Override
//            public void onTapMap(MapView mapView, ILatLng iLatLng) {
//
//            }
//
//            @Override
//            public void onLongPressMap(MapView mapView, ILatLng iLatLng) {
//
//                LatLng lt = ((LatLng) iLatLng);
//                try {
//                    mark.closeToolTip();
//                } catch (NullPointerException e){
//                    e.printStackTrace();
//                }
//                mark=new Marker("I'm here","Description",lt);
//                mark.setMarker(getResources().getDrawable(R.drawable.pin_blue));
//                mv.clearMarkerFocus();
//                mv.clear();
//                mv.addMarker(mark);
//
//
//
//
//
//                Repository.save(getActivity(), Constants.LATITUDE_STARTING_POINT, String.valueOf(lt.getLatitude()));
//                Repository.save(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.valueOf(lt.getLongitude()));
//
//
//            }
//        });
//

//          16/07 OsmDroid Map

        map = (org.osmdroid.views.MapView) view.findViewById(R.id.mapview);
        map.setTileSource(new XYTileSource("MapQuest",
                ResourceProxy.string.mapquest_osm, 17, 17, 300, ".jpg", new String[]{
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(false);

        mapController = map.getController();
        mapController.setZoom(ZOOM);
// TODO decommentare per consentire l'acquisizione dell'attuale (fisica) posizione
 GeoPoint startPoint = new GeoPoint(Double.valueOf(Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)),Double.valueOf(Repository.retrieve(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.class)));
//        GeoPoint startPoint = new GeoPoint(Double.valueOf(Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)),Double.valueOf(Repository.retrieve(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.class)));

        mapController.setCenter(startPoint);


        org.osmdroid.bonuspack.overlays.Marker startMarker=new org.osmdroid.bonuspack.overlays.Marker(map);
        startMarker.setIcon(getResources().getDrawable(R.drawable.pin_acqua));
        startMarker.setPosition(startPoint);
        map.getOverlays().add(startMarker);



        mapEventsReceiver=new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                Log.d("miotag","premuto");
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
        Log.d("miotag","onStop from MapDialogFragment");
        map.getOverlays().remove(overlayEventos);
        map.getOverlays().clear();
        map.getTileProvider().createTileCache();
        map.getTileProvider().detach();
    }





}