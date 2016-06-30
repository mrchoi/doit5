package com.ckstack.ckpush.common;

/**
 * Created by dhkim94 on 15. 3. 11..
 */
public interface MDV {
    int NUSE    = -100;
    int NONE    = -1;

    int YES     = 1;
    int NO      = 2;
    int DENY    = 3;

    int SECRET_DOCUMENT_OF_PUBLIC_CATEGORY  = -2;   // 공개글 일때 비밀글(작성자가 패스워드 입력해야 조회 가능) 표시

    int PUBLIC  = 1;            // 공개용(로그인 필요 없음)
    int PRIVATE = 2;            // 비공개용(로그인 필요 함)

    int TAG_ADMIN   = 1;
    int TAG_USER    = 2;

    int NORMAL_CATEGORY = 1;    // 일반 게시물을 포함하는 카테고리 타입
    int LINK_CATEGORY = 2;      // 링크 게시물을 포함하는 카테고리 타입

    int FILE_REPOSITORY = 1;    // 리포지토리용 파일
    int FILE_MSG_PUSH   = 2;    // 메시지 푸쉬용 파일
    int FILE_DOC_ATTACH = 3;    // 게시물 첨부용 파일

    // 유저의 종류(유저 1차 분류)
    int USER_ROOT       = 1;        // 관리자
    int USER_NORMAL     = 2;        // 일반 사용자

    // 앱에 가입된 유저의 종류(유저 2차 분류)
    int APP_USER_ROOT       = 1;    // 앱 관리자
    int APP_USER_NORMAL     = 2;    // 앱 사용자
    int APP_USER_VISITOR    = 3;    // 앱 방문자
    int APP_USER_ADVISOR    = 4;    // 상담사

    // push sound 설정(Android 일때는 앱에서 사운드 설정 가능하고, iOS는 해당 설정이 의미 없다-os단에서 사운드 설정됨)
    int PUSH_NOTI_SOUND     = 1;    // 사운드로 사용
    int PUSH_NOTI_VIB       = 2;    // 바이브레이션으로 사용
    int PUSH_NOTI_MUTE      = 99;   // 묵음으로 사용

    // 소셜 타입
    int [] SOCIAL_SUPPORT   = {1,2,3,4,5,6};    // 지원하는 전체
    int SOCIAL_NONE         = -1;               // 소셜 없음
    int SOCIAL_KAKAOTOC     = 1;                // 카카오톡
    int SOCIAL_FACEBOOK     = 2;                // 페이스북
    int SOCIAL_TWITTER      = 3;                // 트위터
    int SOCIAL_GOOGLE       = 4;                // 구글
    int SOCIAL_NAVER        = 5;                // 네이버
    int SOCIAL_DAUM         = 6;                // 다음

    // 404(파일 없음): 이미지 프로세싱에서 주로 사용하며 파일이 존재하지 않거나 내부 오류시 사용
    int HTTP_ERR_X_REQUEST_404  = 404;

    // 405(지원하는 Method 없음): Request URL에 맞는 GET, POST 등등의 지원하는 메소드가 없음
    int HTTP_ERR_X_REQUEST_405  = 405;

    // 412(사전조건 실패): 서버가 요청자가 요청 시 부과한 사전조건을 만족하지 않는다.
    int HTTP_ERR_X_REQUEST      = 412;

    // 500(내부 서버 오류): 서버에 오류가 발생하여 요청을 수행할 수 없다.
    int HTTP_ERR_SERVER          = 500;

    // 901 : Ajax 호출시 session timeout 에러
    int HTTP_ERR_AJAX_SESSION_TIMEOUT   = 901;


    //상담 신청 테이블 상태값
    int APPLICATION_STATUS_INSERT = 0; // 접수
    int APPLICATION_STATUS_READY = 1; // 준비중
    int APPLICATION_STATUS_PROGRESS = 2; // 진행중
    int APPLICATION_STATUS_COMPLETE = 3; // 완료
    int APPLICATION_STATUS_CANCLE = 4; // 취소


}
