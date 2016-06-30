package com.ckstack.ckpush.service.board;

import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.*;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 7. 24..
 */
public interface DocumentService {

    /**
     * 앱에 포함되어 있는 모든 게시판, 카테고리 리스트를 구한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param categoryType 카테고리 종류. MDV.NUSE 라면 모든 카테고리 정보를 구한다.
     * @param openType 게시판및 카테고리의 공개(로그인 필요) 여부. MDV.NUSE 라면 모든 리스트
     * @param enabled 게시판및 카테고리의 활성 여부. MDV.NUSE 라면 모든 리스트
     * @param secrets 비공개 카테고리일 경우 비밀글 조건에 따른 게시물 카운트를 위한 값.
     *                1. -1 만들어 있으면 비공개 카테고리에서 비밀글 아닌 게시물만 카운트.
     *                2. member_srl 만 들어 잇으면 비공개 카테고리에서 해당 유저의 비밀글 게시물만 카운트.
     *                3. -1, member_srl 인 경우는 비공개 카테고리에서 해당 유저의 비밀글 포함한 게시물을 카운트
     *                4. null 이면 비밀글 여부 상관 없이 모든 글 카운트
     * @param block block 게시물 카운트에서 block 된 것을 포함할지 여부
     *              MDV.YES : block된 게시물 카운트
     *              MDV.NO  : block 안된 게시물 카운트
     *              MDV.NUSE: block 여부 관계 없는 게시물 카운트
     * @return 앱에 포함된 모든 게시판, 카테고리 리스트. 다음의 값이 들어 있는 리스트 이다.
     *         {
     *             board_srl : [numeric] 게시판 시리얼 넘버
     *             board_name : [string] 게시판 이름
     *             board_description : [string] 게시판 설명
     *             enabled : [numeric] 게시판 활성 여부. 1:활성, 2:비활성
     *             open_type : [numeric] 공개용 게시판(조회시 로그인 필요 여부) 인지 여부
     *             categories : [list] 게시판이 포함하고 있는 카테고리 리스트
     *             {
     *                 category_srl : [numeric] 카테고리 시리얼 넘버
     *                 board_srl : [numeric] 카테고리가 포함된 게시판 시리얼 넘버
     *                 category_name : [string] 카테고리 이름
     *                 category_description : [string] 카테고리 설명
     *                 category_type : [numeric] 카테고리 종류. 1:일반, 2:링크
     *                 enabled : [numeric] 카테고리 활성 여부. 1:활성, 2:비활성
     *                 open_type: [numeric] 공개용 카테고리(조회시 로그인 필요 여부) 인지 여부
     *                 document_count : [numeric] 카테고리에 포함된 게시물 또는 링크 게시물 개수
     *             }
     *         }
     */
    List<Map<String, Object>> getBoardCategoryInApp(int appSrl, int categoryType, int openType,
                                                    int enabled, List<Long> secrets, int block);

    /**
     * 게시물의 카운트를 구한다.
     *
     * @param appSrl 게시물이 속한 앱 시리얼 넘버
     * @param boardSrl 게시물이 속한 게시판 시리얼 넘버
     * @param categorySrl 게시물이 속한 카테고리 시리얼 넘버
     * @param documentTitle 게시물의 제목. 첫글자 부터 like 검색
     * @param secret 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 중 하나
     * @param secrets 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 등등
     * @param block 게시물의 차단 여부. 1:차단, 2:차단되지 않음
     * @param allowComment 게시물 댓글 달기 허용 여부. 1:댓글 허용함, 2:댓글 허용하지 않음
     * @param allowNotice 게시물이 공지 사항인지 여부. 1:공지 게시물, 2:일반 게시물
     * @param memberSrl 게시물을 작성한 작성자의 사용자 시리얼 넘버
     * @param userId 게시물을 작성한 작성자의 사용자 아이디
     * @param userName 게시물을 작성한 작성자의 이름
     * @param nickName 게시물을 작성한 작성자의 별명
     * @return 조건에 맞는 게시물 카운트
     */
    long countDocument(int appSrl, long boardSrl, long categorySrl, String documentTitle, long secret,
                       List<Long> secrets, int block, int allowComment, int allowNotice, long memberSrl,
                       String userId, String userName, String nickName);

    /**
     * 게시물의 카운트를 구한다.
     * 내부에서 countDocument(int, long, long, String, int, int, int, int, long, String, String, String)을
     * 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_srl, category_srl, document_title,
     *                     secret, block, allow_comment, allow_notice, member_srl, user_id,
     *                     user_name, nick_name 에 대해서 지원 한다.
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @return 조건에 맞는 게시물 카운트
     */
    long countDocument(Map<String, String> searchFilter, List<Long> secrets);

