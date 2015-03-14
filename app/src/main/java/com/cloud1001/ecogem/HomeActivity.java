package com.cloud1001.ecogem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojio.mojiosdk.MojioClient;
import com.mojio.mojiosdk.models.Trip;

/**
 * Created by Sarah on 2015-03-14.
 */
public class HomeActivity extends Activity {

    private Button mLoginButton, mCouponButton, mVehiclesButton;
    private TextView mUserName, fuelScore, goal, scoreHeader;
    private ImageView goalIcon;

    private Singleton singleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        singleton = Singleton.getInstance();
        mUserName = (TextView)findViewById(R.id.userName);
        fuelScore = (TextView)findViewById(R.id.fuelScore);
        scoreHeader = (TextView)findViewById(R.id.scoreHeader);
        mCouponButton = (Button)findViewById(R.id.couponButton);
        mVehiclesButton = (Button)findViewById(R.id.vehiclesButton);
        goal = (TextView)findViewById(R.id.goal);
        goalIcon = (ImageView)findViewById(R.id.goalIcon);

        initMap();

    }

    public void initMap() {

        // Show user data
        mUserName.setText("Hello " + singleton.currentUser.UserName);

        goal.setText("Goal:" + Integer.toString(calculateGoal()));

        goalIcon.setBackgroundResource(R.drawable.goal_reached);

        fuelScore.setText(Integer.toString(calculateFuelScore()));

    }

    // Start new VehiclesActivity
    public void onVehicleButton(View view) {
        startActivity(new Intent(this, VehiclesActivity.class));
    }

    // Start new CouponsActivity
    public void onCouponsButton(View view) {
        startActivity(new Intent(this, CouponsActivity.class));
    }

    public int calculateFuelScore() {
        float fuel = 0;
        float totalDistance = 0;
        if (singleton.userTrips  == null) {
            return 0;
        } else {
            for (Trip t : singleton.userTrips) {
                fuel += t.FuelEfficiency * t.Distance;
                totalDistance += t.Distance;
            }
            return (int) (fuel / totalDistance);
        }

    }

    public int calculateGoal() {
        return 8;
    }



}
