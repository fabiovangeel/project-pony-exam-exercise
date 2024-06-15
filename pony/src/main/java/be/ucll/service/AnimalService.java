package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Animal;
import be.ucll.model.Stable;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.StableRepository;

@Service
public class AnimalService {

    private AnimalRepository animalRepository;
    private StableRepository stableRepository;

    public AnimalService(AnimalRepository animalRepository, StableRepository stableRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;

    }

    public Animal addAnimal(Animal animal) {
        if (animalRepository.existsByName(animal.getName().toLowerCase())) {
            throw new ServiceException("Animal already exists.");
        }
        return animalRepository.save(animal);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Animal> getAnimalsByAgeAbove(int age) {
        return animalRepository.findByAgeGreaterThan(age);
    }

    public Animal getOldestAnimal() {
        return animalRepository.findFirstByOrderByAgeDesc();
    }

    public Stable addAnimalToNewStable(String name, Stable stable) {
        Animal animal = animalRepository.findById(name).orElse(null);
        if (animal == null) {
            throw new ServiceException("Animal does not exist.");
        }
        if (animal.getStable() != null) {
            throw new ServiceException("Animal is already in a stable.");
        }
        animal.setStable(stable);
        stable.addAnimal(animal);
        stableRepository.save(stable);
        return stable;
    }

    public Stable addAnimalToExistingStable(String name, Long stableId) {
        Animal animal = animalRepository.findById(name).orElse(null);
        Stable stable = stableRepository.findById(stableId).orElse(null);

        if (animal == null) {
            throw new ServiceException("Animal does not exist.");
        }
        if (stable == null) {
            throw new ServiceException("Stable does not exist.");
        }
        if (animal.getStable() != null) {
            throw new ServiceException("Animal is already in a stable.");
        }
        if (stable.getMaxAnimals() == stable.getAnimals().size()) {
            throw new ServiceException("Stable is full. Cant add any animals to stable.");
        }
        animal.setStable(stable);
        stable.addAnimal(animal);
        stableRepository.save(stable);

        return stable;
    }
}
