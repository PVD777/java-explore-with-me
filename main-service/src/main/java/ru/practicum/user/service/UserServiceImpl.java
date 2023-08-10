package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.model.dto.UserDto;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public List<UserDto> getUsersFromIdList(int[] ids, Pageable pageable) {
        if (ids == null) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findUsersByIdIn(ids, pageable).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto createNewUser(UserDto userDto) {
        validate(userDto);
        User savedUser = userRepository.save(UserMapper.dtoToUser(userDto));
        return UserMapper.userToDto(savedUser);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }

    private void validate(UserDto userDto) {
        if (userRepository.findByName(userDto.getName()).isPresent()) {
            throw new ValidationException("user name exist");
        }
    }
}
