package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
