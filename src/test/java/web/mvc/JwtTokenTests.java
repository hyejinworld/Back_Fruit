package web.mvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.mvc.domain.Member;
import web.mvc.jwt.JWTUtil;
import web.mvc.repository.MemberRepository;

@SpringBootTest
@Slf4j
class JwtTokenTests {

    @Autowired
    private JWTUtil jwtUtil;


    /**
     *  Registed claims : 미리 정의된 클레임
     *      iss(issuer: 발행자),
     * 		exp(expireation time: 만료 시간),
     * 		sub(subject: 제목),
     * 		iat(issued At: 발행 시간),
     * 		jti(JWI ID: 토큰의 고유 식별자)
     *
     * 	Registed claims : 미리 정의된 클레임
     * */
    @Test
    void tokenCreateTest() {
        Member member = Member.builder().id("jin").memberNo(1L).role("ROLE_USER").name("희정").build();

         String token = jwtUtil.createJwt(member, "ROLE_USER",1000L*60*10L);//10분
        System.out.println("token = "  + token);

        System.out.println("---조회하기 (검증)---");
        System.out.println("userName = " + jwtUtil.getUsername(token));
        System.out.println("id = " + jwtUtil.getId(token));
        System.out.println("Role = " + jwtUtil.getRole(token));
        System.out.println("isExpired = " + jwtUtil.isExpired(token));
    }
}
