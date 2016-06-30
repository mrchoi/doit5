package com.ckstack.ckpush.service.mineral;

import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
import com.ckstack.ckpush.domain.mineral.PushMessagePicEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 4. 30..
 */
public interface CkFileService {
    /**
     * 게시물에 첨부된 첨부 파일 리스트 맵을 구한다.
     *
     * @param documentSrls 첨부 파일 리스트 맵을 구할 게시물 시리얼 넘버 리스트
     * @return 첨부 파일 리스트 맵. 게시물 시리얼 넘버와 매핑된 첨부 파일 리스트임
     */
    Map<Long, List<DocumentAttachEntity>> getDocumentAttachFile(List<Long> documentSrls);

    DocumentAttachEntity getDocumentAttachFile(long fileSrls);

    /**
     * repository 파일 카운트를 구한다.
     *
     * @param origName 원본 파일명. 첫글자 부터 like 검색 된다.
     * @param deleted 파일의 삭제 여부
     * @return 조건에 맞는 이미지 카운트
     */
    long getFileCount(String origName, int deleted);

    long getFileCountPlymind(String origName, int deleted, int file_type, List<Integer> file_types);

    /**
     * repository 파일 카운트를 구한다.
     * 내부에서 this.getFileCount(String, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. orig_name, deleted 에 대해서는 지원 한다.
     * @return 조건에 맞는 이미지 카운트
     */
    long getFileCount(Map<String, String> searchFilter);

    long getFileCountPlymind(Map<String, String> searchFilter, List<Integer> file_types);

    /**
     * repository 파일 리스트를 구한다.
     *
     * @param origName 원본 파일명. 첫글자 부터 like 검색 된다.
     * @param deleted 파일의 삭제 여부
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 이미지 리스트
     */
    List<FileRepositoryEntity> getFile(String origName, int deleted,
                                       Map<String, String> sort, int offset, int limit);

   List<FileRepositoryEntity> getFilePlymind(String origName, int deleted, int file_type, List<Integer> file_types,
                                          Map<String, String> sort, int offset, int limit);

    /**
     * repository 파일 리스트를 구한다.
     * 내부에서 this.getFile(String, int, Map, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. orig_name, deleted 에 대해서는 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 이미지 리스트
     */
    List<FileRepositoryEntity> getFile(Map<String, String> searchFilter, Map<String, String> sort,
                                       int offset, int limit);
    /**
     *
     * */
    List<FileRepositoryEntity> getFilePlymind(Map<String, String> searchFilter, List<Integer> file_types, Map<String, String> sort,
                                       int offset, int limit);

    /**
     * 파일 정보를 구한다.
     *
     * @param fileSrl 파일 시리얼 넘버
     * @return 만일 존재하면 파일 정보를 없으면 null 을 리턴한다.
     */
    FileRepositoryEntity getFile(long fileSrl);

    /**
     * 파일(repository 파일) 정보를 수정 한다.
     *
     * @param fileRepositoryEntity 변경할 파일 정보
     */
    void modifyFile(FileRepositoryEntity fileRepositoryEntity);

    /**
     * 게시물 첨부용 파일을 저장 한다.
     *
     * @param multipartFile 업로드된 파일 정보
     * @param fileComment 파일 코맨트
     * @param memberSrl 파일 업로드 한 사용자 시리얼 넘버
     * @param requestIp 파일 업로드한 클라이언트의 아이피
     * @return 저장한 이미지 정보
     */
    DocumentAttachEntity saveFile(MultipartFile multipartFile, String fileComment,
                                  long memberSrl, String requestIp);

    /**
     * 외부 서버에서 server to server 로 byte array 로 가져온 파일을 저장 한다.
     *
     * @param fileURL 파일의 remote url
     * @param fileData file data
     * @param requestIp 파일 업로드한 클라이언트의 IP
     * @param isDelete 이미지 활성화 여부
     * @return 저장한 이미지 정보
     */
    FileRepositoryEntity saveFile(int fileType, String fileURL, byte[] fileData, String userId,
                                  String requestIp, boolean isDelete);

    /**
     * 용도가 지정되지 않은 파일을 저장한다. file 저장소를 제공해 주는데 사용되는 것임.
     *
     * @param multipartFile 저장할 파일 정보
     * @param requestIp 파일 업로드한 클라이언트의 IP
     * @param isDelete 이미지 활성화 여부
     * @return 저장한 이미지 정보
     */
    FileRepositoryEntity saveFile(MultipartFile multipartFile, String userId,
                                  String requestIp, boolean isDelete);

    /**
     * APNs, GCM 에서 사용하는 이미지 파일을 저장 한다.
     * 물리 파일을 저장하고 파일 정보를 database 에 기록 한다.
     *
     * @param multipartFile 저장할 이미지 파일 정보
     * @param requestIp 이미지 올린 클라이언트의 IP
     * @param isDelete 이미지 활성화 여부
     * @return 저장한 이미지 정보
     */
    FileEntity savePushImage(MultipartFile multipartFile, String requestIp, boolean isDelete);