    /**
     * 게시물 리스트를 구한다.
     *
     * @param appSrl 게시물이 속해 있는 앱 시리얼 넘버
     * @param boardSrl 게시물이 속해 있는 게시판 시리얼 넘버
     * @param categorySrl 게시물이 속해 있는 카테고리 시리얼 넘버
     * @param documentTitle 게시물의 제목. 첫글자 부터 like 검색
     * @param secret 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 중 하나
     * @param secrets 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 등등
     * @param block 게시물의 차단 여부. 1:차단, 2:차단되지 않음
     * @param allowComment 게시물 댓글 달기 허용 여부. 1:댓글 허용함, 2:댓글 허용하지 않음
     * @param allowNotice 게시물이 공지 사항인지 여부. 1:공지 게시물, 2:일반 게시물
     * @param memberSrl 게시물을 작성한 작성자의 사용자 시리얼 넘버
     * @param userId 게시물을 작성한 작성자의 사용자 아이디
     * @param userName 게시물을 작성한 작성자의 이름
     * @param nickName 게시물을 작성한 작성자의 별명
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 게시물 리스트
     */
    List<DocumentEntity> getDocument(int appSrl, long boardSrl, long categorySrl, String documentTitle,
                                     long secret, List<Long> secrets, int block, int allowComment, int allowNotice,
                                     long memberSrl, String userId, String userName, String nickName,
                                     Map<String, String> sort, int offset, int limit);

    /**
     * 게시물 리스트를 구한다.
     * 내부에서 getDocument(int, long, long, String, int, int, int, int, long, String,
     *                    String, String, Map, int int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_srl, category_srl,
     *                     document_title, secret, block, allow_comment, allow_notice,
     *                     member_srl, user_id, user_name, nick_name 에 대해서 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 게시물 리스트
     */
    List<DocumentEntity> getDocument(Map<String, String> searchFilter, List<Long> secrets,
                                     Map<String, String> sort, int offset, int limit);

    /**
     * 게시물 정보를 구한다. 게시물의 태그, 첨부 파일 모두 포함되어 있다.
     *
     * @param documentSrl 게시물 시리얼 넘버
     * @return 게시물 정보
     */
    DocumentEntity getDocument(long documentSrl);

    /**
     * API등의 외부에 알려줄 값에서 사용하기 위해서 게시물 리스트를 정리 한다.
     *
     * 1. 다음 항목 삭제
     *   - board_srl
     *   - user_tags
     *   - attach_file_srls
     *   - admin_tags
     *   - block
     *   - template_srl
     *   - document_password
     *   - app_srl
     *   - member_srl
     *
     * 2. 템플릿을 json 형태로 변경(저장은 스트링으로 되어 있음)
     * 3. 사용자 태그를 시리얼 넘버에서 문자로 변경해서 추가
     * 4. 첨부 파일 리스트 추가. 첨부 파일 정보에서 다음 항목은 삭제 된다.
     *   - thumb_url
     *   - deleted
     *   - member_srl
     *   - thumb_height
     *   - file_path
     *   - ipaddress
     *   - thumb_width
     *   - c_date
     *   - u_date
     *   - thumb_path
     *
     * @param documentEntities 정보 정리를 할 게시물 리스트
     * @param containDocumentContent 게시물 본문을 포함 할지 여부. true이면 포함, false이면 무조건 empty string으로 리턴됨
     * @param httpServletRequest file_url을 구하기 위해서 필요한 값. 만일 null 이라면 was의 instance명은 빠진다
     * @return 정리된 게시물 리스트
     */
    List<Map<String, Object>> cleanUpDocumentListForAPI(List<DocumentEntity> documentEntities,
                                                        boolean containDocumentContent,
                                                        HttpServletRequest httpServletRequest);

    /**
     * API등의 외부에 알려줄 값에서 사용하기 위해서 게시물을 정리 한다.
     *
     * @param documentEntity 정리할 게시물
     * @param httpServletRequest file_url을 구하기 위해서 필요한 값. 만일 null 이라면 was의 instance명은 빠진다
     * @return 정리된 게시물 정보
     */
    Map<String, Object> cleanUpDocumentForAPI(DocumentEntity documentEntity, HttpServletRequest httpServletRequest);

