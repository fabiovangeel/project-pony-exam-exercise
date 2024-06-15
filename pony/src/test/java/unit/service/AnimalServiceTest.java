package unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.model.Animal;
import be.ucll.model.Stable;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.StableRepository;
import be.ucll.service.AnimalService;
import be.ucll.service.ServiceException;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    AnimalRepository animalRepository;

    @Mock
    StableRepository stableRepository;

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

    @Test
    public void givenAnimal_whenAddAnimalToNewStable_ThenStableIsCreatedAndAnimalIsAdded() {
        Animal animal = new Animal("Luna", 3);
        Stable stable = new Stable("StblHn", 5);
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(animal));
        when(stableRepository.save(stable)).thenReturn(stable);

        Stable result = animalService.addAnimalToNewStable("Luna", stable);

        verify(stableRepository).save(stable);
        assertEquals(stable, animal.getStable());
    }

    @Test
    public void givenAddAnimalToNewStable_whenAnimalNotExists_thenExceptionIsThrown() {
        Stable stable = new Stable("StblHn", 5);
        when(animalRepository.findById("aefgeg")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ServiceException.class, () -> animalService.addAnimalToNewStable("aefgeg", stable));

        assertEquals(ex.getMessage(), "Animal does not exist.");
        Mockito.verify(animalRepository).findById("aefgeg");
    }

    @Test
    public void givenAddAnimalToNewStable_whenAnimalAlreadyInStable_thenExceptionIsThrown() {
        Animal mockedAnimal = Mockito.mock(Animal.class);
        Stable stable = new Stable("StblHn", 5);
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(mockedAnimal));
        when(mockedAnimal.getStable()).thenReturn(stable);

        Exception ex = assertThrows(ServiceException.class, () -> animalService.addAnimalToNewStable("Luna", stable));

        assertEquals(ex.getMessage(), "Animal is already in a stable.");
    }

    @Test
    public void givenAnimal_whenAddAnimalToExistingStable_ThenAnimalIsAddedToThatStable() {
        Animal animal = new Animal("Luna", 3);
        Stable stable = new Stable("StblHn", 5);
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(animal));
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));

        Stable result = animalService.addAnimalToExistingStable("Luna", stable.getId());

        Mockito.verify(stableRepository).save(stable);
        assertEquals(stable, result);
    }

    @Test
    public void givenAddAnimalToExistingStable_whenAnimalNotExists_thenExceptionIsThrown() {
        Stable stable = new Stable("StblHn", 5);
        when(animalRepository.findById("aefgeg")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ServiceException.class,
                () -> animalService.addAnimalToExistingStable("aefgeg", stable.getId()));

        assertEquals(ex.getMessage(), "Animal does not exist.");
        Mockito.verify(animalRepository).findById("aefgeg");
    }

    @Test
    public void givenAddAnimalToExistingStable_whenStableNotExists_thenExceptionIsThrown() {
        Stable stable = new Stable("StblHn", 5);
        Animal animal = new Animal("Luna", 3);
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(animal));
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.empty());
        Exception ex = assertThrows(ServiceException.class,
                () -> animalService.addAnimalToExistingStable("Luna", stable.getId()));

        assertEquals(ex.getMessage(), "Stable does not exist.");
        Mockito.verify(stableRepository).findById(stable.getId());
    }

    @Test
    public void givenAddAnimalToExistingStable_whenAnimalAlreadyInStable_thenExceptionIsThrown() {
        Stable stable = new Stable("StblHn", 5);
        Animal mockedAnimal = Mockito.mock(Animal.class);
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(mockedAnimal));
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));
        when(mockedAnimal.getStable()).thenReturn(stable);

        Exception ex = assertThrows(ServiceException.class,
                () -> animalService.addAnimalToExistingStable("Luna", stable.getId()));

        assertEquals(ex.getMessage(), "Animal is already in a stable.");
    }

    @Test
    public void givenAddAnimalToExistingStable_whenStableIsFull_thenExceptionIsThrown() {
        Animal animal = new Animal("Luna", 3);
        Stable stable = Mockito.mock(Stable.class);
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("Tom", 7));
        animals.add(new Animal("Simba", 5));
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(animal));
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));
        when(stable.getAnimals()).thenReturn(animals);
        when(stable.getMaxAnimals()).thenReturn(2);

        Exception ex = assertThrows(ServiceException.class,
                () -> animalService.addAnimalToExistingStable("Luna", stable.getId()));

        assertEquals(ex.getMessage(), "Stable is full. Cant add any animals to stable.");
    }
}
