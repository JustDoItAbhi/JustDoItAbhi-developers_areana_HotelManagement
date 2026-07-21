package com.user_service.customer.controller;

import com.commonlibrary.common_library.common.annotation.Tracking;
import com.commonlibrary.common_library.common.ratelimit.RateLimit;
import com.user_service.customer.dto.request.Login;
import com.user_service.customer.dto.request.UserRequestDto;
import com.user_service.customer.dto.response.AuthResponse;
import com.user_service.customer.dto.response.UserResponseDto;
import com.user_service.customer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<UserResponseDto>create(@RequestBody UserRequestDto dto){
        return ResponseEntity.ok(userService.createUser(dto));
    }
    @PostMapping("/login")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<AuthResponse>login(@RequestBody Login dto){
        return ResponseEntity.ok(userService.login(dto));
    }
    @DeleteMapping("/{userId}")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<Boolean>deleteUser(@PathVariable ("userId")UUID id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    @PutMapping("/update/{userId}")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<UserResponseDto>updateUser(@PathVariable ("userId")UUID id,@RequestBody UserRequestDto dto){
        return ResponseEntity.ok(userService.updateUser(id,dto));
    }
    @GetMapping("/{email}")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<UserResponseDto>getUser(@PathVariable ("email")String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    @GetMapping("/getallusers")
    @RateLimit(value = 20,duration = 60000)
    @Tracking
    public ResponseEntity<Page<UserResponseDto>> allUsers(@RequestParam  (defaultValue = "0")int pageNumber,
                                                          @RequestParam (defaultValue = "5")int pageSize){
        return ResponseEntity.ok(userService.getAllUsers(pageNumber,pageSize));
    }
}
