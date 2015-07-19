package com.androidtitan.trooptracker.Data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by amohnacs on 7/18/15.
 */
public class LocationBundle {
    private long id;
    private String localName;
    private LatLng latlng;

    public LocationBundle () {

    }

    public LocationBundle(String name, double latitude, double longitude) {
        this.localName = name;
        this.latlng = new LatLng(latitude, longitude);
    }

    public LocationBundle(String name, LatLng latlng) {
        this.localName = name;
        this.latlng = latlng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
