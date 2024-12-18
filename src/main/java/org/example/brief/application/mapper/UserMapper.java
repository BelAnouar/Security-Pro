package org.example.brief.application.mapper;


import jdk.jfr.Name;
import org.example.brief.application.dto.request.UserRequest;
import org.example.brief.application.dto.response.RoleResponse;
import org.example.brief.application.dto.response.UserResponse;
import org.example.brief.domain.model.Role;
import org.example.brief.domain.model.User;
import org.example.brief.infrastructure.repository.RoleRepository;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "roles", qualifiedByName = "mapRolesToRoleNames")
    UserResponse toResponse(User user);


    @Named("mapRolesToRoleNames")
    default List<String> mapRolesToRoleNames(Collection<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @AfterMapping
    default void mapRoles(@MappingTarget User user, UserRequest request) {

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {

            user.setRoles(request.getRoles().stream()
                    .map(roleName -> {
                        Role role = new Role();
                        role.setName(roleName);
                        return role;
                    })
                    .collect(Collectors.toList()));
        }
    }
}