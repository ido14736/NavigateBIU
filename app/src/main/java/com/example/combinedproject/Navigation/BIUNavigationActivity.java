package com.example.combinedproject.Navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Data.InformationHandler;
import com.example.combinedproject.Others.MarkersOnMap;
import com.example.combinedproject.R;
import com.google.android.material.tabs.TabLayout;
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
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationUnitType;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//the navigation activity
public class BIUNavigationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener,
        PermissionsListener, MapboxMap.OnMapClickListener {

    //constant values
    private final double BIUMinLongitude = 34.836282;
    private final double BIUMaxLongitude = 34.854054;
    private final double BIUMinLatitude = 32.063779;
    private final double BIUMaxLatitude = 32.076654;

    private MapView mapView;
    private MapboxMap map;
    private Button startNavigationButton;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private AutoCompleteTextView srcET;
    private AutoCompleteTextView dstET;
    private Location currentLocation;
    private Point originPosition;
    private Marker originMarker;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActivity";
    private CheckBox cb;
    private TabLayout tabLayout;
    private MarkersOnMap markersOnMap;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up the map
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_biu_navigation);

        mapView = (MapView) findViewById(R.id.navProjMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //initializing the DB if needed
        boolean addedSuccesfully = InformationHandler.initializeInformation(getBaseContext());
        if(!addedSuccesfully){
            Toast.makeText(getBaseContext(), "Error While Reading The Data From The Database.",
                    Toast.LENGTH_LONG).show();
        }

        //create the tabLayout for choosing the route type
        String[] tabsTitles = {"cycling","driving","walking"};
        int[] tabsIcons = {R.drawable.cycling, R.drawable.driving, R.drawable.walking};
        tabLayout = findViewById(R.id.routeTypeTL);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout customTab = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.my_dropdown_item, null);
            TextView tab_text = customTab.findViewById(R.id.text);
            tab_text.setText("  " + tabsTitles[i]);
            tab_text.setCompoundDrawablesWithIntrinsicBounds(tabsIcons[i], 0, 0, 0);
            tabLayout.getTabAt(i).setCustomView(tab_text);
        }

        //selecting the "walking" route type as a default(in index 2)
        tabLayout.selectTab(tabLayout.getTabAt(2));

        //getting data from the previous activity
        username = getIntent().getExtras().getString("username");

        //when a tab is selected - changing the route if displayed(by the chosen type)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //origin and destination aren't null so a route is currently displayed on the map
                if(originPosition != null && destinationPosition != null)
                {
                    routeHandler(originPosition, destinationPosition, false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        startNavigationButton = findViewById(R.id.navigationBT);

        //start the navigation when the navigation button is clicked
        startNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //creating the navigation process
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder().directionsProfile(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString())
                            .origin(originPosition)
                            .destination(destinationPosition).unitType(NavigationUnitType.TYPE_METRIC)
                            .shouldSimulateRoute(false).build();
                    //starting the navigation
                    NavigationLauncher.startNavigation(BIUNavigationActivity.this, options);
                } catch (RuntimeException e) {
                    Toast.makeText(getBaseContext(), "Unable to start the navigation.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, InformationHandler.getNamesList());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, InformationHandler.getNamesList());

        srcET = findViewById(R.id.srcET);
        srcET.setAdapter(adapter1);

        dstET = findViewById(R.id.dstET);
        dstET.setAdapter(adapter2);

        //if a user chose a sevice from the auto-complete of the src edit text
        srcET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getting the information of the chosen service and handling it's selection
                Information markerInfo = InformationHandler.getInfoByIndex(markersOnMap.getMarkerIndexById(markersOnMap.getMarkerIdByName(adapter1.getItem(position).toString())));
                if(markerInfo != null)
                {
                    navigationPointsHandler(markerInfo.getPosition(), markerInfo.getName());
                }
                else {}
            }
        });

        //if a user chose a sevice from the auto-complete of the dst edit text
        dstET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getting the information of the chosen service and handling it's selection
                Information markerInfo = InformationHandler.getInfoByIndex(markersOnMap.getMarkerIndexById(markersOnMap.getMarkerIdByName(adapter2.getItem(position).toString())));
                if(markerInfo != null)
                {
                    navigationPointsHandler(markerInfo.getPosition(), markerInfo.getName());
                }
                else {
                    Toast.makeText(getBaseContext(), "Error accured while searching for the marker.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        cb = findViewById(R.id.currentLocationCB);
        Bitmap mBitmap = getBitmapFromVectorDrawable(this, R.drawable.src_marker);
        Icon srcIC = IconFactory.getInstance(BIUNavigationActivity.this).fromBitmap(mBitmap);

        //when the checkbox is checked - using the current location as a src location
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //if checked(true) - the origin location will be the current location + adding the marker on the map
                if(b){
                    if(currentLocation != null){
                        double userLongitude = currentLocation.getLongitude();
                        double userLatitude = currentLocation.getLatitude();

                        //checking if the location is in in BIU range(legal current location)
                        if((userLongitude >= BIUMinLongitude && userLongitude <= BIUMaxLongitude)
                                && (userLatitude >=BIUMinLatitude && userLatitude <= BIUMaxLatitude)){
                            srcET.setText("מיקום נוכחי");
                            srcET.setEnabled(false);
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

                            //origin and destination aren't null => a route is displayed on the map(updating the route)
                            if(originPosition != null && destinationPosition != null)
                            {
                                routeHandler(originPosition, destinationPosition, false);

                                //enabling the navigation button if a route is displayed + the origin location is the current location
                                if(cb.isChecked())
                                {
                                    startNavigationButton.setEnabled(true);
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
                }

                //if unchecked - removing the origin marker from the map and setting it to null
                else
                {
                    srcET.setText("");
                    srcET.setEnabled(true);
                    if(originMarker != null)
                    {
                        map.removeMarker(originMarker);
                        originMarker = null;
                        originPosition = null;

                    }
                    //removing the route from the map if displayed + disabling the navigation button
                    routeHandler(originPosition, destinationPosition, true);
                    startNavigationButton.setEnabled(false);
                }
            }
        });

        //by pressing the "clear" icon on that is on the source location EditText - the text will be cleared +
        //the src marker will be removed from the map
        srcET.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getX() >= (srcET.getRight() - srcET.getCompoundDrawables()[2].getBounds().width()))  {
                        srcET.setText("");

                        if(originMarker != null)
                        {
                            map.removeMarker(originMarker);
                            originMarker = null;
                            originPosition = null;
                            routeHandler(originPosition, destinationPosition, true);
                            startNavigationButton.setEnabled(false);

                        }

                        return true;
                    }
                }
                return false;
            }
        });

        //by pressing the "clear" icon on that is on the destination location EditText - the text will be cleared +
        // the dst marker will be removed from the map
        dstET.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getX() >= (dstET.getRight() - dstET.getCompoundDrawables()[2].getBounds().width()))  {
                        dstET.setText("");

                        if(destinationMarker != null)
                        {
                            map.removeMarker(destinationMarker);
                            destinationMarker = null;
                            destinationPosition = null;
                            routeHandler(originPosition, destinationPosition, true);
                            startNavigationButton.setEnabled(false);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
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
        markersOnMap.initializeMarkersToMap(map, "All");

        //when a marker is clicked
        map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //getting the information of the selected marker
                Information markerInfo = InformationHandler.getInfoByIndex(markersOnMap.getMarkerIndexById(marker.getId()));
                if(markerInfo != null)
                {
                    //handling the selection of the marker
                    navigationPointsHandler(markerInfo.getPosition(), markerInfo.getName());

                    return true;
                }
                return false;
            }
        });
    }

    //if the location permission is enabled - initializing the current location and some other settings
    private void enableLocation(){
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

    //setting the camera position to a location
    private void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 16.0));
    }

    //if the map is clicked(not an icon) - handling the src and dst locations initialization
    @Override
    public void onMapClick(@NonNull LatLng point) {
        navigationPointsHandler(point, "no name");
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

    //handling a click on the map or on an icon: setting the src/dst location,
    //displaying the src/dst marker on the map, and displaying the route on the map if both locations are available
    private void navigationPointsHandler(LatLng point, String pointName)
    {
        //the src/dst icons
        Bitmap srcBitmap = getBitmapFromVectorDrawable(this, R.drawable.src_marker);
        Icon srcIcn = IconFactory.getInstance(BIUNavigationActivity.this).fromBitmap(srcBitmap);
        Bitmap dstBitmap = getBitmapFromVectorDrawable(this, R.drawable.dst_marker);
        Icon dstIcn = IconFactory.getInstance(BIUNavigationActivity.this).fromBitmap(dstBitmap);

        //in BIU range
        if((point.getLongitude() >= BIUMinLongitude && point.getLongitude() <= BIUMaxLongitude)
                && (point.getLatitude() >=BIUMinLatitude && point.getLatitude() <= BIUMaxLatitude)){
            //if the origin marker isn't initialized - setting it as the selected location
            if(originMarker == null)
            {
                originMarker = map.addMarker(new MarkerOptions().position(point).icon(srcIcn));
                originPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());

                //if the selected location is a marker - setting the text to the name of the service
                if(pointName != "no name")
                {
                    srcET.setText(pointName);
                }
                //if the selected location is not a marker - setting the text to the coordinates of the location
                else
                {
                    srcET.setText(point.getLatitude() + " , " + point.getLongitude());
                }

                //zooming the camera to the selected location
                Location originLocation = new Location("");
                originLocation.setLatitude(originPosition.latitude());
                originLocation.setLongitude(originPosition.longitude());
                setCameraPosition(originLocation);
            }

            //if the origin marker initialized - setting the dst marker as the selected location
            else
            {
                //removing the prev dst marker
                if(destinationMarker != null)
                {
                    map.removeMarker(destinationMarker);
                }

                //adding the new one
                destinationMarker = map.addMarker(new MarkerOptions().position(point).icon(dstIcn));
                destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());

                //if the selected location is a marker - setting the text to the name of the service
                if(pointName != "no name")
                {
                    dstET.setText(pointName);
                }
                //if the selected location is not a marker - setting the text to the coordinates of the location
                else
                {
                    dstET.setText(point.getLatitude() + " , " + point.getLongitude());
                }
            }

            //origin and destination aren't null => a route is displayed on the map
            if(originPosition != null && destinationPosition != null)
            {
                //handling the selection of the location
                routeHandler(originPosition, destinationPosition, false);

                //if the user uses it's current location as the src point - enabling the navigation
                if(cb.isChecked())
                {
                    startNavigationButton.setEnabled(true);
                }
            }
        }

        //the chosen location isn't in biu range
        else {
            Toast.makeText(getBaseContext(), "The Selected Location Is Not In BIU",
                    Toast.LENGTH_LONG).show();
        }
    }


    //handling the route on the map
    private void routeHandler(Point origin, Point destination, boolean removeRoute){
        //removing the route if there is one displayed
        if(removeRoute)
        {
            if(navigationMapRoute != null)
            {
                navigationMapRoute.removeRoute();
            }
        }
        else
        {
            //generating the routes by the origin and destination points
            NavigationRoute.builder().accessToken(Mapbox.getAccessToken()).origin(origin).destination(destination).profile(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString())
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

                    //removing the displayed route
                    if(navigationMapRoute != null)
                    {
                        navigationMapRoute.removeRoute();
                    }

                    //creating a new route object
                    else
                    {
                        navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                    }

                    //adding the new route
                    navigationMapRoute.addRoute(currentRoute);
                }

                @Override
                public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                    Log.e(TAG, "Error:" + t.getMessage());
                }
            });
        }

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
            setCameraPosition(location);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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