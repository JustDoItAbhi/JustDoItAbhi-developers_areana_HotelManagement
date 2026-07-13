//package com.user_service.customer.controller;
//
//
//import com.commonlibrary.common_library.common.enums.UserRole;
//import com.user_service.customer.dto.request.Login;
//import com.user_service.customer.dto.request.UserRequestDto;
//import com.user_service.customer.dto.response.AuthResponse;
//import com.user_service.customer.jwtservice.JwtService;
//import com.user_service.customer.model.User;
//import com.user_service.customer.repository.UserRepository;
//import com.user_service.customer.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/user")
//public class AuthController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@RequestBody Login request) {
//    return ResponseEntity.ok(userService.login(request));
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody UserRequestDto dto) {
//        return ResponseEntity.ok(userService.createUser(dto));
//    }
//}
