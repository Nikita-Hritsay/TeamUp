package org.users.users.service;

import org.users.users.dto.UserDto;
import org.users.users.entity.User;

public interface IUserService
{

    User createUser(UserDto userDto);

    UserDto fetchUser(Long userId);

    boolean updateUser(UserDto userDto);

    boolean deleteUser(Long userId);

}
