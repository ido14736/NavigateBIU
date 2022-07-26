package com.example.combinedproject.Others;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Data.InformationHandler;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.view.menu.ExpandedMenuView;
import androidx.fragment.app.FragmentManager;

/** this class is in-charge for initializing the lists from the DB, and
 * for the functionality of the menu-list */

public class RowsInList {

    // constructor input
    private final Context c;
    private final FragmentManager fm;
    ExpandableListView myList;
    private final String username;

    // local lists
    List<Service> services; // connection between category (Service) and his list (of Information)
    MainAdapter adapter; //  Loads the data into the items associated with 'myList'.
    // service's lists:
    List<Information> buildings;
    List<Information> libraries;
    List<Information> shuttles;
    List<Information> gates;
    List<Information> water;
    List<Information> refrigerators;
    List<Information> microwaves;
    List<Information> dorms;
    List<Information> sports;
    List<Information> amphitheaters;
    List<Information> parking_spots;
    List<Information> restaurants;
    List<Information> coffee_shops;
    List<Information> favorites;
    Information currentSelectedItem;  // for marking selected Information:

    //constructor
    public RowsInList(Context c, FragmentManager fm, String username, ExpandableListView e)
    {
        /* input data */
        this.c = c;
        this.fm = fm;
        myList = e; // myList
        this.username = username;
        /* Initialize other variables: */
        services = new ArrayList<Service>();
        InitializeAndLoad();
        // delete:
        setOnInfoClickedListener();
        currentSelectedItem = null;
    }

    /* This method expands all the groups of the menu */

    public void expandAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.expandGroup(i);
        }
    }

    /* This method collapses all the groups of the menu */

    public void collapseAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.collapseGroup(i);
        }
    }

    /* This method initializes the list of Services, set's an adapter to work, and
     * load the data from the DB into each service's list */

    private void InitializeAndLoad() {
        addListToService();
        initializeLists();
        adapter = new MainAdapter(c, fm, username, services);
        myList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /* this method loops over the data from the information handler, and adds to right list - based
     * on type */

    public void initializeLists() {
        for (int i = 0; i < InformationHandler.getSize(); i++) {
            String type = InformationHandler.getInfoByIndex(i).getType();
            // based on the current row's type - enter the name to the right list
            switch(type){
                case "building":
                    buildings.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "library":
                    libraries.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "restaurant":
                    restaurants.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "coffee":
                    coffee_shops.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "shuttle":
                    shuttles.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "gate":
                    gates.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "water":
                    water.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "refrigerator":
                    refrigerators.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "microwave":
                    microwaves.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "dorms":
                    dorms.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "sports":
                    sports.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "amphitheater":
                    amphitheaters.add(InformationHandler.getInfoByIndex(i));
                    break;
                case "parking":
                    parking_spots.add(InformationHandler.getInfoByIndex(i));
            }
            // if current information is already in the favorite table -
            // add him to the favorites list
            if(InformationHandler.doesInfoInFavorites(username, InformationHandler.getInfoByIndex(i).getName())) {
                favorites.add(InformationHandler.getInfoByIndex(i));
            }
        }
    }

    /* This method set's a list of Information for every Service, with the correct icon */

    public void addListToService(){
        buildings = new ArrayList<Information>();
        services.add(new Service("בנייני לימוד", buildings, 1));
        libraries = new ArrayList<Information>();
        services.add(new Service("ספריות", libraries, 2));
        shuttles = new ArrayList<Information>();
        services.add(new Service("תחנות שאטל", shuttles, 3));
        gates = new ArrayList<Information>();
        services.add(new Service("שערים", gates, 4));
        water = new ArrayList<Information>();
        services.add(new Service("עמדות מים", water, 5));
        refrigerators = new ArrayList<Information>();
        services.add(new Service("מקררים", refrigerators, 6));
        microwaves = new ArrayList<Information>();
        services.add(new Service("מיקרוגלים", microwaves, 7));
        dorms = new ArrayList<Information>();
        services.add(new Service("מעונות", dorms, 8));
        sports = new ArrayList<Information>();
        services.add(new Service("מתקני ספורט", sports, 9));
        amphitheaters = new ArrayList<Information>();
        services.add(new Service("מרכזי הופעות ואירועים", amphitheaters, 10));
        parking_spots = new ArrayList<Information>();
        services.add(new Service("מגרשי חנייה", parking_spots, 11));
        restaurants = new ArrayList<Information>();
        services.add(new Service("מסעדות", restaurants, 12));
        coffee_shops = new ArrayList<Information>();
        services.add(new Service("בתי קפה", coffee_shops, 13));
        favorites = new ArrayList<Information>();
        services.add(new Service("מועדפים", favorites, 14));
    }

    public MainAdapter getAdapter(){
        return this.adapter;
    }

    public Information getSelectedItem() {
        return currentSelectedItem;
    }

    /* this method set's listener for the children when they are clicked:
     *  It opens a dialog for the user  */

    public void setOnInfoClickedListener() {
        this.myList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition,
                                        int childPosition, long id) {

                Information clicked = (Information) adapter.getChild(groupPosition, childPosition);

                currentSelectedItem = clicked;
                adapter.openDialog(clicked, favorites);
                System.out.println(clicked.getName());
                return true;

            }
        });

        this.myList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView,
                                        View view, int i, long l) {
                System.out.println("parent selected");
                view.setBackgroundColor(Color.WHITE);
                currentSelectedItem = null;
                return false;
            }
        });
    }
}
