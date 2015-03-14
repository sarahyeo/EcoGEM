package com.cloud1001.ecogem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mojio.mojiosdk.MojioClient;
import com.mojio.mojiosdk.models.Trip;
import com.mojio.mojiosdk.models.User;
import com.mojio.mojiosdk.models.Vehicle;

import java.util.HashMap;


public class MainActivity extends Activity {
    //===========================================================================
    // Mojio App Setup
    // These values will match the keys given to you for your Mojio application in the
    // Mojio Developer panel.
    //===========================================================================
    private final static String MOJIO_APP_ID = "980bfaf7-eb5a-4f7f-ba17-9f2ecd79de08";
    private final static String REDIRECT_URL = "ecogem://"; // Example "myfirstmojio://"

    //===========================================================================
    // Activity properties
    //===========================================================================
    // Activity request ID to allow us to listen for the OAuth2 response
    private static int OAUTH_REQUEST = 0;

    // The main mojio client object; allows login and data retrieval to occur.
    private MojioClient mMojio;
    private Singleton singleton;

    public User mCurrentUser;
    public Vehicle[] mUserVehicles;
    public Trip[] mUserTrips;

    private Button mLoginButton, mCouponButton, mVehiclesButton;
    private TextView mUserName, fuelScore, goal;
    private ImageView goalIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup mojio client with app keys.
        mMojio = new MojioClient(this, MOJIO_APP_ID, null, REDIRECT_URL);
        singleton = Singleton.getInstance();

        mLoginButton = (Button)findViewById(R.id.oauth2LoginButton);
        mUserName = (TextView)findViewById(R.id.userName);
        fuelScore = (TextView)findViewById(R.id.fuelScore);
        fuelScore.setVisibility(View.GONE);
        mCouponButton = (Button)findViewById(R.id.couponButton);
        mCouponButton.setVisibility(View.GONE);
        mVehiclesButton = (Button)findViewById(R.id.vehiclesButton);
        mVehiclesButton.setVisibility(View.GONE);
        goal = (TextView)findViewById(R.id.goal);
        goal.setVisibility(View.GONE);
        goalIcon = (ImageView)findViewById(R.id.goalIcon);
        goalIcon.setVisibility(View.GONE);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOauth2Login();
            }
        });
    }

    // IMPORTANT: This uses the com.mojio.mojiosdk.networking.OAuthLoginActivity class.
    // For this to work correctly, we must declare it as an Activity in our app's AndroidManifest.xml file.
    private void doOauth2Login() {
        // Launch the OAuth request; this will launch a web view Activity for the user enter their login.
        // When the Activity finishes, we listen for it in the onActivityResult method
        mMojio.launchLoginActivity(this, OAUTH_REQUEST);

    }

    // IMPORTANT: Must be overridden so that we can listen for the OAuth2 result and know if we were
    // logged in successfully. We do not have to bother with storing the auth tokens, the SDK codes that
    // for us.
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == OAUTH_REQUEST) {
            // We now have a stored access token
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                getCurrentUser(); // Now attempt to get user info

            }
            else {
                Toast.makeText(MainActivity.this, "Problem logging in", Toast.LENGTH_LONG).show();
            }
        }
    }

    // We have our access token stored now with the client, but we now need to grab our user ID.
    private void getCurrentUser() {
        String entityPath = "Users"; // TODO need userID?
        HashMap<String, String> queryParams = new HashMap<>();

        mMojio.get(User[].class, entityPath, queryParams, new MojioClient.ResponseListener<User[]>() {
            @Override
            public void onSuccess(User[] result) {
                // Should have one result
                try {
                    mCurrentUser = result[0]; // Save user info so we can use ID later
                    singleton.currentUser = mCurrentUser;
                    mCouponButton.setVisibility(View.VISIBLE);

                    // Show user data
                    mUserName.setText("Hello " + mCurrentUser.FirstName + " " + mCurrentUser.LastName);
                    mLoginButton.setVisibility(View.GONE);

                    getUserVehicles();
                    getUserTrips();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Problem getting users", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this, "Problem getting users", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Now that we have the current user, we can use their ID to get data
    private void getUserVehicles() {
        String entityPath = String.format("Users/%s/Vehicles", mCurrentUser._id);
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("sortBy", "Name");
        queryParams.put("desc", "true");

        mMojio.get(Vehicle[].class, entityPath, queryParams, new MojioClient.ResponseListener<Vehicle[]>() {
            @Override
            public void onSuccess(Vehicle[] result) {
                mUserVehicles = result; // Save
                singleton.userVehicles = mUserVehicles;
                mVehiclesButton.setVisibility(View.VISIBLE);

                goal.setText("Goal:" + Integer.toString(calculateGoal()));
                goal.setVisibility(View.VISIBLE);
                goalIcon.setBackgroundResource(R.drawable.goal_reached);
                goalIcon.setVisibility(View.VISIBLE);

                if (mUserVehicles.length == 0) {
                    Toast.makeText(MainActivity.this, "No vehicles found", Toast.LENGTH_LONG).show();
                }

                // Create list data from result
                /*
                ArrayList<String> listData = new ArrayList<String>();
                Vehicle v;
                for (int i = 0; i < mUserVehicles.length; i++) {
                    v = result[i];
                    listData.add(String.format("%s %s", v.getNameDescription(), v.LicensePlate));
                }*/

            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this, "Problem getting vehicles", Toast.LENGTH_LONG).show();
            }
        });
    }


    // Now that we have the current user, we can use their ID to get data
    private void getUserTrips() {
        String entityPath = String.format("Users/%s/Trips", mCurrentUser._id); //!!!??!?!?!?!
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("sortBy", "Name");
        queryParams.put("desc", "true");

        mMojio.get(Trip[].class, entityPath, queryParams, new MojioClient.ResponseListener<Trip[]>() {
            @Override
            public void onSuccess(Trip[] result) {
                mUserTrips = result; // Save
                singleton.userTrips = mUserTrips;

                fuelScore.setText(Integer.toString(calculateFuelScore()));
                fuelScore.setVisibility(View.VISIBLE);


                if (mUserTrips.length == 0) {
                    Toast.makeText(MainActivity.this, "No trips found", Toast.LENGTH_LONG).show();
                }

                /*
                // Create list data from result
                ArrayList<String> listData = new ArrayList<String>();
                Trip t;
                for (int i = 0; i < mUserTrips.length; i++) {
                    t = result[i];
                    listData.add(String.format("%s %s", t.Distance, t.FuelEfficiency));
                }*/

                // Show result in list
               // ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listData);
                //mVehicleList.setAdapter(itemsAdapter);

            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this, "Problem getting trips", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
       for (Trip t: mUserTrips) {
           fuel += t.FuelEfficiency * t.Distance;
           totalDistance += t.Distance;
       }
        return (int) (fuel / totalDistance);

    }

    public int calculateGoal() {
        return 8;
    }
}
