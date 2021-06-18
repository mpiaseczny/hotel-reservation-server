package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Attachment;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findByHotel_Id(Long id);

    List<Attachment> findByRoom_Id(Long id);

    void deleteByHotel_Id(Long id);

    void deleteByRoom_Id(Long id);
}
