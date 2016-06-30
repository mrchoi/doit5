package com.ckstack.ckpush.service.plymind;

import java.util.List;

/**
 * Created by root on 16. 2. 10.
 */
public interface SignupService {
    /**
     * 사용자 정보가 일치하는지 확인한다.
     *
     * @param userId 임시비밀번호를 전송할 사용자 아이디
     * @param userName 임시비밀번호를 전송할 사용자 이름
     * @param emailAddress 임시비밀번호를 전송할 메일 주소
     * @return member_srl 사용자 일련번호
     */
    long isCorrectMember(String userId, String userName, String emailAddress);
    /**
     * 임시 비밀번호를 생성한다.
     * 생성된 임시 비밀번호를 tbl_member 테이블에 update 한다.
     *
     * @return 임시비밀번호
     */
    String createTempPassword(long memberSrl);
    /**
     * 임시 비밀번호를 전송한다.
     *
     * @param userId 임시비밀번호를 전송할 사용자 아이디
     * @param userName 임시비밀번호를 전송할 사용자 이름
     * @param emailAddress 임시비밀번호를 전송할 메일 주소
     * @return Error Code ( 1 : 성공, 0 : 실패,   )
     */
    int sendTempPassword(String userId, String userName, String tempPassword, String emailAddress);
    /**
     * 사용자 아이디 찾기
     *
     * @param userName 임시비밀번호를 전송할 사용자 이름
     * @param emailAddress 임시비밀번호를 전송할 메일 주소
     * @return user_id 리스트
     */
    List<String> searchUserId(String userName, String emailAddress);
    /**
     * 사용자 아이디 이메일 받기
     *
     * @param userName 임시비밀번호를 전송할 사용자 이름
     * @param emailAddress 임시비밀번호를 전송할 메일 주소
     * @param userIds 사용자 아이디 목록
     * @return user_id 리스트
     */
    int emailUserId(String userName, String emailAddress, List<String> userIds);
}
