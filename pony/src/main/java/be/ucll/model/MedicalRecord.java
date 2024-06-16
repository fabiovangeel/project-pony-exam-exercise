package be.ucll.model;

import java.time.LocalDate;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "medical_record")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent(message = "Registration date cannot be in the future.")
    private LocalDate registrationDate;

    private LocalDate closingDate;

    @NotBlank(message = "Description is required.")
    private String description;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    @JsonBackReference
    private Animal animal;

    public MedicalRecord(LocalDate registrationDate, String description) {
        this.registrationDate = registrationDate;
        this.closingDate = null;
        this.description = description;
    }

    protected MedicalRecord() {
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        if (closingDate.isBefore(registrationDate.plusDays(1))) {
            throw new DomainException("Closing date must be at least 1 day after Registration date");
        }
        this.closingDate = closingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void closeMedicalRecord() {
        closingDate = LocalDate.now();
    }
}
