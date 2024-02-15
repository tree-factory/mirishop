package com.hh.mirishop.user.auth.domain;

import com.hh.mirishop.user.member.entity.Member;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Builder
public class UserDetailsImpl implements UserDetails {

    private final Long number;
    private final String email;
    private final String password;
    private final String nickname;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl from(Member member) {
        return UserDetailsImpl.builder()
                .number(member.getNumber())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .authorities(Set.of())   // 추후 role로 수정
                .build();
    }

    public Long getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
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
        return true;
    }
}
