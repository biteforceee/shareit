package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();
    User getById(long id);
    User update(long id, User updatedUser);
    User create(User newUser);
    void delete(long id);
}
