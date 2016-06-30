package com.ckstack.ckpush.common.security;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.service.user.MemberService;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.domain.user.GroupEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
public class CkUserDetailsService implements UserDetailsService {
    private final static Logger LOG = LoggerFactory.getLogger(CkUserDetailsService.class);

    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        LOG.info("try to login using user_id[" + userId + "]");

        MemberEntity memberEntity = memberService.getMemberInfo(userId);
        if(memberEntity == null) {
            LOG.info("failed login. not found user_id["+userId+"]");
            throw new UsernameNotFoundException("not found user_id["+userId+"]");
        }

        CkUserDetails userDetails = new CkUserDetails();
        userDetails.setMember_srl(memberEntity.getMember_srl());
        userDetails.setUser_id(memberEntity.getUser_id());
        userDetails.setUser_password(memberEntity.getUser_password());
        userDetails.setUser_name(memberEntity.getUser_name());
        userDetails.setNick_name(memberEntity.getNick_name());
        userDetails.setEmail_address(memberEntity.getEmail_address());

        // 유저 프로필 이미지 정보를 넣는다.(여러장 등록 되어 있더라도 첫번째 이미지만 사용 한다)
        if(memberEntity.getFileEntities() == null || memberEntity.getFileEntities().size() <= 0) {
            userDetails.setProfile_url(null);
            userDetails.setProfile_width(0);
            userDetails.setPrfile_height(0);
            userDetails.setProfile_thumb_url(null);
            userDetails.setProfile_thumb_width(0);
            userDetails.setProfile_thumb_height(0);
        } else {
            FileEntity fileEntity = memberEntity.getFileEntities().get(0);
            userDetails.setProfile_url(fileEntity.getFile_url());
            userDetails.setProfile_width(fileEntity.getWidth());
            userDetails.setPrfile_height(fileEntity.getHeight());
            userDetails.setProfile_thumb_url(fileEntity.getThumb_url());
            userDetails.setProfile_thumb_width(fileEntity.getThumb_width());
            userDetails.setProfile_thumb_height(fileEntity.getThumb_height());
        }

        List<GroupEntity> memberGroupList = null;
        try {
            memberGroupList = memberService.getMemberGroup(memberEntity.getMember_srl());
        } catch(Exception e) {
            LOG.error("failed login. user_id["+userId+"] has not any group");
            try { throw new AccessDeniedException("failed login. user_id["+userId+"] has not any group"); }
            catch (AccessDeniedException e1) { e1.printStackTrace(); }
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        assert memberGroupList != null;
        for(GroupEntity element : memberGroupList) authorities.add(new SimpleGrantedAuthority(element.getAuthority()));
        userDetails.setAuthorities(authorities);

        // disable, deny 인 경우에는 로그인 불가
        userDetails.setEnabled(memberEntity.getEnabled() == MDV.YES);
        userDetails.setSign_out(memberEntity.getSign_out() == MDV.YES);

        LOG.info("process login using user_id["+userId+"]");
        return userDetails;
    }
}
