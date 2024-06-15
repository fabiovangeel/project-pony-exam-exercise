package be.ucll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.ucll.model.Animal;
import be.ucll.model.Stable;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private AnimalRepository animalRepository;
    private StableRepository stableRepository;

    @Autowired
    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
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

        Stable homeStable = new Stable("HomeStable", 6);
        Stable ponyStable = new Stable("PonyStable", 10);
        Stable chickenStable = new Stable("ChickenStable", 4);

        ben.setStable(homeStable);
        tony.setStable(ponyStable);

        homeStable.addAnimal(ben);
        ponyStable.addAnimal(tony);

        stableRepository.save(homeStable);
        stableRepository.save(ponyStable);
        stableRepository.save(chickenStable);
        animalRepository.save(ben);
        animalRepository.save(tony);
    }
}
