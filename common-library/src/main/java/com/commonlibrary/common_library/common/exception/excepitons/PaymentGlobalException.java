package com.commonlibrary.common_library.common.exception.excepitons;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PaymentGlobalException {
    @ExceptionHandler(BookingDetailsNotFoundException.class)
    public ResponseEntity<ExcepitonDto> bookingDetailsNotFOUND(BookingDetailsNotFoundException e){
        ExcepitonDto dto=new ExcepitonDto(
                e.getMessage(),
                404
        );
        return new ResponseEntity<>(dto,HttpStatus.NOT_FOUND);
    }
}
