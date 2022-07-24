package com.example.combinedproject.Others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Data.InformationHandler;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

//a class to handle the markers on the map
public class MarkersOnMap{
    private Context c;
    private Map<Long, Pair<Boolean, Integer>> markers;
    private String username;

    //constructor
    public MarkersOnMap(Context c, String username)
    {
        //the context of the activity
        this.c = c;
        this.username = username;

        //a map of the icons on the map contains:
        //marker id(long) paired with a pair of if the marker appears on the map(boolean) with the index of the marker (int)
        markers = new HashMap<Long, Pair<Boolean, Integer>>();
    }

    //initializing the markers on the map
    public void initializeMarkersToMap(MapboxMap map, String type)
    {
        int i;
        Marker m;
        Icon ic;

        //if the input type is 'All' - adding all the markers to the map,
        //else - adding the markers that are matching the input type
        for (i = 0; i < InformationHandler.getSize(); i++) {
            //checking if the current service is matching the input type
            if((type == "All") || (type != "All" && InformationHandler.getInfoByIndex(i).getType().equals(type))) {
                //the icon by the type
                ic = getIconByType(InformationHandler.getInfoByIndex(i).getType());

                //adding the marker to the map
                m = map.addMarker(new MarkerOptions().position(InformationHandler.getInfoByIndex(i).getPosition()).icon(ic));
                markers.put(m.getId(), new Pair<Boolean, Integer>(true, i));
            }
        }
    }

    //getting an icon of a marker by type
    public Icon getIconByType(String type){
        int drawableID = c.getResources().getIdentifier(type, "drawable", c.getPackageName());
        Bitmap mBitmap = getBitmapFromVectorDrawable(c, drawableID);
        return IconFactory.getInstance(c).fromBitmap(mBitmap);
    }

    //filtering the markers on the map by a type
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void MarkersSelectionToMap(MapboxMap map, String type)
    {
        Marker m;
        int currentInfoIndex = -1;
        Information currentInfo = null;
        Icon ic = null;

        //creating those lists because you can't add/remove from map(of the markers) during the looping
        List<Long> removeFromList = new ArrayList();
        List<Pair<Long, Integer>> addToList = new ArrayList();

        //if the type is all - adding all the markers to the map
        if(type.equals("All")) {
            for(Map.Entry<Long, Pair<Boolean, Integer>> entry : markers.entrySet()) {
                //if the marker doesn't appear on the map - adding it
                if(entry.getValue().first == false) {
                    //getting the information of the marker and it's icon
                    currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());

                    //adding the marker to the map
                    m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                    addToList.add(new Pair<>(m.getId(), currentInfoIndex));
                }
            }
        }

        //if the type is favorites - adding only the favorites markers to the map
        else if(type.equals("favorites")) {
            for(Map.Entry<Long, Pair<Boolean, Integer>> entry : markers.entrySet()) {
                //checking if the current marker is in the favorites of the current user
                boolean isCurrentInFavorites = InformationHandler.doesInfoInFavorites(username, InformationHandler.getInfoByIndex(entry.getValue().second).getName());

                //if in favorites and doesn't appear on the map - adding it
                if(isCurrentInFavorites &&
                        entry.getValue().first == false) {
                    //getting the information of the marker and it's icon
                    currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());

                   //adding the marker to the map
                    m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                    addToList.add(new Pair<>(m.getId(), currentInfoIndex));
                    removeFromList.add(entry.getKey());
                }

                //if the current marker isn't a favorite and appears on the map - removing it
                else if((!isCurrentInFavorites) &&
                        entry.getValue().first == true) {
                    m = getMarkerById(entry.getKey(), map);
                    if(m != null) {
                        map.removeMarker(m);
                    }

                    //setting that the marker doesn't appear on the map
                    markers.replace(entry.getKey(), new Pair<Boolean, Integer>(false, entry.getValue().second));
                }
            }
        }

        //other regular type
        else {
            for(Map.Entry<Long, Pair<Boolean, Integer>> entry : markers.entrySet()) {
                //if the current marker is from the qnted type and it's doesn't appear on the map - adding it
                if(InformationHandler.getInfoByIndex(entry.getValue().second).getType().equals(type) &&
                        entry.getValue().first == false) {
                    //getting the information of the marker and it's icon
                    currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());

                    //adding the marker to the map
                    m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                    addToList.add(new Pair<>(m.getId(), currentInfoIndex));
                    removeFromList.add(entry.getKey());
                }

                //if the current marker isn't from the wanted type and it appears on the map - removing it
                else if((!InformationHandler.getInfoByIndex(entry.getValue().second).getType().equals(type)) &&
                        entry.getValue().first == true) {
                    m = getMarkerById(entry.getKey(), map);
                    if(m != null) {
                        map.removeMarker(m);
                    }

                    //setting that the marker doesn't appear on the map
                    markers.replace(entry.getKey(), new Pair<Boolean, Integer>(false, entry.getValue().second));
                }
            }
        }

        //removing the unwanted markers from the map
        int i;
        for(i = 0; i < removeFromList.size(); i++) {
            markers.remove(removeFromList.get(i));
        }

        //removing the wanted markers to the map
        for(i = 0; i < addToList.size(); i++) {
            markers.put(addToList.get(i).first, new Pair<>(true, addToList.get(i).second));
        }
    }

    //getting a marker id by it's name(of the service)
    public long getMarkerIdByName(String name) {
        for(Long key : markers.keySet()) {
            if(InformationHandler.getInfoByIndex(getMarkerIndexById(key)).getName().equals(name)) {
                return key;
            }
        }
        return -1;
    }

    //getting a marker by it's id
    public Marker getMarkerById(Long id, MapboxMap map) {
        for(Marker m : map.getMarkers()) {
            if(m.getId() == id) {
                return m;
            }
        }
        return null;
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

    //getting marker's index by it's id
    public int getMarkerIndexById(Long id) {
        if(markers.get(id) != null) {
            return markers.get(id).second;
        }
        return -1;
    }
}
