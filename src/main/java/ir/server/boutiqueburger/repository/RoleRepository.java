package ir.server.boutiqueburger.repository;

import ir.server.boutiqueburger.models.Role;
import ir.server.boutiqueburger.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}