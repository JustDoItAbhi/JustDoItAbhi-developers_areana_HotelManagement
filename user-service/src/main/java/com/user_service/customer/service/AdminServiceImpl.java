package com.user_service.customer.service;

import com.user_service.customer.dto.request.UserRequestDto;
import com.user_service.customer.dto.response.UserResponseDto;

import com.user_service.customer.model.User;
import com.commonlibrary.common_library.common.enums.UserRole;
import com.user_service.customer.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;




@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private ModelMapper mapper;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        Optional<User> userOptional=userRepository.findByEmail(dto.getEmail());
        if(userOptional.isPresent()){
            return mapper.map(userOptional.get(),UserResponseDto.class);
        }
        User user=new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCountry(dto.getCountry());
        user.setState(dto.getState());
        user.setCity(dto.getCity());
        user.setRole(UserRole.ADMIN);
        userRepository.save(user);
        return mapper.map(user,UserResponseDto.class);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<User>userPage=userRepository.findAll(pageable);

        return userPage.map(user->
                mapper.map(user,UserResponseDto.class));
    }

}
