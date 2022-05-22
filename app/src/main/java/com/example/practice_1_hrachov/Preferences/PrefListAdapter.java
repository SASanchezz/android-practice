package com.example.practice_1_hrachov.Preferences;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice_1_hrachov.Database.TblPrefs;
import com.example.practice_1_hrachov.R;

import java.util.List;

public class PrefListAdapter extends RecyclerView.Adapter<PrefListAdapter.PrefViewHolder> {

    private final LayoutInflater mInflater;
    private List<TblPrefs> mPrefs; // Cached copy of words
    private String mBackgroundColor = "BLUE";
    private String mFontColor = "BLACK";
    PrefListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public PrefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.preference_item, parent, false);
        return new PrefViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrefViewHolder holder, int position) {
        if (mPrefs != null) {
            TblPrefs current = mPrefs.get(position);
            holder.prefItemView.setText(current.getPrefKey() + " - " + current.getPrefValue());
            holder.prefItemView.setTextColor(Color.parseColor(mFontColor));
            holder.prefItemView.setBackgroundColor(Color.parseColor(mBackgroundColor));
        } else {
            // Covers the case of data not being ready yet.
            holder.prefItemView.setText("No preferences yet");
        }
    }

    void setWords(List<TblPrefs> prefs, String backgroundColor, String fontColor){
        mPrefs = prefs;
        mBackgroundColor = backgroundColor;
        mFontColor = fontColor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mPrefs == null) ? 0 : mPrefs.size();
    }


    class PrefViewHolder extends RecyclerView.ViewHolder {
        private final TextView prefItemView;

        private PrefViewHolder(View itemView) {
            super(itemView);
            prefItemView = itemView.findViewById(R.id.textView);
        }
    }
}
