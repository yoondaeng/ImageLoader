package com.example.myapplication.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class MemoryMonitor {
    private static final String TAG = "MemoryMonitor";
    private static final long MONITORING_INTERVAL = 5000; // 5초

    private final Handler handler;
    private boolean isMonitoring = false;

    public MemoryMonitor() {
        handler = new Handler(Looper.getMainLooper());
    }

    private final Runnable memoryCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isMonitoring) return;

            Runtime runtime = Runtime.getRuntime();
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            float memoryUsagePercent = (usedMemory * 100f) / maxMemory;

            Log.d(TAG, String.format(
                    "Memory Usage:\n" +
                            "사용 중인 메모리: %.2f MB\n" +
                            "최대 메모리: %.2f MB\n" +
                            "사용률: %.1f%%",
                    usedMemory / (1024.0 * 1024.0),
                    maxMemory / (1024.0 * 1024.0),
                    memoryUsagePercent
            ));

            handler.postDelayed(this, MONITORING_INTERVAL);
        }
    };

    public void startMonitoring() {
        isMonitoring = true;
        handler.post(memoryCheckRunnable);
    }

    public void stopMonitoring() {
        isMonitoring = false;
        handler.removeCallbacks(memoryCheckRunnable);
    }
}

