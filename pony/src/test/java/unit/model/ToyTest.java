package unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.model.Toy;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ToyTest {
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
    public void givenValidvalues_whenCreatingToy_thenToyIsCreatedWithThoseValues() {
        Toy toy = new Toy("Ball");
        assertEquals(toy.getName(), "Ball");
    }

    @Test
    public void givenInvalidName_whenCreatingToy_thenExceptionIsThrown() {
        Toy toy = new Toy("      ");

        Set<ConstraintViolation<Toy>> violations = validator.validate(toy);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Name is required.");
    }

    @Test
    public void givenNullName_whenCreatingToy_thenExceptionIsThrown() {
        Toy toy = new Toy(null);

        Set<ConstraintViolation<Toy>> violations = validator.validate(toy);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Name is required.");
    }
}
