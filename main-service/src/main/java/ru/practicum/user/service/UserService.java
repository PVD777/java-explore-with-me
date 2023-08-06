package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsersFromIdList(int[] ids, Pageable pageable);

    UserDto createNewUser(UserDto userDto);

    void deleteUser(int userId);
}
