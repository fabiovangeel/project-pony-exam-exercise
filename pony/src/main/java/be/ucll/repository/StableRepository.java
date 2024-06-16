package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.Stable;
import java.util.List;
import java.util.Optional;
import be.ucll.model.Animal;

@Repository
public interface StableRepository extends JpaRepository<Stable, Long> {
    Stable findByAddressId(Long id);
}
