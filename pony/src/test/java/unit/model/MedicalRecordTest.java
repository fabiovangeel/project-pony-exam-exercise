package unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.model.DomainException;
import be.ucll.model.MedicalRecord;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MedicalRecordTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidValues_whenCreatingMedicalRecord_thenMedicalrecordIsCreatedWithThoseValues() {
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 1, 12), "Animal had fever.");

        assertEquals(medicalRecord.getRegistrationDate(), LocalDate.of(2024, 1, 12));
        assertEquals(medicalRecord.getClosingDate(), null);
        assertEquals(medicalRecord.getDescription(), "Animal had fever.");
    }

    @Test
    public void givenInvalidRegistrationDate_whenCreatingMedicalRecord_thenExceptionIsThrown() {
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.now().plusDays(1), "Animal had fever.");

        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Registration date cannot be in the future.");
    }

    @Test
    public void givenInvalidClosingDate_whenCreatingMedicalRecord_thenExceptionIsThrown() {
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.now(), "Animal had fever.");

        Exception ex = assertThrows(DomainException.class, () -> medicalRecord.setClosingDate(LocalDate.now()));
        assertEquals(ex.getMessage(), "Closing date must be at least 1 day after Registration date");
    }

    @Test
    public void givenInvalidDescription_whenCreatingMedicalRecord_thenExceptionIsThrown() {
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.now(), "      ");

        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Description is required.");
    }

    @Test
    public void givenNullDescription_whenCreatingMedicalRecord_thenExceptionIsThrown() {
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.now(), null);

        Set<ConstraintViolation<MedicalRecord>> violations = validator.validate(medicalRecord);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Description is required.");
    }
}
