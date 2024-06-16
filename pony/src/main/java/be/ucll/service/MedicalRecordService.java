package be.ucll.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

import be.ucll.model.Animal;
import be.ucll.model.MedicalRecord;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {
    private MedicalRecordRepository medicalRecordRepository;
    private AnimalRepository animalRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, AnimalRepository animalRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.animalRepository = animalRepository;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord, String name) {
        Animal animal = animalRepository.findById(name).orElse(null);
        if (animal == null) {
            throw new ServiceException("Animal not found.");
        }
        medicalRecord.setAnimal(animal);
        animalRepository.save(animal);
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord closMedicalRecord(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id).orElse(null);
        if (medicalRecord == null) {
            throw new ServiceException("MedicalRecord not found.");
        }
        medicalRecord.closeMedicalRecord();
        return medicalRecordRepository.save(medicalRecord);
    }

    public List<Animal> getAllAnimalsWithOpenMedicalRecord() {
        return animalRepository.findByMedicalRecordsIsNotNullAndMedicalRecordsClosingDateIsNull();
    }

    public List<MedicalRecord> getMedicalRecordsByAnimalAfter(String name, LocalDate date) {
        Animal animal = animalRepository.findById(name).orElse(null);
        if (animal == null) {
            throw new ServiceException("Animal not found.");
        }
        return medicalRecordRepository.findByAnimalAndRegistrationDateAfter(animal, date);
    }
}
