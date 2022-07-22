package com.example.combinedproject.Data;

import android.content.Context;
import android.database.Cursor;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//handling everything that relates to the information about the different services on campus
public class InformationHandler {
    private static List<Information> info;
    private static List<String> namesList;
    private static DatabaseHandler myDB;
    private static String[] english_types;
    private static String[] hebrew_types;

    //initializing the list - adding every information from the DB to the list
    public static boolean initializeInformation(Context c) {
        info = new ArrayList();
        namesList = new ArrayList();
        myDB = new DatabaseHandler(c);

        //setting the possible services types in hebrew
        hebrew_types = new String[]{
                "כל השירותים", "בנייני לימוד", "ספריות", "שערים","תחנות שאטל", "עמדות מים", "מקררים", "מיקרוגלים"
                , "מעונות", "מתקני ספורט", "מרכזי הופעות ואירועים", "מגרשי חנייה", "מסעדות", "בתי קפה"
        };

        //setting the possible services types in english
        english_types = new String[]{
                "All", "building", "library", "gate", "shuttle", "water", "refrigerator", "microwave"
                , "dorms", "sports", "amphitheater", "parking", "restaurant", "coffee"
        };

        //getting all the markers
        Cursor cursor = myDB.getAllMarkers();

        //if the cursur is null
        //we will request all the markers again because they will be added after the last request
        if (cursor == null) {
            cursor = myDB.getAllMarkers();
        }

        Context initilizedContext = c;

        //iterating on the cursur(the markers) and updating the lists
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

    //getters
    public static String[] getFields() {
        return Arrays.copyOfRange(myDB.getMarkersFields(), 1, myDB.getMarkersFields().length);
    }

    public static List<String> getNamesList() {
        return namesList;
    }

    public static int getSize() {
        return info.size();
    }

    public static String[] getHebrew_types() {
        return hebrew_types;
    }

    public static String[] getEnglish_types() {
        return Arrays.copyOfRange(english_types, 1, english_types.length);
    }

    //getting english type by hebrew type
    public static String getEnglishTypeByHebrewType(String name) {
        if(name.equals("מועדפים")) {
            return "favorites";
        }

        //getting the index of the input hebrew service type
        int i;
        for (i = 0; i < hebrew_types.length; i++) {
            if (hebrew_types[i].equals(name)) {
                break;
            }
        }

        //if the input hebrew type is invalids
        if(i == hebrew_types.length){
            return "";
        }
        return english_types[i];
    }

    //getting Information by the index in the list
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

    //checking if a username has a favorite with the input name
    public static boolean doesInfoInFavorites(String username, String name) {
        return myDB.doesFavoriteExists(username, name);
    }

    //adding a marker to the DB
    public static boolean addMarkerToDB(String name, String type, double location_lat, double location_lon, String description) {
        return myDB.insertMarker(name, type, location_lat, location_lon, description);
    }

    //editing a marker in the DB
    public static boolean editMarkerInDB(String name, String field, String newValue) {
        return myDB.editMarker(name, field, newValue);
    }

    //removing a marker from the DB
    public static boolean removeMarkerFromDB(String name) {
        return myDB.removeMarker(name);
    }
}
