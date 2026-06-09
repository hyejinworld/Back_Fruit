package web.mvc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import web.mvc.jwt.JWTUtil;
import web.mvc.jwt.JwtFilter;
import web.mvc.jwt.LoginFilter;
import java.util.List;



@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        log.info("bCryptPasswordEncoder call.....");
        return new BCryptPasswordEncoder(); //패스워드encoder의 구현체 중 하나
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain filterChain(HttpSecurity http) call.....");
       /////////////////////////////////
        //프론트연결
        http.cors((cors) -> cors.configurationSource(request -> {
            var config = new org.springframework.web.cors.CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:5173"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE",  "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setExposedHeaders(List.of("Authorization")); // ← 토큰 헤더 노출
            config.setAllowCredentials(true);
            return config;
        }));
        //csrf disable //리액트로하면 disable로 꺼놓는다.
        http.csrf((auth) -> auth.disable()); //csrf공격을 방어하기 위한 토큰 주고 받는 부분을 비활성화!
        //Form 로그인 방식 disable -> React, JWT 인증 방식으로 변겨예정
        //disable 를 설정하면 시큐리티의 UsernamePasswordAuthenticationFilter비활성됨.
        http.formLogin((auth) -> auth.disable());
      //  http.formLogin(Customizer.withDefaults());
       // http.formLogin(Customizer.withDefaults());
        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ← 추가
        .requestMatchers("/index", "/members", "/members/**", "/login").permitAll()
        // [1] GET 요청: 누구나 접근 가능
        .requestMatchers(HttpMethod.GET, "/boards").permitAll()
        .requestMatchers(HttpMethod.GET, "/boards/**").permitAll()

        // [2] POST 요청: 인증 필요
        .requestMatchers(HttpMethod.POST, "/boards").authenticated()
        // [3] PUT 요청: 인증 필요
        .requestMatchers(HttpMethod.PUT, "/boards").authenticated()
        // [4] DELETE 요청: 인증 필요
        .requestMatchers(HttpMethod.DELETE, "/boards").authenticated()

        /*https://docs.spring.io/spring-security/site/docs/5.5.6/api/org/springframework/security/config/annotation/web/configurers/AuthorizeHttpRequestsConfigurer.AuthorizedUrl.html#hasAnyRole(java.lang.String...)
        * 사용자가 적어도 하나 이상 가져야 하는 역할(예: ADMIN, USER 등). 각 역할은 ROLE_로 시작하면 안 됩니다. 이미 자동으로 ROLE_이 붙기 때문입니
        * */

                        .requestMatchers(HttpMethod.PUT, "/boards/**").authenticated()  // /** 추가
                        .requestMatchers(HttpMethod.DELETE, "/boards/**").authenticated() // /** 추가
        .requestMatchers("/admin").hasRole("ADMIN") // 자동으로 ROLE_ 붙는다.
        .anyRequest().authenticated());


        // LoginFilter 등록 위에 이 줄 추가

        // JwtFilter 등록
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

    // LoginFilter 등록 (로그인필터)
        LoginFilter loginFilter = new LoginFilter(this.authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/login");
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);


        //게시판 글쓰기 필터
        // LoginFilter 등록 위에 추가
        // http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        SecurityFilterChain chain = http.build();
        System.out.println("--------------------------");
        chain.getFilters().forEach(System.out::println);

        System.out.println("-------------------------");
        return chain;
    }

}