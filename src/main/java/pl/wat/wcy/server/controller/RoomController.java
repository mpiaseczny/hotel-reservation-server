package pl.wat.wcy.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import pl.wat.wcy.server.dto.RoomDTO;
import pl.wat.wcy.server.dto.RoomListItem;
import pl.wat.wcy.server.dto.RoomRequest;
import pl.wat.wcy.server.dto.TimeInterval;
import pl.wat.wcy.server.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@AllArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping()
    public ResponseEntity<List<RoomListItem>> getRooms(
            @RequestParam String hotelNameOrCity,
            @RequestParam(required = false) @Nullable Long dateFrom,
            @RequestParam(required = false) @Nullable Long dateTo,
            @RequestParam(required = false) @Nullable Integer people
    ) {
        return roomService.getRooms(hotelNameOrCity, dateFrom, dateTo, people);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long roomId) {
        return roomService.getRoom(roomId);
    }

    @GetMapping("/{roomId}/reservation-times")
    public ResponseEntity<List<TimeInterval>> getReservationsTimes(@PathVariable Long roomId) {
        return roomService.getReservationTimes(roomId);
    }

    @PostMapping()
    public ResponseEntity<RoomDTO> addRoom(@RequestBody RoomRequest roomRequest) {
        return roomService.addRoom(roomRequest);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequest roomRequest) {
        return roomService.updateRoom(roomId, roomRequest);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Long> deleteRoom(@PathVariable Long roomId) {
        return roomService.deleteRoom(roomId);
    }
}
