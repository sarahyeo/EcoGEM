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
import android.widget.ImageButton;
import android.widget.TextView;

import com.mojio.mojiosdk.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarah on 2015-03-14.
 */

    public class VehiclesActivity extends Activity {
        Vehicle[] userVehicles;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_vehicle);

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#2ABB9B"));

            ActionBar actionBar = getActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#019875")));
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>EcoGEM</font>"));

            userVehicles = Singleton.getInstance().userVehicles;

            initVehicles();
        }

        public void initVehicles() {
            List<TextView> text = new ArrayList<TextView>();
            if(userVehicles == null || userVehicles.length == 0){
                findViewById(R.id.button0).setVisibility(View.GONE);
                findViewById(R.id.button1).setVisibility(View.GONE);
                findViewById(R.id.button2).setVisibility(View.GONE);
            }

            for (int i = 0; i < userVehicles.length; i++) {
                switch (i) {
                    case 0:
                        text.add(i, (TextView) findViewById(R.id.button0));
                        findViewById(R.id.button1).setVisibility(View.GONE);
                        findViewById(R.id.button2).setVisibility(View.GONE);
                        break;
                    case 1:
                        text.add(i, (TextView) findViewById(R.id.button1));
                        findViewById(R.id.button1).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        text.add(i, (TextView) findViewById(R.id.button2));
                        findViewById(R.id.button2).setVisibility(View.VISIBLE);
                        break;
                }
                String vehicleName = userVehicles[i].getNameDescription();
                if(vehicleName == null){
                    text.get(i).setText("Unknown");
                }
                else{
                    text.get(i).setText(vehicleName);
                }
            }
        }

        public void onCarButton(View view) {
            Intent intent = new Intent(this, CarActivity.class);
            switch(view.getId()){
                case R.id.button0:
                    intent.putExtra("car",0);
                    break;
                case R.id.button1:
                    intent.putExtra("car",1);
                    break;
                case R.id.button2:
                    intent.putExtra("car",2);
                    break;
            }
            startActivity(intent);
        }

        public void onBackButton(View view) {
            finish();
        }

        public void onLogButton(View view) {
            Intent intent1 = new Intent(this, LogActivity.class);
            startActivity(intent1);
        }
 }



