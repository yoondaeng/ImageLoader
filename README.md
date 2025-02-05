# ImageLoader

## 프로젝트 설명
외부 API에서 썸네일 이미지 목록을 불러와 GridView 형태로 출력하는 안드로이드 앱입니다. 
Glide 라이브러리를 사용하여 이미지 로딩 및 캐싱 성능을 최적화하였으며, 가로/세로 모드에 따라 유연하게 레이아웃을 조정합니다.


## 실행 방법
1. Android Studio에서 프로젝트 클론
2. Gradle Sync
3. 에뮬레이터 또는 안드로이드 기기에서 앱 실행
   

## 기술 스택
- `Java`
- `Android SDK`
- `Jsoup`
  - 웹 크롤링 및 API 응답 파싱에 최적화된 라이브러리
- `Glide`
  - 빠른 이미지 로딩 속도
  - 메모리 및 디스크 캐싱 메커니즘
  - GIF, WebP 등 다양한 이미지 포맷 지원
  - 이미지 변환 및 후처리 기능 제공
  - 코드 간결성 좋음
- `RecyclerView`
  - 뷰 재사용을 통한 메모리 효율성
- `GridLayoutManager`
  - 화면 방향에 따른 동적 열 개수 조정


## 구현 기능 목록
### 🔻 세로 모드 (이미지 3열 출력)

<img src="https://raw.githubusercontent.com/yoondaeng/ImageLoader/7f2df69d0dafe663d2d913cec947f58cad76cc63/Screenshot_20250206_052017.png" width="300" height="auto">

### 🔻 가로 모드 (이미지 4열 출력)

<img src="https://raw.githubusercontent.com/yoondaeng/ImageLoader/7f2df69d0dafe663d2d913cec947f58cad76cc63/Screenshot_20250206_052047.png" width="600" height="auto">

- 외부 API에서 이미지 URL 리스트 로드
- 가로/세로 모드 레이아웃 지원 (가로 4열, 세로 3열)
- Glide를 이용한 이미지 캐싱 및 효율적 로딩
- 에러 발생 시 기본 이미지 대체 기능
- 반응형 그리드 레이아웃 구현


## 이미지 로더 동작 순서도 
```mermaid
graph TD
    A[이미지 URL 요청] --> B{메모리 캐시 확인}
    B -->|캐시 히트| C[메모리 캐시에서 이미지 반환]
    B -->|캐시 미스| D[네트워크에서 이미지 다운로드]
    D --> E{다운로드}
    E -->|성공| F[메모리 캐시에 이미지 저장]
    E -->|실패| G[기본 이미지 로드]
    F --> H[이미지 뷰에 표시]
    G --> H
```


## 이미지 로딩 세부 프로세스

1. 이미지 URL 요청
2. 메모리 캐시 확인
   - 캐시된 이미지 존재 시 즉시 반환
3. 네트워크에서 이미지 다운로드
4. 다운로드 성공 시 메모리 캐시에 저장
5. 이미지 뷰에 이미지 표시
6. 다운로드 실패 시 기본 이미지 대체


## 주요 특징
- 안정적인 이미지 로딩 메커니즘
- 메모리 및 디스크 캐시 최적화
- 네트워크 예외 처리 및 대체 이미지 지원
