package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Item> findAll(long userId) {
        return itemRepository.findAll(userId);
    }

    @Override
    public Item create(long userId, ItemDto dto) {
        validateForCreate(dto);
        userRepository.getById(userId);
        return itemRepository.create(userId, ItemMapper.mapToItem(dto));
    }

    @Override
    public Item getById(long userId, long itemId) {
        return itemRepository.getById(userId, itemId);
    }

    @Override
    public Item update(long itemId, long userId, ItemDto dto) {
        userRepository.getById(userId);
        Item oldItem = itemRepository.getById(userId, itemId);
        Item newItem = ItemMapper.mapToUpdate(oldItem, dto);

        return itemRepository.update(itemId, userId, newItem);
    }

    @Override
    public Void delete(long userId, long itemId) {
        itemRepository.delete(userId, itemId);
        return null;
    }

    @Override
    public List<Item> searchByTextInDescription(String text) {
        return itemRepository.searchByTextInDescription(text);
    }

    private void validateForCreate(ItemDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ConditionsNotMetException("Имя не может быть пустым");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (dto.getAvailable() == null) {
            throw new ConditionsNotMetException("Доступность должна быть указана");
        }
    }
}
