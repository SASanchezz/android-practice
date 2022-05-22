package com.example.practice_1_hrachov;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice_1_hrachov.Views.CanvasView;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

public class CanvasFragment extends Fragment {

    private CanvasView mCanvasView;

    public CanvasFragment() {

    }

    public static CanvasFragment newInstance() {
        CanvasFragment fragment = new CanvasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCanvasView = new CanvasView(getActivity());
        mCanvasView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        mCanvasView.resume();
        return mCanvasView;
    }
}