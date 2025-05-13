package ru.yandex.practicum.filmorate.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {

    public UserDto convertToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
    }

    public User convertToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getLogin(), userDto.getEmail(), userDto.getBirthday());
    }
}