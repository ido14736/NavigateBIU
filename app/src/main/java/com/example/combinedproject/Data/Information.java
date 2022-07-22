package com.example.combinedproject.Data;
import com.mapbox.mapboxsdk.geometry.LatLng;

//an Information about an service on campus(marker data)
public class Information {
    private LatLng position;
    private String type;
    private String name;
    private String description;

    //constructor
    public Information(LatLng position, String type, String name, String description)
    {
        this.position = position;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    //compare to an input Information
    public boolean comapre(Information information) {
        if (this.position.equals(information.getPosition()) &&
                this.type.equals(information.getType()) &&
                this.name.equals(information.getName()) &&
                this.description.equals(information.getDescription())) {
            return true;
        }
        return false;
    }

    //getters

    public LatLng getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
