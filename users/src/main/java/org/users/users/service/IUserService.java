package org.users.users.service;

import org.users.users.dto.UserDto;

public interface IUserService
{

    void createUser(UserDto userDto);

    UserDto fetchUser(Long userId);

    boolean updateUser(UserDto userDto);

    boolean deleteUser(Long userId);

}