    /**
     * API등의 외부에 알려줄 값에서 사용하기 위해서 링크 게시물 리스트를 정리 한다.
     *
     * 1. 다음 항목 삭제
     *   - board_srl
     *   - user_tags
     *   - attach_file_srls
     *   - admin_tags
     *   - block
     *   - template_srl
     *   - document_password
     *   - app_srl
     *   - member_srl
     *
     * 2. 템플릿을 json 형태로 변경(저장은 스트링으로 되어 있음)
     * 3. 사용자 태그를 시리얼 넘버에서 문자로 변경해서 추가
     * 4. 첨부 파일 리스트 추가. 첨부 파일 정보에서 다음 항목은 삭제 된다.
     *   - thumb_url
     *   - deleted
     *   - member_srl
     *   - thumb_height
     *   - file_path
     *   - ipaddress
     *   - thumb_width
     *   - c_date
     *   - u_date
     *   - thumb_path
     *
     * @param documentLinkEntities 정보 정리를 할 링크 게시물 리스트
     * @param containDocumentContent 게시물 본문을 포함 할지 여부. true이면 포함, false이면 무조건 empty string으로 리턴됨
     * @param httpServletRequest file_url을 구하기 위해서 필요한 값. 만일 null 이라면 was의 instance명은 빠진다
     * @return 정리된 게시물 리스트
     */
    List<Map<String, Object>> cleanUpDocumentLinkListForAPI(List<DocumentLinkEntity> documentLinkEntities,
                                                            boolean containDocumentContent,
                                                            HttpServletRequest httpServletRequest);

    /**
     * 게시물을 저장 한다.
     *
     * @param documentEntity 저장할 게시물 정보
     */
    void addDocument(DocumentEntity documentEntity);

    /**
     * 게시물을 삭제 한다. 게시물을 삭제하면 게시물에 포함된 첨부 파일의 삭제 flag를 delete로 변경 한다.
     * 실제 물리 파일을 삭제 하는 것은 아니다. 물리 파일은 delete flag로 cron 으로 삭제 된다.
     *
     * @param documentSrls 삭제할 게시물의 시리얼 넘버 리스트
     */
    void deleteDocument(List<Long> documentSrls);

    /**
     * 게시물을 수정 한다.
     *
     * @param documentEntity 수정할 게시물 항목
     * @param documentSrl 수정할 게시물 시리얼 넘버
     */
    void modifyDocument(DocumentEntity documentEntity, int documentSrl);

    /**
     * 카테고리를 삭제 한다.
     *
     * @param categorySrls 삭제할 카테고리 시리얼 넘버 리스트
     */
    void deleteDocumentCategory(List<Long> categorySrls);

    /**
     * 카테고리를 추가 한다.
     *
     * @param documentCategoryEntity 추가할 카테고리 정보
     */
    void addDocumentCategory(DocumentCategoryEntity documentCategoryEntity);

    /**
     * 카테고리를 수정 한다.
     *
     * @param categorySrl 수정 할 카테고리 시리얼 넘버
     * @param modifyValue 수정 할 카테고리 정보
     */
    void modifyDocumentCategory(long categorySrl, Map<String, String> modifyValue);

    /**
     * 카테고리 정보를 구한다.
     *
     * @param categorySrl 카테고리 시리얼 넘버
     * @return 카테고리 정보. 만일 존재하지 않으면 null 을 리턴한다.
     */
    DocumentCategoryEntity getDocumentCategory(long categorySrl);

    /**
     * 카테고리 리스트를 구한다.
     *
     * @param appSrl 카테고리가 포함된 앱 시리얼 넘버
     * @param boardSrl 카테고리가 포함된 게시판 시리얼 넘버
     * @param categoryName 카테고리 이름. 첫글자 기준 like 검색
     * @param categoryType 카테고리 타입. 일반 게시물 포함 카테고리, 링크 게시물 포함 카테고리 구분
     * @param enabled 카테고리 활성 여부. 1:활성, 2:비활성
     * @param openType 카테고리 공개(로그인 해야 사용할지) 여부. 1:공개(로그인 하지 않아도 볼 수 있음), 2:비공개(로그인 해야 볼 수 있음)
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 카테고리 리스트
     */
    List<DocumentCategoryEntity> getDocumentCategory(int appSrl, long boardSrl, String categoryName, int categoryType,
                                                     int enabled, int openType, Map<String, String> sort,
                                                     int offset, int limit);

    /**
     * 카테고리 리스트를 구한다.
     * 내부에서 getDocumentCategory(int, long, String, int, int, int, Map, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_srl, category_name,
     *                     category_type, enabled, open_type 에 대해서 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 카테고리 리스트
     */
    List<DocumentCategoryEntity> getDocumentCategory(Map<String, String> searchFilter, Map<String, String> sort,
                                                     int offset, int limit);

    /**
     * 카테고리 카운트를 구한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param boardSrl 게시판 시리얼 넘버
     * @param categoryName 카테고리 이름. 첫글자 기준 like 검색
     * @param categoryType 카테고리 타입. 일반 게시물 포함 카테고리, 링크 게시물 포함 카테고리 구분
     * @param enabled 카테고리 활성 여부. 1:활성, 2:비활성
     * @param openType 카테고리 공개(로그인 해야 사용할지) 여부. 1:공개(로그인 하지 않아도 볼 수 있음), 2:비공개(로그인 해야 볼 수 있음)
     * @return 조건에 맞는 카테고리 카운트
     */
    long countDocumentCategory(int appSrl, long boardSrl, String categoryName, int categoryType,
                               int enabled, int openType);

