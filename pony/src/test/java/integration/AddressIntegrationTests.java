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
public class AddressIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setup() {
        dbInitializer.initialize();
    }

    @Test
    public void givenAddress_whenAddAddress_thenAddressIsAdded() {
        webTestClient.post()
                .uri("/address")
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                        "street": "Halensebaan",
                        "number": 18,
                        "place": "Kortenaken"
                        }
                        """)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                        "id": 3,
                        "street": "Halensebaan",
                        "number": 18,
                        "place": "Kortenaken"
                        }
                        """);
    }

    @Test
    public void givenNewStableWithNewAddress_whenAddingStableAndAddress_thenStableAndAddressAreAdded() {
        webTestClient.post()
                .uri("/address/stable")
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                        "name": "cowStable",
                        "maxAnimals": 8,
                        "address": {
                            "street": "halensebaan",
                            "number": 18,
                            "place": "Kortenaken"
                        }
                        }
                        """)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "id": 4,
                          "name": "cowStable",
                          "maxAnimals": 8,
                          "animals": [],
                          "address": {
                            "id": 3,
                            "street": "halensebaan",
                            "number": 18,
                            "place": "Kortenaken"
                          }
                        }
                        """);
    }

    @Test
    public void givenAddressIdAndStableId_whenAsigningAddressToStable_thenAddressIsAssignedToStable() {
        webTestClient.post()
                .uri("/address/2/2")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "id": 2,
                          "name": "PonyStable",
                          "maxAnimals": 10,
                          "animals": [
                            {
                              "name": "Tony",
                              "age": 15
                            }
                          ],
                          "address": {
                            "id": null,
                            "street": "Ponyweg",
                            "number": 9,
                            "place": "Waanrode"
                          }
                        }
                        """);
    }

    @Test
    public void givenStables_whenGetAllAddressesWithStableMoreThan3Animals_thenReturnThoseAddresses() {
        webTestClient.get()
                .uri("/address/stable/3animals")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        [
                          {
                            "id": null,
                            "street": "Halensebaan",
                            "number": 9,
                            "place": "Waanrode"
                          }
                        ]
                        """);
    }
}
