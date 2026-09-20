package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAll(long userId);
    Item create(long userId, ItemDto item);
    Item getById(long userId, long itemId);
    Item update(long itemId, long userId, ItemDto item);
    Void delete(long userId, long itemId);
    List<Item> searchByTextInDescription(String text);
}
