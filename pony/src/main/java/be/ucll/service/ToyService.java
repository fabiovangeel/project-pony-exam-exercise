package be.ucll.service;

import org.springframework.stereotype.Service;

import be.ucll.model.Animal;
import be.ucll.model.Toy;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.ToyRepository;

@Service
public class ToyService {

    private ToyRepository toyRepository;
    private AnimalRepository animalRepository;

    public ToyService(ToyRepository toyRepository, AnimalRepository animalRepository) {
        this.toyRepository = toyRepository;
        this.animalRepository = animalRepository;
    }

    public Toy addToy(Toy toy) {
        return toyRepository.save(toy);
    }

    public Animal assignToyToAnimal(String name, Long id) {
        Toy toy = toyRepository.findById(id).orElse(null);
        Animal animal = animalRepository.findById(name).orElse(null);
        if (toy == null) {
            throw new ServiceException("Toy not found.");
        }
        if (animal == null) {
            throw new ServiceException("Animal not found");
        }
        animal.addToy(toy);
        toyRepository.save(toy);
        return animalRepository.save(animal);
    }
}
