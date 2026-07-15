package com.notification_service.feignclient;

import com.notification_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/user/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable("email")String email);
}
