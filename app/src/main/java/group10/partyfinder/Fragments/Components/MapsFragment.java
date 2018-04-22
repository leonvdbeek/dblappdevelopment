package group10.partyfinder.Fragments.Components;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import group10.partyfinder.DataStructure.DBSnapshot;
import group10.partyfinder.Event;
import group10.partyfinder.DataStructure.Party;
import group10.partyfinder.PermissionUtils;
import group10.partyfinder.R;

public class MapsFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    //private SensorManager sensorManager;
    private GoogleMap mMap;
    private DBSnapshot DB = DBSnapshot.getInstance();
    private LocationManager locationManager;

    private boolean subscribe;

    private boolean initLocPoll;

    private boolean initMap;

    @Override
    public void onStart() {
        super.onStart();
        if (!subscribe) {
            EventBus.getDefault().register(this);
            subscribe = true;
        }
    }

    // UI updates must run on MainThread
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event.event == 2) {
            addMarkers(2);
        }
        if (event.event == 3) {
            addMarkers(3);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
/*
        if ( ContextCompat.checkSelfPermission( getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        return view;
    }

    private void addMarkers(int tab){

        //clear old markers
        mMap.clear();

       // int getArgument = getArguments().getInt("map");//Get pass data with its key value

        if (tab == 2) {
            for (Party party : DB.getTodayParties()){
                LatLng coor = new LatLng(party.getLattitude(), party.getLongitude());
                Marker marker = mMap.addMarker(
                        new MarkerOptions().position(coor).title(party.getName()));
                marker.setTag(party.getId());
            }
        }
        if (tab == 3) {
            for (Party party : DB.getFutureParties()){
                LatLng coor = new LatLng(party.getLattitude(), party.getLongitude());
                Marker marker = mMap.addMarker(
                        new MarkerOptions().position(coor).title(party.getName()));
                marker.setTag(party.getId());
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.setOnMarkerClickListener(this);
        mMap = googleMap;
        // Add a marker in Amsterdam as default zoomed out position for NL
        LatLng amsterdam = new LatLng(52.370216, 4.895168);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(amsterdam));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));

       // locationManager = (LocationManager) getActivity()
         //     .getSystemService(Context.LOCATION_SERVICE);



        if ( ContextCompat.checkSelfPermission( getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {


          //  if(!initLocPoll) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, this);
                initLocPoll = true;
          //  }

            Location location = locationManager
                  .getLastKnownLocation(locationManager.GPS_PROVIDER);

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

            mMap.setMyLocationEnabled(true);
            onLocationChanged(location);

        } else {
            enableMyLocation();
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), 1,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        int targetPartyId = (int) marker.getTag();
        // Create a test party object for the PartyView activity.
        Party party = DBSnapshot.getInstance().getParty(targetPartyId);

        // Open partyView activity
        Intent i = new Intent("android.intent.action.PartyView");
        i.putExtra("ID", party.getId());
        this.startActivity(i);


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if ( ContextCompat.checkSelfPermission( getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {
            try {//mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                   // location.getLongitude())));
                Log.d("my tag", "location is changed to "+location.getLatitude()
                        +" "+location.getLongitude());
                if(location != null && !initMap){
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                    location.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
                    initMap = true;
                }

            }
            catch (NullPointerException e) { }

            DBSnapshot.getInstance().setLocation(location);
            EventBus.getDefault().postSticky(new Event(1));
        } else {
            enableMyLocation();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        if ( ContextCompat.checkSelfPermission( getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, this);
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        initMap = false;
        DBSnapshot.getInstance().setLocation(null);
        EventBus.getDefault().postSticky(new Event(1));
    }
}
