package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Animal;
import be.ucll.repository.AnimalRepository;

@Service
public class AnimalService {

    private AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
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
}
