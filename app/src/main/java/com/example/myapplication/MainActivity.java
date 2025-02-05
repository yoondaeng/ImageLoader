package com.example.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ImageAdapter;
import com.example.myapplication.utils.ImageLoader;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView imageGridView;
    private ImageAdapter imageAdapter;
    private ImageLoader imageLoader;

    // 테스트용 이미지 URL 목록
    private List<String> imageUrls = Arrays.asList(
//            "https://example.com/image1.jpg",
//            "https://example.com/image2.jpg",
            // 실제 웹사이트 이미지 URL 추가
            "https://www.sooplive.co.kr/directory/category"  // URL
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 가로/세로 모드 지원을 위한 GridLayoutManager 설정
        int orientation = getResources().getConfiguration().orientation;
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 4 : 2;

        imageGridView = findViewById(R.id.imageGridView);
        imageGridView.setLayoutManager(new GridLayoutManager(this, spanCount));

        // 이미지 로더 초기화
        imageLoader = new ImageLoader(this);

        // 이미지 어댑터 초기화 및 설정
        imageAdapter = new ImageAdapter(this, imageUrls, imageLoader);
        imageGridView.setAdapter(imageAdapter);
    }
}