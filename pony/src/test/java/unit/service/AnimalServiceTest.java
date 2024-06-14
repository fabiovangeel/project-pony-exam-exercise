package unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.model.Animal;
import be.ucll.repository.AnimalRepository;
import be.ucll.service.AnimalService;
import be.ucll.service.ServiceException;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    AnimalRepository animalRepository;

    @InjectMocks
    AnimalService animalService;

    @Test
    public void givenNewAnimal_whenSavingAnimal_thenItIsStoredInTheDatabase() {
        Animal suzy = new Animal("Suzy", 5);
        Mockito.when(animalRepository.save(suzy)).thenReturn(suzy);
        Animal saved = animalService.addAnimal(suzy);

        Mockito.verify(animalRepository).save(suzy);
        assertEquals(saved, suzy);
    }

    @Test
    public void givenExistingAnimal_whenSavingAnimal_thenExceptionIsThrown() {
        Animal animal = new Animal("Suzy", 15);
        Mockito.when(animalRepository.existsByName("suzy")).thenReturn(true);

        Exception ex = Assertions.assertThrows(ServiceException.class, () -> animalService.addAnimal(animal));
        assertEquals(ex.getMessage(), "Animal already exists.");
        Mockito.verify(animalRepository).existsByName("suzy");
    }

    @Test
    public void givenFindAllAnimals_WhenAnimalsInDatabase_ThenReturnAnimals() {
        Animal ben = new Animal("Ben", 8);
        Animal tom = new Animal("Tom", 5);
        List<Animal> animals = new ArrayList<>();
        animals.add(tom);
        animals.add(ben);
        Mockito.when(animalRepository.findAll()).thenReturn(animals);

        List<Animal> returned = animalService.getAllAnimals();
        Mockito.verify(animalRepository).findAll();

        assertEquals(animals, returned);
    }

    @Test
    public void givenAnimals_whenGetAnimalsAboveAge_thenThoseAnimalsAreReturned() {
        Animal ben = new Animal("Ben", 8);
        Animal tom = new Animal("Tom", 10);
        List<Animal> animals = new ArrayList<>();
        animals.add(tom);
        animals.add(ben);
        Mockito.when(animalRepository.findByAgeGreaterThan(6)).thenReturn(animals);

        List<Animal> returned = animalService.getAnimalsByAgeAbove(6);
        Mockito.verify(animalRepository).findByAgeGreaterThan(6);

        assertEquals(animals, returned);
    }

    @Test
    public void givenAnimals_whenGetOldestAnimal_thenOldestAnimalIsReturned() {
        Animal tom = new Animal("Tom", 10);
        Mockito.when(animalRepository.findFirstByOrderByAgeDesc()).thenReturn(tom);

        Animal returned = animalService.getOldestAnimal();
        Mockito.verify(animalRepository).findFirstByOrderByAgeDesc();

        assertEquals(tom, returned);
    }
}
