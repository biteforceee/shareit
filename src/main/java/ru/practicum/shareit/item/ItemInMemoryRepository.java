package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemInMemoryRepository implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private long idCounter = 1;

    @Override
    public List<Item> findAll(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Item create(long userId, Item item) {
        if (item.getId() == 0) {
            item.setId(idCounter++);
        }

        items.computeIfAbsent(userId, k -> new ArrayList<>()).add(item);
        return item;
    }

    @Override
    public Item getById(long userId, long itemId) {
        List<Item> userItems = items.get(userId);
        if (userItems == null) {
            throw new NotFoundException("User items not found");
        }
        return userItems.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item with id = " + itemId + " not found"));
    }

    @Override
    public Item update(long itemId, long userId, Item item) {
        List<Item> userItems = items.get(userId);
        if (userItems == null) {
            throw new NotFoundException("User items not found");
        }

        boolean removed = userItems.removeIf(item1 -> item1.getId() == itemId);

        if (removed) {
            userItems.add(item);
        } else {
            throw new NotFoundException("Item with id = " + itemId + " not found");
        }

        return item;
    }

    @Override
    public void delete(long userId, long itemId) {
        List<Item> userItems = items.get(userId);
        if (userItems == null) {
            throw new NotFoundException("User items not found");
        }

        boolean removed = userItems.removeIf(item -> item.getId() == itemId);

        if (!removed) {
            throw new NotFoundException("Item with id " + itemId + " not found");
        }
    }

    @Override
    public List<Item> searchByTextInDescription(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String lowerCaseText = text.toLowerCase();

        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(elem -> elem.getDescription() != null &&
                        elem.getDescription().toLowerCase().contains(lowerCaseText))
                .collect(Collectors.toList());
    }
}
