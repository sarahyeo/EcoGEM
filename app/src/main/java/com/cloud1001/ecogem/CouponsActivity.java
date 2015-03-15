package com.cloud1001.ecogem;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mojio.mojiosdk.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class CouponsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initImageView();
    }

    public void initImageView() {
        List<ImageView> image = new ArrayList<>();
        image.set(0, (ImageView) findViewById(R.id.imageCoupon));
        image.set(1, (ImageView) findViewById(R.id.imageNext));

        if (calculateFuelScore() < calculateGoal()) {
            image.get(0).setBackgroundResource(R.drawable.coupon);
        }
        image.get(1).setBackgroundResource(R.drawable.coupon_next);

        Button backButton = (Button) findViewById(R.id.buttonBack);
        backButton.setBackgroundResource(R.drawable.button_background);
        backButton.setTextColor(Color.BLACK);
    }

    public int calculateFuelScore() {
        float fuel = 0;
        float totalDistance = 0;
        if (Singleton.getInstance().userTrips  == null) {
            return 0;
        } else {
            for (Trip t : Singleton.getInstance().userTrips) {
                fuel += t.FuelEfficiency * t.Distance;
                totalDistance += t.Distance;
            }
            return (int) (fuel / totalDistance);
        }

    }

    public int calculateGoal() {
        return 8;
    }

    public void onBackButton(View view) {
        finish();
    }
}
