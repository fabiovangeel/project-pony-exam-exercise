package unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import be.ucll.model.Animal;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AnimalTest {

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
    public void givenValidValues_whenCreatingAnimal_thenAnimalIsCreatedWithThoseValues() {
        Animal animal = new Animal("Ben", 10);

        assertEquals(animal.getName(), "Ben");
        assertEquals(animal.getAge(), 10);
    }

    @Test
    public void givenInvalidName_whenCreatingAnimal_thenExceptionIsThrown() {
        Animal animal = new Animal("   ", 10);
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        assertEquals(1, violations.size());
        assertEquals("Name is required.", violations.iterator().next().getMessage());
    }

    @Test
    public void givenNullName_whenCreatingAnimal_thenExceptionIsThrown() {
        Animal animal = new Animal(null, 10);
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        assertEquals(1, violations.size());
        assertEquals("Name is required.", violations.iterator().next().getMessage());
    }

    @Test
    public void givenInvalidAgeUnder1_whenCreatingAnimal_thenExceptionIsThrown() {
        Animal animal = new Animal("Ben", 0);
        Set<ConstraintViolation<Animal>> violations = validator.validate(animal);
        assertEquals(1, violations.size());
        assertEquals("Age must be a positive integer between 1 and 50.", violations.iterator().next().getMessage());
    }
}
