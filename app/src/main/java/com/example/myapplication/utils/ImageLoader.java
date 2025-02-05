package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.net.URL;

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private LruCache<String, Bitmap> memoryCache;
    private Context context;

    public ImageLoader(Context context) {
        this.context = context;

        // 메모리 캐시 초기화
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Bitmap loadImage(String imageUrl) {
        // 메모리 캐시 확인
        Bitmap cachedBitmap = memoryCache.get(imageUrl);
        if (cachedBitmap != null) return cachedBitmap;

        // 네트워크에서 이미지 다운로드
        Bitmap downloadedBitmap = downloadImageFromNetwork(imageUrl);

        if (downloadedBitmap != null) {
            // 메모리 캐시에 저장
            memoryCache.put(imageUrl, downloadedBitmap);
        }

        return downloadedBitmap;
    }

    private Bitmap downloadImageFromNetwork(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "이미지 다운로드 실패: " + imageUrl, e);
            return null;
        }
    }
}