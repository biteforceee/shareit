package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

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
    public ResponseEntity<List<Item>> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return new ResponseEntity<>(itemService.findAll(userId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                        @PathVariable long itemId) {
        return new ResponseEntity<>(itemService.getById(userId, itemId), HttpStatus.OK) ;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam("text") String text) {
        return new ResponseEntity<>(itemService.searchByTextInDescription(text), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Item> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestBody ItemDto item) {
        return new ResponseEntity<>(itemService.create(userId, item), HttpStatus.CREATED) ;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Item> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable long itemId,
                    @RequestBody ItemDto item) {
        return new ResponseEntity<>(itemService.update(itemId, userId, item), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name="itemId") long itemId) {
        return new ResponseEntity<>(itemService.delete(userId, itemId), HttpStatus.OK);
    }
}
