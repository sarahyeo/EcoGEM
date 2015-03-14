package com.cloud1001.ecogem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

               /*
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);*/

            setContentView(R.layout.activity_vehicle);

            userVehicles = Singleton.getInstance().userVehicles;

            initVehicles();
        }

        public void initVehicles() {
            List<TextView> text = new ArrayList<TextView>();

            for (int i = 0; i < userVehicles.length; i++) {
                switch (i) {
                    case 0:
                        text.set(i, (TextView) findViewById(R.id.button0));
                        findViewById(R.id.button1).setVisibility(View.GONE);
                        findViewById(R.id.button2).setVisibility(View.GONE);
                        break;
                    case 1:
                        text.set(i, (TextView) findViewById(R.id.button1));
                        findViewById(R.id.button1).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        text.set(i, (TextView) findViewById(R.id.button2));
                        findViewById(R.id.button2).setVisibility(View.VISIBLE);
                        break;
                }
                text.get(i).setText(userVehicles[i].VIN);

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



