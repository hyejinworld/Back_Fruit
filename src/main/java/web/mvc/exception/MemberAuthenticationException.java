package web.mvc.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MemberAuthenticationException extends  RuntimeException implements ErrorCodeProvider{
    private final ErrorCode errorCode ;

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
