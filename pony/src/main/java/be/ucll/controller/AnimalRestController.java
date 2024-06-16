package be.ucll.controller;

import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Animal;
import be.ucll.model.Stable;
import be.ucll.model.Toy;
import be.ucll.service.AnimalService;
import be.ucll.service.ServiceException;
import be.ucll.service.StableService;
import be.ucll.service.ToyService;
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
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("animals")
public class AnimalRestController {
    private AnimalService animalService;
    private StableService stableService;
    private ToyService toyService;

    public AnimalRestController(AnimalService animalService, StableService stableService, ToyService toyService) {
        this.animalService = animalService;
        this.stableService = stableService;
        this.toyService = toyService;
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

    @PostMapping("/{animalName}/stable")
    public Stable addAnimalToNewStable(@PathVariable String animalName, @Valid @RequestBody Stable stable) {
        return animalService.addAnimalToNewStable(animalName, stable);
    }

    @PostMapping("/{animalName}")
    public Stable addAnimalToExistingStable(@PathVariable String animalName,
            @RequestParam(value = "stableId", required = true) Long stableId) {
        return animalService.addAnimalToExistingStable(animalName, stableId);
    }

    @GetMapping("/stables")
    public List<Stable> getAllStables() {
        return stableService.getAllStables();
    }

    @GetMapping("/{animalName}/stable")
    public Stable getStableByAnimalName(@PathVariable String animalName) {
        return stableService.findStableByAnimal(animalName);
    }

    @PostMapping("/toy")
    public Toy addToy(@Valid @RequestBody Toy toy) {
        return toyService.addToy(toy);
    }

    @PutMapping("{name}/toy/{id}")
    public Animal assignToyToAnimal(@PathVariable String name, @PathVariable Long id) {
        return toyService.assignToyToAnimal(name, id);
    }

}
