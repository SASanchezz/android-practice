package com.example.practice_1_hrachov.Preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import com.example.practice_1_hrachov.Data.myColors;
import com.example.practice_1_hrachov.Database.TblPrefs;
import com.example.practice_1_hrachov.R;

import java.util.List;


public class PreferencesActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private final String sharedPrefFile = "com.example.android.sharedPreferences";

    private PrefViewModel mPrefViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        //Set Preference folder
        mPreferences = getSharedPreferences(
                sharedPrefFile, MODE_PRIVATE);

        //Get Preferences from database
        RecyclerView recyclerView = findViewById(R.id.preferencesview);
        final PrefListAdapter adapter = new PrefListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Fill font spinner with rows
        Spinner fontSpinner = findViewById(R.id.spinnerFontColor);
        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(this,
                R.array.preferences_colors, android.R.layout.simple_spinner_item);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontAdapter);
        String fontColor = mPreferences.getString("fontColor", "BLACK");
        fontSpinner.setSelection(myColors.ColorMap.get(fontColor));

        //Fill background spinner with rows
        Spinner backgroundSpinner = findViewById(R.id.spinnerBackgroundColor);
        ArrayAdapter<CharSequence> backgroundAdapter = ArrayAdapter.createFromResource(this,
                R.array.preferences_colors, android.R.layout.simple_spinner_item);
        backgroundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backgroundSpinner.setAdapter(backgroundAdapter);
        String backgroundColor = mPreferences.getString("backgroundColor", "BLUE");
        backgroundSpinner.setSelection(myColors.ColorMap.get(backgroundColor));


        //Spinner listeners
        backgroundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = (String) parent.getItemAtPosition(position);

                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString("backgroundColor", color);
                preferencesEditor.apply();

                if (mPrefViewModel.getPrefByPk("backgroundColor").getPrefValue()
                        != mPreferences.getString("backgroundColor", "blue")) {
                    mPrefViewModel.update(new TblPrefs("backgroundColor", color));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {;
            }
        });

        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = (String) parent.getItemAtPosition(position);

                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString("fontColor", color);
                preferencesEditor.apply();

                if (mPrefViewModel.getPrefByPk("fontColor").getPrefValue()
                        != mPreferences.getString("fontColor", "black")) {
                    mPrefViewModel.update(new TblPrefs("fontColor", color));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {;
            }
        });


        mPrefViewModel = new ViewModelProvider(this).get(PrefViewModel.class);
        mPrefViewModel.getAllPrefs().observe(this, new Observer<List<TblPrefs>>() {

            @Override
            public void onChanged(@Nullable final List<TblPrefs> prefs) {
                String backgroundColor = "BLUE";
                String fontColor = "BLACK";

                for (int i = 0; i < prefs.size(); ++i) {
                    Log.d("MyApp", "HEEY:  "+prefs.get(i).getPrefKey());
                    if (prefs.get(i).getPrefKey() == "backgroundColor" || i == 0) {
                        backgroundColor = prefs.get(i).getPrefValue();

                    } else if (prefs.get(i).getPrefKey() == "fontColor" || i == 1) {
                        fontColor = prefs.get(i).getPrefValue();
                    }
                }

                adapter.setWords(prefs, backgroundColor, fontColor);
            }
        });
    }


    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.apply();
    }

    public void back(View view) {
        finish();
    }
}
