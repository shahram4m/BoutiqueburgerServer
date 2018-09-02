package ir.server.boutiqueburger.repository;

import ir.server.boutiqueburger.models.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {

}
