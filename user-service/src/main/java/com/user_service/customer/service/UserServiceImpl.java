package com.user_service.customer.service;

import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.UserLoginEvent;
import com.commonlibrary.common_library.common.event.UserRegisteredEvent;
import com.commonlibrary.common_library.common.kafka.EventProducer;
import com.commonlibrary.common_library.common.mail.JavaMailCreator;
import com.commonlibrary.common_library.common.redis.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_service.customer.dto.request.Login;
import com.user_service.customer.dto.request.UserRequestDto;
import com.user_service.customer.dto.response.AuthResponse;
import com.user_service.customer.dto.response.UserResponseDto;
import com.user_service.customer.jwtservice.JwtService;
import com.user_service.customer.mapper.UserMapper;
import com.user_service.customer.model.User;
import com.commonlibrary.common_library.common.enums.UserRole;
import com.user_service.customer.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private JavaMailCreator javaMailCreator;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());
        if (userOptional.isPresent()) {
            return mapper.map(userOptional.get(), UserResponseDto.class);
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCountry(dto.getCountry());
        user.setState(dto.getState());
        user.setCity(dto.getCity());

        try {
            UserRole role = UserRole.valueOf(dto.getRole().toUpperCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("INVALID ROLE: " + dto.getRole());
        }

        User savedUser = userRepository.save(user);
        UserResponseDto responseDto=mapper.map(savedUser, UserResponseDto.class);
        redisService.set("user:"+user.getId(),responseDto,1, TimeUnit.HOURS);
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .username(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
        eventProducer.sendEvent(KafkaTopics.USER_REGISTERED,event);
        return responseDto;
    }



    @Override
    public AuthResponse login(Login request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtService.generateJwtToken(user);

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .userId(user.getId().toString())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
        UserLoginEvent event=new UserLoginEvent();
        event.setLoginTime(LocalDateTime.now());
        event.setUserName(user.getName());
        event.setUserEmail(user.getEmail());
        event.setUserRole(user.getRole().name());
        eventProducer.sendEvent(KafkaTopics.USER_LOGIN,event);

        return response;
    }




    @Override
    public boolean deleteUser(UUID id) {
        userRepository.deleteById(id);
        redisService.delete("user:"+id);
        return true;
    }




    @Override
    public UserResponseDto updateUser(UUID id, UserRequestDto dto) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }

        User user = userOptional.get();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCountry(dto.getCountry());
        user.setState(dto.getState());
        user.setCity(dto.getCity());
        userRepository.save(user);
        return UserMapper.fromUsers(user);
    }




    @Override
    public UserResponseDto getUserByEmail(String email) {
        String key="user:"+email;
       Object  cache=redisService.get(key);
        if(cache!=null){
            return objectMapper.convertValue(cache,UserResponseDto.class);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        UserResponseDto dto= UserMapper.fromUsers(userOptional.get());
        redisService.set(key,dto,1,TimeUnit.HOURS);
        return dto;
    }




    @Override
    public Page<UserResponseDto> getAllUsers(int pageNumber, int pageSize) {
        String key="allUsers";
        Object cache=redisService.get(key);
        if(cache!=null){
            return (Page<UserResponseDto>)cache;
        }
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<User>userPage=userRepository.findAll(pageable);
        Page<UserResponseDto> responseDtos=userPage.map(user-> UserMapper.fromUsers(user));
        redisService.set(key,responseDtos,30,TimeUnit.HOURS);
        return responseDtos;
    }

}