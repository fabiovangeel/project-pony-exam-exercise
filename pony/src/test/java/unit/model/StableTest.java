package unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import be.ucll.model.Stable;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class StableTest {
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
    public void givenValidValues_whenCreatingStable_thenStableIsCreatedWithThoseValues() {
        Stable stable = new Stable("HomeStable", 6);

        assertEquals(stable.getName(), "HomeStable");
        assertEquals(stable.getMaxAnimals(), 6);
    }

    @Test
    public void givenInvalidName_whenCreatingAStable_thenExceptionIsThrown() {
        Stable stable = new Stable("     ", 6);

        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Name is required.");
    }

    @Test
    public void givenNullName_whenCreatingAStable_thenExceptionIsThrown() {
        Stable stable = new Stable(null, 6);

        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Name is required.");
    }

    @Test
    public void givenInvalidMaxAnimals_whenCreatingAStable_thenExceptionIsThrown() {
        Stable stable = new Stable("HomeStable", 0);

        Set<ConstraintViolation<Stable>> violations = validator.validate(stable);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(),
                "Maximum Animals in Stable must be a positive integer.");
    }

}
