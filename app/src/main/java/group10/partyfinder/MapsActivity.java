package group10.partyfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

public class MapsActivity extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    //private SensorManager sensorManager;
    private GoogleMap mMap;
    private DBSnapshot DB = DBSnapshot.getInstance();
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {

            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            

        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        onLocationChanged(location);

        return view;
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                    location.getLongitude())));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

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

        int getArgument = getArguments().getInt("map");//Get pass data with its key value


        googleMap.setOnMarkerClickListener(this);
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng eindhoven = new LatLng(51.447573, 5.487507);

        //Todo customize which list to show all parties of instead of allparties
        if (getArgument == 1) {
            for (Party party : DB.getTodayParties()){
                LatLng coor = new LatLng(party.getLattitude(), party.getLongitude());
                Marker marker = mMap.addMarker(
                        new MarkerOptions().position(coor).title(party.getName()));
                marker.setTag(party.getId());
            }
        }
        if (getArgument == 2) {
            for (Party party : DB.getFutureParties()){
                LatLng coor = new LatLng(party.getLattitude(), party.getLongitude());
                Marker marker = mMap.addMarker(
                        new MarkerOptions().position(coor).title(party.getName()));
                marker.setTag(party.getId());
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eindhoven));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                location.getLongitude())));
        Log.d("my tag", "location is changed to"+location.getLatitude()+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
