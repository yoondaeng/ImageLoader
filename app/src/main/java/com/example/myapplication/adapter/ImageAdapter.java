package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.utils.ImageLoader;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<String> imageUrls;
    private ImageLoader imageLoader;
    private ExecutorService executorService;

    public ImageAdapter(Context context, List<String> imageUrls, ImageLoader imageLoader) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.imageLoader = imageLoader;
        this.executorService = Executors.newFixedThreadPool(4);
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

        executorService.execute(() -> {
            Bitmap bitmap = imageLoader.loadImage(imageUrl);
            holder.imageView.post(() -> {
                if (bitmap != null) {
                    holder.imageView.setImageBitmap(bitmap);
                }
            });
        });
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