package web.mvc.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.mvc.domain.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Slf4j
public class CustomMemberDetails implements UserDetails {
    private final Member member;
    public CustomMemberDetails(Member member) {
        this.member = member;
        log.info("CustomMemberDetails : {}", member);
    }

    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        log.info("getAuthorities.....");
//        Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();; //리스트 하나 만들어서 그 타입으로 리턴
//        collection.add(()->member.getRole()); // ROLE_xx저장
//        return collection;
//
//
//    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기존 람다 방식 → SimpleGrantedAuthority로 변경
        return List.of(new SimpleGrantedAuthority(member.getRole()));
    }

    @Override
    public String getPassword() {
        log.info("getPassword....");
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        log.info("getUsername....");
        return member.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        log.info("isAccountNonExpired....");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.info("isAccountNonLocked....");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.info("isCredentialsNonExpired....");
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.info("isEnabled....");
        return true;
    }
}
