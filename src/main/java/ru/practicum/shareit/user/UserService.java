package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {
    List<User> findAll();
    User findById(long userId);
    @Transactional
    User create(UserDto newUser);
    @Transactional
    User update(long userId, UserDto user);
    @Transactional
    Void delete(long userId);
}
