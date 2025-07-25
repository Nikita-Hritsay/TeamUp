package org.users.users.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.users.users.dto.CardResponseDto;
import org.users.users.dto.UserDto;
import org.users.users.entity.Role;
import org.users.users.entity.User;
import org.users.users.exception.ResourceNotFoundException;
import org.users.users.exception.UserAlreadyExistsException;
import org.users.users.mapper.UserMapper;
import org.users.users.repository.RoleRepository;
import org.users.users.repository.UserRepository;
import org.users.users.service.IUserService;
import org.users.users.service.client.CardsFeignClient;
import org.users.users.service.client.TeamsFeignClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private CardsFeignClient cardsFeignClient;

    @Override
    public void createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto, new User());
        Optional<User> optionalUser = userRepository.findByMobileNumber(
                userDto.getMobileNumber());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User already exists with given mobile number: " + userDto.getMobileNumber());
        }
        createNewUserWithDefaultRole(user);
        userRepository.save(user);
    }

    private Role createNewUserWithDefaultRole(User user) {
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> roleRepository.save(new Role().withRoleName("USER")));

        user.setRoles(Set.of(userRole));
        return userRole;
    }

    @Override
    public UserDto fetchUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id",
                        id.toString()));

        List<CardResponseDto> userCards = cardsFeignClient.getCardsByUserId(user.getId()).getBody();

        return UserMapper.mapToUserDto(user, new UserDto(), userCards);
    }

    @Override
    public boolean updateUser(UserDto userDto) {
        boolean updated = false;

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id", userDto.getId().toString()));

        Optional<User> byMobileNumber = userRepository.findByMobileNumber(
                userDto.getMobileNumber());
        if (byMobileNumber.isPresent() && !Objects.equals(user.getId(),
                byMobileNumber.get().getId())) {
            throw new UserAlreadyExistsException(
                    "User already exists with given mobile number: " + userDto.getMobileNumber());
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());

        userRepository.save(user);
        updated = true;

        return updated;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        boolean deleted = false;
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id",
                                userId.toString())
                );
        userRepository.delete(user);
        deleted = true;

        return deleted;
    }

}
