package ir.server.boutiqueburger.repository;

import ir.server.boutiqueburger.models.Booth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {

    Optional<Booth> findById(Long boothId);

    Page<Booth> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Booth> findByIdIn(List<Long> boothIds);

    List<Booth> findByIdIn(List<Long> boothIds, Sort sort);

}