    /**
     * 카테고리 카운트를 구한다.
     * 내부에서 countDocumentCategory(int, long, String, int, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_srl, category_name, category_type,
     *                     enabled, open_type 에 대해서 지원 한다.
     * @return 조건에 맞는 카테고리 카운트
     */
    long countDocumentCategory(Map<String, String> searchFilter);

    /**
     * 게시판 카운트를 구한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param boardName 게시판 이름. 첫글자 기준 like 검색
     * @param enabled 게시판 활성 여부. 1:활성, 2:비활성
     * @param openType 게시판 공개(로그인 해야 사용할지) 여부. 1:공개(로그인 하지 않아도 볼 수 있음), 2:비공개(로그인 해야 볼 수 있음)
     * @return 조건에 맞는 게시판 카운트
     */
    long countDocumentBoard(int appSrl, String boardName, int enabled, int openType);

    /**
     * 게시판 카운트를 구한다.
     * 내부에서 countDocumentBoard(int, String, int)를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_name, enabled, open_type 에 대해서 지원 한다.
     * @return 조건에 맞는 게시판 카운트
     */
    long countDocumentBoard(Map<String, String> searchFilter);

    /**
     * 게시판 리스트를 구한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param boardName 게시판 이름. 첫글자 기준 like 검색
     * @param enabled 게시판 활성 여부.
     * @param openType 게시판 공개(로그인 해야 사용할지) 여부. 1:공개(로그인 하지 않아도 볼 수 있음), 2:비공개(로그인 해야 볼 수 있음)
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 게시판 리스트
     */
    List<DocumentBoardEntity> getDocumentBoard(int appSrl, String boardName, int enabled, int openType,
                                               Map<String, String> sort, int offset, int limit);

    /**
     * 게시판 리스트를 구한다.
     * 내부에서 getDocumentBoard(int, String, int, Map, int, int)를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_name,
     *                     board_description, enabled, open_type 에 대해서 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 게시판 리스트
     */
    List<DocumentBoardEntity> getDocumentBoard(Map<String, String> searchFilter, Map<String, String> sort,
                                               int offset, int limit);

    /**
     * 게시판을 정보를 구한다.
     *
     * @param boardSrl 게시판 시리얼 넘버
     * @return 게시판 정보. 만일 존재하지 않는 게시판이면 null을 리턴한다.
     */
    DocumentBoardEntity getDocumentBoard(long boardSrl);

    /**
     * 게시판을 추가 한다.
     *
     * @param documentBoardEntity 추가할 게시판 정보
     */
    void addDocumentBoard(DocumentBoardEntity documentBoardEntity);

    /**
     * 게시판을 삭제 한다.
     *
     * @param boardSrls 삭제할 게시판 시리얼 넘버를 포함하는 리스트
     */
    void deleteDocumentBoard(List<Long> boardSrls);

    /**
     * 게시판을 수정 한다.
     *
     * @param boardSrl 수정 할 게시판 시리얼 넘버
     * @param modifyValue 수정할 게시판 정보.
     */
    void modifyDocumentBoard(long boardSrl, Map<String, String> modifyValue);

    /**
     * 게시물 템플릿을 추가 한다.
     *
     * @param templateEntity 추가할 템플릿 정보
     */
    void addDocumentTemplate(TemplateEntity templateEntity);

    /**
     * 게시물 템플릿의 카운트를 구한다.
     *
     * @param templateTitle 템플릿 이룸. 첫글자 기준 like 검색
     * @return 템플릿 카운트
     */
    long countDocumentTemplate(int templateSrl, String templateTitle);

    /**
     * 게시물 템플릿의 카운트를 구한다.
     * 내부에서 countDocumentTemplate(int, String)을 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. template_title 에 대해서는 지원 한다.
     * @return 템플릿 카운트
     */
    long countDocumentTemplate(Map<String, String> searchFilter);

    /**
     * 게시물 템플릿의 리스트를 구한다.
     *
     * @param templateTitle 템플릿 이름. 첫글자 기준 like 검색
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 템플릿 리스트
     */
    List<TemplateEntity> getDocumentTemplate(List<Integer> templateSrls, String templateTitle,
                                             Map<String, String> sort, int offset, int limit);

    /**
     * 게시물 템플릿의 리스트를 구한다.
     * 내부에서 getTemplate(String, Map, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. template_title 에 대해서 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 템플릿 리스트
     */
    List<TemplateEntity> getDocumentTemplate(Map<String, String> searchFilter, Map<String, String> sort,
                                             int offset, int limit);

