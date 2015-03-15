package com.cloud1001.ecogem;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initTextView();
    }

    public void hideNavigation() {
        // Get rid of banner, fill screens the app
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void initTextView() {
        int numLogs = Math.min(Singleton.getInstance().userTrips.length, 10); //number of logs, max 10
        if (Singleton.getInstance().userTrips.length == 0 || Singleton.getInstance().userTrips == null)
            for (int i=9; i>=Singleton.getInstance().userTrips.length; i--) {
                getTextView(i).setVisibility(View.GONE);
            } //hide the unneeded ones
        List<TextView> text = new ArrayList<>(); //list of log texts
        for (int i=0; i<numLogs; i++) {
            text.set(i, getTextView(i));
            text.get(i).setTextSize(20);

            String startTime = Singleton.getInstance().userTrips[Singleton.getInstance().userTrips.length - i - 1].StartTime;
            String endTime = Singleton.getInstance().userTrips[Singleton.getInstance().userTrips.length - i - 1].EndTime;
            String delims = "-|:|T|Z|\\.";
            String[] startTimeSplit = startTime.split(delims); //parse start time into array
            String[] endTimeSplit = endTime.split(delims); //parse end time into array

            int durationDays, durationHours, durationMinutes, durationSeconds; //duration of trip

            int secondsInMonths = 0;
            for (int j =Integer.parseInt(startTimeSplit[1]); j > 0; j--) {
                secondsInMonths += 60 * 60 * 24 * numberDaysInMonth(Integer.toString(i), startTimeSplit[0]);
            }

            int startTimeSeconds = Integer.parseInt(startTimeSplit[5]) + //seconds
                    60 * Integer.parseInt(startTimeSplit[4]) + //minutes
                    60 * 60 * Integer.parseInt(startTimeSplit[3]) + //hours
                    60 * 60 * 24 * Integer.parseInt(startTimeSplit[2]) + //days
                    secondsInMonths + //months
                    60 * 60 * 24 * 365 * Integer.parseInt(startTimeSplit[0]) + //years
                    60 * 60 * 24 * ((Integer.parseInt(startTimeSplit[0]) % 4) -
                            (Integer.parseInt(startTimeSplit[0]) % 100) +
                            (Integer.parseInt(startTimeSplit[0]) % 400)); //leap years

            int endTimeSeconds = Integer.parseInt(endTimeSplit[5]) + //seconds
                    60 * Integer.parseInt(endTimeSplit[4]) + //minutes
                    60 * 60 * Integer.parseInt(endTimeSplit[3]) + //hours
                    60 * 60 * 24 * Integer.parseInt(endTimeSplit[2]) + //days
                    secondsInMonths + //months
                    60 * 60 * 24 * 365 * Integer.parseInt(endTimeSplit[0]) + //years
                    60 * 60 * 24 * ((Integer.parseInt(endTimeSplit[0]) % 4) -
                            (Integer.parseInt(endTimeSplit[0]) % 100) +
                            (Integer.parseInt(endTimeSplit[0]) % 400)); //leap years

            int differenceStartEnd = endTimeSeconds - startTimeSeconds; //duration in seconds

            durationDays = differenceStartEnd / (60 * 60 * 24);
            differenceStartEnd = differenceStartEnd % (60 * 60 * 24);

            durationHours = differenceStartEnd / (60 * 60);
            differenceStartEnd = differenceStartEnd % (60 * 60);

            durationMinutes = differenceStartEnd / 60;
            differenceStartEnd = differenceStartEnd % 60;

            durationSeconds = differenceStartEnd;

            text.get(i).setText("\nDate: " +
                    getMonthFromString(endTimeSplit[1]) + " " +
                    endTimeSplit[2] + ", " +
                    endTimeSplit[0] + "\n");
            text.get(i).append("Duration: " + durationDays + " days, " +
                    durationHours + " hours, " +
                    durationMinutes + " minutes, and " +
                    durationSeconds + " seconds\n");
            text.get(i).append("Fuel Efficiency: " +
                    Singleton.getInstance().userTrips[Singleton.getInstance().userTrips.length - i - 1].FuelEfficiency +
                    " L/100km\n");
        }

        Button backButton = (Button) findViewById(R.id.buttonBack);
        backButton.setBackgroundResource(R.drawable.button_background);
        backButton.setTextColor(Color.BLACK);
    }

    private TextView getTextView(int i) {
        switch (i) {
            case 0:
                return (TextView) findViewById(R.id.text_0);
            case 1:
                return (TextView) findViewById(R.id.text_1);
            case 2:
                return (TextView) findViewById(R.id.text_2);
            case 3:
                return (TextView) findViewById(R.id.text_3);
            case 4:
                return (TextView) findViewById(R.id.text_4);
            case 5:
                return (TextView) findViewById(R.id.text_5);
            case 6:
                return (TextView) findViewById(R.id.text_6);
            case 7:
                return (TextView) findViewById(R.id.text_7);
            case 8:
                return (TextView) findViewById(R.id.text_8);
            case 9:
                return (TextView) findViewById(R.id.text_9);
            default:
                return (TextView) findViewById(R.id.text_0);
        }
    }

    private String getMonthFromString(String s) {
        switch(s) {
            case "01": return "January";
            case "02": return "February";
            case "03": return "March";
            case "04": return "April";
            case "05": return "May";
            case "06": return "June";
            case "07": return "July";
            case "08": return "August";
            case "09": return "September";
            case "10": return "October";
            case "11": return "November";
            case "12": return "December";
            default: return "Invalid Month";
        }
    }

    private int numberDaysInMonth(String month, String year) {
        switch(month) {
            case "01": return 31;
            case "02": if (Integer.parseInt(year) % 4 != 0) return 28;
                else if (Integer.parseInt(year) % 100 != 0) return 29;
                else if (Integer.parseInt(year) % 400 != 0) return 28;
                else return 29;
            case "03": return 31;
            case "04": return 30;
            case "05": return 31;
            case "06": return 30;
            case "07": return 31;
            case "08": return 31;
            case "09": return 30;
            case "10": return 31;
            case "11": return 30;
            case "12": return 31;
            default: return 0;
        }
    }

    public void onBackButton(View view) {
        finish();
    }
}
