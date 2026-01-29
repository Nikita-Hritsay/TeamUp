package org.users.users.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.users.users.dto.UserDto;
import org.users.users.dto.UserMessageDto;
import org.users.users.entity.Role;
import org.users.users.entity.User;
import org.users.users.exception.ResourceNotFoundException;
import org.users.users.exception.UserAlreadyExistsException;
import org.users.users.mapper.UserMapper;
import org.users.users.repository.RoleRepository;
import org.users.users.repository.UserRepository;
import org.users.users.service.IUserService;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final StreamBridge streamBridge;

    @Override
    public User createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto, new User());
        Optional<User> optionalUser = userRepository.findByMobileNumber(
                userDto.getMobileNumber());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User already exists with given mobile number: " + userDto.getMobileNumber());
        }
        createNewUserWithDefaultRole(user);
        User saved = userRepository.save(user);
        var send = streamBridge.send("sendCommunication-out-0",
                new UserMessageDto(saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getMobileNumber()));
        log.info("User send out: {}", send);
        return saved;
    }

    private void createNewUserWithDefaultRole(User user) {
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> roleRepository.save(new Role().withRoleName("USER")));

        user.setRoles(Set.of(userRole));
    }

    @Override
    public UserDto fetchUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id",
                        id.toString()));

        return UserMapper.mapToUserDto(user, new UserDto());
    }

    @Override
    public boolean updateUser(UserDto userDto) {
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

        return true;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id",
                                userId.toString())
                );
        userRepository.delete(user);
        return true;
    }

}
