package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingProjection;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<ItemDto> findAll(long userId) {
        return ItemMapper.mapToItemDto(itemRepository.findAllByUser_Id(userId));
    }

    @Override
    public ItemDto create(long userId, ItemDto dto) {
        validateForCreate(dto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));;

        return ItemMapper.mapToItemDto(itemRepository.save(ItemMapper.mapToItem(dto, user)));
    }

    @Override
    public ItemDto getById(long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item with id "+ itemId + " not found"));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto update(long itemId, long userId, ItemDto dto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));

        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item with id "+ itemId + " not found"));
        Item newItem = ItemMapper.mapToUpdate(oldItem, dto);

        return ItemMapper.mapToItemDto(itemRepository.save(newItem));
    }

    @Override
    public Void delete(long userId, long itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));

        itemRepository.deleteById(itemId);
        return null;
    }

    @Override
    public List<ItemDto> searchByTextInDescription(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return ItemMapper.mapToItemDto(itemRepository.findAllByDescriptionContainingIgnoreCase(text));
    }

    @Override
    public CommentDto createComment(long userId, long itemId, CommentDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item with id "+ itemId + " not found"));

        List<BookingProjection> bookings = bookingRepository.findAllByBooker_IdAndItem_Id(userId, itemId);
        Boolean containApproved = bookings.stream()
                .map(BookingProjection::getStatus)
                .toList().contains("APPROVED");

        if (!containApproved) {
            throw new ConditionsNotMetException("user don`t book item with id " + itemId);
        }

        Comment comment = CommentMapper.mapToComment(dto, item, user);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto getItemWithComments(long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item with id "+ itemId + " not found"));

        ItemDto dto = ItemMapper.mapToItemDto(item);
        // 1. Добавляем комментарии (видны всем)
        List<CommentDto> comments = commentRepository.findAllByItem_Id(itemId).stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
        dto.setComments(comments);

        // 2. Бронирования добавляем ТОЛЬКО если запрос делает владелец вещи
        if (item.getUser().getId().equals(userId)) {
            Instant now = Instant.now();

            // Ищем последнее прошедшее бронирование
            ItemDto.BookingShortDto last = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByEndDesc(
                            itemId, BookingStatus.APPROVED, now)
                    .map(b -> new ItemDto.BookingShortDto(b.getId(), b.getBooker().getId()))
                    .orElse(null);

            // Ищем следующее предстоящее бронирование
            ItemDto.BookingShortDto next = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                            itemId, BookingStatus.APPROVED, now)
                    .map(b -> new ItemDto.BookingShortDto(b.getId(), b.getBooker().getId()))
                    .orElse(null);

            dto.setLastBooking(last);
            dto.setNextBooking(next);
        }


        return dto;
    }

    @Override
    public List<CommentDto> getItemComments(long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item with id "+ itemId + " not found"));

        return CommentMapper.mapToCommentDto(commentRepository.findAllByItem_Id(itemId));
    }

    @Override
    public List<CommentDto> getCommentsForUserItems(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id "+ userId + " not found"));

        return CommentMapper.mapToCommentDto(commentRepository.findAllByItem_User_Id(userId));
    }

    private void validateForCreate(ItemDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ConditionsNotMetException("Name can't be empty");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Description can't be empty");
        }
        if (dto.getAvailable() == null) {
            throw new ConditionsNotMetException("Avaible can't be empty");
        }
    }
}
