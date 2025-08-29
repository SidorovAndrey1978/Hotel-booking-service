package com.skillbox.hotel_reservations.controller;

import com.skillbox.hotel_reservations.enyity.Room;
import com.skillbox.hotel_reservations.mapper.RoomMapper;
import com.skillbox.hotel_reservations.model.filter.RoomFilter;
import com.skillbox.hotel_reservations.model.roomDTO.PagedRoomResponse;
import com.skillbox.hotel_reservations.model.roomDTO.RoomRequest;
import com.skillbox.hotel_reservations.model.roomDTO.RoomResponse;
import com.skillbox.hotel_reservations.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable Long id) {
        Room room = roomService.getById(id);
        return ResponseEntity.ok(roomMapper.toResponse(room));
    }

    @GetMapping
    public ResponseEntity<PagedRoomResponse> getPagedRooms(@RequestBody RoomFilter filter,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Room> resultPage = roomService.getFilteredRooms(filter, paging);
        List<RoomResponse> rooms = roomMapper.toList(resultPage.getContent());

        PagedRoomResponse pagedResponse = PagedRoomResponse.builder()
                .rooms(rooms)
                .totalItems(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .currentPage(resultPage.getNumber())
                .hasNext(resultPage.hasNext())
                .hasPrevious(resultPage.hasPrevious())
                .build();

        return ResponseEntity.ok(pagedResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody RoomRequest request) {
        Room createRoom = roomService.create(roomMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomMapper.toResponse(createRoom));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody RoomRequest request) {

        Room updateRoom = roomService.update(roomMapper.toEntity(id, request));
        return ResponseEntity.ok(roomMapper.toResponse(updateRoom));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
