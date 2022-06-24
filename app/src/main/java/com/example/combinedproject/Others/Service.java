package com.example.combinedproject.Others;

import com.example.combinedproject.Data.Information;

import java.util.ArrayList;
import java.util.List;

/* this class represents a service - coffee shops/ restaurants etc.
* A service has a list of information lists regarding the service */

public class Service {
    private String name;
    private List<Information> childList;
    private int icon;

    // constructor
    public Service(String name, List<Information> childList, int icon) {
        this.name = name;
        this.childList = childList;
        this.icon = icon;
    }
    /* getters */
    public String getName() {
        return name;
    }

    public List<Information> getChildList() {
        return childList;
    }

    public int getIcon() {
        return icon;
    }

    /* setters */
    public void setName(String name) {
        this.name = name;
    }

    public void setChildList(ArrayList<Information> childList) {
        this.childList = childList;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}