    /**
     * 내부에 업로드 하지 않고 외부의 링크만 사용하는 APNs, GCM 에서 사용되는 이미지를 생성 한다.
     * 파일은 생성되지 않과 파일 관리 데이터만 생성 된다.
     *
     * @param fileURL 외부 이미지의 링크 URL
     * @param fileData 저장할 파일의 byte data
     * @param requestIp 이미지 올린 클라이언트의 IP
     * @param isDelete 활성 여부. true 이면 삭제로 취급
     * @return 파일 메타 데이터 정보
     */
    FileEntity savePushImage(String fileURL, byte[] fileData, String requestIp, boolean isDelete);

    /**
     * GCM, APNs 에서 사용하는 이미지 파일 카운트를 구한다.
     *
     * @param origName 원본 파일명. 첫글자 부터 like 검색 된다.
     * @param deleted 파일의 삭제 여부
     * @return 조건에 맞는 이미지 카운트
     */
    long getPushMessagePicCount(String origName, int deleted);

    /**
     * GCM, APNs 에서 사용하는 이미지 파일 카운트를 구한다.
     * 내부에서 this.getPushMessagePicCount(String, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. orig_name, deleted 에 대해서는 지원 한다.
     * @return 조건에 맞는 이미지 카운트
     */
    long getPushMessagePicCount(Map<String, String> searchFilter);

    /**
     * GCM, APNs 에서 사용하는 이미지 파일 리스트를 구한다.
     *
     * @param origName 원본 파일명. 첫글자 부터 like 검색 된다.
     * @param deleted 파일의 삭제 여부
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 Push 이미지 리스트. 다음 값이 들어 있는 map list를 리턴 한다.
     *         image    : FileEntiry object
     *         app      : AppEntity object list
     *         message  : PushMessageEntity object list
     */
    List<Map<String, Object>> getPushMessagePic(String origName, int deleted,
                                                Map<String, String> sort, int offset, int limit);

    /**
     * GCM, APNs 에서 사용하는 이미지 파일 리스트를 구한다.
     * 내부에서 this.getPushMessagePic(String, int, Map, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. orig_name, deleted 에 대해서는 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 Push 이미지 리스트. 다음 값이 들어 있는 map list를 리턴 한다.
     *         image    : FileEntiry object
     *         app      : AppEntity object list
     *         message  : PushMessageEntity object list
     */
    List<Map<String, Object>> getPushMessagePic(Map<String, String> searchFilter, Map<String, String> sort,
                                                int offset, int limit);

    /**
     * 저장되어 있는 이미지 파일을 특정 비율로 보여 준다.
     *
     * @param fileSrl Push 메시지용 파일의 시리얼 넘버
     * @param ratio 축소나 확대할 비율. 0.5 이면 반으로 축소. 1.5 이면 반 확대
     * @param fileType 파일의 종류. 1:리포티토리용 파일, 2:메시지 푸쉬용 파일, 3:게시물 첨부용 파일
     * @return ratio 적용된 출력할 이미지 byte array
     */
    byte[] getImageFileRatio(long fileSrl, double ratio, int fileType);

    /**
     * 저장되어 있는 이미지 파일을 특정 사이즈로 resize 한다.
     *
     * @param fileSrl Push 메시지용 파일의 시리얼 넘버
     * @param neoWidth resize 할 width
     * @param neoHeight resize 할 height
     * @param fileType 파일의 종류. 1:리포티토리용 파일, 2:메시지 푸쉬용 파일, 3:게시물 첨부용 파일
     * @return resize 적용된 출력할 이미지 byte array
     */
    byte[] getImageFileResize(long fileSrl, int neoWidth, int neoHeight, int fileType);

    /**
     * 저장되어 있는 push 메시지용 이미지 파일을 crop 한다.
     *
     * @param fileSrl Push 메시지용 파일의 시리얼 넘버
     * @param x crop 할 x 위치
     * @param y crop 할 y 위치
     * @param neoWidth crop 할 width
     * @param neoHeight crop 할 height
     * @param fileType 파일의 종류. 1:리포티토리용 파일, 2:메시지 푸쉬용 파일, 3:게시물 첨부용 파일
     * @return crop 적용된 출력할 이미지 byte array
     */
    byte[] getImageFileCrop(long fileSrl, int x, int y, int neoWidth, int neoHeight, int fileType);

    /**
     * 저장되어 있는 이미지 파일을 원본 사이즈 그대로 보여 준다.
     *
     * @param fileSrl Push 메시지용 파일의 시리얼 넘버
     * @param fileType 파일의 종류. 1:리포티토리용 파일, 2:메시지 푸쉬용 파일, 3:게시물 첨부용 파일
     * @return 출력할 이미지 byte array
     */
    byte[] getImageFile(long fileSrl, int fileType);

}
