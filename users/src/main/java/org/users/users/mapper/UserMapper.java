package org.users.users.mapper;

import org.users.users.dto.CardResponseDto;
import org.users.users.dto.RoleDto;
import org.users.users.dto.UserDto;
import org.users.users.entity.Role;
import org.users.users.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper
{

    public static UserDto mapToUserDto(User user, UserDto userDto, List<CardResponseDto> userCards)
    {
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMobileNumber(user.getMobileNumber());
        if (user.getRoles() != null) {
            Set<RoleDto> roleDtos = user.getRoles().stream()
                    .map(role -> {
                        RoleDto dtoRole = new RoleDto();
                        dtoRole.setRoleName(role.getRoleName());
                        return dtoRole;
                    })
                    .collect(Collectors.toSet());
            userDto.setRoles(roleDtos);
        }
        userDto.setCards(userCards);

        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user)
    {
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMobileNumber(userDto.getMobileNumber());
        if (userDto.getRoles() != null) {
            Set<Role> roles = userDto.getRoles().stream()
                    .map(roleDto -> {
                        Role role = new Role();
                        role.setRoleName(role.getRoleName());
                        return role;
                    })
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        return user;
    }

}
