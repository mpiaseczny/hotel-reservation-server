package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
