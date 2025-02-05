# ImageLoader

## 프로젝트 설명
이 안드로이드 앱은 외부 API에서 썸네일 이미지 목록을 불러와 GridView 형태로 출력하는 애플리케이션입니다. 
Glide 라이브러리를 사용하여 이미지 로딩 및 캐싱 성능을 최적화하였으며, 가로/세로 모드에 따라 유연하게 레이아웃을 조정합니다.

## 실행 방법
1. Android Studio에서 프로젝트 클론
2. Gradle Sync 수행
3. 에뮬레이터 또는 안드로이드 기기에서 앱 실행
4. 인터넷 연결 필요

## 사용한 기술 스택
- Java
- Android SDK
- Jsoup (HTML 파싱)
- Glide (이미지 로딩 및 캐싱)
- RecyclerView
- GridLayoutManager

## 구현 기능 목록
- 외부 API에서 이미지 URL 목록 동적 로드
- ![앱 스크린샷](https://raw.githubusercontent.com/yoondaeng/ImageLoader/7f2df69d0dafe663d2d913cec947f58cad76cc63/Screenshot_20250206_052017.png)
- ![앱 스크린샷](https://raw.githubusercontent.com/yoondaeng/ImageLoader/blob/7f2df69d0dafe663d2d913cec947f58cad76cc63/Screenshot_20250206_052047.png)
- 가로/세로 모드 레이아웃 지원 (가로 4열, 세로 3열)
- Glide를 이용한 이미지 캐싱 및 효율적 로딩
- 에러 발생 시 기본 이미지 대체 기능
- 반응형 그리드 레이아웃 구현

## 주요 특징
- 안정적인 이미지 로딩 메커니즘
- 메모리 및 디스크 캐시 최적화
- 네트워크 예외 처리 및 대체 이미지 지원