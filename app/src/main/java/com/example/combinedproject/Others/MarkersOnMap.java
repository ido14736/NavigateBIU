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

        //a map of the icons on the map(icon id(long) paired with the icon (MapIcon))
        markers = new HashMap<Long, Pair<Boolean, Integer>>();

        //myDB.addAllMarkers();
        //amphitheaters
        //myDB.insertData("1501 – אמפי-תיאטרון מרכזי", "amphitheater", 32.07265, 34.848452, "האמפי-תיאטרון המרכזי בבר אילן, ממוקם בחלק הצפוני של הקמפוס. משמש לטקסים, הופעות אירועים שונים ועוד");
       // myDB.insertData("110 – אמפי-תיאטרון דרומי", "amphitheater", 32.066434, 34.842545, "האמפי-תיאטרון הדרומי בבר אילן,  משמש לטקסים, הופעות אירועים שונים ועוד.");

      //  Cursor cur = myDB.getAllMarkers();
    }

    public void initializeMarkersToMap(MapboxMap map, String type)
    {
        /*int drawableID = initilizedContext.getResources().getIdentifier("amphitheater", "drawable", initilizedContext.getPackageName());
        Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
        Icon amphitheaterIc = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);

        Icon currentIc = null;*/

        Context initilizedContext = c;

        //INIT ICONS, change addMarker, put params
        int i;
        Marker m;
        Icon ic;
        for (i = 0; i < InformationHandler.getSize(); i++) {
            if((type == "All") || (type != "All" && InformationHandler.getInfoByIndex(i).getType().equals(type))) {
                ic = getIconByType(InformationHandler.getInfoByIndex(i).getType());
                /*int drawableID = initilizedContext.getResources().getIdentifier(InformationHandler.getInfoByIndex(i).getType(), "drawable", initilizedContext.getPackageName());
                Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
                Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);*/

                m = map.addMarker(new MarkerOptions().position(InformationHandler.getInfoByIndex(i).getPosition()).icon(ic));
                markers.put(m.getId(), new Pair<Boolean, Integer>(true, i));
            }
        }
        //System.out.println(markers);
        //map.getMarkers()
        /*DO FOR WITH INDEXES
        for (Information information : InformationHandler.getInfo()) {
            int drawableID = initilizedContext.getResources().getIdentifier(information.getType(), "drawable", initilizedContext.getPackageName());
            Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
            Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);

            m = map.addMarker(new MarkerOptions().position(information.getPosition()).icon(ic));
            icons.put( ,m.getId());

        }*/

        //initializing the icons dictionary

       /* Context initilizedContext = c;
        int drawableID = initilizedContext.getResources().getIdentifier("amphitheater", "drawable", initilizedContext.getPackageName());
        Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
        Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);
        Marker m = map.addMarker(new MarkerOptions().position(new LatLng(32.07265, 34.848452)).icon(ic));
        Marker m1 = map.addMarker(new MarkerOptions().position(new LatLng(32.066434, 34.842545)).icon(ic));
        icons.put(m.getId(), new MapIcon(m.getId(), new LatLng(32.07265, 34.848452), "amphitheater", "1501 – אמפי-תיאטרון מרכזי", "האמפי-תיאטרון המרכזי בבר אילן, ממוקם בחלק הצפוני של הקמפוס. משמש לטקסים, הופעות אירועים שונים ועוד"));
        icons.put(m1.getId(), new MapIcon(m1.getId(), new LatLng(32.066434, 34.842545), "amphitheater", "110 – אמפי-תיאטרון דרומי", "האמפי-תיאטרון הדרומי בבר אילן,  משמש לטקסים, הופעות אירועים שונים ועוד."));
        return true;*/

        /*Context initilizedContext = c;
        int drawableID = initilizedContext.getResources().getIdentifier("ic_airport", "drawable", initilizedContext.getPackageName());
        Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
        Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);
        Marker m = map.addMarker(new MarkerOptions().position(new LatLng(32.068,34.845)).icon(ic));
        Marker m1 = map.addMarker(new MarkerOptions().position(new LatLng(32.067,34.843)).icon(ic));
        icons.put(m.getId(), new MapIcon(m.getId(), new LatLng(32.068,34.845), "ic_airport", "airplain1", "first airplain"));
        icons.put(m1.getId(), new MapIcon(m1.getId(), new LatLng(32.067,34.843), "ic_airport", "airplain2", "second airplain"));*/
    }

    public Icon getIconByType(String type){
        int drawableID = c.getResources().getIdentifier(type, "drawable", c.getPackageName());
        Bitmap mBitmap = getBitmapFromVectorDrawable(c, drawableID);
        return IconFactory.getInstance(c).fromBitmap(mBitmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void MarkersSelectionToMap(MapboxMap map, String type)
    {
        Marker m;
        int currentInfoIndex = -1;
        Information currentInfo = null;
        Icon ic = null;
        //because you can't add/remove from map during the looping
        List<Long> removeFromList = new ArrayList();
        List<Pair<Long, Integer>> addToList = new ArrayList();
        if(type.equals("All")) {
            for(Map.Entry<Long, Pair<Boolean, Integer>> entry : markers.entrySet()) {
                if(entry.getValue().first == false) {
                    currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());

                    /*int drawableID = initilizedContext.getResources().getIdentifier(currentInfo.getType(), "drawable", initilizedContext.getPackageName());
                    Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
                    Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);*/

                    m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                  //  markers.remove(entry.getKey());
                    addToList.add(new Pair<>(m.getId(), currentInfoIndex));
                   // markers.put(m.getId(), new Pair<Boolean, Integer>(true, currentInfoIndex));
                }
            }
        }

        else if(type.equals("favorites")) {
            for(Map.Entry<Long, Pair<Boolean, Integer>> entry : markers.entrySet()) {
                boolean isCurrentInFavorites = InformationHandler.doesInfoInFavorites(username, InformationHandler.getInfoByIndex(entry.getValue().second).getName());
                //if a marker from the wnted type isn't exist
                if(isCurrentInFavorites &&
                        entry.getValue().first == false) {
                    currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());

                    /*int drawableID = initilizedContext.getResources().getIdentifier(currentInfo.getType(), "drawable", initilizedContext.getPackageName());
                    Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
                    Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);*/

                    m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                    //markers.remove(entry.getKey());
                    //markers.put(m.getId(), new Pair<Boolean, Integer>(true, currentInfoIndex));
                    addToList.add(new Pair<>(m.getId(), currentInfoIndex));
                    removeFromList.add(entry.getKey());
                }

                else if((!isCurrentInFavorites) &&
                        entry.getValue().first == true) {
                    m = getMarkerById(entry.getKey(), map);
                    if(m != null) {
                        map.removeMarker(m);
                    }

                    markers.replace(entry.getKey(), new Pair<Boolean, Integer>(false, entry.getValue().second));
                    //first in second is false + loop by indexes


                    /*currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());*/

                    /*int drawableID = initilizedContext.getResources().getIdentifier(currentInfo.getType(), "drawable", initilizedContext.getPackageName());
                    Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
                    Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);*/

                    /*m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                    markers.remove(entry.getKey());
                    markers.put(m.getId(), new Pair<Boolean, Integer>(true, currentInfoIndex));*/
                }
            }

        }

        else {
            for(Map.Entry<Long, Pair<Boolean, Integer>> entry : markers.entrySet()) {
                //if a marker from the wnted type isn't exist
                if(InformationHandler.getInfoByIndex(entry.getValue().second).getType().equals(type) &&
                        entry.getValue().first == false) {
                    currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());

                    /*int drawableID = initilizedContext.getResources().getIdentifier(currentInfo.getType(), "drawable", initilizedContext.getPackageName());
                    Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
                    Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);*/

                    m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                    //markers.remove(entry.getKey());
                    //markers.put(m.getId(), new Pair<Boolean, Integer>(true, currentInfoIndex));
                    addToList.add(new Pair<>(m.getId(), currentInfoIndex));
                    removeFromList.add(entry.getKey());
                }

                else if((!InformationHandler.getInfoByIndex(entry.getValue().second).getType().equals(type)) &&
                        entry.getValue().first == true) {
                    m = getMarkerById(entry.getKey(), map);
                    if(m != null) {
                        map.removeMarker(m);
                    }

                    markers.replace(entry.getKey(), new Pair<Boolean, Integer>(false, entry.getValue().second));
                    //first in second is false + loop by indexes


                    /*currentInfoIndex = entry.getValue().second;
                    currentInfo = InformationHandler.getInfoByIndex(currentInfoIndex);
                    ic = getIconByType(currentInfo.getType());*/

                    /*int drawableID = initilizedContext.getResources().getIdentifier(currentInfo.getType(), "drawable", initilizedContext.getPackageName());
                    Bitmap mBitmap = getBitmapFromVectorDrawable(initilizedContext, drawableID);
                    Icon ic = IconFactory.getInstance(initilizedContext).fromBitmap(mBitmap);*/

                    /*m = map.addMarker(new MarkerOptions().position(currentInfo.getPosition()).icon(ic));
                    markers.remove(entry.getKey());
                    markers.put(m.getId(), new Pair<Boolean, Integer>(true, currentInfoIndex));*/
                }
            }
        }

        int i;
        for(i = 0; i < removeFromList.size(); i++) {
            markers.remove(removeFromList.get(i));
        }

        for(i = 0; i < addToList.size(); i++) {
            markers.put(addToList.get(i).first, new Pair<>(true, addToList.get(i).second));
        }
    }

    public long getMarkerIdByName(String name) {
        for(Long key : markers.keySet()) {
            if(InformationHandler.getInfoByIndex(getMarkerIndexById(key)).getName().equals(name)) {
                return key;
            }
        }
        return -1;
    }

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

    /*/returnes the icons that are on the map
    public Map<Long, Pair<Boolean, Integer>> getIcons() {
        return icons;
    }*/

    /*public Pair<Boolean, Integer> getIconById(Long id) {
        return icons.get(id);
    }*/
    public int getMarkerIndexById(Long id) {
        if(markers.get(id) != null) {
            return markers.get(id).second;
        }
        return -1;
    }
}
