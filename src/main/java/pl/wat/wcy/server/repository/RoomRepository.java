package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
