package com.cloud1001.ecogem;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.mojio.mojiosdk.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class CouponsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#2ABB9B"));

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#019875")));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>EcoGEM</font>"));

        initImageView();
    }

    public void initImageView() {
        ImageView couponCurrent = (ImageView) findViewById(R.id.imageCoupon);

        if (calculateFuelScore() < calculateGoal()) {
            couponCurrent.setBackgroundResource(R.drawable.coupon);
        }

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
