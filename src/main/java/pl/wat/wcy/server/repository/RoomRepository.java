package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import pl.wat.wcy.server.dao.Room;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    long deleteByHotel_Id(Long id);

    List<Room> findDistinctByNameOrHotel_CityAllIgnoreCase(@Nullable String name, @Nullable String city);

    long removeById(Long id);

}
