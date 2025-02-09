package com.example.myapplication.model.repository;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.example.myapplication.model.data.ImageItem;
import com.example.myapplication.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class ImageRepository {
    private static final String TAG = "ImageRepository";

    public interface ImageLoadCallback {
        void onSuccess(List<ImageItem> images);
        void onError(Exception e);
    }

    public void loadImages(int pageNumber, int listCount, int offset, ImageLoadCallback callback) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(Constants.API_URL)
                        .data("m", Constants.METHOD_CATEGORY_LIST)
                        .data("szKeyword", "")
                        .data("szOrder", Constants.ORDER_BY_VIEW_COUNT)
                        .data("nPageNo", String.valueOf(pageNumber))
                        .data("nListCnt", String.valueOf(listCount))
                        .data("nOffset", String.valueOf(offset))
                        .data("szPlatform", Constants.PLATFORM_PC)
                        .ignoreContentType(true)
                        .header("Accept", "application/json")
                        .get();

                String responseText = doc.text();
                Log.d(TAG, "API Response: " + responseText);

                JSONObject response = new JSONObject(responseText);
                JSONObject data = response.getJSONObject("data");
                JSONArray list = data.getJSONArray("list");

                List<ImageItem> imageItems = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    String imageUrl = item.getString("cate_img");
                    if (!imageUrl.isEmpty()) {
                        imageItems.add(new ImageItem(imageUrl));
                        Log.d(TAG, "Adding image URL: " + imageUrl);
                    }
                }

                callback.onSuccess(imageItems);

            } catch (Exception e) {
                Log.e(TAG, "Error loading images: " + e.getMessage());
                e.printStackTrace();

                // Fallback images
                List<ImageItem> fallbackImages = new ArrayList<>();
                for (int i = 0; i < Constants.FALLBACK_IMAGE_COUNT; i++) {
                    fallbackImages.add(new ImageItem(
                            Constants.FALLBACK_IMAGE_BASE_URL + (400 + i)));
                }
                callback.onSuccess(fallbackImages);
            }
        }).start();
    }
}