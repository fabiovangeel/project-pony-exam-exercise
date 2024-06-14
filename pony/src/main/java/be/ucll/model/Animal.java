package be.ucll.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected Animal() {
    };

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

}