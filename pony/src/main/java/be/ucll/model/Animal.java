package be.ucll.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "MY_ANIMALS")
public class Animal {

    @Id
    @NotBlank(message = "Name is required.")
    private String name;

    @Column(name = "age")
    @Min(value = 1, message = "Age must be a positive integer between 1 and 50.")
    @Max(value = 50, message = "Age must be a positive integer between 1 and 50.")
    private int age;

    @ManyToOne
    @JoinColumn(name = "stable_id")
    @JsonBackReference
    private Stable stable;

    @ManyToMany
    @JoinTable(name = "ANIMAL_TOYS", joinColumns = @JoinColumn(name = "animal_id"), inverseJoinColumns = @JoinColumn(name = "toy_id"))
    private List<Toy> toys = new ArrayList<>();

    @OneToMany(mappedBy = "animal")
    @JsonManagedReference
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected Animal() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Stable getStable() {
        return stable;
    }

    public void setStable(Stable stable) {
        this.stable = stable;
    }

    public void addToy(Toy toy) {
        toys.add(toy);
        toy.getAnimals().add(this);
    }

    public List<Toy> getToys() {
        return toys;
    }

    public void setToys(List<Toy> toys) {
        this.toys = toys;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
    }
}
