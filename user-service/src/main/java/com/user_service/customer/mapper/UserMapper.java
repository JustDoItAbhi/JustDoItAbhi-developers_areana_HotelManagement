package com.user_service.customer.mapper;

import com.user_service.customer.dto.response.UserResponseDto;
import com.user_service.customer.model.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    public static UserResponseDto fromUsers(User user){
        UserResponseDto dto=new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setCountry(user.getCountry());
        return dto;
    }
}
