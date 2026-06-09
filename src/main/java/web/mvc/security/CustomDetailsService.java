package web.mvc.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mvc.domain.Member;
import web.mvc.repository.MemberRepository;

//userDetailsService
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username: {}", username);
        //username에 해당되는 정보 조회
        Member member = memberRepository.findById(username);
//        if(member!= null) {
//            log.info("member: {}", member);
//            return new CustomMemberDetails(member);
//        }
        //
        if (member == null) {           // null이면 예외 던지기
            throw new UsernameNotFoundException("사용자 없음: " + username);
        }
        return new CustomMemberDetails(member);

    }
}
