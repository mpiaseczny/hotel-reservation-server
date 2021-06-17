package pl.wat.wcy.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wat.wcy.server.dto.ReservationDTO;
import pl.wat.wcy.server.dto.ReservationListItem;
import pl.wat.wcy.server.dto.ReservationRequest;
import pl.wat.wcy.server.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping()
    public ResponseEntity<List<ReservationListItem>> getReservations() {
        return null;
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Long reservationId) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody ReservationRequest reservationRequest) {
        return null;
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        return null;
    }
}
