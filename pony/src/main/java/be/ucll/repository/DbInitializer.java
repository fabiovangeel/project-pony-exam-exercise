package be.ucll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.ucll.model.Animal;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private AnimalRepository animalRepository;

    @Autowired
    public DbInitializer(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @PostConstruct
    public void initialize() {
        Animal ben = new Animal("Ben", 8);
        Animal tom = new Animal("Tom", 5);
        Animal freddy = new Animal("Freddy", 12);
        Animal tony = new Animal("Tony", 15);

        animalRepository.save(ben);
        animalRepository.save(tom);
        animalRepository.save(freddy);
        animalRepository.save(tony);
    }
}
