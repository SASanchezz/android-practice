package com.example.practice_1_hrachov;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MyAsyncTaskLoader extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = MyAsyncTaskLoader.class.getSimpleName();
    private final Random r = new Random();
    private WeakReference<TextView> mTextView;
    private final Integer step = 100; //milliseconds
    private Integer sum = 0;

    public MyAsyncTaskLoader(@NonNull Context context, TextView tv) {
        super(context);
        mTextView = new WeakReference<>(tv);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mTextView.get().setText("Teleporting...");
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int seconds = (r.nextInt(3)+2) * 1000;

        while (sum < seconds) {

            sum += step;
            Log.d(LOG_TAG, sum.toString());
            try {
                Integer pct = (sum > seconds) ? 100 : (Integer) (sum * 100 / seconds);
                mTextView.get().setText(pct.toString() + "%");
                Thread.sleep(step);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return "AsyncLoader worked more than " + sum / 1000 + " seconds!";
    }
}
