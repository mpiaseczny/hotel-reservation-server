package pl.wat.wcy.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<RoomListItem>> getRooms(@RequestParam String nameOrCity, @RequestParam Long dateFrom, @RequestParam Long dateTo, @RequestParam Integer people) {
        return null;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long roomId) {
        return null;
    }

    @GetMapping("/{roomId}/reservation-times")
    public ResponseEntity<List<TimeInterval>> getReservationsTimes(@PathVariable Long roomId) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<RoomDTO> addRoom(@RequestBody RoomRequest roomRequest) {
        return null;
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequest roomRequest) {
        return null;
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        return null;
    }
}