    /**
     * 템플릿 시리얼 넘버를 사용하여 템플릿 내용이 json 형태로 들어 있는 템플릿 맵을 구한다.
     * 만일 템플릿 구조가 잘못된 것이거나 오류가 발생하면 템플릿 json은 null로 들어 간다.
     *
     * @param templateSrls 템플릿 맵을 구할 템플릿 시리얼 넘버 리스트
     * @return 템플릿 구조가 json으로 들어 있는 템플릿 맵
     */
    Map<Integer, JSONObject> getDocumentTemplate(List<Integer> templateSrls);

    /**
     * 게시물 템플릿 정보를 구한다.
     *
     * @param templateSrl 템플릿 시리얼 넘버
     * @return 템플릿 정보. 만일 없으면 null 을 리턴한다.
     */
    TemplateEntity getDocumentTemplate(int templateSrl);

    /**
     * 게시물 템플릿을 사용하는 앱 정보를 구한다.
     *
     * @param templateSrl 템플릿 시리얼 넘버
     * @return 앱 리스트
     */
    List<AppEntity> getAppInfoUsingTemplate(int templateSrl);

    /**
     * 앱에서 사용하는(매핑 되어 있는) 템플릿 리스트를 구한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @return 템플릿 리스트
     */
    List<TemplateEntity> getTemplateOfApp(int appSrl);

    /**
     * 게시물 템플릿을 삭제 한다.
     *
     * @param templateSrls 삭제할 템플릿 리스트
     */
    void deleteDocumentTemplate(List<Integer> templateSrls);

    /**
     * 게시물 템플릿을 수정 한다.(템플릿, 앱 매핑도 같이 수정 한다.)
     * 템플릿은 다음이 수정 가능 하다.
     * - 템플릿 이름
     * - 템플릿 설명
     * - 템플릿 구성
     * - 템플랏 사용하는 앱
     *
     * @param templateEntity 변경할 템플릿 정보
     * @param templateSrl 변경 할 템플릿 시리얼 넘버
     */
    void modifyDocumentTemplate(TemplateEntity templateEntity, int templateSrl);

    /**
     * 태그의 카운트를 구한다.
     *
     * @param appSrl 태그가 속해 있는 앱 시리얼 넘버
     * @param memberSrl 태그를 만든 사용자 시리얼 넘버
     * @param tagName 태그 이름. 첫글자 부터 like 검색
     * @param adminTag 관리자용 태그 여부. 1:관리자용 태그, 2:일반 사용자용 태그
     * @return 조건에 맞는 태그의 카운트를 구한다.
     */
    long countDocumentTag(int appSrl, long memberSrl, String tagName, int adminTag);

    /**
     * 태그의 카운트를 구한다.
     * 내부에서 countDocumentTag(int, long, String, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, member_srl, tag_name, admin_tag 에 대해서는 지원 한다.
     * @return 조건에 맞는 태그의 카운트를 구한다.
     */
    long countDocumentTag(Map<String, String> searchFilter);

    /**
     * 태그 리스트를 구한다.
     *
     * @param appSrl 태그의 앱 시리얼 넘버
     * @param memberSrl 태그를 만든 사용자 시리얼 넘버
     * @param tagName 태그 이름. 첫글자 부터 like 검색
     * @param adminTag 관리자용 태그 여부. 1:관리자용 태그, 2:일반 사용자용 태그
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 태그 리스트를 구한다.
     */
    List<TagEntity> getDocumentTag(int appSrl, long memberSrl, String tagName, int adminTag,
                                   Map<String, String> sort, int offset, int limit);

    /**
     * 태그 리스트를 구한다.
     * 내부에서 getDocumentTag(int, long, String, int, Map, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, member_srl, tag_name, admin_tag 에 대해서 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 태그 리스트를 구한다.
     */
    List<TagEntity> getDocumentTag(Map<String, String> searchFilter, Map<String, String> sort,
                                   int offset, int limit);

    /**
     * 링크 게시물을 생성 한다.
     *
     * @param documentLinkEntity 추가할 게시물 링크 정보
     */
    void addDocumentLink(DocumentLinkEntity documentLinkEntity);

    /**
     * 링크 게시물 카운트를 구한다. 게시물 정보와 연동되어 있는 링크 카운트 이다.
     *
     * @param documentSrl 원본 게시물 시리얼 넘버
     * @param documentSrls 원본 게시물 시리얼 넘버 리스트
     * @param appSrl 링크 게시물이 포함된 앱 시리얼 넘버
     * @param boardSrl 링크 게시물이 포함된 게시판 시리얼 넘버
     * @param boardSrls 링크 게시물이 포함된 게시판 시리얼 넘버 리스트
     * @param categorySrl 링크 게시물이 포함된 카테고리 시리얼 넘버
     * @param categorySrls 링크 게시물이 포함된 카테고리 시리얼 넘버 리스트
     * @param documentTitle 원본 게시물 제목
     * @param secret 원본 게시물 비밀글 구분값
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param block 원본 게시물 차단 여부
     * @param allowComment 원본 게시물 코멘트 허용 여부
     * @param allowNotice 원본 게시물 공지 사항 여부
     * @param memberSrl 원본 게시물 게시자 시리얼 넘버
     * @param userId 원본 게시물 게시자 사용자 아이디
     * @param userName 원본 게시물 게시자 이름
     * @param nickName 원본 게시물 게시자 별명
     * @return 조건에 맞는 링크 게시물 카운트
     */
    long countDocumentLink(long documentSrl, List<Long> documentSrls, int appSrl, long boardSrl,
                           List<Long> boardSrls, long categorySrl, List<Long> categorySrls,
                           String documentTitle, long secret, List<Long> secrets, int block,
                           int allowComment, int allowNotice, long memberSrl, String userId,
                           String userName, String nickName);

