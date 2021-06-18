package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoom_IdOrderByDateFromAsc(Long id);

    List<Reservation> findByUser_IdOrderByDateFromAsc(Long id);

    long deleteByRoom_Id(Long id);

    long deleteByRoom_Hotel_Id(Long id);

    long removeById(Long id);
}
