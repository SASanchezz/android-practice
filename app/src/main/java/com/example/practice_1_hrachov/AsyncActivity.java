package com.example.practice_1_hrachov;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;


public class AsyncActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    TextView mTextAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_activity);
        mTextAsync = findViewById(R.id.loadText);

        //new MyAsyncTask(mTextAsync).execute();
        Bundle bundle = new Bundle();
        getSupportLoaderManager().restartLoader(0, bundle, this);

    }

    public void back(View view) {
        finish();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MyAsyncTaskLoader(this, mTextAsync);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mTextAsync.setText(data);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        //pass
    }
}
