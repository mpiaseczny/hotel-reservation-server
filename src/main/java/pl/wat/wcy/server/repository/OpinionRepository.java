package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Opinion;

import java.util.List;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    List<Opinion> findByHotel_Id(Long id);

}
