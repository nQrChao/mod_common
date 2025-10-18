package com.box.other.huantansheng.easyphotos.models.ad;

import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.box.com.R;

public class AdViewHolder extends RecyclerView.ViewHolder {
    public FrameLayout adFrame;
    public AdViewHolder(View itemView) {
        super(itemView);
        adFrame = (FrameLayout) itemView.findViewById(R.id.ad_frame_easy_photos);
    }
}