    /**
     * 링크 게시물 카운트를 구한다. 게시물 정보와 연동되어 있는 링크 카운트 이다.
     * 내부에서 countDocumentLink(long, List, int, long, ...)을 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. document_srl, app_srl, board_srl, category_srl, document_title,
     *                     secret, block, allow_comment, allow_notice, member_srl, user_id,
     *                     user_name, nick_name 에 대해서 지원 한다.
     *                     document_srls, board_srls, category_srls 는 지원하지 않는다.
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @return 조건에 맞는 링크 게시물 카운트
     */
    long countDocumentLink(Map<String, String> searchFilter, List<Long> secrets);

    /**
     * 링크 게시물 리스트를 구한다. 게시물 정보와 연동되어 있는 링크 카운트 이다.
     *
     * @param documentLinkSrls 링크 게시물 시리얼 넘버 리스트
     * @param documentSrl 원본 게시물 시리얼 넘버
     * @param documentSrls 원본 게시물 시리얼 넘버 리스트
     * @param appSrl 링크 게시물이 포함된 앱 시리얼 넘버
     * @param boardSrl 링크 게시물이 포함된 게시판 시리얼 넘버
     * @param boardSrls 링크 게시물이 포함된 게시판 시리얼 넘버 리스트
     * @param categorySrl 링크 게시물이 포함된 카테고리 시리얼 넘버
     * @param categorySrls 링크 게시물이 포함된 카테고리 시리얼 넘버 리스트
     * @param documentTitle 원본 게시물 제목
     * @param secret 원본 게시물 비밀글 구분값
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param block 원본 게시물 차단 여부
     * @param allowComment 원본 게시물 코멘트 허용 여부
     * @param allowNotice 원본 게시물 공지 사항 여부
     * @param memberSrl 원본 게시물 게시자 시리얼 넘버
     * @param userId 원본 게시물 게시자 사용자 아이디
     * @param userName 원본 게시물 게시자 이름
     * @param nickName 원본 게시물 게시자 별명
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 링크 게시물 리스트
     */
    List<DocumentLinkEntity> getDocumentLink(List<Long> documentLinkSrls, long documentSrl, List<Long> documentSrls,
                                             int appSrl, long boardSrl, List<Long> boardSrls,
                                             long categorySrl, List<Long> categorySrls, String documentTitle, long secret,
                                             List<Long> secrets, int block, int allowComment, int allowNotice,
                                             long memberSrl, String userId, String userName, String nickName,
                                             Map<String, String> sort, int offset, int limit);

    /**
     * 링크 게시물 리스트를 구한다. 게시물 정보와 연동되어 있는 링크 카운트 이다.
     * 내부에서 this.getDocumentLink(List, List, int, long, ...) 을 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. document_srl, app_srl, board_srl, category_srl, document_title,
     *                     secret, block, allow_comment, allow_notice, member_srl, user_id,
     *                     user_name, nick_name 에 대해서 지원 한다.
     *                     document_link_srls, document_srls, board_srls, category_srls 는 지원하지 않는다.
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 링크 게시물 리스트
     */
    List<DocumentLinkEntity> getDocumentLink(Map<String, String> searchFilter, List<Long> secrets,
                                             Map<String, String> sort, int offset, int limit);

    /**
     * 게시물 링크를 삭제 한다.
     *
     * @param documentLinkSrls 삭제할 게시물 시리얼 넘버 리스트
     */
    void deleteDocumentLink(List<Long> documentLinkSrls);

