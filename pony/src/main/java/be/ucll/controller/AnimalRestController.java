package be.ucll.controller;

import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Animal;
import be.ucll.service.AnimalService;
import be.ucll.service.ServiceException;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("animals")
public class AnimalRestController {
    private AnimalService animalService;

    public AnimalRestController(AnimalService animalService) {
        this.animalService = animalService;
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
    public Animal addAnimal(@Valid @RequestBody Animal animal) {
        return animalService.addAnimal(animal);
    }

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/age/{age}")
    public List<Animal> getAnimalsByAgeAbove(@PathVariable int age) {
        return animalService.getAnimalsByAgeAbove(age);
    }

    @GetMapping("/age/oldest")
    public Animal getOldestAnimal() {
        return animalService.getOldestAnimal();
    }

}
