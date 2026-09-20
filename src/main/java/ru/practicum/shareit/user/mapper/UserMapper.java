package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

public final class UserMapper {
    public static User mapToUser(UserDto dto) {
        User user = new User();
        user.setId(0L);
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return user;
    }

    public static User mapToUpdate(User user, UserDto dto) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            newUser.setEmail(dto.getEmail());
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            newUser.setName(dto.getName());
        }

        return newUser;
    }
}