    /**
     * 특정 태그를 포함하고 있는 게시물 카운트를 구한다.
     *
     * @param tagSrl 태그 시리얼 넘버
     * @param tagSrls 태그 시리얼 넘버 리스트
     * @param documentSrl 게시물 시리얼 넘버
     * @param documentSrls 게시물 시리얼 넘버 리스트
     * @param appSrl 앱 시리얼 넘버
     * @param boardSrl 게시판 시리얼 넘버
     * @param boardSrls 게시판 시리얼 넘버 리스트
     * @param categorySrl 카테고리 시리얼 넘버
     * @param categorySrls 카테고리 시리얼 넘버 리스트
     * @param documentTitle 게시물 제목. 첫 글자 부터 like 검색
     * @param secret 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 중 하나
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param block 차단 여부
     * @param allowComment 댓글 허용 여부
     * @param allowNotice 공지 사항 여부
     * @param memberSrl 게시물 작성자 시리얼 넘버
     * @param userId 게시물 작성자 유저 아이디
     * @param userName 게시물 작성자 이름
     * @param nickName 게시물 작성자 별명
     * @return 태그를 포함하고 있는 게시물 카운트
     */
    long countDocumentOfTag(long tagSrl, List<Long> tagSrls, long documentSrl, List<Long> documentSrls,
                            int appSrl, long boardSrl, List<Long> boardSrls, long categorySrl,
                            List<Long> categorySrls, String documentTitle, long secret,
                            List<Long> secrets, int block, int allowComment, int allowNotice,
                            long memberSrl, String userId, String userName, String nickName);

    /**
     * 특정 태그를 포함하고 있는 게시물 카운트를 구한다.
     * 내부에서 this.countDocumentOfTag(long, List, long, List, ...)를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_srl, category_srl, document_title,
     *                     secret, block, allow_comment, allow_notice, member_srl, user_id,
     *                     user_name, nick_name 에 대해서 지원 한다.
     * @param tagSrls 검색할 태그 시리얼 넘버 리스트
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @return 조건에 맞는 게시물 카운트
     */
    long countDocumentOfTag(Map<String, String> searchFilter, List<Long> tagSrls, List<Long> secrets);

    /**
     * 특정 태그를 포함하고 있는 게시물 리스트를 구한다.
     *
     * @param tagSrl 태그 시리얼 넘버
     * @param tagSrls 태그 시리얼 넘버 리스트
     * @param documentSrl 게시물 시리얼 넘버
     * @param documentSrls 게시물 시리얼 넘버 리스트
     * @param appSrl 앱 시리얼 넘버
     * @param boardSrl 게시판 시리얼 넘버
     * @param boardSrls 게시판 시리얼 넘버 리스트
     * @param categorySrl 카테고리 시리얼 넘버
     * @param categorySrls 카테고리 시리얼 넘버 리스트
     * @param documentTitle 게시물 제목. 첫글자 부터 like 검색
     * @param secret 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 중 하나
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param block 차단 여부
     * @param allowComment 댓글 허용 여부
     * @param allowNotice 공지사항 여부
     * @param memberSrl 게시물 작성자 시리얼 넘버
     * @param userId 게시물 작성자 유저 아이디
     * @param userName 게시물 작성자 이름
     * @param nickName 게시물 작성자 별명
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 게시물 리스트
     */
    List<DocumentEntity> getDocumentOfTag(long tagSrl, List<Long> tagSrls, long documentSrl, List<Long> documentSrls,
                                          int appSrl, long boardSrl, List<Long> boardSrls, long categorySrl,
                                          List<Long> categorySrls, String documentTitle, long secret,
                                          List<Long> secrets, int block, int allowComment, int allowNotice,
                                          long memberSrl, String userId, String userName, String nickName,
                                          Map<String, String> sort, int offset, int limit);

    /**
     * 특정 태그를 포함하고 있는 게시물 리스트를 구한다.
     * 내부에서 this.getDocumentOfTag(Map, List, List, Map, int, int)를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, board_srl, category_srl, document_title,
     *                     secret, block, allow_comment, allow_notice, member_srl, user_id,
     *                     user_name, nick_name 에 대해서 지원 한다.
     * @param tagSrls 검색할 태그 시리얼 넘버 리스트
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 게시물 리스트
     */
    List<DocumentEntity> getDocumentOfTag(Map<String, String> searchFilter, List<Long> tagSrls, List<Long> secrets,
                                          Map<String, String> sort, int offset, int limit);

    /**
     * 특정 태그를 이용하여 링크 게시물 리스트 카운트를 구한다. 링크 게시물은 링크 하고 있는 일반 게시물과 연결(join)되어 있다.
     *
     * @param tagSrl 게시물이 포함하고 있는 태그 시리얼 넘버
     * @param tagSrls 태그 시리얼 넘버 리스트
     * @param documentSrl 게시물 시리얼 넘버
     * @param documentSrls 게시물 시리얼 넘버 리스트
     * @param appSrl 앱 시리얼 넘버
     * @param boardSrl 게시판 시리얼 넘버
     * @param boardSrls 게시판 시리얼 넘버 리스트
     * @param categorySrl 카테고리 시리얼 넘버
     * @param categorySrls 카테고리 시리얼 넘버 리스트
     * @param documentTitle 게시물 제목. 첫글자 부터 like 검색
     * @param secret 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 중 하나
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param block 차단 여부
     * @param allowComment 댓글 허용 여부
     * @param allowNotice 공지사항 여부
     * @param memberSrl 게시물 작성자 시리얼 넘버
     * @param userId 게시물 작성자 유저 아이디
     * @param userName 게시물 작성자 이름
     * @param nickName 게시물 작성자 별명
     * @return 링크 게시물 카운트
     */
    long countDocumentLinkOfTag(long tagSrl, List<Long> tagSrls, long documentSrl, List<Long> documentSrls,
                                int appSrl, long boardSrl, List<Long> boardSrls, long categorySrl,
                                List<Long> categorySrls, String documentTitle, long secret, List<Long> secrets,
                                int block, int allowComment, int allowNotice, long memberSrl, String userId,
                                String userName, String nickName);

