package ir.server.boutiqueburger.repository;

import ir.server.boutiqueburger.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> FindByEmail(String email);

    Optional<User> FindByUsernameOrEmail(String username, String email);

    List<User> FindByIdIn(List<Long> userIds);

    Optional<User> FindByUsername(String username);

    Boolean ExistsByUsername(String username);

    Boolean ExistsByEmail(String email);
}