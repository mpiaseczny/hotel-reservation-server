package pl.wat.wcy.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.wcy.server.dao.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
