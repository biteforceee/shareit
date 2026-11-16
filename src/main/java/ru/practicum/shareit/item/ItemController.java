package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return new ResponseEntity<>(itemService.findAll(userId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable long itemId) {
        return new ResponseEntity<>(itemService.getById(userId, itemId), HttpStatus.OK) ;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam("text") String text) {
        return new ResponseEntity<>(itemService.searchByTextInDescription(text), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody ItemDto item) {
        return new ResponseEntity<>(itemService.create(userId, item), HttpStatus.CREATED) ;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable long itemId,
                                          @RequestBody ItemDto item) {
        return new ResponseEntity<>(itemService.update(itemId, userId, item), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable(name="itemId") long itemId) {
        return new ResponseEntity<>(itemService.delete(userId, itemId), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long itemId,
                                                 @RequestBody CommentDto comment) {
        return new ResponseEntity<>(itemService.createComment(userId, itemId, comment), HttpStatus.CREATED) ;
    }

    @GetMapping("/{itemId}/comment")
    public ResponseEntity<ItemDto> getItemWithComments(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.getItemWithComments(userId, itemId), HttpStatus.OK);
    }

    @GetMapping("/comment")
    public ResponseEntity<List<CommentDto>> getAllCommentsForUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(itemService.getCommentsForUserItems(userId), HttpStatus.OK);
    }
}
