package unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.model.Animal;
import be.ucll.model.MedicalRecord;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.MedicalRecordRepository;
import be.ucll.service.MedicalRecordService;
import be.ucll.service.ServiceException;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {
    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @Mock
    AnimalRepository animalRepository;

    @InjectMocks
    MedicalRecordService medicalRecordService;

    @Test
    public void givenMedicalRecord_whenAddMedicalRecord_thenMedicalRecordIsAdded() {
        Animal animal = new Animal("Ben", 4);
        MedicalRecord medicalRecord = Mockito.mock(MedicalRecord.class);
        when(animalRepository.findById(animal.getName())).thenReturn(Optional.of(animal));
        doNothing().when(medicalRecord).setAnimal(animal);
        when(animalRepository.save(animal)).thenReturn(animal);
        when(medicalRecordRepository.save(medicalRecord)).thenReturn(medicalRecord);

        MedicalRecord returnedmedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord, animal.getName());

        Mockito.verify(medicalRecord).setAnimal(animal);
        assertEquals(medicalRecord, returnedmedicalRecord);
    }

    @Test
    public void givenAddMedicalRecord_whenAnimalNotFound_thenExceptionisThrown() {
        Animal animal = new Animal("Ben", 4);
        MedicalRecord medicalRecord = Mockito.mock(MedicalRecord.class);
        when(animalRepository.findById(animal.getName())).thenThrow(new ServiceException("Animal not found."));

        Exception ex = assertThrows(ServiceException.class,
                () -> medicalRecordService.addMedicalRecord(medicalRecord, animal.getName()));
        assertEquals("Animal not found.", ex.getMessage());
    }

    @Test
    public void givenMedicalRecordId_whenCloseMedicalRecord_thenMedicalRecordIsClosed() {
        Animal animal = new Animal("Ben", 4);
        MedicalRecord medicalRecord = Mockito.mock(MedicalRecord.class);
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.of(medicalRecord));
        doNothing().when(medicalRecord).closeMedicalRecord();
        when(medicalRecordRepository.save(medicalRecord)).thenReturn(medicalRecord);

        MedicalRecord returneMedicalRecord = medicalRecordService.closMedicalRecord(medicalRecord.getId());

        Mockito.verify(medicalRecord).closeMedicalRecord();
        assertEquals(medicalRecord, returneMedicalRecord);
    }

    @Test
    public void givenCloseMedicalRecord_whenMedicalRecordNotFound_thenExceptionisThrown() {
        Animal animal = new Animal("Ben", 4);
        MedicalRecord medicalRecord = Mockito.mock(MedicalRecord.class);
        when(medicalRecordRepository.findById(medicalRecord.getId()))
                .thenThrow(new ServiceException("MedicalRecord not found."));

        Exception ex = assertThrows(ServiceException.class,
                () -> medicalRecordService.closMedicalRecord(medicalRecord.getId()));

        assertEquals(ex.getMessage(), "MedicalRecord not found.");
    }

    @Test
    public void givenAnimals_whenGetAllAnimalsWithOpenMedicalRecord_thenThoseAnimalsAreReturned() {
        Animal animal = new Animal("Ben", 4);
        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        when(animalRepository.findByMedicalRecordsIsNotNullAndMedicalRecordsClosingDateIsNull())
                .thenReturn(animals);

        List<Animal> returnedanimals = medicalRecordService.getAllAnimalsWithOpenMedicalRecord();

        Mockito.verify(animalRepository).findByMedicalRecordsIsNotNullAndMedicalRecordsClosingDateIsNull();
        assertEquals(animals, returnedanimals);
    }

    @Test
    public void getMedicalRecordsByAnimalAfter_WhenAnimalFound_ThenRecordsReturned() {
        LocalDate date = LocalDate.now().minusDays(1);
        Animal animal = new Animal("Ben", 4);
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord medicalRecord = Mockito.mock(MedicalRecord.class);
        medicalRecords.add(medicalRecord);

        when(animalRepository.findById(animal.getName())).thenReturn(Optional.of(animal));
        when(medicalRecordRepository.findByAnimalAndRegistrationDateAfter(animal, date)).thenReturn(medicalRecords);

        List<MedicalRecord> returnedMedicalRecords = medicalRecordService
                .getMedicalRecordsByAnimalAfter(animal.getName(), date);

        verify(animalRepository).findById(animal.getName());
        verify(medicalRecordRepository).findByAnimalAndRegistrationDateAfter(animal, date);
        assertEquals(medicalRecords, returnedMedicalRecords);
    }

    @Test
    public void getMedicalRecordsByAnimalAfter_WhenAnimalNotFound_ThenExceptionThrown() {
        LocalDate date = LocalDate.now().minusDays(1);
        Animal animal = new Animal("Ben", 4);

        when(animalRepository.findById(animal.getName())).thenThrow(new ServiceException("Animal not found."));

        ServiceException ex = assertThrows(ServiceException.class,
                () -> medicalRecordService.getMedicalRecordsByAnimalAfter(animal.getName(), date));

        assertEquals("Animal not found.", ex.getMessage());
    }
}
