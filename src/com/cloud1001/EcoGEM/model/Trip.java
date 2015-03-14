package com.cloud1001.EcoGEM.model;

import java.util.Calendar;

public class Trip {

    Calendar date;
    int fuelEfficiency;
    int distance;

    public Trip(Calendar date, int fuelEfficiency, int distance) {
        this.date = date;
        this.fuelEfficiency = fuelEfficiency;
        this.distance = distance;
    }

    public Calendar getDate() {
        return date;
    }

    public int getFuelEfficiency() {
        return fuelEfficiency;
    }

    public int getDistance() {
        return distance;
    }
}
