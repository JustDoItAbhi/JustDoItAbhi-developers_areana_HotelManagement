package com.commonlibrary.common_library.common.exception.excepitons;

import lombok.Data;

@Data
public class ExcepitonDto {
    private String message ;
    private int code;

    public ExcepitonDto(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
