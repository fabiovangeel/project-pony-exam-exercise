package be.ucll.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Animal;
import be.ucll.model.DomainException;
import be.ucll.model.MedicalRecord;
import be.ucll.service.MedicalRecordService;
import be.ucll.service.ServiceException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/medicalrecords")
public class MedicalRecordRestController {

    private MedicalRecordService medicalRecordService;

    public MedicalRecordRestController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException.class)
    public Map<String, String> handleDomainException(DomainException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("DomainException", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }

    @PostMapping("/{name}")
    public MedicalRecord addMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord, @PathVariable String name) {
        return medicalRecordService.addMedicalRecord(medicalRecord, name);
    }

    @PutMapping("/close/{id}")
    public MedicalRecord closeMedicalRecord(@PathVariable Long id) {
        return medicalRecordService.closMedicalRecord(id);
    }

    @GetMapping("/open")
    public List<Animal> getaAnimalWithOpenMedicalRecord() {
        return medicalRecordService.getAllAnimalsWithOpenMedicalRecord();
    }

    @GetMapping("/{name}/{date}")
    public List<MedicalRecord> getMedicalRecordsByAnimalAfter(@PathVariable String name, @PathVariable LocalDate date) {
        return medicalRecordService.getMedicalRecordsByAnimalAfter(name, date);
    }

}
