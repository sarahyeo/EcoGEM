package com.cloud1001.ecogem;

import com.mojio.mojiosdk.models.Trip;
import com.mojio.mojiosdk.models.User;
import com.mojio.mojiosdk.models.Vehicle;

/**
 * Created by Sarah on 2015-03-14.
 */
public class Singleton {

    public User currentUser;
    public Vehicle[] userVehicles;
    public Trip[] userTrips;


    private static Singleton instance = null;
    protected Singleton() {
        // Exists only to defeat instantiation.
    }
    public static Singleton getInstance() {
        if(instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
