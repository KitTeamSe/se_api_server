[33mcommit 0e5060c22ffaf40493bfb471bf33558e37e6da1a[m[33m ([m[1;36mHEAD -> [m[1;32mf/#175[m[33m, [m[1;31morigin/f/#175[m[33m)[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sat May 15 20:53:55 2021 +0900

    delete zip file

[33mcommit 746e2742e1bb9fce1451c5cdbaf5146cf92f5606[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sat May 15 20:42:29 2021 +0900

    Update MultipartFileServices url injection method

[33mcommit 321dc0eb7a9eb323575755c9a239d89b17f2b5ec[m[33m ([m[1;32mmain[m[33m)[m
Merge: b5d2299 77afb15
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sat May 15 17:23:37 2021 +0900

    Merge branch 'dev_api' of https://github.com/KitTeamSe/se_api_server into main

[33mcommit 77afb1570f57d35723cbfbc46bd5cbb758095d29[m[33m ([m[1;31morigin/dev_api[m[33m)[m
Merge: 43fdf1a 0cdb6f2
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 17:43:02 2021 +0900

    Merge pull request #171 from KitTeamSe/f/#170
    
    게시글(Post) DTO 패키지 이동

[33mcommit 0cdb6f2b8ed6b9b9cb8b85dc5a6a2c5b075f45fa[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 17:41:21 2021 +0900

    게시글(Post) DTO 패키지 이동
    infra 패키지에서 application 패키지로 이동.

[33mcommit 43fdf1ac53e9c7c7278cba568498f23de36f8818[m
Merge: f20b606 973b494
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 16:24:11 2021 +0900

    Merge pull request #169 from KitTeamSe/f/#123
    
    F/#123 CORS 허용 목록에 localhost 추가

[33mcommit 973b49424da01a762120eb9131e13eca4cd073cd[m
Merge: a475dfc f20b606
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 16:18:07 2021 +0900

    Merge remote-tracking branch 'origin/f/#123' into f/#123

[33mcommit a475dfc3f43cf2601e248a4e9c1cb6320288559a[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 16:17:36 2021 +0900

    CORS 허용 목록에 localhost 추가

[33mcommit f20b606e60f82bec6a09993500ae0e12eb05374c[m
Merge: 820188b 62e26e3
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 15:24:00 2021 +0900

    Merge pull request #168 from KitTeamSe/f/#123
    
    F/#123 사용자 및 관리자 도메인 URL을 외부에서 주입받도록 설정

[33mcommit 62e26e393b5e6d14bbcfa234c15a96a03066b162[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 15:14:34 2021 +0900

    CORS 필터에 사용자 및 관리자 Web 서버 주소를 외부에서 주입하도록 설정하였음.

[33mcommit 655d62d0ad3c19a3d90c08fdbe1877f18f96e7c2[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri May 14 15:13:28 2021 +0900

    사용자 및 관리자 Web 서버 주소를 외부에서 주입하도록 설정

[33mcommit 820188b5b75a7da64ad8912e5041d50191c3eeb2[m
Merge: fc57af4 a4dd43c
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 18:17:04 2021 +0900

    Merge pull request #167 from KitTeamSe/f/#164
    
    F/#164 첨부파일 API와 MultipartFile API 연동

[33mcommit a4dd43cfa4d0a9b26c4632bcf89188d8c98aa7b2[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 18:16:00 2021 +0900

    첨부파일 API와 MultipartFile Service 연동
    1.첨부파일 생성 및 삭제에서 적절한 MultipartFile Service 호출하도록 하였음.
    2. 첨부파일 생성 DTO 삭제. RequestParam으로 Mutlipartfile 받을 때는 RequestParam으로 게시글 ID와 댓글 ID를 받아야함.
    3. MultipartFile API Create, Delete Controller 삭제.
    Download API를 제외하면 서비스 레이어에서만 사용되므로 API 컨트롤러 삭제함.

[33mcommit 39505c10bd877c09c0e47c705300775d9ef7118d[m
Merge: da5b794 bfe6485
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 17:16:42 2021 +0900

    Merge pull request #166 from KitTeamSe/f/#163
    
    F/#163

[33mcommit bfe6485ab554f09abe664b9410c096f85bfced3c[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 17:10:54 2021 +0900

    Filter Chain Exception Handler 추가
    1. 필터에서 예외가 발생할 때 핸들링하는 Handler 클래스 추가
    2. JWT 세션 만료 에러 추가
    2. 페이징 기본 페이지 수를 10에서 50으로 변경

[33mcommit ed95b89aa1715bb578aa6bce5df95d84b0bc0619[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 15:47:31 2021 +0900

    게시글 API 픽스
    1. 게시글 API opeartion 중 일부를 명확한 명칭으로 변경.
    2. 비밀글은 권한 획득 시에만 첨부파일 반환하도록 수정.
    3. 익명 게시글 조회 시 닉네임만 반환하도록 수정.
    4. 익명 사용자가 게시글 조회 시 정상적으로 조회될 수 있도로 수정.
    5. 게시글 목록 조회 시 비밀글인 경우 본문 미리보기를 반환하지 않도록 수정.

[33mcommit 06d0beba9a83d8f4f9021f709b71f4ff6e456963[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 15:42:39 2021 +0900

    AOP JsonProcessingException 핸들 메소드 추가
    JSON 양식이 올바르지 않게 주어진 경우
    400 BadRequest 말고, 올바르지 않은 JSON 입력 ErrorCode 반환하도록 수정.

[33mcommit afbc9d5d054d94fdba06fe9f15ae41bf9c1fa9e2[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 15:26:09 2021 +0900

    Account API 픽스
    계정 생성 시 accountType 설정되도록 수정.
    회원 정보 조회 응답 메시지 오타 수정.
    Account 테이블의 휴대폰번호 unique 속성 제거.

[33mcommit da5b7947e884cd6040caac214c4fd9186f32dfeb[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed May 12 14:19:32 2021 +0900

    첨부파일 생성 시 조건문 & 오타 수정

[33mcommit fc57af4c92f2154fc6d9e14ddf343bc40589915c[m
Merge: 5258c10 a7e4dce
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 10 23:00:59 2021 +0900

    Merge pull request #162 from KitTeamSe/f/#161
    
    F/#161 Dockerize

[33mcommit a7e4dce7b8b618006dd3243f85bff74811df39b3[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 10 22:55:16 2021 +0900

    ARG JAR_FILE 수정

[33mcommit 1be9520f8bb6607ce2cf9dcf5b5ec6fbaa55bb27[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 10 22:39:14 2021 +0900

    도커 파일 추가

[33mcommit 5258c10dcda5fd4f9253a15c9fda9781367d2b99[m
Merge: 17affcd 806bfff
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 10 18:31:01 2021 +0900

    Merge pull request #160 from KitTeamSe/f/#153
    
    F/#153 파일 삭제 API 구현 및 파일 서버 Error 핸들링 로직 추가

[33mcommit 806bffff0ac72462accc70f510950ec3e6e28086[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 10 18:25:49 2021 +0900

    파일 서버에서 예외 발생 시 핸들링하는 메소드의 이름 변경.

[33mcommit fa9347cb1d3a1c5015ba9ec365049e835b359cb3[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 10 18:24:23 2021 +0900

    파일 삭제 API 구현
    1. MultipartFileDeleteAPI 작성.
    2. FileServerErrorCodeProxy 작성.
    파일서버에서 발생한 에러가 Json으로 반환되면, 해당 클래스로 변환되어 사용됨. 결과적으로 BusinessException으로 aop에서 핸들링됨.
    3. MultipartFileService abstract class 작성.
    MultipartFile~ 서비스들이 공통적으로 수행하는 기능을 가진 추상 클래스.

[33mcommit 17affcdaefcbb991973bf569eacf230a2b4bf642[m
Merge: 1448cf9 bbb33b5
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 23:11:41 2021 +0900

    Merge pull request #159 from KitTeamSe/f/#155
    
    F/#155 게시글 목록 조회시 닉네임 및 본문 미리보기 표시

[33mcommit bbb33b56e3d54a2d06b0c11f35849949dda7af21[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 23:08:31 2021 +0900

    익명 게시글 작성 시 익명 비밀번호가 null로 들어가는 오류 픽스

[33mcommit 5869ccd358c96e86c37942754ce3086265b65e5f[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 22:47:42 2021 +0900

    init_lower 오타 수정

[33mcommit c0165e3abf968b5c5b45ef22fd9b54db617e0450[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 22:03:52 2021 +0900

    Post Create Dto와 Update Dto의 Api Model 이름이 같았던 것 수정

[33mcommit 8af05d1248974ad1bad86c58dab058b4cb477289[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 21:29:55 2021 +0900

    게시글 리스트 조회 시 게시자 닉네임과 본문 미리보기를 함께 반환하도록 수정.

[33mcommit 85334394ba1b1e0fa494955f04a72870dbb42228[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 21:14:44 2021 +0900

    BoardApiController 오타 수정

[33mcommit 1448cf951b8547cc1de1df1ee26acdeea7890d82[m
Merge: a5abdde 2d00ed8
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 21:01:41 2021 +0900

    Merge pull request #158 from KitTeamSe/f/#156
    
    F/#156 누락된 Transactional 어노테이션 추가

[33mcommit a5abdde4a1485fac93243ce3a0eeec51dde94c67[m
Merge: 6ccce88 e075b9d
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 21:01:26 2021 +0900

    Merge pull request #157 from KitTeamSe/f/#152
    
    F/#152 API 서버와 파일 서버간 통신 구현

[33mcommit 2d00ed8d91a715b6f3dd070197d1e3877b7b5bb1[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 18:48:37 2021 +0900

    등록, 수정, 삭제 서비스임에도 불구하고 @Transactional이 붙어있지 않은 메소드에 어노테이션 추가.

[33mcommit b82dbb63cd099467c3d2ec7d4c181ce988961f7f[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 18:43:46 2021 +0900

    익명이 아닐 때 익명 비밀번호를 넣으면, 익명 비밀번호 매칭 로직을 돌리지 않게 수정.

[33mcommit 1ad1a0a6dd4a0e9108e6a9e8665b589fc008dffc[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 18:43:03 2021 +0900

    Post Jpa Repository 에서 게시판으로 게시글 조회 시 게시글이 아닌 게시판이 조회되는 오류 픽스

[33mcommit e075b9d15041ba4f23af3d169755006d1bba027d[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 18:06:44 2021 +0900

    MultipartFile Upload Service 코드 정리

[33mcommit 10e0ed9e619afa99fb68fff3fec007eb564ede8b[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 15:26:30 2021 +0900

    파일 다운 시 파일 서버에서 Response 받으면 그대로 클라이언트로 전달하게끔 수정.
    멀티파트 서비스에서 사용하지 않는 에러코드 삭제.

[33mcommit a1f7623bfbc50875be5b6a193794ab4ffb27a713[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue May 4 14:50:07 2021 +0900

    현재 호스트 도메인을 동적으로 받아오게 하였음.

[33mcommit aabb35d40877808995aa0311b993366d7accc111[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 3 20:17:06 2021 +0900

    멀티파트 파일 다운로드 코드 추가

[33mcommit 2214ece6886e7867b75c11fa892ca5a005f7fd31[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 3 18:01:00 2021 +0900

    파일 업로드 도중 내부 파일 서버 예외 처리 추가. 용량 예외 처리 추가.

[33mcommit fd87c45e7aeb664fdc026e7834f633ed3298a770[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon May 3 16:49:56 2021 +0900

    멀티파트 파일 업로드 코드 추가.

[33mcommit 6ccce887c818e93c9e3ab36a0d82dcc831b222c9[m
Merge: 4567562 4550277
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun May 2 15:12:59 2021 +0900

    Merge pull request #151 from KitTeamSe/f/#150
    
    메뉴가 삭제되지 않는 오류 픽스

[33mcommit 4550277d8d5554ae3e6ff30b716f5ad4fbd4fdad[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun May 2 15:10:46 2021 +0900

    메뉴가 삭제되지 않는 오류 픽스

[33mcommit 4567562d0d9ba91b5bef010f912d759d17364241[m
Merge: a18ba04 22cf283
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sat May 1 18:08:14 2021 +0900

    Merge pull request #149 from KitTeamSe/f/#147
    
    교시(Period) 수정 시 Response에 pk만 반환하도록 수정.

[33mcommit 22cf2837b7b5980dac70488387dfb1b5f20bbb7a[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sat May 1 18:03:47 2021 +0900

    교시 수정 시 pk만 반환하도록 수정.

[33mcommit a18ba049951f7c4c67f4303bbce6592b0b922eab[m
Merge: 9cef5da 03b88a1
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sat May 1 17:49:39 2021 +0900

    Merge pull request #148 from KitTeamSe/f/#122
    
    F/#122

[33mcommit 03b88a10459a28bee9cd30666b761675963c6e70[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sat May 1 17:48:32 2021 +0900

    Update Account services

[33mcommit 7a47972c39e8f35eb5252107f4b43142373f39ef[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Apr 30 22:16:01 2021 +0900

    RefactoR aCCOUNtUpdateService

[33mcommit 9cef5da6d5a26a8356cf1d81ca386b4ee3dbaf7d[m
Merge: ec9007f 5ae3597
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Fri Apr 30 21:24:39 2021 +0900

    Merge pull request #146 from KitTeamSe/f/#139
    
    Fix api's http method

[33mcommit 5ae35974b4c7d418ed59ef44d646427fa128fcad[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Apr 30 21:22:45 2021 +0900

    Fix api's http method
    
    reference&resolved : #139

[33mcommit ec9007fff57755bde7918b1b7ff2dcb4950492a2[m
Merge: c95100e 1eade6d
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Fri Apr 30 21:18:36 2021 +0900

    Merge pull request #145 from KitTeamSe/f/#141
    
    Fix menu read api spec

[33mcommit 1eade6d57df753d4ec3fc6da20e325c1522ffdd9[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Apr 30 21:16:35 2021 +0900

    Fix menu read api spec
    
    resolved : #141

[33mcommit c95100edd40e489bf04d40191d76c89dc877ea49[m
Merge: e4b84ed eeb628d
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Fri Apr 30 20:56:02 2021 +0900

    Merge pull request #143 from KitTeamSe/f/#140
    
    Fix menu read all api's error

[33mcommit eeb628d1382dd1fc1318c5ca2dd32bee9a51a5ba[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Apr 30 20:54:37 2021 +0900

    Fix menu read all api's error
    
    resolve : #140

[33mcommit e4b84ed5709197a4ae01398ed58037f180a499db[m
Merge: c99cd97 b065631
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sat Apr 17 21:13:39 2021 +0900

    Merge pull request #138 from KitTeamSe/f/#114
    
    F/#114 배치 API 기능 추가 완료.

[33mcommit b0656316bdc21963d6b93c1265a0e86e45f630d0[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 17:28:57 2021 +0900

    분반 배치 확인 테스트 메소드 작성. 특정 시점 배치 전체 조회 테스트 메소드 작성.

[33mcommit 635b7bf83d9998facb8dfedb2c867274d97fb9e4[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 17:05:06 2021 +0900

    배치 API 기능 추가.
    - 시간표, 요일, 교시를 특정하면 포함된 모든 배치를 반환하는 기능

[33mcommit 2976bf894a7ac26ada8baef4ffab00919278a1ae[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 16:32:35 2021 +0900

    분반 배치 확인(Division Check Service) 서비스 작성.
    해당 분반이 모두 배치완료 되었는지 검사하는 기능을 수행.

[33mcommit c99cd971c86da91a7d257dc12fd59520fb978869[m
Merge: f157644 b0ae8b9
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Fri Apr 16 01:14:01 2021 +0900

    Merge pull request #137 from KitTeamSe/f/#123-re5
    
    Update CorsHeaderFilter to Allow test origin

[33mcommit b0ae8b9d5dfe3b3cf828f65748ea4504703b05e7[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Apr 16 01:11:03 2021 +0900

    Update CorsHeaderFilter to Allow test origin

[33mcommit 711fbc6feaa9027281121c7c2b5b06cc54291667[m
Merge: 72358fd ae9e9f0
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 01:06:36 2021 +0900

    Merge pull request #136 from KitTeamSe/f/#133
    
    F/#133 시간표 엔티티 Cascading 설정 및 생성자 Builder 제거

[33mcommit ae9e9f09fd349397212d344c7eae59be941e5a75[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 01:04:26 2021 +0900

    교시 생성자 Builder 삭제

[33mcommit 568c1d67db819565a3d76b411e0227b655a0dedd[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 00:47:22 2021 +0900

    배치 생성자 빌더 삭제

[33mcommit 41c659acf6dd46969ef8cf892e86a7bac7da16c8[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 00:33:46 2021 +0900

    시간표 관련 엔티티 양방향 매핑 제거
    개설 교과 - 분반 양방향 매핑은 삭제하지 않음.

[33mcommit ce2577114eeff7405efec0a8cd0ec8d99101643a[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Apr 16 00:12:39 2021 +0900

    강의 불가 시간 Cascade 설정 & 생성자 빌더 제거

[33mcommit 5962504e6cff01ac442cfbaa60a65e55c3dc3fe2[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 18:04:27 2021 +0900

    강의실 Cascading 설정 완료 & 생성자 빌더 삭제

[33mcommit 986586ebba5e94a6fb559032648771cc8040e55f[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 17:24:26 2021 +0900

    개설 교과 수정 시 분반 자동생성 안되는 버그 픽스

[33mcommit f4715c4c31c998f1dab266402981f2a815228bed[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 17:07:54 2021 +0900

    시간표 Cascading 설정 완료 & 생성자 빌더 삭제

[33mcommit 0982404f7c91c4c930a8458ec329fefd9482b8a2[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 16:37:03 2021 +0900

    교원, 참여교원 연관관계 Cascading 설정 완료 & 생성자 빌더 삭제

[33mcommit 72358fddd53182539e2cb388955ba6045b373b5c[m
Merge: ff50136 c5d1b4c
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 15:36:30 2021 +0900

    Merge pull request #135 from KitTeamSe/f/#124
    
    시간표 관련 엔티티 ErrorCode status 모두 400으로 변경

[33mcommit c5d1b4c112786885d1ff14ebb18fcd901749407d[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 15:32:26 2021 +0900

    시간표 관련 엔티티 ErrorCode status 모두 400으로 변경

[33mcommit ff501363a092864cbf970961d60baecd6c6eed30[m
Merge: 46d4648 f7c8b99
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 15:24:43 2021 +0900

    Merge pull request #134 from KitTeamSe/f/#119
    
    F/#119

[33mcommit f7c8b9973ee2802522d86735ac66c75baeb8547a[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 15:03:18 2021 +0900

    분반 엔티티 생성 완료
    교과-개설교과-분반 연관관계 N:M으로 설정
    Cascade 설정
    교과, 개설교과, 분반 생성자 Builder 패턴 제거
    테스트코드 수정

[33mcommit da1ae75ee2d9154b20cd69f5f39394f2e94d55df[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 15 13:20:17 2021 +0900

    교과 컨트롤러 오타 수정
    강의실 삭제 -> 교과 삭제

[33mcommit f1576440f2b95a032fb3ce4acc264768c06ec839[m
Merge: bf5bea7 3636e74
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 14 23:51:28 2021 +0900

    Merge pull request #132 from KitTeamSe/f/#123-re5
    
    Update CorsConfig

[33mcommit 3636e7481f26188756fee55403d4ea5f7a5ecb39[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Apr 14 23:49:48 2021 +0900

    Update CorsConfig

[33mcommit bf5bea74db02854a44a04a956087ff5b092fe67d[m
Merge: ead261d fbe66fe
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 14 23:32:16 2021 +0900

    Merge pull request #130 from KitTeamSe/f/#123-re4
    
    Add CorHeaderFilter

[33mcommit fbe66fe8b95d79f259591d2485b651acbf76420a[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Apr 14 23:31:10 2021 +0900

    Add CorHeaderFilter

[33mcommit ead261d59e41cb7d26d59b1d1f954aedce03cc0d[m
Merge: 39edc55 fea838a
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 14 23:16:53 2021 +0900

    Merge pull request #129 from KitTeamSe/f/#123-re3
    
    Update allowed origin

[33mcommit fea838af4bf0f5c181bd0b50927e74fe1bace090[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Apr 14 23:13:27 2021 +0900

    Update allowed origin

[33mcommit 14cceffc589a60f30af480db5924f31c7e5b2da8[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 14 23:04:06 2021 +0900

    Division에 연관관계 편의 메서드 추가

[33mcommit 39edc5536236a296c20ecbdf6de803025593a257[m
Merge: 0495252 1be59a4
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 14 23:02:52 2021 +0900

    Merge pull request #127 from KitTeamSe/f/#123-re2
    
    Add add allowed cors origin

[33mcommit 1be59a461edd85a1dfe69e6d38f5a94c2e4a9e88[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Apr 14 22:59:38 2021 +0900

    Add add allowed cors origin

[33mcommit f7afd0c974a26bb820310d1a92f3e91d6200bc1e[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 14 22:52:17 2021 +0900

    개설 교과 생성 시 분반 자동으로 추가 삭제되게 하였음.

[33mcommit 04952525a4c543232492f421fa4a5f5c8c7b9941[m
Merge: 6542483 5b998ee
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 14 21:44:40 2021 +0900

    Merge pull request #126 from KitTeamSe/f/#123-re
    
    Add cors exclude methods

[33mcommit 5b998ee50e451ba19638c6aa3d0b8ddbc161cc8e[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Apr 14 21:42:09 2021 +0900

    Add cors exclude methods

[33mcommit 65424834d39b28d57f5d1ed8262d1ae655d55b2f[m
Merge: fd145ab 75cf764
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 14 21:23:13 2021 +0900

    Merge pull request #125 from KitTeamSe/f/#123
    
    Add CORS Config

[33mcommit 75cf764f53a026dadf2254af63756f52ede6e92c[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Apr 14 21:22:07 2021 +0900

    Add CORS Config

[33mcommit b5d22999022452f7db7dc2be271beb1edb07ca26[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 14 21:16:01 2021 +0900

    Update issue templates

[33mcommit fd145abecab638353e161cfc934739fc77c92ecc[m
Merge: ee5bc14 00ba17f
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 14 20:41:22 2021 +0900

    Merge pull request #121 from KitTeamSe/f/#120
    
    Fix Blacklist service error

[33mcommit 00ba17fd8496e72c61e6354980dcb0e77f9d93d9[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Apr 14 20:39:49 2021 +0900

    Fix Blacklist service error

[33mcommit aeebf812bf6eb732ca6ff5f04f8029bb69bec2ce[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 14 18:32:37 2021 +0900

    분반 엔티티 및 API 작성

[33mcommit 46d4648b19540607edda44a5315940896ce22490[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 13 13:40:45 2021 +0900

    배치에서 AlertMessage 반환할 때 ErrorCode Json으로 에러코드 및 메시지 포함하여 반환하게끔 수정.

[33mcommit ee5bc14c9b88a4dc9a82505f8ed8a389bf1f8c67[m
Merge: 3333154 41741e5
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Apr 12 21:01:26 2021 +0900

    Merge pull request #117 from KitTeamSe/f/#113
    
    F/#113

[33mcommit 3333154eefde3105b9bbbd91a43beceda18675bc[m
Merge: 480136a 0644734
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Apr 12 21:01:12 2021 +0900

    Merge pull request #112 from KitTeamSe/f/#111
    
    F/#111

[33mcommit 41741e5747ca692d537956baaf36952afb8d1b20[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 12 15:48:44 2021 +0900

    DeplyomentAlertMessage 추가

[33mcommit 064473408f7aa5df2f749fd8e6d8929698f19ea7[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 12 14:34:34 2021 +0900

    개설 교과(OpenSubject)의 컬럼 수정
    분반 수는 NULLABLE로 변경.
    주간 수업 시간은 NOT NULL로 변경.

[33mcommit 41b26b45381d8aef2a90e6ffaafa9a19791f118c[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sat Apr 10 18:06:33 2021 +0900

    테스트코드에 autoCreated 추가 반영

[33mcommit 912c40127282fa1c043641df53ed80fc00a04868[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sat Apr 10 17:41:20 2021 +0900

    개설 교과 엔티티에 autoCreated, note 컬럼 추가에 따른 Dto, Service, Test 코드 수정.

[33mcommit 8ad99433de35181431ce32517355ae6ebf838774[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sat Apr 10 17:23:54 2021 +0900

    교과 엔티티에 autoCreated, note 컬럼 추가에 따른 Dto, Service, Test 코드 수정.
    UpdateService는 pk를 반환하게끔 수정.

[33mcommit 60f8b71b45f0b10d8e1ad1d97500eef819b36bc5[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sat Apr 10 17:06:26 2021 +0900

    교원 엔티티에 autoCreated, note 컬럼 추가에 따른 Dto, Service, Test 코드 수정.
    UpdateService는 pk를 반환하게끔 수정.

[33mcommit 6c92719faaa0a22cf76241094810439977ec2507[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sat Apr 10 16:34:28 2021 +0900

    강의실 note 컬럼 추가에 따른 Dto, Service, Test 코드 수정.
    UpdateService는 pk를 반환하게끔 수정.

[33mcommit dfb9e80b076cf7c64538a389fa62d5f27c7dc7d2[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sat Apr 10 16:20:54 2021 +0900

    강의실, 교원, 교과, 개설 교과 엔티티 비고 컬럼 추가.
    교원, 교과, 개설 교과 엔티티에 자동생성여부 컬럼 추가

[33mcommit 480136a04c66de673519b8b58197263a131755a9[m
Merge: f80ae51 e18fa42
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Thu Apr 8 14:49:23 2021 +0900

    Merge pull request #110 from KitTeamSe/f/#60
    
    F/#60

[33mcommit e18fa426d335010a9491f95de2b7f4940009ff91[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 8 14:19:54 2021 +0900

    배치(Deployment) 삭제 Service 작성
    수정 Service 제외

[33mcommit 4be2b2695ed9323bb9b041e1a75b318116de5d58[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 8 13:58:46 2021 +0900

    배치(Deployment) 조회 Service 작성

[33mcommit 32c9ed6ca04e9b8bfa6052f634ffa38b25263b6a[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 8 13:15:19 2021 +0900

    겹치는 교시 검사 로직 수정

[33mcommit 12bd5860d49cd24d99c54e19d4c54a295a5a7609[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 7 21:17:25 2021 +0900

    시간표 관련 Dto API프로퍼티 수정
    notes id->번호

[33mcommit b7db5d4b0a2aca069191f7943eebb133856804d0[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 7 21:08:00 2021 +0900

    배치(Deployment) Create Service 작성

[33mcommit f80ae51addc2ea16e3045f8f79a92f549a4b4667[m
Merge: 889b294 929d211
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Apr 7 18:10:15 2021 +0900

    Merge pull request #109 from KitTeamSe/f/#108
    
    Entity 내 검증 로직 추가.

[33mcommit 8c5c521a7b9edf74b04ce27335a4b35aece59a03[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 7 17:34:14 2021 +0900

    배치(Deployment) API 작성 시작
    배치 Create Service 작성.
    검증 로직 추가 필요

[33mcommit 51fe073b4e0cb76a20f5b54bed2b7b54d01818d6[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 7 16:52:30 2021 +0900

    Embeddable PeriodRange 추가
    강의 불가 시간(LectureUnableTime)에 startPeriod, endPeriod를 periodRange로 수정
    시작교시-종료교시 관련 로직은 PeriodRange에서 관리

[33mcommit b06b53994113ec3161de43f239db505b250d56c1[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 7 15:29:57 2021 +0900

    강의 불가 시간 검증 로직 추가

[33mcommit ea52806bfd4b6ec83ec16fd72d9fba7d6e7493ca[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 7 14:43:48 2021 +0900

    배치 이름 변경 Placement -> Deployment

[33mcommit 929d211a5d7abfeb506c0e3e834d6efc711dc5d3[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Apr 7 14:27:22 2021 +0900

    Entity 내 검증 로직 추가.
    TimeTable : 년도, 학기 검증
    Period : 시작-종료 시간 교차, 교시 순서 검증
    LectureRoom : 정원 검증
    Subject : 대상학년, 개설학기, 학점 검증
    OpenSubject : 분반 수, 주간 수업 시간 검증
    
    + init_sql로 교시 정보가 자동 생성됨에 따라
     Period Test 코드 수정.

[33mcommit 889b294c518247db6168c49ce0157f7f8654b22b[m
Merge: 00a4d7c a8fe2dd
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Apr 6 23:05:36 2021 +0900

    Merge pull request #107 from KitTeamSe/f/#63
    
    F/#63

[33mcommit a8fe2ddf0fcf1b49c9c77c0720da9961ebb58740[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 23:03:13 2021 +0900

    개설 교과 Delete Service 작성

[33mcommit 867cc194b379a503f0f44d7a7b16fdf2ec650b3d[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 22:54:42 2021 +0900

    개설 교과 Update Service 작성
    개설 교과 Create Service, Update Service 에서 데이터 검증 로직 추가
    (ex. 분반수가 0이하인지, 주간 수업 시간이 0이하인지)
    다른 시간표 엔티티도 데이터 추가 검증 로직이 필요함.

[33mcommit 5aa5fb30e383c3b425e1ac6939feaf7fa1c67392[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 22:31:39 2021 +0900

    강의 불가 DTO 시간 오타 수정

[33mcommit 0c6c9b8776d7afedf205926650f1ad7f95a76d76[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 22:27:03 2021 +0900

    참여 교원 테스트 리팩토링
    테스트 코드에 createTeacher, createTimeTable 추가.

[33mcommit 277166396f56f8cda39b8e33a57815bb3d04b6a3[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 22:17:21 2021 +0900

    개설 교과 Read Service 작성

[33mcommit 9d3566180161f3ba63beb4ace71449101ac4bec1[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 22:01:04 2021 +0900

    개설 교과 API 작성 시작
    개설 교과 Create Service 작성
    Semester Enum 삭제.
    교과 이름 길이 Validation 1~50으로 변경.

[33mcommit 00a4d7c4cefa80d358d15de62469efe8ace81e1d[m
Merge: 13247c4 27490c9
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Apr 6 21:19:50 2021 +0900

    Merge pull request #106 from KitTeamSe/f/#85
    
    F/#85

[33mcommit 27490c974db5919e92be820d97b0d93ac1d219ce[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 21:17:57 2021 +0900

    사용 가능 강의실 Delete Service 작성.

[33mcommit 75a27a4b4211e6cf24ca27b308d92d55c6299eb2[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 21:05:14 2021 +0900

    사용 가능 강의실 Read Service 작성.

[33mcommit 74719f413ee468d7d15c008d7b4c62682c788566[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 20:26:58 2021 +0900

    사용 가능 강의실 Create Service 작성.
    + 시간표 Validation 이름 길이  1~50으로 수정

[33mcommit 131cbbdd4766402b1b9bb1d63fb0e53e0673c27e[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 19:42:45 2021 +0900

    사용 가능 강의실 API 작성 시작
    lectureroom 패키지로부터 분리.

[33mcommit 13247c403490f12f0842f91ad13cc93604512de1[m
Merge: c475d44 c454869
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Apr 6 15:36:02 2021 +0900

    Merge pull request #105 from KitTeamSe/f/#87
    
    F/#87

[33mcommit c4548695a4cc985236655125c19b47f2bbf10cef[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 15:34:15 2021 +0900

    강의 불가 시간 Update Service 작성

[33mcommit 874a4c9038851254fd3e1a077e4c003aeb3f0a7b[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 15:11:20 2021 +0900

    강의 불가 시간 Delete Service 작성

[33mcommit 700e65d5d26be99564f4c01e53da2f2e72a76384[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 15:02:52 2021 +0900

    강의 불가 시간 Read Service 작성

[33mcommit 673067ccdaa897a4a2c5cdb75f49e8a89ab0ac77[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 14:28:33 2021 +0900

    강의 불가 시간 Create Service 작성
    Period Create DTO, Period Read DTO에서비고(note)의 min validation 삭제.

[33mcommit 7fff9061f60aca6354765cf63ad4c5d446ab89da[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Apr 6 13:27:52 2021 +0900

    Init SQL에 교시 추가
    교시, 강의불가시간의 note Column의 size min 조건 삭제
    교시 Update 시 비고(note)가 비어있으면 null값으로 DB에 삽입.

[33mcommit c475d449bf203cc098db3390fdc068b6be23f0d0[m
Merge: 14e7c4c 3226be0
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Apr 5 23:43:02 2021 +0900

    Merge pull request #104 from KitTeamSe/f/#103
    
    Refactor packaging from clean architecture to DDD

[33mcommit 3226be058aef4d7266454b89962cf9e03de164e6[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Apr 5 23:41:45 2021 +0900

    Refactor packaging from clean architecture to DDD

[33mcommit 14e7c4cc5d2225f4e958dcbf9776058a20570329[m
Merge: 1f1b8c2 65c56bf
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Apr 5 21:57:10 2021 +0900

    Merge pull request #102 from KitTeamSe/f/#99
    
    F/#99

[33mcommit 65c56bf29f1dab1294ca5e9bc949603cc7b3873d[m
Merge: ccfc143 1f1b8c2
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Apr 5 21:52:18 2021 +0900

    Merge branch 'dev_api' of https://github.com/KitTeamSe/se_api_server into f/#99

[33mcommit ccfc14341d074883cbb20c75342e0afec26c0fe6[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Apr 5 21:51:34 2021 +0900

    Update main logic from usecase to entitiy

[33mcommit 1f1b8c27aa379e94d3ccda3e116b64d0592168b4[m
Merge: e19af58 77ee80f
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Apr 5 18:21:43 2021 +0900

    Merge pull request #101 from KitTeamSe/f/#86
    
    F/#86

[33mcommit 77ee80f43a1dc0796c7f7e8bb894b01c3e4817b7[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 5 17:34:55 2021 +0900

    참여 교원 Read Use Case Test 수정

[33mcommit 690663f92637abc30c5a2e8247a75129de86eff3[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 5 17:33:39 2021 +0900

    참여 교원 Read All에 timeTableId 인자 추가.
    시간표를 특정하면, 참여한 모든 교원(페이지)을 반환하게 변경함.

[33mcommit 35f7d1e155cee50b56b64931990236b9fed53a2d[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 5 17:02:45 2021 +0900

    참여 교원 Delete UseCase 작성

[33mcommit 80618cf42e8d5b67f23275dfd1539feb24569ad9[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 5 16:54:00 2021 +0900

    참여 교원 Read UseCase 작성

[33mcommit f4d3799b7ad72cb73a4459d5a50f87254bf20466[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 5 16:26:57 2021 +0900

    참여 교원 API 작성 시작
    참여 교원 Create UseCase 작성
    CreatedBy -> CreatedAccountBy 변경에 따른 참조 코드 수정

[33mcommit e19af5876e6ec0565c56601f3b8e05183148748c[m
Merge: d53a312 d32cf51
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Apr 5 15:09:16 2021 +0900

    Merge pull request #100 from KitTeamSe/f/#59
    
    F/#59

[33mcommit d32cf51b0f7f20f005aeb910f12bae2086494de9[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 5 13:50:01 2021 +0900

    오타 수정

[33mcommit 54637a3cf35d11545ed3c64570d8efa38a6719a6[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Apr 5 13:46:38 2021 +0900

    TimeTable Update UseCase 작성

[33mcommit 279cc5589b27ba74b6edb1cb870ea0c84564544a[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun Apr 4 20:20:38 2021 +0900

    TimeTable Update Dto 작성

[33mcommit a8685d9ccb32aa9d2c3be8fbc55a2151d3cf7644[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun Apr 4 19:16:34 2021 +0900

    TimeTable Delete Usecase 작성

[33mcommit ed3056abb77dd6c49e6d3b72baced3fbfc97a52e[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun Apr 4 18:24:07 2021 +0900

    TimeTable Read Usecase 작성

[33mcommit b9c5b51e17758ed63e9e0bb053aaec4673e94739[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun Apr 4 00:07:42 2021 +0900

    Time Table Create UseCase 작성

[33mcommit d53a3125ec19503a00243b1e0825ea6d8c7305a8[m
Merge: 1a950a7 882ca2e
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sat Apr 3 01:59:03 2021 +0900

    Merge pull request #91 from KitTeamSe/f/#49
    
    F/#49

[33mcommit 882ca2e463e22aaf5f72a81b931c809e452d5a4a[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sat Apr 3 01:57:52 2021 +0900

    Add post api and test coverage library

[33mcommit 1a950a7d9523505c41a8272fbd192ac96be91e29[m
Merge: 5b8cb3f 0124fd4
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sat Apr 3 01:57:18 2021 +0900

    Merge pull request #90 from KitTeamSe/f/#89
    
    F/#89

[33mcommit e0c278793a3453f455f5378896abe2ed2644f34c[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 1 22:10:40 2021 +0900

    시간표 API 작성 시작
    시간표 Create UseCase 작성하였으나, 아직 동작하지 않음.
    시간표 생성 시 생성자의 계정(Account) 정보를 어디에서 받는지 명확하게 정의해야함.

[33mcommit 0124fd42fff748743efff4ce6cdfedb9209d25d0[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Apr 1 21:03:11 2021 +0900

    교시 API 작성
    교시 삭제 Usecase 및 테스트 작성

[33mcommit 6076fb751cca15a381cc6f99cc3ae021b64b4c20[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 18:54:46 2021 +0900

    Period UseCase의 Repository에 AutoWired 추가.
    
    AutoWired 안해주면 의존성 주입이 안될때가 있음. PeriodUpdateUseCase가 그럼.

[33mcommit 09d6009f362842d8ddfb999a02334023b491572f[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 18:52:43 2021 +0900

    Period Update UseCase 작성중...
    PeriodUpdateUseCase의 JpaRepository가 의존성 주입이 되지 않고 있음. null로 뜸.

[33mcommit 6f7dd45f3de5e4aa709c4738d7145745b2023ffd[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 18:16:40 2021 +0900

    Period Read UseCase 작성

[33mcommit 162dbb699bc0d4267c271e6f6cb4623e6ee111f0[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 17:38:39 2021 +0900

    교시 Create DTO periodOrder 언더바 삭제

[33mcommit 7b3810cc559ac5a278b5f14e85121619435250b1[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 17:36:47 2021 +0900

    교시 Create UseCase 작성

[33mcommit 5b8cb3fa00966df0e4116ede9f65ea914e32c74b[m
Merge: d94b3f3 51b8556
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Mar 31 16:39:45 2021 +0900

    Merge pull request #88 from KitTeamSe/f/#64
    
    교과 API 작성

[33mcommit 51b8556c235ee515c10739cc020f10c308eaaa2d[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 02:10:26 2021 +0900

    교과 삭제 Usecase 및 테스트 작성
    
    Subject Entity의 Code Column에 Unique 속성 추가

[33mcommit 347b726d79cc81b522514137eae525dc6d4385d7[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 01:50:57 2021 +0900

    LectureRoom UseCase Test 양식 수정

[33mcommit df5f746729eccf3bdb359e9208c5f7f4c665f011[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 01:47:05 2021 +0900

    LectureRoom Api Controller 및 DeleteUseCase 양식 통일화

[33mcommit 5646194b3408d8a49e0f410ec1b5aef0a4308750[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 01:43:20 2021 +0900

    Delete DTO의 pk note 수정
    삭제할 ㅇㅇ pk -> 삭제할 ㅇㅇ id

[33mcommit f4cb000312dc067f2a02348767cbab54ce1e4421[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Wed Mar 31 01:39:59 2021 +0900

    교과 수정 UseCase 작성

[33mcommit 3b105d46bfe5d93bf7f15c0d71e56767dca85bc9[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 22:20:10 2021 +0900

    Swagger Response 예제 표기 출력 픽스

[33mcommit e545255521dd00d61aa9eb4a2305d7f1f03a3b4c[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 22:12:18 2021 +0900

    Subject ReadDTO Swagger Response 예제 표시 문제 해결

[33mcommit 9f2de5f306d079fa81a4fcdaca495432986f9280[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 21:40:02 2021 +0900

    Subject Read UseCase 작성

[33mcommit 69f9fdbb547d3d9c8c0b28f15ba06afcf9c46add[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 21:05:24 2021 +0900

    Subject Create UseCase 작성
    Semester Enum 추가.
    학기를 Enum으로 할지, Integer로 할지 결정되지 않았기에 사용하지 않음.
    현재는 학기를 Integer로 다룸.

[33mcommit 5e3954c6bed1cb0eb258b02fdd2e95246758fcea[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 30 20:52:23 2021 +0900

    Add attach api

[33mcommit ee67f562d967a2c048d424fa541663066ff53136[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 20:20:12 2021 +0900

    교원 생성 Test Assertion 수정

[33mcommit 0c5b0a7b1c6fec9b53adde3ba31971518c85ac14[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 20:19:16 2021 +0900

    교과 UseCase Initialization

[33mcommit d94b3f3302298ce2e2fbe96b83c9d592e43f8bbd[m
Merge: ddc3e55 3bc69ef
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 30 19:59:22 2021 +0900

    Merge pull request #84 from KitTeamSe/f/#62
    
    F/#62

[33mcommit 3bc69ef7a7e331ca0854be9fc2fc1092261a1faf[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 19:49:48 2021 +0900

    교원 삭제 API 작성.

[33mcommit daa70ba39d227d990e1bd896543dc9dfa2058f53[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 18:48:49 2021 +0900

    교원 수정 UseCase 및 테스트 작성

[33mcommit 89a46174b308dc130014893e655b1ceb7474a2ff[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 17:38:15 2021 +0900

    교원 생성 UseCase, 교원 조회 UseCase 코드 작성

[33mcommit 3238176b9125c75be7f9654fe3dbb54701f40867[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 30 15:56:36 2021 +0900

    Add Post Create Api

[33mcommit 4ac776ed5ecac1c95a930c48a007839f90c0b638[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 15:44:02 2021 +0900

    강의실 DTO ApiModelProperty note와 example 순서 변경

[33mcommit 75e2f72679b8edaf8025505c0b064f35dab16243[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Tue Mar 30 15:40:56 2021 +0900

    교원 API 개발 시작
    기본 클래스 및 패키지 생성

[33mcommit ddc3e55ec5beee011633f36f8468efab975d2220[m
Merge: c5ff1c7 7266e98
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 30 01:08:26 2021 +0900

    Merge pull request #83 from KitTeamSe/f/#61
    
    강의실 API 구현

[33mcommit 7266e982840e84645acc8ca463fb3e7cbdff8ca5[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Mar 29 18:51:53 2021 +0900

    강의실 API 구현
    유닛 테스트만 수행해보았음.
    init_lower.sql의 비밀번호 대문자 복구

[33mcommit 4c74a29b910bd651aafba56e95f94701d64e763f[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 29 18:07:30 2021 +0900

    Add post api

[33mcommit c5ff1c7b029c899cf1c45d39b3cc758fd87a7f59[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Mar 29 15:38:17 2021 +0900

    강의실 유스케이스 BusinessException 패키지경로 수정
    강의실 엔티티 상속을 BaseEntity에서 AccountGenerateEntity로 수정

[33mcommit 44a1bd8483544242a811100fe29ccf7c26a8cd00[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 15:26:28 2021 +0900

    Update init.sql

[33mcommit 59e15b4fdf96df10ae325113393e428b0c522e94[m
Merge: f94ae04 ab9f0dc
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 15:24:58 2021 +0900

    Merge pull request #82 from KitTeamSe/dev_sch_api
    
    Dev sch api

[33mcommit ab9f0dc2634c12034c9bc066b0046a1e5f8e1ffd[m
Merge: a8e626c f94ae04
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 15:24:37 2021 +0900

    Merge branch 'dev_api' into dev_sch_api

[33mcommit a8e626c4677ba68217316a8fca1d27c61f3214fc[m
Merge: 9df6109 88a2ac0
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 15:22:12 2021 +0900

    Merge pull request #81 from KitTeamSe/f/#61
    
    F/#61

[33mcommit 88a2ac0cf36f0c0c21cdcbb8a13a748baa72784c[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Mon Mar 29 15:14:00 2021 +0900

    강의실 전체 조회 추가

[33mcommit f94ae0412bee25dae5c0809a8c3fb300c7a89a58[m
Merge: aaed6f3 a60425a
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 04:32:12 2021 +0900

    Merge pull request #80 from KitTeamSe/fixforjenkins
    
    Update update some test case for jenkins setting

[33mcommit a60425a671ec0143d07c1ac94bc682721abc35e0[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 29 04:29:45 2021 +0900

    Update update some test case for jenkins setting

[33mcommit aaed6f3f2d8fb960502ce595ab8c386b005a4d1a[m
Merge: e006026 6b16186
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 03:48:00 2021 +0900

    Merge pull request #79 from KitTeamSe/f/#78
    
    Add TagListening Api

[33mcommit 6b16186b0df99634c7f6b974eff7d629a6d7cb37[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 29 03:46:56 2021 +0900

    Add TagListening Api

[33mcommit e0060263cb3facc8a6f77c42773bc3e858c67c2b[m
Merge: 4ac60a3 a4665bf
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 02:15:22 2021 +0900

    Merge pull request #77 from KitTeamSe/f/#73/#74
    
    Resolve #73 #74

[33mcommit a4665bf71287ad5463f855dff6251a2e593bcf47[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 29 02:14:26 2021 +0900

    Resolve #73 #74

[33mcommit 4ac60a3de62b6144010a6bd0ccb2043e1cb257c2[m
Merge: f7e4d03 0b025bb
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 29 01:41:26 2021 +0900

    Merge pull request #76 from KitTeamSe/f/#53
    
    Add Authority api

[33mcommit 0b025bb3773645bc242c9aae55e88ad3f2c370f1[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 29 01:39:12 2021 +0900

    Add Authority api

[33mcommit 961ea4a984827d3349bcdbe9763389a0d3db54b6[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun Mar 28 22:11:39 2021 +0900

    강의실 생성 Usecase 테스트 완료

[33mcommit 9df6109aafec6506985356a992d0c1618976db75[m
Merge: 97ef2a3 b7970bb
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sun Mar 28 18:20:21 2021 +0900

    Merge pull request #72 from KitTeamSe/f/#40
    
    F/#40

[33mcommit b7970bbe4728ffd39474be57aa8cc2d5d0f440e0[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun Mar 28 18:15:16 2021 +0900

    강의실(LectureRoom) API 생성중

[33mcommit abfab460125b1a344a63a6e49f351674224adcd4[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Sun Mar 28 15:55:28 2021 +0900

    엔티티 연관관계 설정

[33mcommit f7e4d03523828a5547e62debe01aaf1f54e59130[m
Merge: 433bbde a8c4615
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sun Mar 28 03:26:30 2021 +0900

    Merge pull request #71 from KitTeamSe/f/#53
    
    Add blacklist api

[33mcommit a8c46153a960aa524ac7c5efd774a48bf93245d8[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sun Mar 28 03:25:40 2021 +0900

    Add blacklist api

[33mcommit 433bbde51ecb55c1cf479bc5b1ca4127a9013c99[m
Merge: f75a5a2 ad0aaa2
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sun Mar 28 02:46:46 2021 +0900

    Merge pull request #70 from KitTeamSe/f/#47
    
    Add Tag api

[33mcommit ad0aaa20589bdb7d7a807e378ea0cff1f5b3ed38[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sun Mar 28 02:44:03 2021 +0900

    Add Tag api

[33mcommit f75a5a2294056ca9c71f6aa736585ba378aeb807[m
Merge: 3b4528c 48d2a7d
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Fri Mar 26 23:48:28 2021 +0900

    Merge pull request #69 from KitTeamSe/f/#47
    
    Add board api

[33mcommit 48d2a7dda697ba94066def481cfe473a0f3215b4[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Mar 26 23:47:26 2021 +0900

    Add board api

[33mcommit 7a774fe97c5de7ff9fa2b3dffe1e30de344eb0e3[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Fri Mar 26 18:27:28 2021 +0900

    엔티티 작성중

[33mcommit 3b4528cc24d12b95e8b171202b9b2ab125a4ef34[m
Merge: 97ef2a3 c45af2d
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Fri Mar 26 02:44:50 2021 +0900

    Merge pull request #68 from KitTeamSe/f/#46
    
    Add menu api

[33mcommit c45af2d21fbbd97b9b543bd06e54b7cdaec03329[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Mar 26 02:43:26 2021 +0900

    Add menu api

[33mcommit 8e894b284d925ade2109bb8a6df74ca435f6d8c9[m
Author: dldhk97 <dldhk97@gmail.com>
Date:   Thu Mar 25 18:33:42 2021 +0900

    Mysql 테이블 접근 문제 해결
    SQL을 Lowercase로 수정.

[33mcommit 97ef2a3d803b8a93fda63cdc1234af5cdac38770[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 23 18:34:18 2021 +0900

    Update update WebSecurityConfig

[33mcommit af1818116e13769ed5384e4c57d2484dce7de0cb[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 23 09:25:48 2021 +0900

    Add Blacklist check ip ban filter

[33mcommit 9428c1053f158387f9cccd8afa710ca3f6ce4966[m
Merge: 15ed20d 74a425a
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 23 05:15:28 2021 +0900

    Merge branch 'dev_api' of https://github.com/KitTeamSe/se_api_server into f/#45

[33mcommit 15ed20d4d0d38e7c62662c7a77e75cee68a9c30d[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 23 05:14:50 2021 +0900

    Update Account logics

[33mcommit 74a425a56c3d180ed55564cb3691b4fb514cda94[m
Merge: b8c5600 24b4475
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 23 01:52:59 2021 +0900

    Merge pull request #67 from KitTeamSe/f/#45
    
    F/#45

[33mcommit 24b4475506add35b23d3ad4f14ccb318056bf40e[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 23 01:51:29 2021 +0900

    Add add Account entity tests

[33mcommit ede2aea387f4ff466b7953a9f7ee7aacfa0c11ac[m
Merge: 14340ae b8c5600
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 22 19:57:00 2021 +0900

    Merge branch 'f/#45' of https://github.com/KitTeamSe/se_api_server into f/#45

[33mcommit 14340ae6474906f166481449fd2f8fab07cd0b5b[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 22 19:56:15 2021 +0900

    Add add empty files

[33mcommit b8c5600dbe3bbea6d125fdfcc23bf90e50f4628c[m
Merge: 795daf7 590b8b6
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Mon Mar 22 19:40:06 2021 +0900

    Merge pull request #66 from KitTeamSe/f/#45
    
    F/#45

[33mcommit 590b8b6938cbd468b8dd47ec4e435b964b485877[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Mon Mar 22 19:37:45 2021 +0900

    Refactor refactoring packaging

[33mcommit b4b1686e1e65d55e648727bdc14c1a125c12fc5f[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sun Mar 21 23:10:25 2021 +0900

    Feature Update and Add account logics

[33mcommit 8679395744a2525b5173583d60e0dd9d1e77697b[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sun Mar 21 17:55:23 2021 +0900

    Feature Update and Add Accoutn entitie's API Logic

[33mcommit 795daf7372711cdf46f6f552cfb3204a577ab4ee[m
Merge: 3222c62 950e23d
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sun Mar 21 02:22:54 2021 +0900

    Merge branch 'dev_api' of https://github.com/KitTeamSe/se_api_server into dev_api

[33mcommit 3222c62a503483af1358957f73ec929dce7ebfd1[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sun Mar 21 02:21:35 2021 +0900

    Feature Implements Api boilerplate code

[33mcommit 950e23d4e732737d5f5fd81458c43cc8be815f04[m
Merge: b5c705a ede067f
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sat Mar 20 02:30:00 2021 +0900

    Merge pull request #43 from KitTeamSe/dev_auth
    
    Dev auth

[33mcommit ede067fdb32a1d55f5676f3960b5b9ed20910fb6[m
Merge: b5c705a 1148e89
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Sat Mar 20 02:29:16 2021 +0900

    Merge pull request #42 from KitTeamSe/f/#39
    
    F/#39

[33mcommit 1148e898cc101e5d6415f6724e47dd0ce2e22a7a[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Sat Mar 20 02:27:27 2021 +0900

    feature Implements Authentication and Authorize Logic

[33mcommit cc49472fa66bff0ca6fb7c449dce03250425b277[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Fri Mar 19 19:40:11 2021 +0900

    Update Authority Entities

[33mcommit b5c705abdbac279b3de5550c522c06d32b8532d5[m
Merge: 7d9aa32 b970b52
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Mar 17 23:59:37 2021 +0900

    Merge pull request #38 from hyodadak/entity_h
    
    Entity h

[33mcommit b970b5296208aa4a888e47d08d23d8b072ddd8b9[m
Author: HyoDaDak <rlagywls1015@gmail.com>
Date:   Wed Mar 17 22:21:56 2021 +0900

    Entity_modify_1
    
    type : Entity_modify_1
    
    Resolves : #14 #15 #16 #17 #19
    
    ##################################################
        types = {
          chore : 오타 수정, 단방향으로 수정, DB 확인
        }

[33mcommit 37fae7a07b4a55a3296767dac99c6a01a1921495[m
Author: HyoDaDak <rlagywls1015@gmail.com>
Date:   Wed Mar 17 12:01:25 2021 +0900

    #14 #15 #16 #17 #18 #19
    
    type : Entity
    
    Resolves : #14 #15 #16 #17 #18 #19
    
    See also : #11 #2
    ##################################################
        types = {
          feat :  ReportAccontMapping 추가
          chore : Post Entity, Account Entity 매핑관련 코드 추가
        }

[33mcommit 7d9aa322daa0b6292aa2b31ea3cfeddd23529b80[m
Merge: 166b7f1 c1bc23e
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Wed Mar 17 00:50:36 2021 +0900

    Merge pull request #35 from KitTeamSe/f/#34
    
    Update Build.gradle

[33mcommit c1bc23e6b6d84931057554c5992da7cca9f82e7f[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Wed Mar 17 00:49:09 2021 +0900

    Update Build.gradle
    
    resolve : #34

[33mcommit 166b7f194a843b8d05c1df9a8ce0f964415aa5b9[m
Merge: 896b93a 23efef0
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 23:00:06 2021 +0900

    Merge pull request #33 from KitTeamSe/f/#32
    
    Update Post, Reply Entity

[33mcommit 23efef0da22c7fbd4647e106be310e9a1d265af7[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 22:59:02 2021 +0900

    Update Post, Reply Entity
    
    add validation about size
    resolve : #32

[33mcommit 896b93a99fb2b05738812df5689854aa15151391[m
Merge: bba1163 834b2dd
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 22:52:02 2021 +0900

    Merge pull request #31 from KitTeamSe/f/#9
    
    Add MenuAuthorityGroupMapping Entity

[33mcommit 834b2dd5a8b37bdb059b42c52539cb92dd0e2b81[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 22:49:23 2021 +0900

    Add MenuAuthorityGroupMapping Entity
    
    resolve : #9

[33mcommit bba11630262d973710441586bfb1e5cc05ec97df[m
Merge: fa0cfa7 26c0840
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 22:46:23 2021 +0900

    Merge pull request #30 from KitTeamSe/f/#8
    
    Add AuthorityGroupAccountMapping Entity

[33mcommit 26c0840487f265a75171b5fe0f9c06133a995e4d[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 22:45:08 2021 +0900

    Add AuthorityGroupAccountMapping Entity
    
    resolve : #8

[33mcommit fa0cfa7199f6977dcd9b80822784ae663c9bbf5e[m
Merge: 09ce14d af10cdf
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 22:41:20 2021 +0900

    Merge pull request #29 from KitTeamSe/f/#7
    
    Add add AuthorityGroup Entity

[33mcommit af10cdf058158677ec6b06546f44598e8fcd9236[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 22:39:53 2021 +0900

    Add add AuthorityGroup Entity
    
    resolve : #7

[33mcommit 09ce14de9aeeb3341642cc8354f8e8a50eb22e96[m
Merge: 964570a 779fbf6
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 22:30:39 2021 +0900

    Merge pull request #28 from KitTeamSe/f/#11#12
    
    feature relove #11 #12 #27

[33mcommit 779fbf6acddd54346d6e1f8c9704e546f9e3cf84[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 22:28:54 2021 +0900

    feature relove #11 #12 #27
    
    resolve : #11, #12 , #27

[33mcommit 964570a4ad7f867f65cc51dac8c060262c2e2268[m
Merge: 4a2606c c41509a
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 21:55:55 2021 +0900

    Merge pull request #26 from KitTeamSe/f/#10
    
    Add add Board Entity

[33mcommit c41509a00689f6b7ccb4a2712b8f4880dbda5844[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 21:54:25 2021 +0900

    Add add Board Entity
    resolve : #10

[33mcommit 4a2606c696b2f52c46ec2eb576d927122c07a932[m
Merge: dedc256 b400342
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 19:37:11 2021 +0900

    Merge pull request #25 from KitTeamSe/f/#6
    
    Add Menu Entity

[33mcommit b4003421d6d800f366137eaf3e6515f9687a44a3[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 18:47:39 2021 +0900

    Add Menu Entity
    
    resolved : #6

[33mcommit dedc256ddd6e1e8fc4207832333934da2d634ea0[m
Merge: 272f361 69fe291
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 14:00:31 2021 +0900

    Merge pull request #24 from KitTeamSe/f/#5
    
    Add add Blacklist Entity

[33mcommit 69fe291e1039b5ea81dafe87ba71b3d87df8026f[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 13:59:02 2021 +0900

    Add add Blacklist Entity
    
    resolved #5

[33mcommit 272f361585de1f567cece5321e5040d55cee8908[m
Merge: 0599e3c 3a137cc
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 13:52:16 2021 +0900

    Merge pull request #23 from KitTeamSe/f/#4
    
    Add Career Entity

[33mcommit 3a137cc9fd13898d83ef3b77b5e95ed0543acf87[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 13:47:18 2021 +0900

    Add Career Entity

[33mcommit 0599e3cab667265babc52672f501b43ed0039e93[m
Merge: fcdb4bc d98edf3
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 13:25:09 2021 +0900

    Merge pull request #22 from KitTeamSe/f/#3
    
    F/#3

[33mcommit d98edf3c263caa1569ac29ee9513b14809e04da9[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 13:23:51 2021 +0900

    Reformat reformat all code by google style guide

[33mcommit 36b5b4e96310c9767fb4cffdfd670406997f63d9[m
Merge: 991cf20 bb7af68
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 12:59:42 2021 +0900

    Merge branch 'f/#3' of https://github.com/KitTeamSe/se_api_server into f/#3

[33mcommit 991cf2014baa29b2c4e1d3792528f3b03a0d3cc9[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 12:55:33 2021 +0900

    Update update EmploymentInfo Entity
    
    resolved : #2

[33mcommit bb7af68510a3a8093b76b4a9474812e160d85b99[m
Merge: fcdb4bc 1bf520f
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 12:42:28 2021 +0900

    Merge pull request #21 from KitTeamSe/f/#1
    
    Update Update Account Entity

[33mcommit 1bf520f95884f8149ede5553c63ae5a1902b5593[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 12:41:23 2021 +0900

    Update Update Account Entity

[33mcommit fcdb4bc4ac9f2390e21fe61772b94cd52f31aa09[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 00:54:04 2021 +0900

    Update issue templates

[33mcommit f2ae99ce999bad7e2cb13258e302129cb4d60c80[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 00:53:45 2021 +0900

    Update issue templates

[33mcommit 35164a6977da6ad832d4677901c6079c41477744[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 00:50:18 2021 +0900

    Update issue templates

[33mcommit bcbb7033fef2d90679fd0127cff491328746317b[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 00:49:20 2021 +0900

    Update issue templates

[33mcommit 930084f64256787c15395c82db8ca31071c4201f[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 00:48:50 2021 +0900

    Update issue templates

[33mcommit a1519c4b877733b0e2afb10b3445fd03aa520fb2[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 00:48:12 2021 +0900

    Update issue templates

[33mcommit b83c1067ca8c2810d50161fdbfa8411ebe647772[m
Author: junho Do <50814086+junhodo@users.noreply.github.com>
Date:   Tue Mar 16 00:47:17 2021 +0900

    Update issue templates

[33mcommit dcb03591fb4e90dc7327c26532469d678b2e0ed2[m
Author: junho Do <50814086+djh20@users.noreply.github.com>
Date:   Tue Mar 16 00:43:41 2021 +0900

    first commit
