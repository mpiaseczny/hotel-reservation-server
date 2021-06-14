package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
