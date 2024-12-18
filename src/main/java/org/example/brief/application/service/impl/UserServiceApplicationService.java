package org.example.brief.application.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.brief.application.dto.request.LoginRequest;
import org.example.brief.application.dto.request.UserRequest;
import org.example.brief.application.dto.response.UserResponse;
import org.example.brief.application.mapper.UserMapper;
import org.example.brief.application.service.UserService;
import org.example.brief.domain.model.Role;
import org.example.brief.domain.model.User;
import org.example.brief.infrastructure.exception.UserAlreadyExistsException;
import org.example.brief.infrastructure.exception.UserNotFoundException;
import org.example.brief.infrastructure.repository.RoleRepository;
import org.example.brief.infrastructure.repository.UserRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceApplicationService implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final   AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public UserResponse registerUser(UserRequest userDto) {

        if (userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException("User with login " + userDto.getLogin() + " already exists");
        }


        User user = userMapper.toEntity(userDto);


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default USER role not found"));
            user.setRoles(Set.of(defaultRole));
        }


        User savedUser = userRepository.save(user);

        log.info("User registered: {}", savedUser.getLogin());

        return userMapper.toResponse(savedUser);
    }
    @Override
    public UserResponse authenticateUser(LoginRequest userDto) {


        try {
            User user = userRepository.findByLogin(userDto.getLogin())
                    .filter(u -> passwordEncoder.matches(userDto.getPassword(), u.getPassword()))
                    .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));

            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(user.getLogin(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);

            log.info("User authenticated: {}", user.getLogin());
            return userMapper.toResponse(user);
        } catch (Exception e) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                log.error("ip address: {}", localHost.getHostAddress());
            }catch (UnknownHostException uhe) {
                log.error("Could not retrieve local host address", uhe);
                throw new UserNotFoundException("Invalid credentials");
            }


            log.error("Authentication failed for user: {}", userDto.getLogin(), e);

            throw new UserNotFoundException("Invalid credentials");
        }
    }
    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        log.info("Retrieved all users, total count: {}", users.size());

        return users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserResponse updateUserRoles(Long userId, List<String> roleNames) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));


        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());


        user.setRoles(roles);

        User updatedUser = userRepository.save(user);

        log.info("Updated roles for user: {}", updatedUser.getLogin());

        return userMapper.toResponse(updatedUser);
    }
}