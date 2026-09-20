package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findAll(long userId);
    Item create(long userId, Item item);
    Item getById(long userId, long itemId);
    Item update(long itemId, long userId, Item item);
    void delete(long userId, long itemId);
    List<Item> searchByTextInDescription(String text);
}
