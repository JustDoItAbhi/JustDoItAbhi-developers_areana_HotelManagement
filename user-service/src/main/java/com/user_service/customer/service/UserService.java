package com.user_service.customer.service;

import com.user_service.customer.dto.request.Login;
import com.user_service.customer.dto.request.UserRequestDto;
import com.user_service.customer.dto.response.AuthResponse;
import com.user_service.customer.dto.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
   AuthResponse login(Login login);
    boolean deleteUser(UUID id);
    UserResponseDto updateUser(UUID id, UserRequestDto dto);
    UserResponseDto getUserByEmail(String email);
}
