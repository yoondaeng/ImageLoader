package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    private static final int MEMORY_CACHE_SCREEN_COUNT = 2;
    private static final long DISK_CACHE_SIZE_MB = 250;
    private static final long MEGABYTE_TO_BYTE = 1024 * 1024;

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // 메모리 캐시 크기 설정
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(MEMORY_CACHE_SCREEN_COUNT)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        // 디스크 캐시 크기 설정
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                DISK_CACHE_SIZE_MB * MEGABYTE_TO_BYTE)); // 250MB
    }
}
