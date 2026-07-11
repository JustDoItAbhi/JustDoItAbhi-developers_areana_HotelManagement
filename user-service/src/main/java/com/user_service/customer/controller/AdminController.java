package com.user_service.customer.controller;

import com.commonlibrary.common_library.common.ratelimit.RateLimit;
import com.user_service.customer.dto.request.Login;
import com.user_service.customer.dto.request.UserRequestDto;
import com.user_service.customer.dto.response.AuthResponse;
import com.user_service.customer.dto.response.UserResponseDto;
import com.user_service.customer.service.AdminService;
import com.user_service.customer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @PostMapping("/admin")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto){
        return ResponseEntity.ok(adminService.createUser(dto));
    }
    @GetMapping
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<Page<UserResponseDto>> allUsers(@RequestParam  (defaultValue = "0")int pageNumber,
                                                          @RequestParam (defaultValue = "5")int pageSize){
        return ResponseEntity.ok(adminService.getAllUsers(pageNumber,pageSize));
    }
}
