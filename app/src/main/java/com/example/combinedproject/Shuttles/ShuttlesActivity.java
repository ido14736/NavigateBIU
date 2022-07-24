package com.example.combinedproject.Shuttles;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Data.InformationHandler;
import com.example.combinedproject.Others.MarkersOnMap;
import com.example.combinedproject.R;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import java.util.Date;
import java.util.List;

//the shuttles activity
public class ShuttlesActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener,
        PermissionsListener, MapboxMap.OnMapClickListener{

    //constant values
    private final double BIUMinLongitude = 34.836282;
    private final double BIUMaxLongitude = 34.854054;
    private final double BIUMinLatitude = 32.063779;
    private final double BIUMaxLatitude = 32.076654;
    private final int ShuttleLoopTimeInMillisecond = 7 * 60000;
    private final String ShuttleStatingTime = "07:30:00 AM";
    private final String ShuttleRegularEndingTime = "8:00:00 PM";
    private final String ShuttleFridayEndingTime = "1:00:00 PM";

    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location currentLocation;
    private Point originPosition;
    private Marker originMarker;
    private MarkersOnMap markersOnMap;
    private CheckBox cb;
    private Button closestButton;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActivity";
    DirectionsRoute currentShortestRoute = null;
    double currentShortestDistance = -1;
    Long currentShortestId;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up the map
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_shuttles);

        mapView = (MapView) findViewById(R.id.shuProjMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //initializing the DB if needed
        boolean addedSuccesfully = InformationHandler.initializeInformation(getBaseContext());
        if(!addedSuccesfully){
            Toast.makeText(getBaseContext(), "Error While Reading The Data From The Database.",
                    Toast.LENGTH_LONG).show();
        }

        //when clicking the "closest" button
        cb = findViewById(R.id.shuttlesCurrentLocationCB);
        closestButton = (Button)findViewById(R.id.closestBT);
        closestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(closestButton.getText());
                //if there is no route on the map - finding the route
                if(closestButton.getText().equals("Get Closest")) {
                    findAndCreateShortest();
                    closestButton.setText("Remove Route");
                }

                //if there is a route on the map - removing the route
                else {
                    if(navigationMapRoute != null)
                    {
                        navigationMapRoute.removeRoute();
                        currentShortestRoute = null;
                        currentShortestDistance = 1000;
                    }
                    closestButton.setText("Get Closest");
                }
            }
        });

        Bitmap mBitmap = getBitmapFromVectorDrawable(this, R.drawable.src_marker);
        Icon srcIC = IconFactory.getInstance(com.example.combinedproject.Shuttles.ShuttlesActivity.this).fromBitmap(mBitmap);

        //getting data from the previous activity
        username = getIntent().getExtras().getString("username");

        //when the checkbox is checked - using the current location as a src location
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //if checked(true) - the origin location will be the current location + adding the marker on the map
                if(b){
                    double userLongitude = currentLocation.getLongitude();
                    double userLatitude = currentLocation.getLatitude();

                    //checking if the location is in in BIU range(legal current location)
                    if((userLongitude >= BIUMinLongitude && userLongitude <= BIUMaxLongitude)
                            && (userLatitude >=BIUMinLatitude && userLatitude <= BIUMaxLatitude)){
                        if(originMarker != null)
                        {
                            map.removeMarker(originMarker);
                        }

                        //adding the marker to the map
                        originMarker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                .icon(srcIC));
                        originPosition = Point.fromLngLat(currentLocation.getLongitude(), currentLocation.getLatitude());

                        //zooming the camera on the current location
                        Location originLocation = new Location("");
                        originLocation.setLatitude(originPosition.latitude());
                        originLocation.setLongitude(originPosition.longitude());
                        setCameraPosition(originLocation);

                        //if the origin position is valid - enabling to find the closest station to it
                        if(originPosition != null)
                        {
                            if(cb.isChecked())
                            {
                                closestButton.setEnabled(true);
                            }
                        }
                    }

                    //if the location isn't in BIU range
                    else {
                        Toast.makeText(getBaseContext(), "Invalid Current Location - It Is Not In BIU Area",
                                Toast.LENGTH_LONG).show();
                        cb.setChecked(false);
                    }

                }

                //if unchecked - removing the origin marker from the map and setting it to null
                else
                {
                    if(originMarker != null)
                    {
                        map.removeMarker(originMarker);
                        originMarker = null;
                        originPosition = null;

                    }
                    //removing the route from the map if displayed + disabling the navigation button
                    //routeHandler(originPosition, destinationPosition, true);
                    closestButton.setEnabled(false);
                }
            }
        });
    }

    //generating the shortest path from the current location to the closest shuttle station
    public void findAndCreateShortest() {
        //if the location of the user is valid
        if(originPosition != null && originMarker != null) {
            List<Marker> markers = map.getMarkers();
            Long id = markers.get(0).getId();
            InformationHandler.getSize();

            //the only markers that we will get are a current location marker and shuttles stations markers
            currentShortestDistance = -1;
            for(Marker marker : map.getMarkers()) {
                int currentMarkerIndex = markersOnMap.getMarkerIndexById(marker.getId());

                //checking that the current marker isn't the current location marker
                if(currentMarkerIndex != -1) {
                    LatLng currentMarkerLatLng = InformationHandler.getInfoByIndex(currentMarkerIndex).getPosition();
                    Point currentMarkerPoint = Point.fromLngLat(currentMarkerLatLng.getLongitude(), currentMarkerLatLng.getLatitude());

                    //building the route to the current shuttle station
                    NavigationRoute.builder().accessToken(Mapbox.getAccessToken()).origin(originPosition).destination(currentMarkerPoint).profile("walking")
                            .build().getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            if(response.body() == null)
                            {
                                Log.e(TAG, "No routes found, check right user and access token");
                                return;
                            }
                            else if(response.body().routes().size() == 0)
                            {
                                Log.e(TAG, "No routes found");
                                return;
                            }

                            //if we have a route - getting the highest ranked route(in index 0)
                            DirectionsRoute currentRoute = response.body().routes().get(0);

                            //if the current station is the first that is checkes - setting to the distance to this station
                            if(currentShortestDistance == -1) {
                                currentShortestDistance = currentRoute.distance();
                            }

                            //if the distance to the current station is shorter than the current shortest distance -
                            //updating the current shortest distance
                            if(currentRoute.distance() <= currentShortestDistance) {
                                currentShortestId = marker.getId();
                                currentShortestDistance = currentRoute.distance();

                                //if there is a route on the map - removing the old route
                                if(navigationMapRoute != null)
                                {
                                    navigationMapRoute.removeRoute();
                                }

                                //if there is no route on the map - creating a route object
                                else
                                {
                                    navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                                }

                                //adding the new route to the map
                                navigationMapRoute.addRoute(currentRoute);
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                            Log.e(TAG, "Error:" + t.getMessage());
                        }
                    });
                }
            }
        }
    }

    //converting vector to bitmap(for the icons)
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    //setting the camera position to a location
    private void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 16.0));
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            currentLocation = location;
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    //when clicking on a marker on the map
    public boolean handleMarkerClick(Marker marker) throws ParseException {
        Information markerInfo = InformationHandler.getInfoByIndex(markersOnMap.getMarkerIndexById(marker.getId()));
        if(markerInfo != null)
        {
            boolean calculateTime = true;

            //getting the current day and time
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            //getting the time by a specific format
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
            String currentDateTimeString = sdf.format(d);

            //if the day is saturday - the shuttles aren't active
            if(day == 7) {
                Toast.makeText(getBaseContext(), "The shuttles aren't active today.",
                        Toast.LENGTH_LONG).show();
                calculateTime = false;
            }

            //if the day is friday -
            //checking the time because the shuttles working hours are different that any other day
            else if(day == 6) {
                //if the time isn't in the shuttles friday working time range
                if(sdf.parse(currentDateTimeString).before(sdf.parse(ShuttleStatingTime)) ||
                        sdf.parse(currentDateTimeString).after(sdf.parse(ShuttleFridayEndingTime))) {
                    Toast.makeText(getBaseContext(), "The shuttles aren't active at the current time.",
                            Toast.LENGTH_LONG).show();
                    calculateTime = false;
                }
            }

            //in any other day-
            //if the time isn't in the shuttles regular working time range
            else if(sdf.parse(currentDateTimeString).before(sdf.parse(ShuttleStatingTime)) ||
                    sdf.parse(currentDateTimeString).after(sdf.parse(ShuttleRegularEndingTime))) {
                Toast.makeText(getBaseContext(), "The shuttles aren't active at the current time.",
                        Toast.LENGTH_LONG).show();
                calculateTime = false;
            }

            //if the current time and day are valid - calculating the arrival time of the shuttle to the chosen station
            if(calculateTime) {
                long differentInMilliseconds = sdf.parse(currentDateTimeString).getTime() - sdf.parse(ShuttleStatingTime).getTime();
                double numOfDoneLoops = Math.floor(differentInMilliseconds/ShuttleLoopTimeInMillisecond);

                double millisecondsInCurrentLoop = differentInMilliseconds - (numOfDoneLoops*ShuttleLoopTimeInMillisecond);

                int selectedStationNumber = Integer.valueOf(markerInfo.getName().split(" ")[3]);

                if(millisecondsInCurrentLoop > selectedStationNumber*(ShuttleLoopTimeInMillisecond/17)) {
                    double timeUntilStationMilliseconds = ShuttleLoopTimeInMillisecond - millisecondsInCurrentLoop + (selectedStationNumber*(ShuttleLoopTimeInMillisecond/17));
                    //expectedArrivalTime add the num of miliseconds
                    double updatedTimeInMilliseconds = sdf.parse(currentDateTimeString).getTime() + timeUntilStationMilliseconds;
                    Date updatedTimeDate = new Date((long)updatedTimeInMilliseconds);
                    Toast.makeText(getBaseContext(), "Expexted time left for arrival:" + String.valueOf(timeUntilStationMilliseconds/60000) + " minutes",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getBaseContext(), "Expected arrival time:" + sdf.format(updatedTimeDate),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    double timeUntilStationMilliseconds = (selectedStationNumber*(ShuttleLoopTimeInMillisecond/17)) - millisecondsInCurrentLoop;
                    double updatedTimeInMilliseconds = sdf.parse(currentDateTimeString).getTime() + timeUntilStationMilliseconds;
                    Date updatedTimeDate = new Date((long)updatedTimeInMilliseconds);
                    Toast.makeText(getBaseContext(), "Expexted time left for arrival:" + String.valueOf(timeUntilStationMilliseconds/60000) + " minutes",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getBaseContext(), "Expected arrival time:" + sdf.format(updatedTimeDate),
                            Toast.LENGTH_LONG).show();
                }
            }

            return true;
        }
        return false;
    }

    //when the map is ready
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);

        //removing the irrelevant layers(irrelevant data on the map) from the map
        List<Layer> layers = map.getLayers();
        for (Layer la : layers) {
            if(la.getId().contains("poi-scalerank"))
            {
                System.out.println(la.getId());
                map.removeLayer(la.getId());
            }
        }

        //checking if the location is enabled
        enableLocation();

        //adding the markers to the map
        markersOnMap = new MarkersOnMap(getBaseContext(), username);
        markersOnMap.initializeMarkersToMap(map, "shuttle");

        //when a marker is clicked
        map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                try {
                    //handling marker click
                    return handleMarkerClick(marker);
                }
                catch (java.text.ParseException e){
                    //exception
                }
                return false;
            }
        });
    }

    //initializing the LocationEngine and getting the current location
    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();
        locationEngine.requestLocationUpdates();
        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation != null)
        {
            currentLocation = lastLocation;
        }
    }

    //initializing navigation settings
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
        locationLayerPlugin.setLocationLayerEnabled(false);
    }


    //if the location permission is enabled - initializing the current location and some other settings
    private void enableLocation(){
        CheckBox cb = findViewById(R.id.shuttlesCurrentLocationCB);

        //if the location permission accepted - allowing to use the current location(enabling the checkbox)
        if(PermissionsManager.areLocationPermissionsGranted(this))
        {
            cb.setEnabled(true);
            initializeLocationEngine();
            initializeLocationLayer();

            if(currentLocation != null)
            {
                double userLongitude = currentLocation.getLongitude();
                double userLatitude = currentLocation.getLatitude();

                //if the current location is in BIU range - setting it as the origin location
                if((userLongitude >= BIUMinLongitude && userLongitude <= BIUMaxLongitude)
                        && (userLatitude >=BIUMinLatitude && userLatitude <= BIUMaxLatitude)){
                    Toast.makeText(getBaseContext(), "valid Current Location - In BIU Area",
                            Toast.LENGTH_LONG).show();
                    cb.setChecked(true);
                }

                //if the current location is not in BIU range
                else {
                    Toast.makeText(getBaseContext(), "Invalid Current Location - It Is Not In BIU Area",
                            Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                //origin location is null
                Toast.makeText(getBaseContext(), "Permission Accepted But The Current Location Is Invallid",
                        Toast.LENGTH_LONG).show();
                //create without user location option + without navigation(just displaying the route)
                cb.setEnabled(false);
            }

        }

        //if the location permission isn't accepted
        else
        {
            //create without user location option + without navigation(just show the route)
            cb.setEnabled(false);
            //requesting location permission again
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    //overwriting the activity lifecycle events

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onStart() {
        super.onStart();
        if(locationEngine != null)
        {
            locationEngine.requestLocationUpdates();
        }

        if(locationLayerPlugin != null)
        {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine != null)
        {
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null)
        {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine != null) {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

}