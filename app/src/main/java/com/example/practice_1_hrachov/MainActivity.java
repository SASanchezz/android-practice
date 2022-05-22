package com.example.practice_1_hrachov;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice_1_hrachov.Preferences.PreferencesActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements FetchAddressTask.OnFetchAddressCompleted, NavigationView.OnNavigationItemSelectedListener {

    //Texts
    public static final String EXTRA_MESSAGE1 = "extra.MESSAGE1";
    public static final int TEXT_REQUEST = 1;
    private EditText mMessageEditText1;
    public TextView mReplyTextView;
    private EditText fragmentReply;
    //Navigation bar
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    //Broadcast
    CustomReceiver mReceiver = new CustomReceiver();

    //Google location
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static FusedLocationProviderClient mFusedLocationClient;

    //Orientation
    private OrientationFragment orientationFragment;

    //Canvas
    private CanvasFragment canvasFragment;
    private AnimationFragment animationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        //Broadcast
        setBroadcast();

        //Navigation bar
        setNavigationBar(savedInstanceState);

        //Text fields
        setTextFields();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(), mLocationCallback,null);
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            new FetchAddressTask(MainActivity.this, MainActivity.this)
                    .execute(locationResult.getLastLocation());
        }
    };

    @Override
    public void OnFetchAddressCompleted(String result) {
        //Send update to widget
        result = (result == null) ? "Unknown area" : result;
        Intent intent = new Intent(MainActivity.this, GPSWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), GPSWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra("location", result);
        sendBroadcast(intent);
    }

    private void setTextFields() {
        mMessageEditText1 = findViewById(R.id.var1);
        mReplyTextView = findViewById(R.id.backVar);
    }

    private void setBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        this.registerReceiver(mReceiver, filter);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, new IntentFilter(CustomBroadcast.ACTION_CUSTOM_BROADCAST));
    }

    private void setNavigationBar(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.to_home);
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void startOrientationFragment(View view) {
        orientationFragment = OrientationFragment.newInstance(mMessageEditText1.getText().toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, orientationFragment).addToBackStack(null).commit();
    }

    public void closeOrientationFragment(View view) {
        fragmentReply = findViewById(R.id.valueToMain);
        String reply = (fragmentReply.getText().toString().equals("")) ? "No data from fragment" : fragmentReply.getText().toString();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(orientationFragment).commit();

        mReplyTextView.setText(reply);
    }

    public void startCanvasFragment() {
        canvasFragment = CanvasFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer, canvasFragment).addToBackStack(null).commit();
    }
    public void closeCanvasFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(canvasFragment).commit();
    }

    public void startAnimationFragment() {
        animationFragment = AnimationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer, animationFragment).addToBackStack(null).commit();
    }
    public void closeAnimationFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(animationFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply = data.getStringExtra(SecondaryActivity.EXTRA_REPLY);
                mReplyTextView.setText(reply);
            }
        }
    }
    public void sendCustomBroadcast(View view) {
        Intent customBroadcastIntent = new Intent(CustomBroadcast.ACTION_CUSTOM_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.to_home):
                Toast.makeText(MainActivity.this, "You're already on main :)", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.to_help):
                openHelp(null);
                Toast.makeText(MainActivity.this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.to_preferences):
                openPreferencesActivity(null);
                Toast.makeText(MainActivity.this, "Preferences", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.to_canvas):
                startCanvasFragment();
                Toast.makeText(MainActivity.this, "Canvas", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.to_animations):
                startAnimationFragment();
                Toast.makeText(MainActivity.this, "Animations", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.start_broadcast):
                sendCustomBroadcast(null);
                break;
            case (R.id.start_background_app):
                startTask(null);
                break;
        }

        return true;
    }

    public void openHelp(MenuItem item) {
        Intent newIntent = new Intent(this, HelpActivity.class);
        startActivityForResult(newIntent, TEXT_REQUEST);
    }

    private void startTask(View view) {
        Intent newIntent = new Intent(this, AsyncActivity.class);
        startActivityForResult(newIntent, TEXT_REQUEST);
    }

    public void openPreferencesActivity(View view) {
        Intent newIntent = new Intent(this, PreferencesActivity.class);
        startActivityForResult(newIntent, TEXT_REQUEST);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


}