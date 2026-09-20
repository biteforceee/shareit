package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(UserDto newUser) {
        validateForCreate(UserMapper.mapToUser(newUser));
        return userRepository.create(UserMapper.mapToUser(newUser));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().toList();
    }

    @Override
    public User findById(long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public User update(long userId, UserDto dto) {
        User user = userRepository.getById(userId);
        User updatedUser = UserMapper.mapToUpdate(user, dto);

        return userRepository.update(userId, updatedUser);
    }

    @Override
    public Void delete(long userId) {
        userRepository.delete(userId);
        return null;
    }

    private void validateForCreate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Ошибка с именем пользователя.");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка с электронной почтой.");
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        }
    }
}
