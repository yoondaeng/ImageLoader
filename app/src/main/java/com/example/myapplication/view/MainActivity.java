package com.example.myapplication.view;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.view.adapter.ImageAdapter;
import com.example.myapplication.model.data.ImageItem;
import com.example.myapplication.utils.Constants;
import com.example.myapplication.utils.MemoryMonitor;
import com.example.myapplication.viewmodel.MainViewModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private ImageAdapter imageAdapter;
    private RecyclerView imageGridView;
    private MemoryMonitor memoryMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 메모리 모니터링 시작
        memoryMonitor = new MemoryMonitor();
        memoryMonitor.startMonitoring();

        // ViewModel 초기화
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // RecyclerView 설정
        setupRecyclerView();

        // 데이터 관찰
        viewModel.getImages().observe(this, images -> {
            List<String> imageUrls = new ArrayList<>();
            for (ImageItem item : images) {
                imageUrls.add(item.getImageUrl());
            }
            imageAdapter.updateImages(imageUrls);
        });

        // 이미지 로드 시작
        viewModel.loadImages();
    }

    private void setupRecyclerView() {
        imageGridView = findViewById(R.id.imageGridView);
        int orientation = getResources().getConfiguration().orientation;
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE)
                ? Constants.LANDSCAPE_SPAN_COUNT
                : Constants.PORTRAIT_SPAN_COUNT;

        imageGridView.setLayoutManager(new GridLayoutManager(this, spanCount));
        imageAdapter = new ImageAdapter(this, new ArrayList<>());
        imageGridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (memoryMonitor != null) {
            memoryMonitor.stopMonitoring();
        }
    }
}