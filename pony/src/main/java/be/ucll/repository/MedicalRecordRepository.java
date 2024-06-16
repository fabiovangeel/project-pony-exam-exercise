package be.ucll.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.Animal;
import be.ucll.model.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByAnimalAndRegistrationDateAfter(Animal animal, LocalDate date);
}
