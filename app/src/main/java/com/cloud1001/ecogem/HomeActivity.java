package com.cloud1001.ecogem;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojio.mojiosdk.models.Trip;

public class HomeActivity extends Activity {

    private Button mLoginButton, mCouponButton, mVehiclesButton;
    private TextView mUserName, fuelScore, goal, scoreHeader;
    private ImageView goalIcon;

    private Singleton singleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#87D37C"));

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A65B")));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>EcoGEM</font>"));

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

        goal.setText("Goal: " + Integer.toString(calculateGoal()));

        if (calculateFuelScore() < calculateGoal()) {
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

        String s = String.format("%.2f", calculateFuelScore());

        fuelScore.setText(s);

    }

    // Start new VehiclesActivity
    public void onVehicleButton(View view) {
        startActivity(new Intent(this, VehiclesActivity.class));
    }

    // Start new CouponsActivity
    public void onCouponsButton(View view) {
        startActivity(new Intent(this, CouponsActivity.class));
    }

    public float calculateFuelScore() {
        float fuel = 0;
        float totalDistance = 0;
        if (singleton.userTrips == null) {
            return 0;
        } else {
            for (Trip t : singleton.userTrips) {
                fuel += t.FuelEfficiency * t.Distance;
                totalDistance += t.Distance;
            }
            return (fuel / totalDistance);
        }
    }

    public int calculateGoal() {
        return 15;
    }



}
