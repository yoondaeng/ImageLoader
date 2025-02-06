package com.example.myapplication.utils;

import com.bumptech.glide.load.DataSource;

public class CacheMetrics {
    private int memoryHits = 0;
    private int diskHits = 0;
    private int networkLoads = 0;
    private long totalLoadTime = 0;
    private int totalRequests = 0;

    public synchronized void logRequest(DataSource dataSource, long loadTime) {
        totalRequests++;
        totalLoadTime += loadTime;

        switch (dataSource) {
            case MEMORY_CACHE:
                memoryHits++;
                break;
            case RESOURCE_DISK_CACHE:
                diskHits++;
                break;
            case REMOTE:
                networkLoads++;
                break;
        }
    }

    public synchronized String getStatistics() {
        if (totalRequests == 0) return "No requests recorded";

        float memoryHitRate = (float) memoryHits / totalRequests * 100;
        float diskHitRate = (float) diskHits / totalRequests * 100;
        float networkLoadRate = (float) networkLoads / totalRequests * 100;
        float avgLoadTime = totalLoadTime / (float) totalRequests;

        return String.format(
                "Performance Statistics:\n" +
                        "총 요청: %d\n" +
                        "메모리 캐시 히트: %d (%.1f%%)\n" +
                        "디스크 캐시 히트: %d (%.1f%%)\n" +
                        "네트워크 로드: %d (%.1f%%)\n" +
                        "평균 로딩 시간: %.2fms",
                totalRequests,
                memoryHits, memoryHitRate,
                diskHits, diskHitRate,
                networkLoads, networkLoadRate,
                avgLoadTime
        );
    }

    public void reset() {
        memoryHits = 0;
        diskHits = 0;
        networkLoads = 0;
        totalLoadTime = 0;
        totalRequests = 0;
    }
}