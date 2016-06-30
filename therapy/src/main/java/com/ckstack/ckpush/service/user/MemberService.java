package com.ckstack.ckpush.service.user;

import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.domain.user.*;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 3. 27..
 */
public interface MemberService {
    /**
     * 사용자 정보를 구한다. 다음을 포함 한다.
     * 1. 사용자 기본 정보
     * 2. 사용자 확장 정보 ( memberExtraEntity )
     * 3. 사용자 프로필 이미지 정보 ( fileEntities )
     *
     * @param memberSrl 유저 시리얼 넘버
     * @return 사용자 정보를 리턴 한다. 사용자 정보를 구하지 못하면 null 을 리턴한다.
     */
    MemberEntity getMemberInfo(long memberSrl);

    /**
     * 사용자 정보를 구한다.
     * 1. 사용자 기본 정보
     * 2. 사용자 확장 정보 ( memberExtraEntity )
     * 3. 사용자 프로필 이미지 정보 ( fileEntities )
     *
     * @param userId 유저 아이디
     * @return 사용자 정보를 리턴 한다. 사용자 정보를 구하지 못하면 null 을 리턴한다.
     */
    MemberEntity getMemberInfo(String userId);

    /**
     * 사용자 정보를 구한다.
     * 1. 사용자 기본 정보
     * 2. 사용자 확장 정보 ( memberExtraEntity )
     * 3. 사용자 프로필 이미지 정보 ( fileEntities )
     *
     * @param nickName 유저 닉네임
     * @return 사용자 정보를 리턴 한다. 사용자 정보를 구하지 못하면 null 을 리턴한다.
     */
    long getMemberInfoNick(String nickName);

    /**
     * 유저 아이디로 유저 리스트를 구한다. 사용자 아이디 첫글자를 기준으로 like 검색 되는 리스트가 구해 진다.
     * 유저의 full 정보를 가져온다.(member, member_extra, 프로필 이미지 포함)
     * user_id asc 된 리스트를 가져 온다.
     *
     * @param userId   사용자 아이디. 첫글자 기준으로 like 검색 됨
     * @param userName 사용자 이름. 첫글자 기준으로 like 검색 됨
     * @param enabled  사용자 상태. 1:활성, 2:일시 중단, 3:차단
     * @param signOut  탈퇴 여부. 1:탈퇴, 2:사용중
     * @param offset   list offset
     * @param limit    list limit
     * @return 사용자 리스트
     */
    List<MemberEntity> getMemberList(String userId, String userName, int enabled, int signOut,
                                     Map<String, String> sort, int offset, int limit);

    /**
     * 유저 리스트를 구한다.
     * 내부에서 this.MemberList(String, String, int, int, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. user_id, user_name, enabled, sign_out 에 대해서는 지원 한다.
     * @param sort         리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *                     - column : ordering 할 칼럼명
     *                     - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset       리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit        리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 사용자 리스트
     */
    List<MemberEntity> getMemberList(Map<String, String> searchFilter, Map<String, String> sort,
                                     int offset, int limit);

    /**
     * 유저 카운트를 구한다. 사용자 아이디 첫글자를 기준으로 like 검색 하여 카운트를 구한다.
     * 유저의 full 정보에서 카운트 한다.(member, member_extra join 에서 카운트 함)
     *
     * @param userId   사용자 아이디. 첫글자 기준으로 like 검색 됨
     * @param userName 사용자 이름. 첫글자 기준으로 like 검색 됨
     * @param enabled  사용자 상태. 1:활성, 2:일시 중단, 3:차단
     * @param signOut  탈퇴 여부. 1:탈퇴, 2:탈퇴 아님
     * @return 사용자 카운트
     */
    long countMemberInfo(String userId, String userName, int enabled, int signOut);

    /**
     * 유저 카운트를 구한다.
     * 내부에서 this.countMemberInfo(String, String, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. user_id, user_name, enabled, sign_out 에 대해서는 지원 한다.
     * @return 조건에 맞는 사용자 카운트
     */
    long countMemberInfo(Map<String, String> searchFilter);

    /**
     * 사용자의 상태(enabled) 를 변경 한다.
     *
     * @param userId 유저 아이디
     * @param state  변경할 상태. 1이면 활성화, 2이면 비활성화, 3이면 차단
     */
    void enabledMember(String userId, int state);

    /**
     * 유저의 프로필 이미지를 추가 한다. 프로필 이미지는 무조건 한장으로 고정 한다.
     *
     * @param memberSrl 유저 시리얼 넘버
     * @param fileSrl   파일 시리얼 넘버
     * @return 등록한 파일의 정보
     */
    FileEntity addMemberProfileImage(long memberSrl, long fileSrl);

