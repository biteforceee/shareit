package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingOutDto> create(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                          @RequestBody BookingDto dto) {
        return new ResponseEntity<>(bookingService.create(bookerId, dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam boolean approved) {
        return new ResponseEntity<>(bookingService.changeStatus(bookingId, userId, approved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId) {
        return new ResponseEntity<>(bookingService.getById(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<BookingOutDto>> findAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(required = false) BookingState state) {
        return new ResponseEntity<>(bookingService.findAllByBooker(userId, state), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingOutDto>> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam BookingState state) {
        return new ResponseEntity<>(bookingService.findAllByOwner(userId, state), HttpStatus.OK);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> delete(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PathVariable Long bookingId) {
        return new ResponseEntity<>(bookingService.delete(userId, bookingId), HttpStatus.OK);
    }
}
