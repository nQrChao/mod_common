package com.box.other.huantansheng.easyphotos.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.box.common.R;
import com.box.other.chrisbanes.photoview.OnScaleChangedListener;
import com.box.other.chrisbanes.photoview.PhotoView;
import com.box.other.huantansheng.easyphotos.constant.Type;
import com.box.other.huantansheng.easyphotos.models.album.entity.Photo;
import com.box.other.huantansheng.easyphotos.setting.Setting;
import com.box.other.subscaleview.ImageSource;
import com.box.other.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.ArrayList;

public class PreviewMessagePhotosAdapter extends RecyclerView.Adapter<PreviewMessagePhotosAdapter.PreviewPhotosViewHolder> {
    private ArrayList<Photo> photos;
    private OnClickListener listener;
    private LayoutInflater inflater;

    public interface OnClickListener {
        void onPhotoClick();

        void onPhotoScaleChanged();
    }

    public PreviewMessagePhotosAdapter(Context cxt, ArrayList<Photo> photos, OnClickListener listener) {
        this.photos = photos;
        this.inflater = LayoutInflater.from(cxt);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PreviewPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PreviewPhotosViewHolder(inflater.inflate(R.layout._easy_photos_item_preview_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PreviewPhotosViewHolder holder, int position) {
        final Uri uri = photos.get(position).uri;
        final String path = photos.get(position).path;
        final String type = photos.get(position).type;
        final double ratio = (double) photos.get(position).height / (double) photos.get(position).width;

        holder.ivPlay.setVisibility(View.GONE);
        holder.ivPhotoView.setVisibility(View.GONE);
        holder.ivLongPhoto.setVisibility(View.GONE);

        if (path.endsWith(Type.GIF) || type.endsWith(Type.GIF)) {
            holder.ivPhotoView.setVisibility(View.VISIBLE);
            Setting.imageEngine.loadGif(holder.ivPhotoView.getContext(), uri, holder.ivPhotoView);
        } else {
            if (ratio > 2.3) {
                holder.ivLongPhoto.setVisibility(View.VISIBLE);
                holder.ivLongPhoto.setImage(ImageSource.uri(path));
            } else {
                holder.ivPhotoView.setVisibility(View.VISIBLE);
                Setting.imageEngine.loadPhoto(holder.ivPhotoView.getContext(), uri,
                        holder.ivPhotoView);
            }
        }

        holder.ivLongPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPhotoClick();
            }
        });
        holder.ivPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPhotoClick();
            }
        });
        holder.ivLongPhoto.setOnStateChangedListener(new SubsamplingScaleImageView.OnStateChangedListener() {
            @Override
            public void onScaleChanged(float newScale, int origin) {
                listener.onPhotoScaleChanged();
            }

            @Override
            public void onCenterChanged(PointF newCenter, int origin) {

            }
        });

        holder.ivPhotoView.setScale(1f);

        holder.ivPhotoView.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                listener.onPhotoScaleChanged();
            }
        });
    }

    private void toPlayVideo(View v, Uri uri, String type) {
        Context context = v.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    private Uri getUri(Context context, String path, Intent intent) {
        File file = new File(path);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return FileProvider.getUriForFile(context, Setting.fileProviderAuthority, file);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class PreviewPhotosViewHolder extends RecyclerView.ViewHolder {
        public SubsamplingScaleImageView ivLongPhoto;
        ImageView ivPlay;
        PhotoView ivPhotoView;

        PreviewPhotosViewHolder(View itemView) {
            super(itemView);
            ivLongPhoto = itemView.findViewById(R.id.iv_long_photo);
            ivPhotoView = itemView.findViewById(R.id.iv_photo_view);
            ivPlay = itemView.findViewById(R.id.iv_play);
        }
    }
}
