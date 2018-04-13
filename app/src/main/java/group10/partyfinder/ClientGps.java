package group10.partyfinder;

import android.location.Location;

public class ClientGps
{
    private Location _currentLocation;
    private float _totMeters = 0f;
    public boolean isConnected = false;

    public event ClientDataReceivedEventHandler DataReceived;

    private LocationService locationService;

    public ClientGps()
    {
        locationService = new LocationService();
        locationService.LocationChanged += OnLocationChanged;
    }

    public void Connect(MainActivity activity)
    {
        locationService.StartLocationUpdates(activity);

        isConnected = true;
    }

    public void Disconnect()
    {
        if (isConnected)
            locationService.StopLocationUpdates();

        isConnected = false;
    }

    public void OnLocationChanged(object sender, LocationChangedEventArgs e)
    {
        if (e.Location != null)
        {
            if (_currentLocation != null)
            {
                var results = new float[1];

                Location.DistanceBetween(_currentLocation.Latitude, _currentLocation.Longitude, e.Location.Latitude, e.Location.Longitude, results);

                float speed = e.Location.Speed * 3.6f;

                if (speed >= 1f)
                    _totMeters += results[0];

                DataReceived(null, new ClientDataReceivedEventArgs(results));
            }

            _currentLocation = e.Location;
        }
    }
}


        [Service]
public class LocationService : Java.Lang.Object, ILocationListener
        {
protected LocationManager LocMgr = Android.App.Application.Context.GetSystemService("location") as LocationManager;

public void StartLocationUpdates(MainActivity activity)
        {
        var locationCriteria = new Criteria();
        locationCriteria.Accuracy = Accuracy.Fine;
        locationCriteria.PowerRequirement = Power.NoRequirement;
        var locationProvider = LocMgr.GetBestProvider(locationCriteria, true);

        activity.RunOnUiThread(() => LocMgr.RequestLocationUpdates(locationProvider, 1000, 1, this));
        }

public void StopLocationUpdates()
        {
        LocMgr.RemoveUpdates(this);
        }

public event EventHandler<LocationChangedEventArgs> LocationChanged = delegate { };

public void OnLocationChanged(Location location)
        {
        this.LocationChanged(this, new LocationChangedEventArgs(location));
        //          Log.Debug(this.ToString(), String.Format("Latitude is {0}", location.Latitude));
        //          Log.Debug(this.ToString(), String.Format("Longitude is {0}", location.Longitude));
        //          Log.Debug(this.ToString(), String.Format("Altitude is {0}", location.Altitude));
        //          Log.Debug(this.ToString(), String.Format("Speed is {0}", location.Speed));
        //          Log.Debug(this.ToString(), String.Format("Accuracy is {0}", location.Accuracy));
        //          Log.Debug(this.ToString(), String.Format("Bearing is {0}", location.Bearing));
        }

public void OnProviderDisabled(string provider)
        {
        //          Log.Debug(this.ToString(), String.Format("OnProviderDisabled: {0}", provider));
        }

public void OnProviderEnabled(string provider)
        {
        //          Log.Debug(this.ToString(), String.Format("OnProviderEnabled: {0}", provider));
        }

public void OnStatusChanged(string provider, Availability status, Bundle extras)
        {
        //          Log.Debug(this.ToString(), String.Format("OnStatusChanged: provider {0}, status {1}", provider, status));
        }
        }