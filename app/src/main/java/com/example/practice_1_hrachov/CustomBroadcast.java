package com.example.practice_1_hrachov;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CustomBroadcast {

    //Broadcast
    static CustomReceiver mReceiver = new CustomReceiver();

    public static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";
}
