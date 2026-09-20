package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(long userId);
    User create(UserDto newUser);
    User update(long userId, UserDto user);
    Void delete(long userId);
}
