package unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.model.Address;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AddressTest {
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
    public void givenValidValues_whenCreatingAddress_thenAddressIsCreatedWithThoseValues() {
        Address address = new Address("Halensebaan", 9, "Waanrode");

        assertEquals(address.getStreet(), "Halensebaan");
        assertEquals(address.getNumber(), 9);
        assertEquals(address.getPlace(), "Waanrode");
    }

    @Test
    public void givenInvalidStreet_whenCreatingAddress_thenExceptionIsThrown() {
        Address address = new Address("     ", 9, "Waanrode");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Street is required.");
    }

    @Test
    public void givenNullStreet_whenCreatingAddress_thenExceptionIsThrown() {
        Address address = new Address(null, 9, "Waanrode");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Street is required.");
    }

    @Test
    public void givenInvalidNumber_whenCreatingAddress_thenExceptionIsThrown() {
        Address address = new Address("Halensebaan", 0, "Waanrode");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Streetnumber must be a positive integer.");
    }

    @Test
    public void givenInvalidPlace_whenCreatingAddress_thenExceptionIsThrown() {
        Address address = new Address("Halensebaan", 9, "      ");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Place is required.");
    }

    @Test
    public void givenNullPlace_whenCreatingAddress_thenExceptionIsThrown() {
        Address address = new Address("Halensebaan", 9, null);

        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(violations.size(), 1);
        assertEquals(violations.iterator().next().getMessage(), "Place is required.");
    }
}
