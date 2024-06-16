package be.ucll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.ucll.model.Address;
import be.ucll.model.Animal;
import be.ucll.model.Stable;
import be.ucll.model.Toy;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private AnimalRepository animalRepository;
    private StableRepository stableRepository;
    private AddressRepository addressRepository;
    private ToyRepository toyRepository;

    @Autowired
    public DbInitializer(AnimalRepository animalRepository, StableRepository stableRepository,
            AddressRepository addressRepository, ToyRepository toyRepository) {
        this.animalRepository = animalRepository;
        this.stableRepository = stableRepository;
        this.addressRepository = addressRepository;
        this.toyRepository = toyRepository;
    }

    @PostConstruct
    public void initialize() {
        Animal ben = new Animal("Ben", 8);
        Animal tom = new Animal("Tom", 5);
        Animal freddy = new Animal("Freddy", 12);
        Animal tony = new Animal("Tony", 15);
        Animal juul = new Animal("Juul", 5);
        Animal luna = new Animal("Luna", 5);

        animalRepository.save(ben);
        animalRepository.save(tom);
        animalRepository.save(freddy);
        animalRepository.save(tony);
        animalRepository.save(juul);
        animalRepository.save(luna);

        Stable homeStable = new Stable("HomeStable", 6);
        Stable ponyStable = new Stable("PonyStable", 10);
        Stable chickenStable = new Stable("ChickenStable", 4);

        ben.setStable(homeStable);
        juul.setStable(homeStable);
        luna.setStable(homeStable);
        tony.setStable(ponyStable);

        homeStable.addAnimal(ben);
        homeStable.addAnimal(juul);
        homeStable.addAnimal(luna);
        ponyStable.addAnimal(tony);

        stableRepository.save(homeStable);
        stableRepository.save(ponyStable);
        stableRepository.save(chickenStable);
        animalRepository.save(ben);
        animalRepository.save(tony);
        animalRepository.save(luna);
        animalRepository.save(juul);

        Address homeAddress = new Address("Halensebaan", 9, "Waanrode");
        addressRepository.addAddress(homeAddress);
        Address ponyStableAddress = new Address("Ponyweg", 9, "Waanrode");
        addressRepository.addAddress(ponyStableAddress);

        homeStable.setAddress(homeAddress);
        stableRepository.save(homeStable);

        Toy ball = new Toy("Ball");
        toyRepository.save(ball);
        ben.addToy(ball);
        animalRepository.save(ben);

        toyRepository.save(ball);
        Toy stick = new Toy("Stick");
        toyRepository.save(stick);

    }
}
