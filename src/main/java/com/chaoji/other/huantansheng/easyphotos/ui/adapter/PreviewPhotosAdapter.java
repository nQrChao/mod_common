package com.chaoji.other.huantansheng.easyphotos.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.chaoji.common.R;
import com.chaoji.other.chrisbanes.photoview.OnScaleChangedListener;
import com.chaoji.other.chrisbanes.photoview.PhotoView;
import com.chaoji.other.subscaleview.ImageSource;
import com.chaoji.other.huantansheng.easyphotos.constant.Type;
import com.chaoji.other.huantansheng.easyphotos.models.album.entity.Photo;
import com.chaoji.other.huantansheng.easyphotos.setting.Setting;
import com.chaoji.other.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.ArrayList;

public class PreviewPhotosAdapter extends RecyclerView.Adapter<PreviewPhotosAdapter.PreviewPhotosViewHolder> {
    private ArrayList<Photo> photos;
    private OnClickListener listener;
    private LayoutInflater inflater;

    public interface OnClickListener {
        void onPhotoClick();

        void onPhotoScaleChanged();
    }

    public PreviewPhotosAdapter(Context cxt, ArrayList<Photo> photos, OnClickListener listener) {
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
        final double ratio =
                (double) photos.get(position).height / (double) photos.get(position).width;

        holder.ivPlay.setVisibility(View.GONE);
        holder.ivPhotoView.setVisibility(View.GONE);
        holder.ivVideoView.setVisibility(View.GONE);
        holder.ivLongPhoto.setVisibility(View.GONE);

        if (type.contains(Type.VIDEO)) {
            holder.ivVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    holder.ivPlay.setVisibility(View.GONE);
                    holder.ivPhotoView.setVisibility(View.GONE);
                    return false;
                }

            });
            holder.ivVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    holder.ivPlay.setVisibility(View.VISIBLE);
                    holder.ivPhotoView.setVisibility(View.VISIBLE);
                    //holder.ivVideoView.setVisibility(View.GONE);
                    //mp.reset();

                }
            });
            holder.ivVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            holder.ivVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    holder.ivPlay.setVisibility(View.VISIBLE);
                    holder.ivPhotoView.setVisibility(View.VISIBLE);
                    holder.ivVideoView.setVisibility(View.GONE);
                    holder.ivVideoView.setTag("");
                }

            });

            holder.ivPlay.setVisibility(View.VISIBLE);
            holder.ivPhotoView.setVisibility(View.VISIBLE);
            Setting.imageEngine.loadPhoto(holder.ivPhotoView.getContext(), uri, holder.ivPhotoView);
            holder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivPlay.setVisibility(View.GONE);
                    holder.ivPhotoView.setVisibility(View.GONE);
                    holder.ivVideoView.setVisibility(View.VISIBLE);
                    holder.ivVideoView.setVideoPath(path);
                    holder.ivVideoView.start();
                    holder.ivVideoView.setTag("PLAY");
                    //listener.onPhotoClick();
                    //toPlayVideo(v, uri, type);
                }
            });
        } else if (path.endsWith(Type.GIF) || type.endsWith(Type.GIF)) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    private Uri getUri(Context context, String path, Intent intent) {
        File file = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            return FileProvider.getUriForFile(context, Setting.fileProviderAuthority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class PreviewPhotosViewHolder extends RecyclerView.ViewHolder {
        public SubsamplingScaleImageView ivLongPhoto;
        ImageView ivPlay;
        PhotoView ivPhotoView;
        VideoView ivVideoView;

        PreviewPhotosViewHolder(View itemView) {
            super(itemView);
            ivLongPhoto = itemView.findViewById(R.id.iv_long_photo);
            ivPhotoView = itemView.findViewById(R.id.iv_photo_view);
            ivVideoView = itemView.findViewById(R.id.iv_video_view);
            ivPlay = itemView.findViewById(R.id.iv_play);
        }
    }
}
