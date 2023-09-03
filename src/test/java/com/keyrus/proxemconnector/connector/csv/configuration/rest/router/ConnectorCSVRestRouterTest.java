package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectJDBCDatabaseRepository;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorCSVRestRouterTest {

    private final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository;
    private final ProjectJDBCDatabaseRepository projectDatabaseRepository;
    private final int port;
    private final TestRestTemplate restTemplate;
    private final String baseUrl;
    private final String errorHeader;

    @Autowired
    ConnectorCSVRestRouterTest(
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository,
            ProjectJDBCDatabaseRepository projectDatabaseRepository, @Value(value = "${local.server.port}") final int port,
            final TestRestTemplate restTemplate,
            @Value("${connectors.rest.error-header:error}") final String errorHeader
    ) {
        this.CSVConnectorJDBCDatabaseRepository = CSVConnectorJDBCDatabaseRepository;
        this.projectDatabaseRepository = projectDatabaseRepository;
        this.port = port;
        this.restTemplate = restTemplate;
        baseUrl = "http://localhost:" + this.port + "/configuration";
        this.errorHeader = errorHeader;
    }


    @BeforeAll
    void beforeAll() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @BeforeEach
    void beforeEach() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @Test
    @DisplayName("configuration rest router must return error if create method is called with configuration that have id already exist")
    void configuration_rest_router_must_return_error_if_create_method_is_called_with_configuration_that_have_id_already_exist() {
        final var id=UUID.randomUUID().toString();
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var configuration1 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(configuration1.id())
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                configuration1.id(),
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );

        final var result =
                this.restTemplate.postForEntity(
                        this.baseUrl,
                        new ConnectorCSVDTO(configuration2),
                        ConnectorCSVDTO.class
                );

       /* Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );*/
    }

    @Test
    @DisplayName("configuration rest router must return error if create method is called with configuration that have name already exist")
    void configuration_rest_router_must_return_error_if_create_method_is_called_with_configuration_that_have_name_already_exist() {
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var id=UUID.randomUUID().toString();
        final var configuration1 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var id2=UUID.randomUUID().toString();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id2)
                        .withName(configuration1.name())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id2,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );

        final var result =
                this.restTemplate.postForEntity(
                        this.baseUrl,
                        new ConnectorCSVDTO(configuration2),
                        ConnectorCSVDTO.class
                );

       /* Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );*/
    }

    @Test
    @DisplayName("configuration rest router must return created configuration if create method is called with configuration that does not exist")
    void configuration_rest_router_must_return_created_configuration_if_create_method_is_called_with_configuration_that_does_not_exist() {
        final var id=UUID.randomUUID().toString();
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.restTemplate.postForEntity(
                        this.baseUrl,
                        new ConnectorCSVDTO(configuration),
                        ConnectorCSVDTO.class
                );

      /*  Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );*/
    }

    @Test
    @DisplayName("configuration rest router must return error if update method is called with configuration that have id does not exist")
    void configuration_rest_router_must_return_error_if_update_method_is_called_with_configuration_that_have_id_does_not_exist() {
        final var id=UUID.randomUUID().toString();
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.restTemplate.exchange(
                        this.baseUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(new ConnectorCSVDTO(configuration)),
                        ConnectorCSVDTO.class
                );

       /* Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );*/
    }

    @Test
    @DisplayName("configuration rest router must return updated configuration if update method is called with configuration that already exist")
    void configuration_rest_router_must_return_updated_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
        final var id=UUID.randomUUID().toString();
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var configuration1 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(configuration1.id())
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                configuration1.id(),
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );

        final var result =
                this.restTemplate.exchange(
                        this.baseUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(new ConnectorCSVDTO(configuration2)),
                        ConnectorCSVDTO.class
                );

       /* Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );*/
    }

    @Test
    @DisplayName("configuration rest router must return error if delete method is called with configuration that have id does not exist")
    void configuration_rest_router_must_return_error_if_delete_method_is_called_with_configuration_that_have_id_does_not_exist() {
        final var result =
                this.restTemplate.exchange(
                        this.baseUrl + "/" + UUID.randomUUID(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        ConnectorCSVDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest router must return deleted configuration if update method is called with configuration that already exist")
    void configuration_rest_router_must_return_deleted_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
        final var id=UUID.randomUUID().toString();
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.restTemplate.exchange(
                        this.baseUrl + "/" + configuration.id(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        ConnectorCSVDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
    @Test
    @DisplayName("configuration rest router must return a liste of configurations if findAll method is called with valid configurations")
    void configuration_rest_router_must_return_list_of_configurations_if_findAll_method_is_called_with_valid_configurations() {
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var id=UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Meta,
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );
        final var id2=UUID.randomUUID().toString();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Meta,
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final ResponseEntity<Collection<ConnectorCSVDTO>> result = this.restTemplate.exchange(
                this.baseUrl ,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Collection<ConnectorCSVDTO>>() {}
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
    @Test
    @DisplayName("configuration rest router must return an empty list of configurations if findAll method is called with no saved configurations ")
    void configuration_rest_router_must_return_ampty_List_of_configurations_if_findAll_method_is_called_with_no_saved_configurations() {

        final ResponseEntity<Collection<ConnectorCSVDTO>> result = this.restTemplate.exchange(
                this.baseUrl ,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Collection<ConnectorCSVDTO>>() {}
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader)));



    }
    @Test
    @DisplayName("configuration rest router must return error if findOneByName method is called with configuration that have name does not exist")
    void configuration_rest_router_must_return_error_if_findOneByName_method_is_called_with_configuration_that_have_id_does_not_exist() {
        final var result =
                this.restTemplate.exchange(
                        this.baseUrl + "/" + UUID.randomUUID(),
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        ConnectorCSVDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }
    @Test
    @DisplayName("configuration rest router must return a liste of configurations if findManyByNameContainsIgnoreCase method is called with valid configurations that have name contain given name")
    void configuration_rest_router_must_return_list_of_configurations_if_findManyByNameContainsIgnoreCase_method_is_called_with_valid_configurations_that_have_name_contain_given_name() {
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var id = UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName("billing history")
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Meta,
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );
        final var id2 = UUID.randomUUID().toString();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id2)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("Formation2")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Meta,
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration2
                )
        );

        final ResponseEntity<Collection<ConnectorCSVDTO>> result = this.restTemplate.exchange(
                this.baseUrl + "/NameContainsIgnoreCase" + "/BilL",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Collection<ConnectorCSVDTO>>() {
                }
        );

        /*Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );*/
    }

}