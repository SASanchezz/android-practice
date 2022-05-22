package com.example.practice_1_hrachov;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice_1_hrachov.Views.AnimationsView;
import com.example.practice_1_hrachov.Views.CanvasView;

public class AnimationFragment extends Fragment {

    private AnimationsView mAnimationsView;
    public AnimationFragment() {
    }

    public static AnimationFragment newInstance() {
        AnimationFragment fragment = new AnimationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAnimationsView = new AnimationsView(getActivity());
        mAnimationsView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        return mAnimationsView;
    }
}