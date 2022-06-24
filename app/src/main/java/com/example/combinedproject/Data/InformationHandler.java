package com.example.combinedproject.Data;

import android.content.Context;
import android.database.Cursor;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InformationHandler {
    private static List<Information> info;
    private static List<String> namesList;
    private static DatabaseHandler myDB;
    private static String[] types;
    private static String[] services_names;

    //Adding the information from the DB to the list
    public static boolean initializeInformation(Context c) {
        info = new ArrayList();
        namesList = new ArrayList();
        myDB = new DatabaseHandler(c);

        services_names = new String[]{
                "כל השירותים", "בנייני לימוד", "ספריות", "שערים","תחנות שאטל", "עמדות מים", "מקררים", "מיקרוגלים"
                , "מעונות", "מתקני ספורט", "מרכזי הופעות ואירועים", "מגרשי חנייה", "מסעדות", "בתי קפה"
        };

        types = new String[]{
                "All", "building", "library", "gate", "shuttle", "water", "refrigerator", "microwave"
                , "dorms", "sports", "amphitheater", "parking", "restaurant", "coffee"
        };


        Cursor cursor = myDB.getAllMarkers();
        if (cursor == null) {
            cursor = myDB.getAllMarkers();
        }

        Context initilizedContext = c;

        //INIT INFORMATION
        if (cursor != null) {
            do {
                info.add(new Information(new LatLng(cursor.getDouble(2), cursor.getDouble(3)), cursor.getString(1), cursor.getString(0), cursor.getString(4)));
                namesList.add(cursor.getString(0));


            } while (cursor.moveToNext());

            return true;

        } else {
            return false;
        }
    }

    public static String[] getFields() {
        return Arrays.copyOfRange(myDB.getMarkersFields(), 1, myDB.getMarkersFields().length);
    }

    public static List<String> getNamesList() {
        return namesList;
    }

    public static int getSize() {
        return info.size();
    }

    public static String[] getServices_names() {
        return services_names;
    }

    public static String[] getTypes() {
        return Arrays.copyOfRange(types, 1, types.length);
    }

    public static String getTypeByServiceName(String name) {
        if(name.equals("מועדפים")) {
            return "favorites";
        }

        int i;
        for (i = 0; i < services_names.length; i++) {
            if (services_names[i].equals(name)) {
                break;
            }
        }

        if(i == services_names.length){
            return "";
        }
        return types[i];
    }

    public static Information getInfoByIndex(int index) {
        try {
            if (index != -1) {
                return info.get(index);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean doesInfoInFavorites(String username, String name) {
        return myDB.doesFavoriteExists(username, name);
    }

    public static boolean addMToDB(String name, String type, double location_lat, double location_lon, String description) {
        return myDB.insertData(name, type, location_lat, location_lon, description);
    }

    public static boolean editInDB(String name, String field, String newValue) {
        return myDB.editData(name, field, newValue);
    }

    public static boolean removeFromDB(String name) {
        return myDB.removeData(name);
    }
}
