package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemService {
    List<ItemDto> findAll(long userId);
    @Transactional
    ItemDto create(long userId, ItemDto item);
    ItemDto getById(long userId, long itemId);
    ItemDto update(long itemId, long userId, ItemDto item);
    @Transactional
    Void delete(long userId, long itemId);
    List<ItemDto> searchByTextInDescription(String text);
    @Transactional
    CommentDto createComment(long itemId, long userId, CommentDto dto);
    List<CommentDto> getItemComments(long userId, long itemId);
    List<CommentDto> getCommentsForUserItems(long userId);
    ItemDto getItemWithComments(long userId, long itemId);
}
