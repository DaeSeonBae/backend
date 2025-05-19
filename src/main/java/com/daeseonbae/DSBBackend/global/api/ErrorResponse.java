package com.daeseonbae.DSBBackend.global.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    int code;
    String message;

    public static ErrorResponse of(AppHttpStatus appHttpStatus){
        return ErrorResponse.builder()
                .code(appHttpStatus.getHttpStatus().value())
                .message(appHttpStatus.getMessage())
                .build();
    }

    public static ErrorResponse of(Throwable t){
        // ApiException 유형 처리
        if(t instanceof ApiException e){
            return ErrorResponse.builder()
                    .code(e.getAppHttpStatus().getHttpStatus().value())
                    .message(e.getAppHttpStatus().getMessage())
                    .build();
        }
        // 요청 파라미터 누락
        else if (t instanceof MissingServletRequestParameterException e) {
            return ErrorResponse.builder()
                    .code(400)
                    .message("요청에 필수 파라미터가 없습니다.")
                    .build();
        }
        // 잘못된 엔드포인트
        else if(t instanceof NoResourceFoundException e){
            return ErrorResponse.builder()
                    .code(404)
                    .message(e.getResourcePath() + " : " + AppHttpStatus.NOT_FOUND_ENDPOINT.getMessage())
                    .build();
        }


        // 그 외 알 수 없는 예외 처리
        return ErrorResponse.builder()
                .code(500)
                .message("알 수 없는 예외 발생")
                .build();

    }
}