    /**
     * 사용자의 단말을 등록 시킨다.
     *
     * @param memberDeviceEntity 등록할 사용자 단말 정보
     */
    void addMemberDevice(MemberDeviceEntity memberDeviceEntity);

    /**
     * 사용자를 탈퇴 시킨다. 사용자 DB를 삭제 하지 않고 탈퇴 flag만 바꾼다.
     *
     * @param userId 유저 아이디
     */
    void signOutMember(String userId);

    /**
     * 사용자들을 삭제 한다. signOutMember는 탈퇴 flag만 바꾸는 것이고,
     * deleteMember는 사용자를 DB에서 완전 삭제 하는 것이다.
     * 사용자 삭제하고 만일 사용자가 사용하는 프로필 이미지가 있다면 프로필 이미지의 삭제 flag를 delete로 바꾼다.
     *
     * @param memberSrls 사용자 시리얼 넘버들
     */
    void deleteMember(List<Long> memberSrls);

    /**
     * 사용자를 추가 한다.
     *
     * @param memberEntity 추가할 유저 정보
     * @param fileSrl      유저 프로필용 이미지 시리얼 넘버
     * @param fileURL      유저 프로필용 이미지를 외부 이미지를 사용할때 외부 이미지의 URL
     * @param groupSrl     유저가 가입할 그룹 시리얼 넘버. 만일 NUSE 값이면 기본 그룹에 가입 시킨다.
     * @param tryMemberSrl 유저 추가를 시도한 유저의 시리얼 넘버. 만일 NUSE 값이면 가입한 사람이 스스로 추가한 것으로 판단 한다.
     * @param ip           유저 추가를 시도한 ipaddress
     */
    void signUp(MemberEntity memberEntity, long fileSrl, String fileURL, int groupSrl,
                long tryMemberSrl, String ip);

    /**
     * 사용자가 가입되어 있는 그룹 리스트를 구한다. 사용자는 1개 이상의 그룹에 가입될 수 있기 때문에 리스트를 리턴한다.
     *
     * @param memberSrl 유저 시리얼 넘버
     * @return 사용자가 가입되어 있는 그룹 리스트
     */
    List<GroupEntity> getMemberGroup(long memberSrl);

    /**
     * 유저를 특정 그룹에 가입 시킨다.
     *
     * @param groupSrl 가입 시킬 그룹 시리얼 넘버. MDV.NUSE 라면 기본 그룹에 가입 시킨다.
     */
    void joinMemberGroup(long memberSrl, int groupSrl);

    /**
     * 그룹 권한으로 backoffice 계정의 그룹 정보를 구한다.
     *
     * @param authority 그룹 권한
     * @return 그룹 정보. 권한에 그룹이 존재하지 않으면 null 을 리턴한다.
     */
    GroupEntity getGroup(String authority);

    /**
     * 사용자가 가입되어 있는 앱 정보와 앱 그룹 정보를 구한다.
     *
     * @param memberSrl 앱 정보와 앱 그룹 정보를 구할 사용자 시리얼 넘버
     * @return 여러 군데 앱에 가입되어 있을 수 있으므로 list 형태이며, 내부에
     * app_info, app_group_info 두 개의 요소를 가지는 map 리스트 이다.
     */
    List<Map<String, Object>> getUserAppGroup(long memberSrl);

    /**
     * 사용자 정보를 수정 한다.
     *
     * @param memberSrl   사용할 사용자 시리얼 넘버
     * @param modifyValue 수정 할 값
     */
    void modifyMember(int memberSrl, Map<String, String> modifyValue);

    /**
     * admin에서 사용자 extra 정보를 수정 한다.
     *
     * @param memberSrl   사용할 사용자 시리얼 넘버
     * @param modifyValue 수정 할 값
     */
    void modifyMemberExtra(int memberSrl, Map<String, String> modifyValue);

    /**
     * 사용자 Extra 정보를 수정 한다.
     *
     * @param memberExtraEntity 수정 할 Extra 값
     */
    void modifyExtraMember(MemberExtraEntity memberExtraEntity);

    /**
     * 사용자 알림 정보를 수정 한다. (plymind 전용)
     *
     * @param memberSrl 사용자 시리얼 넘버
     * @param allowCall 알림
     */
    void modifyMemberNotification(long memberSrl, int allowCall);

    /**
     * 그룹별 유저 리스트를 구한다.
     *
     * @param group_srl 그룹 시리얼 넘버
     * @return 사용자 리스트
     */
    long countMemberByGroup(long group_srl, Map<String, String> searchFilter);

