package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.net.HttpURLConnection;
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
        } else {
            Log.e(TAG, "이미지 다운로드 실패: " + imageUrl);
        }

        return downloadedBitmap;
    }

    private Bitmap downloadImageFromNetwork(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Response Code for " + imageUrl + ": " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else {
                Log.e(TAG, "HTTP error code: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "이미지 다운로드 중 예외 발생: " + imageUrl, e);
            e.printStackTrace();
            return null;
        }
    }
}