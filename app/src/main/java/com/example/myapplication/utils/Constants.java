package com.example.myapplication.utils;

public class Constants {
    // API 관련 Constants
    public static final String API_URL = "https://sch.sooplive.co.kr/api.php";
    public static final String METHOD_CATEGORY_LIST = "categoryList";
    public static final String ORDER_BY_VIEW_COUNT = "view_cnt";
    public static final String PLATFORM_PC = "pc";

    // Pagination Constants
    public static final int PAGE_NUMBER = 1;
    public static final int LIST_COUNT = 120;
    public static final int LIST_OFFSET = 0;

    // Fallback Image Constants
    public static final String FALLBACK_IMAGE_BASE_URL = "https://picsum.photos/300/";
    public static final int FALLBACK_IMAGE_COUNT = 3;

    // Image Dimensions
    public static final int IMAGE_WIDTH = 300;
    public static final int IMAGE_HEIGHT = 400;

    // Grid Layout 상수
    public static final int LANDSCAPE_SPAN_COUNT = 4;  // 가로 모드일 때 4열
    public static final int PORTRAIT_SPAN_COUNT = 3;   // 세로 모드일 때 3열
}