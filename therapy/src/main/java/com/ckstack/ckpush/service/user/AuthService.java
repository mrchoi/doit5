package com.ckstack.ckpush.service.user;

import com.ckstack.ckpush.domain.accessory.DeviceAccessTokenEntity;
import com.ckstack.ckpush.domain.user.MemberAccessTokenEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.apache.ibatis.ognl.MemberAccess;

/**
 * Created by dhkim94 on 15. 7. 13..
 */
public interface AuthService {
    /**
     * 앱에 매핑된 단말기에 대해서 access_token을 발급 한다.
     * 가입 절차를 거치지 않고 단말기로 구분 할때 사용되는 access_token을 발급 하는 것임.
     *
     * 1. 발급된 access token이 없다면 신규 발급
     * 2. 발급된 access token이 존재하고 expire 되지 않았다면 그대로 사용. access_token 유지, expire 시간 업데이트
     * 3. 발급된 access token이 존재하고 expire 되었다면 access_token 변경, expire 시간 업데이트
     *
     * @param deviceSrl 등록된 단말기의 시리얼 넘버
     * @param appSrl 단말기가 매핑된 앱 시리얼 넘버
     * @return access token 정보
     */
    DeviceAccessTokenEntity createAccessTokenUsingDevice(long deviceSrl, int appSrl);

    /**
     * access token 정보를 구한다. 만료된 access token 은 없는 것으로 취급 된다.
     *
     * @param accessToken 정보를 구할 access token
     * @return 만일 존재하면 access token 정보를 리턴하고 존재하지 않으면 null을 리턴한다.
     */
    DeviceAccessTokenEntity getAccessTokenUsingDevice(String accessToken);

    /**
     * access token 의 만료 시간을 연장 한다.
     *
     * @param tokenSrl access token 시리얼 넘버
     * @return 연장된 만료 시간(초). 현재 시간 부터 연장된 시간 간격임. 즉 1시간 연장 되었다면 3600 을 리턴한다.
     */
    int renewalAccessTokenExpireUsingDevice(long tokenSrl);

    /**
     * 가입형 서비스 일때 사용자의 access_token을 발급 한다.
     *
     *
     * 1. 사용자, 앱에 맞게 발급된 access token이 없다면 신규 발급
     * 2. 사용자, 앱에 맞게 발급된 access token이 존재하고 expire 되지 않았다면 그대로 사용. access_token 유지, expire 시간 업데이트
     * 3. 사용자, 앱에 맞게 발급된 access token이 존재하고 expire 되었다면 access_token 변경, expire 시간 업데이트
     *
     * @param memberEntity 접속 토큰을 발급하려는 사용자 정보
     * @param memberSrl access_token을 발급 하려는 사용자 시리얼 넘버.
     *                  memberEntity 대신에 memberSrl을 사용하면 내부에서 memberEntity를 구한다.
     * @param appSrl access_token을 발급 하려는 앱 시리얼 넘버
     * @return 발급이나 갱신된 access token 정보
     */
    MemberAccessTokenEntity createAccessTokenUsingMember(MemberEntity memberEntity,
                                                         long memberSrl, int appSrl);

    /**
     * access token 정보를 구한다. 만료된 access token은 없는 것으로 취급 된다.
     *
     * @param accessToken 정보를 구할 access token
     * @return 만일 존재하면 access token 정보를 리턴하고 존재하지 않으면 null을 리턴한다.
     */
    MemberAccessTokenEntity getAccessTokenUsingMember(String accessToken);

    /**
     * access token의 만료 기간을 연장 한다.
     *
     * @param accessToken 만료 기간을 연장 할 access token
     * @param ltm access_token 연장시 사용되는 현재 시간 기준
     * @return 연장된 만료 시간(초). 현재 시간 부터 연장된 시간 간격임. 즉 1시간 연장 되었다면 3600 을 리턴한다.
     */
    int renewalAccessTokenExpireUsingMember(String accessToken, int ltm);
}
