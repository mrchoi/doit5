package com.ckstack.ckpush.service.app;

import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 4. 11..
 */
public interface AppService {
    /**
     * 존재하고 있는 앱 인지 체크 한다.
     *
     * @param apiKey 중복 체크할 api_key
     * @return 이미 존재하면 true, 존재 하지 않으면 false 를 리턴한다.
     */
    boolean isDuplicate(String apiKey);

    /**
     * 신규 앱을 등록 한다.
     *
     * @param memberSrl 앱을 등록 하는 유저 시리얼 넘버
     * @param appEntity 등록할 앱 정보
     * @param tryUserId 앱을 등록 하는 유저 아이디. 만일 null 이면 memberSrl 에서 구한다.
     * @param ip 앱을 등록하는 ip address
     */
    void createApp(long memberSrl, AppEntity appEntity, String tryUserId, String ip);

    /**
     * api_key 를 이용하여 app 정보를 확인 한다.
     *
     * @param apiKey 앱의 api_key
     * @return 확인된 appEntity. 만일 앱이 없다면 null 을 리턴한다.
     */
    AppEntity getAppInfo(String apiKey);

    /**
     * app_srl 을 이용하여 app 정보를 확인 한다.
     *
     * @param appSrl 앱의 시리얼 넘버
     * @return 확인된 appEntity. 만일 앱이 없다면 null 을 리턴한다.
     */
    AppEntity getAppInfo(int appSrl);

    /**
     * 등록된 app 의 개수를 구한다.
     *
     * @param appName 앱 이름. 만일 empty string 이거나 trim 을 했을때 empty string 이라면 null 로 취급 한다.
     * @param enabled 앱 상태. 1:활성, 2:일시중단, 3:차단
     * @return 조건에 맞는 앱 카운트
     */
    long getAppCount(String appName, int enabled);

    /**
     * 등록된 app 의 개수를 구한다.
     * 내부에서 this.getAppCount(String, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_name, enabled 에 대해서는 지원 한다.
     * @return 조건에 맞는 앱 카운트
     */
    long getAppCount(Map<String, String> searchFilter);

    /**
     * 등록된 app 리스트를 가져 온다.
     *
     * @param appSrls app_srl 의 list
     * @param enabled 앱 활성화 여부
     * @param offset list offset
     * @param limit list limit
     * @return 앱 리스트
     */
    Map<Integer, AppEntity> getAppInfo(List<Integer> appSrls, int enabled, int offset, int limit);

    /**
     * 등록된 app 리스트를 가져 온다.
     *
     * @param appName 앱 이름. 만일 empty string 이거나 trim 을 했을때 empty string 이라면 null 로 취급 한다.
     * @param enabled 앱 상태. 1:활성, 2:일시중단, 3:차단
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 앱 리스트
     */
    List<AppEntity> getAppInfo(String appName, int enabled,
                               Map<String, String> sort, int offset, int limit);

    /**
     * 등록된 app 리스트를 가져 온다.
     * 내부에서 this.getAppInfo(String, int, int, Map, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_name, enabled 에 대해서는 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 앱 리스트
     */
    List<AppEntity> getAppInfo(Map<String, String> searchFilter, Map<String, String> sort,
                               int offset, int limit);

    /**
     * 앱을 삭제 한다.
     * @param appSrls 삭제 할 앱의 시리얼 넘버 리스트. null 이거나 size가 0이면 custom exception 발생 한다.
     * @param tryUserId 앱을 삭제 시도하는 유저 아이디
     * @param tryMemberSrl 앱을 삭제 시도하는 유저 시리얼 넘버
     * @param ip 앱을 삭제 시도하는 아이피
     * @return 삭제된 앱 정보. {app_srl : app_name} 형태로 되어 있는 hashmap
     */
    Map<Integer, String> deleteApp(List<Integer> appSrls, String tryUserId, long tryMemberSrl, String ip);

    /**
     * 앱 정보를 수정 한다.
     * modifyValue 에는 다음 값이 사용 가능 하다.
     *
     * - app_name       : 변경할 앱 이름
     * - app_version    : 변경할 앱 버전
     * - enabled        : 변경할 앱 상태
     *
     * @param appSrl 수정할 앱 시리얼 넘버
     * @param modifyValue 수정할 앱 정보를 가지고 있는 hashmap
     */
    void modifyApp(int appSrl, Map<String, String> modifyValue);

    /**
     * 앱 관리자 정보를 구한다.(MemberExtraEntity를 유효하게 포함하고 있음)
     *
     * @param appSrl 앱 시리얼 넘버
     * @return 사용자 리스트
     */
    List<MemberEntity> getAppManager(int appSrl);

    /**
     * 앱 관리자를 변경 한다.
     *
     * @param appSrl 관리자를 변경할 앱 시리얼 넘버
     * @param userIds 관리자 아이디가 들어 있는 array list. userIds 에 포함되어 있지 않는 기존 관리자는 삭제 된다.
     */
    void modifyAppManager(int appSrl, List<String> userIds);

