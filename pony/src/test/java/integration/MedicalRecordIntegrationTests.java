package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.AnimalApplication;
import be.ucll.repository.DbInitializer;

@SpringBootTest(classes = AnimalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql("classpath:schema.sql")
public class MedicalRecordIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setup() {
        dbInitializer.initialize();
    }

    @Test
    public void givenMedicalRecord_whenAddMedicalRecord_thenMedicalRecordIsAdded() {
        webTestClient.post()
                .uri("/medicalrecords/Juul")
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                        "registrationDate": "2024-01-12",
                        "description": "Animal had a wound."
                        }
                        """)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                        "id": 2,
                        "registrationDate": "2024-01-12",
                        "closingDate": null,
                        "description": "Animal had a wound."
                        }
                        """);
    }

    @Test
    public void givenMedicalRecordId_whenCloseMedicalRecord_thenMedicalRecordIsClosed() {
        webTestClient.put()
                .uri("medicalrecords/close/1")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                        "id": 1,
                        "registrationDate": "2024-01-12",
                        "closingDate": "2024-06-16",
                        "description": "Animal had a fever."
                        }
                        """);
    }

    @Test
    public void givenAnimals_whenGetAllAnimalsWithOpenMedicalRecord_thenThoseAnimalsAreReturned() {
        webTestClient.get()
                .uri("medicalrecords/open")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        [
                        {
                            "name": "Ben",
                            "age": 8,
                            "toys": [
                            {
                                "id": 1,
                                "name": "Ball"
                            }
                            ],
                            "medicalRecords": [
                            {
                                "id": 1,
                                "registrationDate": "2024-01-12",
                                "closingDate": null,
                                "description": "Animal had a fever."
                            }
                            ]
                        }
                        ]
                        """);
    }

    @Test
    public void getMedicalRecordsByAnimalAfter_WhenAnimalFound_ThenRecordsReturned() {
        webTestClient.get()
                .uri("medicalrecords/Ben/2024-01-11")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        [
                        {
                            "id": 1,
                            "registrationDate": "2024-01-12",
                            "closingDate": null,
                            "description": "Animal had a fever."
                        }
                        ]
                        """);
    }

}
