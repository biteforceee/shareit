package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {
    public static Item mapToItem(ItemDto dto) {
        Item item = new Item();
        item.setId(0L);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());

        return item;
    }

    public static Item mapToUpdate(Item item, ItemDto dto) {
        if(dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }
        if(dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }
        if(dto.getAvailable() != null && dto.getAvailable() != item.getAvailable()) {
            item.setAvailable(dto.getAvailable());
        }
        return item;
    }
}
