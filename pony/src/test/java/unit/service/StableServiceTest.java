package unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import be.ucll.service.ServiceException;
import be.ucll.service.StableService;

@ExtendWith(MockitoExtension.class)
public class StableServiceTest {

    @Mock
    AnimalRepository animalRepository;

    @Mock
    StableRepository stableRepository;

    @InjectMocks
    StableService stableService;

    @Test
    public void givenGetAllStables_whenStablesInDatabase_thenReturnAllStables() {
        Stable homeStable = new Stable("HomeStable", 6);
        Stable ponyStable = new Stable("PonyStable", 10);
        List<Stable> stables = new ArrayList<>();
        stables.add(ponyStable);
        stables.add(homeStable);
        when(stableRepository.findAll()).thenReturn(stables);

        List<Stable> returnedStables = stableService.getAllStables();

        Mockito.verify(stableRepository).findAll();
        assertEquals(stables, returnedStables);
    }

    @Test
    public void givenAnimalName_whenFindStableByName_thenReturnStable() {
        Stable homeStable = new Stable("HomeStable", 6);
        Animal mockedAnimal = Mockito.mock(Animal.class);
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(mockedAnimal));
        when(mockedAnimal.getStable()).thenReturn(homeStable);

        Stable returnedStable = stableService.findStableByAnimal("Luna");

        Mockito.verify(animalRepository).findById("Luna");
        assertEquals(homeStable, returnedStable);
    }

    @Test
    public void givenFindStableByAnimalName_whenAnimalNotExists_thenThrowException() {
        Animal mockedAnimal = Mockito.mock(Animal.class);
        when(animalRepository.findById("aefg")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ServiceException.class, () -> stableService.findStableByAnimal("aefg"));

        assertEquals(ex.getMessage(), "Animal does not exist.");
        Mockito.verify(animalRepository).findById("aefg");
    }

    @Test
    public void givenFindStableByAnimalName_whenAnimalNotInStable_thenThrowException() {
        Animal mockedAnimal = Mockito.mock(Animal.class);
        when(animalRepository.findById("Luna")).thenReturn(Optional.of(mockedAnimal));
        when(mockedAnimal.getStable()).thenReturn(null);

        Exception ex = assertThrows(ServiceException.class, () -> stableService.findStableByAnimal("Luna"));

        assertEquals(ex.getMessage(), "Animal isnt in a stable.");
    }
}
