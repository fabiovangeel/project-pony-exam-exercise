package be.ucll.service;

import java.util.List;
import org.springframework.stereotype.Service;

import be.ucll.model.Animal;
import be.ucll.model.Stable;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.StableRepository;

@Service
public class StableService {

    private StableRepository stableRepository;
    private AnimalRepository animalRepository;

    public StableService(StableRepository stableRepository, AnimalRepository animalRepository) {
        this.stableRepository = stableRepository;
        this.animalRepository = animalRepository;
    }

    public List<Stable> getAllStables() {
        return stableRepository.findAll();
    }

    public Stable findStableByAnimal(String name) {
        Animal animal = animalRepository.findById(name).orElse(null);
        if (animal == null) {
            throw new ServiceException("Animal does not exist.");
        }
        if (animal.getStable() == null) {
            throw new ServiceException("Animal isnt in a stable.");
        }
        return animal.getStable();
    }
}
