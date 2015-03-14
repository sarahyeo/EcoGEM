package com.cloud1001.ecogem;

import android.app.Activity;
import android.content.Intent;
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

    private boolean goalMet = false;

    private TextView fuelScore, goal, carName, fuelLevel, totalDistance;
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

        initScore();
    }

    public void initScore() {

        fuelScore.setText(Integer.toString(calculateScore()));

        if (goalMet) {
            goalIcon.setBackgroundResource(R.drawable.goal_reached);
        } else {
            goalIcon.setBackgroundResource(R.drawable.goal_reached);
        }

        goal.setText(Integer.toString(calculateGoal()));
        carName.setText(vehicles[vehicle].getNameDescription());
        fuelLevel.setText(Integer.toString((int) vehicles[vehicle].FuelLevel));
        totalDistance.setText(Integer.toString(totalDistanceInt));
    }


    public List<Trip> sortTrips() {
        List<Trip> sortedTrips = new LinkedList<Trip>();
        String vehicleId = vehicles[vehicle]._id;
        for (Trip t: trips) {
            if (t.VehicleId.equals(vehicleId)) {
                sortedTrips.add(t);
            }
        }

        return sortedTrips;
    }

    public int calculateScore() {
        float result = 0;
        float distance = 0;
        List<Trip> trips = sortTrips();
        for (Trip t : trips) {
            result += (t.FuelEfficiency * t.Distance);
            distance += t.Distance;
        }

        totalDistanceInt = (int) distance;

        return  (int) (result/distance);
    }

    public int calculateGoal() {
        int goal = 8;
        if (goal > calculateScore()) {
            goalMet = true;
        }
        return goal;
    }

    // Start new VehiclesActivity
    public void onBackButton(View view) {
        startActivity(new Intent(this, VehiclesActivity.class));
    }

}
