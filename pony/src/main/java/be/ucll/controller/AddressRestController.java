package be.ucll.controller;

import java.util.HashMap;
import java.util.Map;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Address;
import be.ucll.model.Stable;
import be.ucll.service.AddressService;
import be.ucll.service.AnimalService;
import be.ucll.service.ServiceException;
import be.ucll.service.StableService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("address")
public class AddressRestController {
    private AnimalService animalService;
    private StableService stableService;
    private AddressService addressService;

    public AddressRestController(AnimalService animalService, StableService stableService,
            AddressService addressService) {
        this.animalService = animalService;
        this.stableService = stableService;
        this.addressService = addressService;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }

    @PostMapping
    public Address addAddress(@Valid @RequestBody Address address) {
        return addressService.addAddress(address);
    }

    @PostMapping("/stable")
    public Stable addNewAdressToNewStable(@Valid @RequestBody Stable stable) {
        return addressService.addStableToNewAddress(stable);
    }

    @PostMapping("/{addressId}/{stableId}")
    public Stable addExistingAddressToExistingStable(@PathVariable Long addressId, @PathVariable Long stableId) {
        return addressService.addExistingStableToExistingAddress(stableId, addressId);
    }

    @GetMapping("/stable/3animals")
    public List<Address> getAddressesWhereStableHasMoreThan3Animals() {
        return addressService.getAddressesWhereStableHasMoreThan3Animals();
    }

}
