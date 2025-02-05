package com.example.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ImageAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView imageGridView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();

    private void loadImagesFromWebpage() {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.sooplive.co.kr/directory/category")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(15000)
                        .get();

                // div.thumb 내부의 img 태그를 선택
                Elements images = doc.select("div.thumb img");
                Log.d(TAG, "Found " + images.size() + " images");

                imageUrls.clear();
                images.forEach(element -> {
                    String imageUrl = element.attr("src");
                    if (!imageUrl.isEmpty() && imageUrl.contains("admin.img.sooplive.co.kr")) {
                        imageUrls.add(imageUrl);
                        Log.d(TAG, "Added image URL: " + imageUrl);
                    }
                });

                runOnUiThread(() -> {
                    imageAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Updated adapter with " + imageUrls.size() + " images");
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
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 4 : 2;

        imageGridView = findViewById(R.id.imageGridView);
        imageGridView.setLayoutManager(new GridLayoutManager(this, spanCount));

        imageAdapter = new ImageAdapter(this, imageUrls);
        imageGridView.setAdapter(imageAdapter);

        // 웹페이지에서 이미지 URL 로드
        loadImagesFromWebpage();
    }
}