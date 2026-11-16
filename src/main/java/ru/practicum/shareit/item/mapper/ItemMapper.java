package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public final class ItemMapper {
    public static Item mapToItem(ItemDto dto, User user) {
        Item item = new Item();

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setUser(user);
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

    public static List<ItemDto> mapToItemDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for(Item item : items) {
            dtos.add(mapToItemDto(item));
        }
        return dtos;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());

        return dto;
    }
}
