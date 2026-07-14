package com.user_service.customer.service;

import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.UserRegisteredEvent;
import com.commonlibrary.common_library.common.kafka.EventProducer;
import com.commonlibrary.common_library.common.mail.JavaMailCreator;
import com.user_service.customer.dto.request.Login;
import com.user_service.customer.dto.request.UserRequestDto;
import com.user_service.customer.dto.response.AuthResponse;
import com.user_service.customer.dto.response.UserResponseDto;
import com.user_service.customer.jwtservice.JwtService;
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

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

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

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .username(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
        eventProducer.sendEvent(KafkaTopics.USER_REGISTERED, event);
        String messge ="PLEASE LOGIN "+" http://localhost:8080/api/user/login";
        javaMailCreator.send(user.getEmail(),"SIGN UP SUCCESSFULL ",messge);
        System.out.println("EMAIL SENT BY USER SERVICE ");
        log.info("User registered: {}", savedUser.getEmail());

        return mapper.map(savedUser, UserResponseDto.class);
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
        String messge ="welcome back "+user.getName();
        javaMailCreator.send(user.getEmail(),"LOGIN SUCCESSFULL ",messge);
        System.out.println("EMAIL SENT BY USER SERVICE ");
        return response;
    }

    @Override
    public boolean deleteUser(UUID id) {
        userRepository.deleteById(id);
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
        return mapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return mapper.map(userOptional.get(), UserResponseDto.class);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<User>userPage=userRepository.findAll(pageable);

        return userPage.map(user->
                mapper.map(user,UserResponseDto.class));
    }

}