package com.booking_service.feignclient;

import com.booking_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",url = "http://localhost:8083/api/user")
public interface UserClient {
    @GetMapping("/{email}")
    public UserDto getUser(@PathVariable("email")String email);
}
