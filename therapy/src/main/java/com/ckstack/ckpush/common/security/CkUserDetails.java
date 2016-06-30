package com.ckstack.ckpush.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
public class CkUserDetails implements UserDetails {
    private long member_srl;
    private String user_id;
    private String email_address;
    private String user_password;
    private String user_name;
    private String nick_name;
    private List<GrantedAuthority> authorities;
    private boolean enabled;
    private boolean sign_out;
    private String profile_url;         // 프로필 이미지의 URL (domain 제외). 없으면 null. 외부 이미지 이면 http... 로 시작
    private int profile_width;          // 프로필 이미지의 너비. 이미지가 없으면 zero. 외부 이미지 이면 -1
    private int prfile_height;          // 프로필 이미지의 높이. 이미지가 없으면 zero. 외부 이미지 이면 -1
    private String profile_thumb_url;   // 프로필 이미지 썸네일의 URL (domain 제외). 없으면 null. 외부 이미지 이면 http... 로 시작
    private int profile_thumb_width;    // 프로필 이미지 썸네일의 너비. 이미지가 없으면 zero. 외부 이미지 이면 -1
    private int profile_thumb_height;   // 프로필 이미지 썸네일의 높이. 이미지가 없으면 zero. 외부 이미지 이면 -1

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user_password;
    }

    @Override
    public String getUsername() {
        return this.user_id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.sign_out;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public long getMember_srl() {
        return member_srl;
    }

    public void setMember_srl(long member_srl) {
        this.member_srl = member_srl;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSign_out() {
        return sign_out;
    }

    public void setSign_out(boolean sign_out) {
        this.sign_out = sign_out;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public int getProfile_width() {
        return profile_width;
    }

    public void setProfile_width(int profile_width) {
        this.profile_width = profile_width;
    }

    public int getPrfile_height() {
        return prfile_height;
    }

    public void setPrfile_height(int prfile_height) {
        this.prfile_height = prfile_height;
    }

    public String getProfile_thumb_url() {
        return profile_thumb_url;
    }

    public void setProfile_thumb_url(String profile_thumb_url) {
        this.profile_thumb_url = profile_thumb_url;
    }

    public int getProfile_thumb_width() {
        return profile_thumb_width;
    }

    public void setProfile_thumb_width(int profile_thumb_width) {
        this.profile_thumb_width = profile_thumb_width;
    }

    public int getProfile_thumb_height() {
        return profile_thumb_height;
    }

    public void setProfile_thumb_height(int profile_thumb_height) {
        this.profile_thumb_height = profile_thumb_height;
    }
}
