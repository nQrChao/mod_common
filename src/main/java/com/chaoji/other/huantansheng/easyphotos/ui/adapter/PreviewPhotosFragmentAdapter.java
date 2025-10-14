package com.chaoji.other.huantansheng.easyphotos.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chaoji.common.R;
import com.chaoji.other.huantansheng.easyphotos.constant.Type;
import com.chaoji.other.huantansheng.easyphotos.result.Result;
import com.chaoji.other.huantansheng.easyphotos.setting.Setting;
import com.chaoji.other.huantansheng.easyphotos.ui.widget.PressedImageView;
import com.chaoji.other.huantansheng.easyphotos.utils.media.DurationUtils;

public class PreviewPhotosFragmentAdapter extends RecyclerView.Adapter<PreviewPhotosFragmentAdapter.PreviewPhotoVH> {
    private LayoutInflater inflater;
    private OnClickListener listener;
    private int checkedPosition = -1;

    public PreviewPhotosFragmentAdapter(Context context, OnClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @Override
    public PreviewPhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PreviewPhotoVH(inflater.inflate(R.layout._easy_photos_item_preview_selected_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(PreviewPhotoVH holder, int position) {
        final int p = position;
        String path = Result.getPhotoPath(position);
        String type = Result.getPhotoType(position);
        Uri uri = Result.getPhotoUri(position);
        long duration = Result.getPhotoDuration(position);

        final boolean isGif = path.endsWith(Type.GIF) || type.endsWith(Type.GIF);
        if (Setting.showGif && isGif) {
            Setting.imageEngine.loadGifAsBitmap(holder.ivPhoto.getContext(), uri, holder.ivPhoto);
            holder.tvType.setText(R.string.gif_easy_photos);
            holder.tvType.setVisibility(View.VISIBLE);
        } else if (Setting.showVideo && type.contains(Type.VIDEO)) {
            Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), uri, holder.ivPhoto);
            holder.tvType.setText(DurationUtils.format(duration));
            holder.tvType.setVisibility(View.VISIBLE);
        } else {
            Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), uri, holder.ivPhoto);
            holder.tvType.setVisibility(View.GONE);
        }

        if (checkedPosition == p) {
            holder.frame.setVisibility(View.VISIBLE);
        } else {
            holder.frame.setVisibility(View.GONE);
        }
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhotoClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Result.count();
    }

    public void setChecked(int position) {
        if (checkedPosition == position) {
            return;
        }
        checkedPosition = position;
        notifyDataSetChanged();
    }

    class PreviewPhotoVH extends RecyclerView.ViewHolder {
        PressedImageView ivPhoto;
        View frame;
        TextView tvType;

        public PreviewPhotoVH(View itemView) {
            super(itemView);
            ivPhoto = (PressedImageView) itemView.findViewById(R.id.iv_photo);
            frame = itemView.findViewById(R.id.v_selector);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

    public interface OnClickListener {
        void onPhotoClick(int position);
    }
}
