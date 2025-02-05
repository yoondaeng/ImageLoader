// ImageAdapter.java
package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private static final String TAG = "ImageAdapter";
    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 400;
    private Context context;
    private List<String> imageUrls;

    public ImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Log.d(TAG, "Loading image at position " + position + ": " + imageUrl);

        Glide.with(context)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 원본 이미지와 변환된 이미지 모두 디스크에 캐시
                .skipMemoryCache(false) // 메모리 캐시 활성화
                .override(IMAGE_WIDTH, IMAGE_HEIGHT) // 이미지 크기 최적화 300X400
                .centerCrop()

                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(new RequestListener<Drawable>() { // 이미지 로딩 상태와 캐시 사용 여부 모니터링
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Failed to load image: " + imageUrl, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        Log.d(TAG, "Successfully loaded image: " + imageUrl +
                                " from " + dataSource.name()); // 캐시 상태 로깅
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}