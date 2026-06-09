package web.mvc.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import web.mvc.domain.Member;
import web.mvc.security.CustomMemberDetails;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
//@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
//    public JwtFilter(JWTUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    } 위에 @RequiredArgsContructor 쓰면 이건 지워도됨


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String id = jwtUtil.getId(token);
        String role = jwtUtil.getRole(token);
        Long memberNo = jwtUtil.getMemberNo(token);

        Member member = Member.builder()
                .memberNo(memberNo)
                .id(id)
                .role(role)
                .build();

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customMemberDetails, null, customMemberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}