package com.example.combinedproject.Information;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.combinedproject.R;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Data.InformationHandler;
import com.example.combinedproject.Others.MarkersOnMap;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//the map information activity
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap map;
    private MarkersOnMap markersOnMap;
    private Marker currentSelectedMarker;
    private String username;

    //creating the activity
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up the map
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.infoProjMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        currentSelectedMarker = null;

        //getting data from the previous activity
        username = getIntent().getExtras().getString("username");

        //initializing the DB if needed
        boolean addedSuccesfully = InformationHandler.initializeInformation(getBaseContext());
        if(!addedSuccesfully){
            Toast.makeText(getBaseContext(), "Error While Reading The Data From The Database.",
                    Toast.LENGTH_LONG).show();
        }

        //creating an arraylist with all the possible services types
        List<String> typesNames = new ArrayList<String>();
        Collections.addAll(typesNames, InformationHandler.getHebrew_types());
        typesNames.add("מועדפים");

        //setting up the markers filtering spinner
        Spinner spinner = findViewById(R.id.typeSP);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, typesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    //when pressing something on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if the user chose to start the list activity
        if(item.getItemId() == R.id.toListItem)
        {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.putExtra("username", username);
            startActivityForResult(intent, 1);
        }
        return true;
    }

    //searching for the marker with the given position and returning it's id(or -1 if it doesn't exist)
    public long getMarkerIdByPosition(LatLng position) {
        for (Marker marker : map.getMarkers()){
            if(marker.getPosition().getLatitude() == position.getLatitude() &&
                    marker.getPosition().getLongitude() == position.getLongitude()){
                return marker.getId();
            }
        }
        return -1;
    }

    //handling a choice of a marker(a click on a marker on the map)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the request is valid(if the user pressed on a marker)
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //getting the chosen marker
                LatLng chosenItemPosition = new LatLng(data.getDoubleExtra("chosenItemLat", 0), data.getDoubleExtra("chosenItemLng", 0));
                long id = getMarkerIdByPosition(chosenItemPosition);
                Marker chosenItemMarker = markersOnMap.getMarkerById(id, map);

                //if the marker exists
                if(id != -1) {
                    handleMarkerClick(chosenItemMarker);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(chosenItemPosition, 16.0));
                }

                //if the marker doesn't exist
                else {
                    Toast.makeText(getBaseContext(), "The chosen marker isn't displayed.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //creating an options menu by a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_list_menu, menu);
        return true;
    }

    //handling a click on a marker
    public boolean handleMarkerClick(Marker marker) {
        Information markerInfo = InformationHandler.getInfoByIndex(markersOnMap.getMarkerIndexById(marker.getId()));

        //if the marker exists(has information)
        if(markerInfo != null)
        {
            //if there is a chosen marker on the map
            if(currentSelectedMarker != null)
            {
                //if the chosen marker isn't the prev chosen marker -
                //displaying the description of the chosen marker on a popup window,
                //instead of the prev chosen marker popup window
                currentSelectedMarker.hideInfoWindow();
                if(currentSelectedMarker.getId() != marker.getId())
                {
                    marker.setTitle(markerInfo.getName());
                    marker.setSnippet(markerInfo.getDescription());
                    marker.showInfoWindow(map, mapView);
                    currentSelectedMarker = marker;
                }

                //if the chosen marker is the prev chosen marker - removing it's popup window(did before)
                else
                {
                    currentSelectedMarker = null;
                }
            }

            //if there isn't a chosen marker on the map -
            //displaying the chosen marker's description in a popup window
            else
            {
                marker.setTitle(markerInfo.getName());
                marker.setSnippet(markerInfo.getDescription());
                marker.showInfoWindow(map, mapView);
                currentSelectedMarker = marker;
            }
            return true;
        }
        return false;
    }

    //when the map is ready
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
            //when clicking on a popup window - setting the current selected marker to null
            @Override
            public boolean onInfoWindowClick(@NonNull Marker marker) {
                currentSelectedMarker = null;
                return false;
            }
        });

        //removing the irrelevant layers(irrelevant data on the map) from the map
        List<Layer> layers = map.getLayers();
        for (Layer la : layers) {
            if(la.getId().contains("poi-scalerank"))
            {
                map.removeLayer(la.getId());
            }
        }

        //adding the markers to the map
        markersOnMap = new MarkersOnMap(getBaseContext(), username);
        markersOnMap.initializeMarkersToMap(map, "All");

        //when a marker is clicked
        map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                return handleMarkerClick(marker);
            }
        });

        //updates the position of the popup window by the movement of the camera
        map.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if(currentSelectedMarker != null)
                {
                    currentSelectedMarker.showInfoWindow(map, mapView);
                }
            }
        });

        //handling a choice of an item from the spinner(a service type)
        Spinner spinner = findViewById(R.id.typeSP);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //filtering the marker on the map by the chosen service type
                String selectedType = InformationHandler.getEnglishTypeByHebrewType(spinner.getSelectedItem().toString());
                markersOnMap.MarkersSelectionToMap(map, selectedType);

                //if there is a selected marker
                if(currentSelectedMarker != null) {
                    //checking if the type of the selected marker is not the chosen service type
                    //and if it's not - removing it's popup window
                    if(selectedType != "All" && (!selectedType.equals(InformationHandler.getInfoByIndex(markersOnMap.getMarkerIndexById(currentSelectedMarker.getId())).getType()))) {
                        currentSelectedMarker.hideInfoWindow();
                        currentSelectedMarker = null;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    //overwriting the activity lifecycle events

    @Override
    protected void onStart() {
        super.onStart();
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
        mapView.onDestroy();
    }
}