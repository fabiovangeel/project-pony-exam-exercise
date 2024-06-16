package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.Toy;

@Repository
public interface ToyRepository extends JpaRepository<Toy, Long> {
}