    long countMemberByGroups(long group_srl, List<Long> group_srls, Map<String, String> searchFilter);

    /**
     * 그룹별 유저 리스트를 구한다.
     *
     * @param group_srl 그룹 시리얼 넘버
     * @return 사용자 리스트
     */
    List<MemberEntity> getMemberListByGroup(long group_srl, Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit);

    List<MemberEntity> getMemberListByGroups(long group_srl, List<Long> group_srls, Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit);

    /**
     * user_name과 email_address로 user_id를 가져온다.
     *
     * @param userName     user_name 조건
     * @param emailAddress email_address 조건
     * @return 사용 리스트
     */
    List<MemberEntity> searchUserId(String userName, String emailAddress);

    /**
     * 상담사 아이디로 상담사 리스트를 구한다. 사용자 아이디 첫글자를 기준으로 like 검색 되는 리스트가 구해 진다.
     * 상담사의 full 정보를 가져온다.(member, member_extra, 프로필 이미지 포함)
     * user_id asc 된 리스트를 가져 온다.
     *
     * @param userId   상담사 아이디. 첫글자 기준으로 like 검색 됨
     * @param userName 상담사 이름. 첫글자 기준으로 like 검색 됨
     * @param nickName 상담사 별명. like 검색 됨
     * @param enabled  사용자 상태. 1:활성, 2:일시 중단, 3:차단
     * @param signOut  탈퇴 여부. 1:탈퇴, 2:사용중
     * @param groupSrl  group srl
     * @param offset   list offset
     * @param limit    list limit
     * @return 사용자 리스트
     */
    List<MemberEntity> getGroupMemberList(String userId, String userName, String nickName, int enabled, int signOut, int groupSrl,
                                            Map<String, String> sort, int offset, int limit);

    /**
     * 상담사 리스트를 구한다.
     * 내부에서 this.getGroupMemberList(String, String, String, int, int, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. user_id, user_name, enabled, sign_out 에 대해서는 지원 한다.
     * @param sort         리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *                     - column : ordering 할 칼럼명
     *                     - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset       리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit        리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 사용자 리스트
     */
    List<MemberEntity> getGroupMemberList(Map<String, String> searchFilter, Map<String, String> sort, int offset, int limit);

    /**
     * 상담사 카운트를 구한다. 사용자 아이디 첫글자를 기준으로 like 검색 하여 카운트를 구한다.
     * 상담사의 full 정보에서 카운트 한다.(member, member_extra join 에서 카운트 함)
     *
     * @param userId   상담사 아이디. 첫글자 기준으로 like 검색 됨
     * @param userName 상담사 이름. 첫글자 기준으로 like 검색 됨
     * @param enabled  사용자 상태. 1:활성, 2:일시 중단, 3:차단
     * @param signOut  탈퇴 여부. 1:탈퇴, 2:탈퇴 아님
     * @param groupSrl group srl
     * @return 사용자 카운트
     */
    long countGroupMemberInfo(String userId, String userName, int enabled, int signOut, int groupSrl);

    /**
     * 상담사 카운트를 구한다.
     * 내부에서 this.countMemberInfo(String, String, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. user_id, user_name, enabled, sign_out 에 대해서는 지원 한다.
     * @return 조건에 맞는 사용자 카운트
     */
    long countGroupMemberInfo(Map<String, String> searchFilter);

    /**
     * 사용자 정보를 구한다. 다음을 포함 한다.
     * 1. 사용자 기본 정보
     * 2. 사용자 확장 정보 ( memberExtraEntity )
     *
     * @param memberSrl 유저 시리얼 넘버
     * @return 사용자 정보를 리턴 한다. 사용자 정보를 구하지 못하면 null 을 리턴한다.
     */
    MemberExtraEntity getMemberextraInfo(long memberSrl);

    /**
     * 패스워드 matches
     *
     * @param rawPassword 입력받은 패스워드
     * @param encodedPassword 기존패스워드
     * @return encode password를 리턴 한다. 사용자 정보를 구하지 못하면 null 을 리턴한다.
     */
    boolean getmatches(CharSequence rawPassword, String encodedPassword);

    /**
     * 주소 리스트를 구한다. like 검색 되는 리스트가 구해 진다.
     * user_id asc 된 리스트를 가져 온다.
     *
     * @return 사용자 리스트
     */
    List<ZipCodeEntity> getZipcodeList(String query);

    List<ZipCodeEntity> getZipcodejibunList(String query);

    Map<String, String> getAdvisorAuthority();
}