    /**
     * 특정 태그를 이용하여 링크 게시물 리스트 카운트를 구한다. 링크 게시물은 링크 하고 있는 일반 게시물과 연결(join)되어 있다.
     * 내부에서 this.countDocumentLinkOfTag(long, List, long, List, ...)를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. tag_srl, app_srl, board_srl, category_srl, document_title,
     *                     secret, block, allow_comment, allow_notice, member_srl, user_id,
     *                     user_name, nick_name 에 대해서 지원 한다.
     * @param tagSrls 검색할 태그 시리얼 넘버 리스트
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @return 링크 게시물 카운트
     */
    long countDocumentLinkOfTag(Map<String, String> searchFilter, List<Long> tagSrls, List<Long> secrets);

    /**
     * 특정 태그를 이용하여 링크 게시물 리스트 리스트를 구한다. 링크 게시물은 링크 하고 있는 일반 게시물과 연결(join)되어 있다.
     *
     * @param tagSrl 게시물이 포함하고 있는 태그 시리얼 넘버
     * @param tagSrls 태그 시리얼 넘버 리스트
     * @param documentSrl 게시물 시리얼 넘버
     * @param documentSrls 게시물 시리얼 넘버 리스트
     * @param appSrl 앱 시리얼 넘버
     * @param boardSrl 게시판 시리얼 넘버
     * @param boardSrls 게시판 시리얼 넘버 리스트
     * @param categorySrl 카테고리 시리얼 넘버
     * @param categorySrls 카테고리 시리얼 넘버 리스트
     * @param documentTitle 게시물 제목. 첫글자 부터 like 검색
     * @param secret 게시물의 비밀글 여부. -1, -2, 사용자 시리얼 넘버 중 하나
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param block 차단 여부
     * @param allowComment 댓글 허용 여부
     * @param allowNotice 공지사항 여부
     * @param memberSrl 게시물 작성자 시리얼 넘버
     * @param userId 게시물 작성자 유저 아이디
     * @param userName 게시물 작성자 이름
     * @param nickName 게시물 작성자 별명
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 링크 게시물 리스트
     */
    List<DocumentLinkEntity> getDocumentLinkOfTag(long tagSrl, List<Long> tagSrls, long documentSrl,
                                                  List<Long> documentSrls, int appSrl, long boardSrl,
                                                  List<Long> boardSrls, long categorySrl, List<Long> categorySrls,
                                                  String documentTitle, long secret, List<Long> secrets,
                                                  int block, int allowComment, int allowNotice, long memberSrl,
                                                  String userId, String userName, String nickName,
                                                  Map<String, String> sort, int offset, int limit);

    /**
     * 특정 태그를 이용하여 링크 게시물 리스트 리스트를 구한다. 링크 게시물은 링크 하고 있는 일반 게시물과 연결(join)되어 있다.
     * 내부에서 this.getDocumentLinkOfTag(long, List, long, ...)을 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. tag_srl, app_srl, board_srl, category_srl, document_title,
     *                     secret, block, allow_comment, allow_notice, member_srl, user_id,
     *                     user_name, nick_name 에 대해서 지원 한다.
     * @param tagSrls 검색할 태그 시리얼 넘버 리스트
     * @param secrets 비밀글 관련 자세하게 카운팅 하기 위한 비밀글 값.
     *                1. 공개 게시판에서 비밀글만 카운트 : -2
     *                2. 공개 게시판에서 공개글만 카운트 : -1
     *                3. 공개 게시판에서 모든글 카운트 : -2, -1
     *                4. 비공개 게시판에서 비밀글만 카운트 : member_srl
     *                5. 비공개 게시판에서 공개글만 카운트 : -1
     *                6. 비공개 게시판에서 모든글 카운트 : -2, member_srl
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 링크 게시물 리스트
     */
    List<DocumentLinkEntity> getDocumentLinkOfTag(Map<String, String> searchFilter, List<Long> tagSrls,
                                                  List<Long> secrets, Map<String, String> sort, int offset, int limit);


    /**
     * 게시물 선택시 조회수 카운트
     *
     * @param documentSrl
     * @return 게시물 srl
     */
    void getReadCount(long documentSrl);

}
