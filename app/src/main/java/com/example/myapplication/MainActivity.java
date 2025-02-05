package com.example.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ImageAdapter;
import com.example.myapplication.utils.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView imageGridView;
    private ImageAdapter imageAdapter;
    private ImageLoader imageLoader;
    private List<String> imageUrls = new ArrayList<>();

    private void loadImagesFromWebpage() {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.sooplive.co.kr/directory/category")
                        .userAgent("Mozilla/5.0")  // User-Agent 추가
                        .timeout(10000)            // 타임아웃 설정
                        .get();

                Elements images = doc.select("img[src]");
                Log.d(TAG, "Found " + images.size() + " images");

                imageUrls.clear();
                images.forEach(element -> {
                    String imageUrl = element.attr("abs:src");
                    Log.d(TAG, "Image URL: " + imageUrl);  // URL 로깅
                    if (!imageUrl.isEmpty()) {
                        imageUrls.add(imageUrl);
                    }
                });

                // 이미지 URL이 비어있는지 확인
                if (imageUrls.isEmpty()) {
                    Log.e(TAG, "No images found!");
                } else {
                    Log.d(TAG, "Total images added: " + imageUrls.size());
                }

                runOnUiThread(() -> {
                    imageAdapter.notifyDataSetChanged();
                    // 데이터 갱신 후 상태 로깅
                    Log.d(TAG, "Adapter notified with " + imageUrls.size() + " items");
                });
            } catch (IOException e) {
                Log.e(TAG, "Error loading webpage: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 가로/세로 모드 지원을 위한 GridLayoutManager 설정
        int orientation = getResources().getConfiguration().orientation;
        // 가로 모드일때는 4열 세로 모드 3열
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 4 : 3;

        imageGridView = findViewById(R.id.imageGridView);
        imageGridView.setLayoutManager(new GridLayoutManager(this, spanCount));

        imageAdapter = new ImageAdapter(this, imageUrls);
        imageGridView.setAdapter(imageAdapter);

        // 웹페이지에서 이미지 URL 로드
        loadImagesFromWebpage();
    }

    // 테스트용 이미지 URL 목록
//    private List<String> imageUrls = Arrays.asList(
//            // 테스트용 이미지
////            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/640px-PNG_transparency_demonstration_1.png",
////            "https://upload.wikimedia.org/wikipedia/commons/9/9a/Gull_portrait_ca_usa.jpg",
////            "https://upload.wikimedia.org/wikipedia/commons/7/7d/Drosophila_melanogaster_%28fruit_fly%29.jpg"
//    );

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // 가로/세로 모드 지원을 위한 GridLayoutManager 설정
//        int orientation = getResources().getConfiguration().orientation;
//        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 4 : 2;
//
//        imageGridView = findViewById(R.id.imageGridView);
//        imageGridView.setLayoutManager(new GridLayoutManager(this, spanCount));
//
//        // 이미지 로더 초기화
//        imageLoader = new ImageLoader(this);
//
//        // 이미지 어댑터 초기화 및 설정
//        imageAdapter = new ImageAdapter(this, imageUrls, imageLoader);
//        imageGridView.setAdapter(imageAdapter);
//    }
}