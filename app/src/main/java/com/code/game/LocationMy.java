package com.code.game;

import android.location.Location;

public class LocationMy {
    private double latitude=0;
    private double longitude=0;
    private double altitude=0;

    public LocationMy() {
    }
public int distance(LocationMy locationElse){
    Location locationMy=new Location("");
    locationMy.setAltitude(altitude);
    locationMy.setLongitude(longitude);
    locationMy.setLatitude(latitude);
    Location location2=new Location("");
    location2.setAltitude(locationElse.getAltitude());
    location2.setLongitude(locationElse.getLongitude());
    location2.setLatitude(locationElse.getLatitude());
return (int)Math.round(locationMy.distanceTo(location2));
}
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
