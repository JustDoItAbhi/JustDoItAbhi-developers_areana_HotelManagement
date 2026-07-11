package com.user_service.customer.service;

import com.commonlibrary.common_library.common.event.UserRegisteredEvent;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private ModelMapper mapper;
    @Autowired private JwtService jwtService;
    @Autowired private KafkaTemplate<String ,UserRegisteredEvent>kafkaTemplate;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        Optional<User>userOptional=userRepository.findByEmail(dto.getEmail());
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
        user.setRole(UserRole.CUSTOMER);
    User savedUser=    userRepository.save(user);
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .username(savedUser.getName())
                .email(savedUser.getEmail())
                .build();

        kafkaTemplate.send("user-registered", savedUser.getId().toString(), event);
        log.info("User registered: {}", savedUser.getEmail());
        return mapper.map(user,UserResponseDto.class);
    }

    @Override
    public AuthResponse login(Login login) {
    Optional<User>userOptional=userRepository.findByEmail(login.getEmail());
    if(userOptional.isEmpty()){
        throw new RuntimeException("INVALID USER EMAIL "+login.getEmail());
    }
    if(!passwordEncoder.matches(login.getPassword(),userOptional.get().getPassword())){
        throw new RuntimeException("INVALID USER PASSWORD "+login.getPassword());
    }


        String token = jwtService.generateJwtToken(userOptional.get());
        return AuthResponse.builder()
                .token(token)
                .userid(userOptional.get().getId())
                .userName(userOptional.get().getName())
                .build();
    }

    @Override
    public boolean deleteUser(UUID id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserRequestDto dto) {// need new dto
        Optional<User>userOptional=userRepository.findByEmail(dto.getEmail());
        if(userOptional.isEmpty()){
            throw new RuntimeException("INVALID ACCESS , WRONG ID "+id);
        }
        User user=userOptional.get();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCountry(dto.getCountry());
        user.setState(dto.getState());
        user.setCity(dto.getCity());
        user.setRole(UserRole.CUSTOMER);
        userRepository.save(user);
        return mapper.map(user,UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        Optional<User>userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new RuntimeException("INVALID ACCESS , WRONG EMAIL "+email);
        }
        return mapper.map(userOptional.get(),UserResponseDto.class);
    }


}
