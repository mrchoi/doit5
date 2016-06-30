package com.ckstack.ckpush.message;

import com.ckstack.ckpush.data.cache.AccessTokenExtra;

import java.util.List;

/**
 * Created by dhkim94 on 15. 8. 21..
 */
public interface CustomCache {
    /**
     * cache에 사용자의 access_token 정보를 저장한다.
     * 만일 존재하면 덮어 씌운다.
     *
     * @param memberSrl access_token 을 발급한 사용자 시리얼 넘버
     * @param appSrl access_token을 발급한 앱 시리얼 넘버
     * @param accessToken 저장할 access_token 값
     * @param accessTokenExtra access_token에 딸려 있는 정보
     * @param expire cache 만료 시간
     */
    void upsertMemberAccessToken(long memberSrl, int appSrl, String accessToken,
                                 AccessTokenExtra accessTokenExtra, int expire);

    /**
     * cache에서 사용자의 access_token 정보를 읽는다.
     * like 검색 해서 여러 개라면 expire 시간이 가장 늦은 것을 리턴 하고 나머지는 삭제 한다.
     *
     * @param memberSrl access_token을 검색할 사용자 시리얼 넘버
     * @param appSrl access_token을 검색할 앱 시리얼 넘버
     * @return access_token 정보. 존재하지 않으면 null을 리턴한다.
     */
    AccessTokenExtra getMemberAccessToken(long memberSrl, int appSrl);

    /**
     * cache에서 사용자의 access_token 정보를 읽는다.
     * like 검색 해서 여러 개라면 expire 시간이 가장 늦은 것을 리턴 하고 나머지는 삭제 한다.
     *
     * @param accessToken 검색할 access_token
     * @return access_token 정보. 존재하지 않으면 null을 리턴한다.
     */
    AccessTokenExtra getMemberAccessToken(String accessToken);

    /**
     * access_token 사용자의 enabled 값을 변경 시킨다.
     *
     * @param memberSrl 상태를 변경 시킬 사용자의 시리얼 넘버
     * @param state 상태를 변경할 값
     */
    void modifyMemberEnabledOfMemberAccessToken(long memberSrl, int state);

    /**
     * cache에서 사용자의 모든 토큰을 삭제 한다.
     *
     * @param memberSrl 토큰을 삭제할 사용자 시리얼 넘버
     * @param appSrl 토큰을 삭제할 앱 시리얼 넘버
     */
    void deleteMemberAccessToken(long memberSrl, int appSrl);

    /**
     * cache에서 앱 사용자의 접속 토큰을 삭제 한다.
     *
     * @param memberSrl 접속 토큰 삭제할 사용자 시리얼 넘버
     * @param appSrl 접속 토큰 삭제할 사용자 앱 시리얼 넘버
     * @param accessToken 삭제할 접속 토큰
     */
    void deleteMemberAccessToken(long memberSrl, int appSrl, String accessToken);
}
