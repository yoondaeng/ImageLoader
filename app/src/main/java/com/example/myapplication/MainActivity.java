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
    private static final String API_URL = "https://sch.sooplive.co.kr/api.php";
    private static final String METHOD_CATEGORY_LIST = "categoryList";
    private static final String ORDER_BY_VIEW_COUNT = "view_cnt";
    private static final String PLATFORM_PC = "pc";

    private static final int LANDSCAPE_SPAN_COUNT = 4;
    private static final int PORTRAIT_SPAN_COUNT = 3;
    private static final int PAGE_NUMBER = 1;
    private static final int LIST_COUNT = 120;
    private static final int LIST_OFFSET = 0;

    private static final String FALLBACK_IMAGE_BASE_URL = "https://picsum.photos/300/";
    private static final int FALLBACK_IMAGE_COUNT = 3;

    private RecyclerView imageGridView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();

    private void loadImages() {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(API_URL)
                        .data("m", METHOD_CATEGORY_LIST)
                        .data("szKeyword", "")
                        .data("szOrder", ORDER_BY_VIEW_COUNT)
                        .data("nPageNo", String.valueOf(PAGE_NUMBER))
                        .data("nListCnt", String.valueOf(LIST_COUNT))
                        .data("nOffset", String.valueOf(LIST_OFFSET))
                        .data("szPlatform", PLATFORM_PC)
                        .ignoreContentType(true)
                        .header("Accept", "application/json")
                        .get();

                String responseText = doc.text();
                Log.d(TAG, "API Response: " + responseText);

                // JSON 문자열을 JSONObject로 변환 (역직렬화)
                JSONObject response = new JSONObject(responseText);
                JSONObject data = response.getJSONObject("data");
                JSONArray list = data.getJSONArray("list");

                imageUrls.clear();

                // JSON 배열에서 각 아이템의 이미지 URL 추출
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    String imageUrl = item.getString("cate_img");
                    if (!imageUrl.isEmpty()) {
                        Log.d(TAG, "Adding image URL: " + imageUrl);
                        imageUrls.add(imageUrl);
                    }
                }

                runOnUiThread(() -> {
                    imageAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Updated adapter with " + imageUrls.size() + " images");
                });

            } catch (Exception e) {
                Log.e(TAG, "Error loading images: " + e.getMessage());
                e.printStackTrace();

                runOnUiThread(() -> {
                    imageUrls.clear();
                    for (int i = 0; i < FALLBACK_IMAGE_COUNT; i++) {
                        imageUrls.add(FALLBACK_IMAGE_BASE_URL + (400 + i));
                    }
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
        // 가로 모드 4열 출력, 세로 모드 3열 출력
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE)
                ? LANDSCAPE_SPAN_COUNT
                : PORTRAIT_SPAN_COUNT;

        imageGridView = findViewById(R.id.imageGridView);
        imageGridView.setLayoutManager(new GridLayoutManager(this, spanCount));

        imageAdapter = new ImageAdapter(this, imageUrls);
        imageGridView.setAdapter(imageAdapter);

        // 이미지 로드 시작
        loadImages();
    }
}