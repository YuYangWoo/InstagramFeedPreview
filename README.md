# InstagramFeedPreview
인스타그램 피드 미리보기

### Introduction
내 피드를 이쁘게 관리 - 인스타그램 피드 보기 및 꾸미기 어플리케이션
- 인스타그램 계정 로그인
- 인스타그램 게시물 보기 및 상세보기
- 편집화면에서 게시물 추가, 삭제, 순서이동

#### Login & Board
|Login|Board|
|-----|-----|
|![Screen_Recording_20240325_233325_InstagramFeedPreview-ezgif com-video-to-gif-converter (1)](https://github.com/YuYangWoo/InstagramFeedPreview/assets/59405161/14c1efb6-851b-4f03-9830-0b506a784187)|![board-ezgif com-video-to-gif-converter](https://github.com/YuYangWoo/InstagramFeedPreview/assets/59405161/1ff8ebcf-f949-4f0a-ab50-efa6a5fac2e0)|
#### Edit 
|CREATE|READ|
|------|----|
|![create-ezgif com-video-to-gif-converter (2)](https://github.com/YuYangWoo/InstagramFeedPreview/assets/59405161/19ed5029-e408-42d9-be0e-9a582a7015cf)|![READ-ezgif com-video-to-gif-converter (1)](https://github.com/YuYangWoo/InstagramFeedPreview/assets/59405161/5c054f31-e28e-4e7c-92c0-47ec209c9272)|
|UPDATE|DELETE|
|![update-ezgif com-video-to-gif-converter](https://github.com/YuYangWoo/InstagramFeedPreview/assets/59405161/656bb2b7-8cfa-4cc3-9367-a0b66d43ff8b)|![delete-ezgif com-video-to-gif-converter](https://github.com/YuYangWoo/InstagramFeedPreview/assets/59405161/3ec95d38-c371-457d-b18f-a3b97d2d03f8)|

### Clean Architecture
|패키지|설명|
|----|---|
|<img width="162" alt="image" src="https://github.com/YuYangWoo/InstagramFeedPreview/assets/59405161/46e42eae-9715-46ff-bfda-242ab871ef45">|app -> 안드로이드 어플리케이션 관리 및 모듈 생성<br><br>build-logic -> Android Custom Plugin을 활용해 plugin을 통해 각 모듈의 gradle 관리<br><br>core -> 공통 유틸 및 실제 로컬, 원격 통신이 일어나는 모듈 <br>- datastore : jetpack datastore를 관리하는 모듈<br>- network : retrofit 통신을 관리하는 모듈<br>- room : 로컬 데이터 저장을 위한 모듈<br><br>data -> 앱의 데이터를 관리하는 모듈<br>- dto : 순수한 데이터 모델 dto<br>- repository : repository의 구현체를 가지고 있는 repositoryImpl<br>- datasource : datasourceImpl을 캡슐화하는 인터페이스 dataSource<br><br>domain -> 순수 코틀린으로 이루어져있는 앱의 비즈니스 로직을 담당, domain 모듈은 어떤 모듈도 알지못함<br>- model -> domain data model<br>- repository -> repositoryImpl의 캡슐화를 위한 repository interface <br>- usecase -> 사용자가 하려고 하는 실행의 단위<br><br>feature -> 기능에 따라 나눈 화면 모듈, UI Layer<br>- board -> 게시판 기능을 담당하는 모듈<br>- login -> 로그인 기능을 담당하는 모듈<br>- main -> 메인 기능을 담당하는 모듈|

### Skill
|분류|Stack|
|---|------|
|DI|Hilt|
|Network|Retrofit, OkHttp, Serialization|
|Image|Glide|
|Jetpack|ViewModel, Room, Paging3, Navigation, DataStore. LifeCycle|
|Test|Kotest|
|Asynchronous|Coroutine, Flow|
|Gradle|KTS|
|Version|Version Catalog|












