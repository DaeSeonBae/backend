package com.daeseonbae.DSBBackend.global.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BaseResponse<T> {
    private String status;
    private T data;

    public static BaseResponse<Void> success() {
        return BaseResponse.<Void>builder()
                .status("success")
                .build();
    }

    public static <T> BaseResponse<T> success(T data){
        return BaseResponse.<T>builder()
                .status("success")
                .data(data)
                .build();
    }
}
