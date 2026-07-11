package com.user_service.customer.mapper;

import com.user_service.customer.dto.response.UserResponseDto;
import com.user_service.customer.model.User;
import org.modelmapper.ModelMapper;

public class UserMapper {
    private  final ModelMapper mapper;

    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }
    public UserResponseDto fromUsers(User user){
        return mapper.map(user,UserResponseDto.class);
    }
}
