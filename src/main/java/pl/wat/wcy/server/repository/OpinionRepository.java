package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Opinion;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
}
