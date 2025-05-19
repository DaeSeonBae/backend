package com.daeseonbae.DSBBackend.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException{
    private final AppHttpStatus appHttpStatus;
}
