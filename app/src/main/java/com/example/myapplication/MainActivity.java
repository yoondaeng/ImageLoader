package com.example.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView imageGridView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();

    private void loadImages() {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://sch.sooplive.co.kr/api.php")
                        .data("m", "categoryList")
                        .data("szKeyword", "")
                        .data("szOrder", "view_cnt")
                        .data("nPageNo", "1")
                        .data("nListCnt", "120")
                        .data("nOffset", "0")
                        .data("szPlatform", "pc")
                        .ignoreContentType(true)
                        .header("Accept", "application/json")
                        .get();

                // API 응답 내용 확인
                String responseText = doc.text();
                Log.d(TAG, "API Response: " + responseText);

                JSONObject response = new JSONObject(responseText);
                Log.d(TAG, "JSON keys: " + response.keys());

                runOnUiThread(() -> {
                    imageAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Updated adapter with " + imageUrls.size() + " images");
                });

            } catch (Exception e) {
                Log.e(TAG, "Error loading images: " + e.getMessage());
                e.printStackTrace();

                // 에러 시 테스트 이미지 로드
                runOnUiThread(() -> {
                    imageUrls.clear();
                    imageUrls.add("https://picsum.photos/300/400");
                    imageUrls.add("https://picsum.photos/300/401");
                    imageUrls.add("https://picsum.photos/300/402");
                    imageAdapter.notifyDataSetChanged();
                });
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

        // 이미지 로드 시작
        loadImages();
    }
}