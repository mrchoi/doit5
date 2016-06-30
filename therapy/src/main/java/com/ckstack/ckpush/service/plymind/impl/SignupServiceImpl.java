package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.plymind.SignupService;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by kodaji on 16. 2. 10.
 */
@Service
public class SignupServiceImpl implements SignupService {
    private final static Logger LOG = LoggerFactory.getLogger(SignupServiceImpl.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private JavaMailSender javaMailSender;

    private String MD5(String str) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] buffer = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i=0 ; i<buffer.length; i++) {
                sb.append(Integer.toString((buffer[i]&0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();
        }catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(), e);
            MD5 = null;
        }
        return MD5;
    }

    @Override
    public long isCorrectMember(String userId, String userName, String emailAddress) {

        MemberEntity memberEntity = memberService.getMemberInfo(userId);

        // 아이디가 존재하지 않음
        if (memberEntity == null) return -700;
        if (memberEntity.getUser_id() == null) return -700;

        // 사용자 이름이 일치하지 않음.
        if (memberEntity.getUser_name() == null || userName == null) return -701;
        if (!memberEntity.getUser_name().equals(userName)) return -701;

        // 이메일이 일치하지 않음.
        if (memberEntity.getEmail_address() == null || emailAddress == null) return -702;
        if (!memberEntity.getEmail_address().equals(emailAddress)) return -702;

        return memberEntity.getMember_srl();
    }

    @Override
    public String createTempPassword(long memberSrl) {

        char[] password = new char[10];
        Random random = new Random();
        int i=0;

        while (true) {
            int integer = random.nextInt(93) + 33;
            if (integer == 34) continue; // "
            if (integer == 39) continue; // '
            if (integer == 92) continue; // \
            if (integer == 96) continue; // `

            password[i] = (char) integer;
            i++;
            if (i>=password.length) break;
        }

        String tempPassword = new String(password);
        String MD5 = this.MD5(tempPassword);

        Map<String, String> modifyValue = new HashMap<>();
        modifyValue.put("user_password", MD5);
        memberService.modifyMember((int)memberSrl, modifyValue);

        return tempPassword;
    }

    @Override
    public int sendTempPassword(String userId, String userName, String tempPassword, String emailAddress) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("[PlyMind] 임시비밀번호 발송");
        message.setText(userName + "(" + userId + ")" + "님의 비밀번호는 " + tempPassword + " 입니다.");
        message.setTo(emailAddress);
        message.setFrom("플리마인드<plymind@plymind.com>");

        javaMailSender.send(message);

        return 1;
    }

    @Override
    public List<String> searchUserId(String userName, String emailAddress) {

        List<MemberEntity> memberEntities = memberService.searchUserId(userName, emailAddress);

        List<String> list = new ArrayList<>();
        if (memberEntities == null || memberEntities.size() == 0) return list;

        for (MemberEntity memberEntity : memberEntities) {
            list.add(memberEntity.getUser_id());
        }

        return list;
    }

    @Override
    public int emailUserId(String userName, String emailAddress, List<String> userIds) {

        if (userIds == null || userIds.size() == 0) return 0;

        StringBuffer sb = new StringBuffer();
        for (int i=0 ; i<userIds.size() ; i++) {
            String userId = userIds.get(i);
            sb.append(userId);
            if (i<userIds.size()-1) sb.append(", ");
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("[PlyMind] 아이디 발송");
        message.setText(userName + "님의 아이디는 " + sb.toString() + " 입니다.");
        message.setTo(emailAddress);
        message.setFrom("플리마인드<plymind@plymind.com>");

        javaMailSender.send(message);

        return 1;
    }
}
