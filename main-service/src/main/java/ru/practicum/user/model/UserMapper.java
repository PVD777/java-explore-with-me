package ru.practicum.user.model;

import lombok.experimental.UtilityClass;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.model.dto.UserShortDto;

@UtilityClass
public class UserMapper {
    public User dtoToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserShortDto userToShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
