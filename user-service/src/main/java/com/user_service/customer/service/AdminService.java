package com.user_service.customer.service;

import com.user_service.customer.dto.request.UserRequestDto;
import com.user_service.customer.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    UserResponseDto createUser(UserRequestDto dto);
    Page<UserResponseDto> getAllUsers(int pageNumber, int pageSize);
}
