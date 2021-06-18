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
        return reservationService.getReservations();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Long reservationId) {
        return reservationService.getReservation(reservationId);
    }

    @PostMapping()
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.addReservation(reservationRequest);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Long> deleteReservation(@PathVariable Long reservationId) {
        return reservationService.deleteReservation(reservationId);
    }
}
