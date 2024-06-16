package unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.model.Animal;
import be.ucll.model.Toy;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.ToyRepository;
import be.ucll.service.ServiceException;
import be.ucll.service.ToyService;

@ExtendWith(MockitoExtension.class)
public class ToyServiceTest {

    @Mock
    ToyRepository toyRepository;

    @Mock
    AnimalRepository animalRepository;

    @InjectMocks
    ToyService toyService;

    @Test
    public void givenToy_whenAddToy_thenToyIsAdded() {
        Toy toy = new Toy("Bone");
        when(toyRepository.save(toy)).thenReturn(toy);

        Toy returnedToy = toyService.addToy(toy);

        Mockito.verify(toyRepository).save(toy);
        assertEquals(toy, returnedToy);
    }

    @Test
    public void givenToyIdAndAnimalName_whenAssignToyToAnimal_thenToyIsAsignedToAnimal() {
        Toy toy = new Toy("Bone");
        Animal suzy = Mockito.mock(Animal.class);
        when(toyRepository.findById(toy.getId())).thenReturn(Optional.of(toy));
        when(animalRepository.findById(suzy.getName())).thenReturn(Optional.of(suzy));
        doNothing().when(suzy).addToy(toy);
        when(animalRepository.save(suzy)).thenReturn(suzy);

        Animal returnedAnimal = toyService.assignToyToAnimal(suzy.getName(), toy.getId());

        assertEquals(suzy, returnedAnimal);
    }

    @Test
    public void givenToyIdAndAnimalName_whenToyNotFound_thenExceptionIsThorwn() {
        Toy toy = new Toy("Bone");
        Animal suzy = Mockito.mock(Animal.class);
        when(toyRepository.findById(toy.getId())).thenThrow(new ServiceException("Toy not found."));

        Exception ex = assertThrows(ServiceException.class,
                () -> toyService.assignToyToAnimal(suzy.getName(), toy.getId()));

        assertEquals("Toy not found.", ex.getMessage());
    }

    @Test
    public void givenToyIdAndAnimalName_whenAnimalNotFound_thenExceptionIsThorwn() {
        Toy toy = new Toy("Bone");
        Animal suzy = Mockito.mock(Animal.class);
        when(toyRepository.findById(toy.getId())).thenReturn(Optional.of(toy));
        when(animalRepository.findById(suzy.getName())).thenThrow(new ServiceException("Animal not found."));

        Exception ex = assertThrows(ServiceException.class,
                () -> toyService.assignToyToAnimal(suzy.getName(), toy.getId()));

        assertEquals("Animal not found.", ex.getMessage());
    }
}
