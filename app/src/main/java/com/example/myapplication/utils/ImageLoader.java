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

    // 메모리 캐시 관련 상수
    private static final int MEMORY_CACHE_DIVISION_FACTOR = 8;

    // 네트워크 연결 타임아웃 상수 (ms)
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 10000;

    // HTTP 상태 코드
    private static final int HTTP_OK = HttpURLConnection.HTTP_OK;

    // 바이트-킬로바이트 변환 상수
    private static final int BYTES_TO_KILOBYTES = 1024;

    private LruCache<String, Bitmap> memoryCache;
    private Context context;

    public ImageLoader(Context context) {
        this.context = context;

        // 메모리 캐시 초기화
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / BYTES_TO_KILOBYTES);
        int cacheSize = maxMemory / MEMORY_CACHE_DIVISION_FACTOR;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / BYTES_TO_KILOBYTES;
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
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Response Code for " + imageUrl + ": " + responseCode);

            if (responseCode == HTTP_OK) {
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