package org.example.brief.application.service;

import org.example.brief.application.dto.request.LoginRequest;
import org.example.brief.application.dto.request.UserRequest;
import org.example.brief.application.dto.response.UserResponse;
import org.example.brief.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    UserResponse registerUser(UserRequest userDto);


    UserResponse authenticateUser(LoginRequest userDto);


    List<UserResponse> getAllUsers();


    UserResponse updateUserRoles(Long userId, List<String> roleNames);
}
