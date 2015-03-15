package com.cloud1001.ecogem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojio.mojiosdk.models.Trip;
import com.mojio.mojiosdk.models.Vehicle;

import java.util.LinkedList;
import java.util.List;

public class CarActivity extends Activity {

    private Singleton singleton = Singleton.getInstance();
    private int vehicle;
    private Trip[] trips;
    private Vehicle[] vehicles;
    private int totalDistanceInt;

    private TextView fuelScore, goal, carName, fuelLevel, totalDistance, lastFuelE, lastDistance, scoreHeader;
    private ImageView goalIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        Intent intent = getIntent();
        vehicle = intent.getExtras().getInt("car");

        trips = singleton.userTrips;
        vehicles = singleton.userVehicles;

        fuelScore = (TextView)findViewById(R.id.fuelScore);
        goal = (TextView)findViewById(R.id.goal);
        goalIcon = (ImageView)findViewById(R.id.goalIcon);
        fuelLevel = (TextView)findViewById(R.id.fuelLevel);
        carName = (TextView)findViewById(R.id.carName);
        totalDistance = (TextView)findViewById(R.id.totalDistance);
        lastFuelE = (TextView)findViewById(R.id.lastFuelE);
        lastDistance = (TextView)findViewById(R.id.lastDistance);
        scoreHeader = (TextView)findViewById(R.id.scoreHeader);

        initScore();
    }

    public void initScore() {

        String s = String.format("%.2f", calculateScore());

        fuelScore.setText(s);

        if (calculateScore() < calculateGoal()) {
            goalIcon.setBackgroundResource(R.drawable.goal_yes);
            goal.setBackgroundColor(Color.parseColor("#396e11"));
            fuelScore.setBackgroundColor(Color.parseColor("#52961E"));
            scoreHeader.setBackgroundColor(Color.parseColor("#52961E"));

        } else {
            goalIcon.setBackgroundResource(R.drawable.goal_no);
            goal.setBackgroundColor(Color.parseColor("#e67e22"));
            fuelScore.setBackgroundColor(Color.parseColor("#d35400"));
            scoreHeader.setBackgroundColor(Color.parseColor("#d35400"));

        }

        goal.setText("Goal: " + Integer.toString(calculateGoal()));

        carName.setText((vehicles == null || vehicles.length == 0) ? "Unknown" : vehicles[vehicle].getNameDescription());

        fuelLevel.setText((vehicles == null || vehicles.length == 0) ?
                "Remaining Fuel: 0%" : "Remaining Fuel: " + Integer.toString((int) vehicles[vehicle].FuelLevel)+ "%");

        totalDistance.setText("Total Distance: " + Integer.toString(totalDistanceInt) + " km");

        String r = String.format("%.2f", vehicles[vehicle].LastFuelEfficiency);

        lastFuelE.setText((vehicles == null || vehicles.length == 0) ?
                "Most Recent Fuel Efficiency: N/A" : "Most Recent Fuel Efficiency: " + r + " L/100km");

        lastDistance.setText((vehicles == null || vehicles.length == 0) ? "Most Recent Distance: N/A" :
                "Most Recent Distance: " + Integer.toString((int) vehicles[vehicle].LastDistance) + " km");
    }


    public List<Trip> sortTrips() {
        List<Trip> sortedTrips = new LinkedList<Trip>();
        if (vehicles != null && vehicles.length != 0) {
            String vehicleId = vehicles[vehicle]._id;
            if (trips != null && trips.length != 0) {
                for (Trip t : trips) {
                    if (t.VehicleId.equals(vehicleId)) {
                        sortedTrips.add(t);
                    }
                }
            }
        }
        return sortedTrips;
    }

    public float calculateScore() {
        float result = 0;
        float distance = 0;
        List<Trip> trips = sortTrips();
        if (trips != null) {
            for (Trip t : trips) {
                result += (t.FuelEfficiency * t.Distance);
                distance += t.Distance;
            }

            totalDistanceInt = (int) distance;
            return (result/distance);
        } else {
            totalDistanceInt = (int) distance;
            return result;
        }

    }

    public int calculateGoal() {
        int goal = 5;
        return goal;
    }

    // Start new VehiclesActivity
    public void onBackButton(View view) {
        finish();
    }

}
