package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.trace("Получили всех пользователей.");
        return users.values();
    }

    @Override
    public User create(User user) {
        log.trace("Создаем нового пользователя.");

        log.debug("Валидация пользователя.");
        for (Map.Entry<Long,User> e : users.entrySet()) {
            if (e.getValue().getEmail().equals(user.getEmail())) {
                log.warn("Этот имейл нельзя использовать при создании нового пользователя.");
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }

        log.debug("Устанавливаем значение id у пользователя.");
        user.setId(getNextId());

        users.put(user.getId(), user);
        log.trace("Создали нового пользователя с id = {}.", user.getId());
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User update(long userId, User newUser) {
        log.trace("Обновляем данные пользователя.");

        if (users.containsKey(userId)) {
            User oldUser = users.get(userId);
            log.debug("Валидация пользователя.");
            if (!Objects.equals(oldUser.getEmail(), newUser.getEmail())) {
                for (Map.Entry<Long,User> e : users.entrySet()) {
                    if (e.getValue().getEmail().equals(newUser.getEmail())) {
                        log.warn("Этот имейл уже используется.");
                        throw new DuplicatedDataException("Этот имейл уже используется");
                    }
                }
            }
            if (newUser.getEmail() != null) {
                log.debug("Обновили имейл пользователя.");
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getName() != null) {
                log.debug("Обновили имя пользователя.");
                oldUser.setName(newUser.getName());
            }
            log.trace("Данные пользователя обновлены.");
            return oldUser;
        }

        log.warn("Юзер с id = {} не найден.", newUser.getId());
        throw new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
        log.debug("Удалили пользователя.");
    }

    @Override
    public User getById(long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("Юзер с id = " + id + " не найден"));
    }

}
