package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.AnimalApplication;
import be.ucll.repository.AnimalRepository;
import be.ucll.repository.DbInitializer;

@SpringBootTest(classes = AnimalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql("classpath:schema.sql")
public class AnimalIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setup() {
        dbInitializer.initialize();
    }

    @Test
    public void givenAnimals_whenAddAnimal_thenAnimalIsAdded() {
        webTestClient.post()
                .uri("/animals")
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                            "name": "Suzy",
                            "age": 50
                        }
                        """)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                            "name": "Suzy",
                            "age": 50
                        }
                        """);
    }

    @Test
    public void givenAnimals_whenGetAllAnimals_thenAllAnimalsAreReturned() {
        webTestClient.get()
                .uri("/animals")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        [
                        {
                            "name": "Ben",
                            "age": 8
                        },
                        {
                            "name": "Tom",
                            "age": 5
                        },
                        {
                            "name": "Freddy",
                            "age": 12
                        },
                        {
                            "name": "Tony",
                            "age": 15
                        }
                        ]
                        """);
    }

    @Test
    public void givenAnimals_whenGetAllAnimalsAboveCertainAge_thenAnimalsAboveThatAgeAreReturned() {
        webTestClient.get()
                .uri("/animals/age/8")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        [
                        {
                            "name": "Freddy",
                            "age": 12
                        },
                        {
                            "name": "Tony",
                            "age": 15
                        }
                        ]
                        """);
    }

    @Test
    public void givenAnimals_whenGetOldestAnimal_thenOldestAnimalIsReturned() {
        webTestClient.get()
                .uri("/animals/age/oldest")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                        "name": "Tony",
                        "age": 15
                        }
                        """);
    }
}
