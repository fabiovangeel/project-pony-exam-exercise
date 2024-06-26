package be.ucll.repository;

import org.springframework.stereotype.Repository;

import be.ucll.model.Animal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, String> {
    Boolean existsByName(String name);

    List<Animal> findByAgeGreaterThan(int age);

    Animal findFirstByOrderByAgeDesc();

    Optional<Animal> findById(String Name);

    List<Animal> findByMedicalRecordsIsNotNullAndMedicalRecordsClosingDateIsNull();
}