    /**
     * 앱에 등록된 단말기 카운트를 구한다.
     *
     * @param appSrl 앱의 시리얼 넘버
     * @param appSrls 앱의 시리얼 넘버
     * @param deviceId 단말기 아이디. null, empty string 이 아니면 첫글자 부터 like 검색하는 조건이다.
     * @param deviceType 벤더, 모델명. null, empty string 이 아니면 첫글자 부터 like 검색하는 조건이다.
     * @param osVersion 단말기 os version
     * @param mobilePhoneNumber mobile phone number
     * @param deviceClass 단말기 종류.
     * @param existPushId 등록된 push id 가 존재 하는지 여부.
     * @param allowPush push 를 허용하는 단말기 인지 아닌지 여부
     * @param enabled 활성 단말기 인지 아닌지 여부
     * @return 조건에 맞는 앱에 등록된 안드로이드 단말기 카운트를 구한다
     */
    long getAppDeviceCount(int appSrl, List<Integer> appSrls, String deviceId, String deviceType, String osVersion,
                           String mobilePhoneNumber, int deviceClass, int existPushId, int allowPush, int enabled);

    /**
     * 앱에 등록된 단말기 카운트를 구한다.
     * 내부에서 this.getAppDeviceCount(int, String, int, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. 다음 값으로 검색 가능 하다.
     *                     - app_srl
     *                     - app_srls : app_srl 을 comma 로 분리한 string 문자열
     *                     - device_id : 첫글자 기준으로 like 검색 된다.
     *                     - device_type : 첫글자 기준으로 like 검색 된다.
     *                     - os_version : 첫글자 기준으로 like 검색 된다.
     *                     - mobile_phone_number : 첫글자 기준으로 like 검색 된다.
     *                     - device_class : 단말기 종류
     *                     - reg_push_id
     *                     - allow_push
     *                     - enabled
     * @return 조건에 맞는 앱에 등록된 안드로이드 단말기 카운트를 구한다
     */
    long getAppDeviceCount(Map<String, String> searchFilter);

    /**
     * 앱에 등록된 안드로이드 단말기 리스트를 구한다.
     *
     * @param appSrl 앱의 시리얼 넘버
     * @param appSrls 앱의 시리얼 넘버
     * @param deviceSrl 단말기 시리얼 넘버
     * @param deviceId 단말기 아이디. null, empty string 이 아니면 첫글자 부터 like 검색하는 조건이다.
     * @param deviceType 벤더, 모델명. null, empty string 이 아니면 첫글자 부터 like 검색하는 조건이다.
     * @param osVersion 단말기 os version
     * @param mobilePhoneNumber mobile phone number
     * @param deviceClass 단말기 종류
     * @param existPushId 등록된 push id 가 존재 하는지 여부.
     * @param allowPush push 를 허용하는 단말기 인지 아닌지 여부
     * @param enabled 활성 단말기 인지 아닌지 여부
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 앱에 등록된 안드로이드 단말기 리스트
     */
    List<DeviceEntity> getAppDevice(int appSrl, List<Integer> appSrls, long deviceSrl, String deviceId,
                                    String deviceType, String osVersion, String mobilePhoneNumber, int deviceClass,
                                    int existPushId, int allowPush, int enabled,
                                    Map<String, String> sort, int offset, int limit);

    /**
     * 앱에 등록된 안드로이드 단말기 리스트를 구한다.
     * 내부에서 this.getAppAndroidDevice(int, String, int, int, int, map, int, int) 를 호출 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. 다음 값으로 검색 가능 하다.
     *                     - app_srl
     *                     - app_srls : app_srl 을 comma 로 분리한 string 문자열
     *                     - device_id : 첫글자 기준으로 like 검색 된다.
     *                     - device_type : 첫글자 기준으로 like 검색 된다.
     *                     - os_version : 첫글자 기준으로 like 검색 된다.
     *                     - mobile_phone_number : 첫글자 기준으로 like 검색 된다.
     *                     - device_class : 단말기 종류
     *                     - reg_push_id
     *                     - allow_push
     *                     - enabled
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 앱에 등록된 안드로이드 단말기 리스트
     */
    List<DeviceEntity> getAppDevice(Map<String, String> searchFilter,
                                    Map<String, String> sort, int offset, int limit);

    /**
     * 앱에 단말을 등록 한다.
     *
     * @param deviceEntity 등록 할 단말 정보
     * @param appSrl 단말을 등록 할 앱
     * @param pushId 등록 할 단말의 GCM RID 나 Device token
     */
    void joinDeviceInApp(DeviceEntity deviceEntity, int appSrl, String pushId);

    /**
     * 앱에 매핑되어 있는 단말기를 삭제 한다.
     *
     * @param deleteData 삭제할 데이터. 다음 내용을 포함한 list 이다.
     *                   - device_srl
     *                   - app_srl
     */
    void deleteDeviceInApp(List<Map<String, Object>> deleteData);
}
