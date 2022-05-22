package com.example.practice_1_hrachov;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.practice_1_hrachov.Preferences.PreferencesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HelpActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static final int TEXT_REQUEST = 1;

    //Broadcast
    CustomReceiver mReceiver = new CustomReceiver();

    public boolean openMain() {
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivityForResult(newIntent, TEXT_REQUEST);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView textView = findViewById(R.id.mainText);
        textView.setMovementMethod(new ScrollingMovementMethod());

        //Broadcast
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        this.registerReceiver(mReceiver, filter);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, new IntentFilter(CustomBroadcast.ACTION_CUSTOM_BROADCAST));


        //Navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        toggle.setDrawerIndicatorEnabled(true);

        drawer.addDrawerListener(toggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.to_help);
        }

                FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HelpActivity.this, "Поставте автомат :)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        System.out.println("clicked");
        switch (item.getItemId()) {
            case(R.id.to_home):
                openMain();
                Toast.makeText(HelpActivity.this, "Main", Toast.LENGTH_SHORT).show();
                break;
            case(R.id.to_help):
                Toast.makeText(HelpActivity.this, "You're already on help :)", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.to_canvas):
                Toast.makeText(HelpActivity.this, "Please, click canvas button from Main activity :)", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.to_animations):
                Toast.makeText(HelpActivity.this, "Please, click animation button from Main activity :)", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.to_preferences):
                openPreferencesActivity(null);
                Toast.makeText(HelpActivity.this, "Preferences", Toast.LENGTH_SHORT).show();
                break;
            case(R.id.start_broadcast):
                sendCustomBroadcast(null);
                break;
            case(R.id.start_background_app):
                startTask(null);
                break;
        }
        return true;
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

    public void sendCustomBroadcast(View view) {
        Intent customBroadcastIntent = new Intent(CustomBroadcast.ACTION_CUSTOM_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);
    }

}
