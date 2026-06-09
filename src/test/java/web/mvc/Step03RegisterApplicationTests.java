package web.mvc;

import web.mvc.domain.Member;
import web.mvc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Slf4j
class
Step03RegisterApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    String id="8253jang";
    @Test
    void contextLoads() {
        log.info("passwordEncoder :{}", passwordEncoder);

        //평문 -> 암호화
        String encodedPass = passwordEncoder.encode(id);
        log.info("encoded password :{}", encodedPass);

        //암화된 비번 과 평문 비교!!!
        if(passwordEncoder.matches("8253jang1",encodedPass)){
            log.info("같다");
        }else{
            log.info("다르다");
        }
    }

    /**
     * 관리자 등록
     * */
    @Test
    @DisplayName("관리자 계정추가")
    void memberInsert() {
        String encPwd = passwordEncoder.encode("1234");//비번 암호화

        memberRepository.save(
                Member.builder()
                        .id("admin")
                        .pwd(encPwd)
                        .role("ROLE_ADMIN")
                      //  .address("오리역")
                        .name("장희정")
                        .build());
    }

}
