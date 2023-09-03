package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.FieldDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorJDBCServiceTest {

    private final ConnectorCSVService connectorCSVService;
    private final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository;
    private final ProjectJDBCDatabaseRepository projectDatabaseRepository;
    @Autowired
    ConnectorJDBCServiceTest(
            final ConnectorCSVService connectorCSVService,
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository,
            ProjectJDBCDatabaseRepository projectDatabaseRepository) {
        this.connectorCSVService = connectorCSVService;
        this.CSVConnectorJDBCDatabaseRepository = CSVConnectorJDBCDatabaseRepository;
        this.projectDatabaseRepository = projectDatabaseRepository;
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
    @DisplayName("configuration service must return error if create method is called with invalid configuration")
    void configuration_service_must_return_error_if_create_method_is_called_with_invalid_configuration() {
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
                this.connectorCSVService
                        .create(
                                configuration
                        )
                        .isLeft();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return created configuration if create method is called with valid configuration")
    void configuration_service_must_return_created_configuration_if_create_method_is_called_with_valid_configuration() {
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var id=UUID.randomUUID().toString();
        Collection<FieldDAO> fields = IntStream.iterate(1, it -> it + 1)
                .limit(10)
                .mapToObj(it ->
                        Field.of(
                                        UUID.randomUUID().toString(),
                                        "id",
                                        UUID.randomUUID().toString(),
                                        it,
                                        UUID.randomUUID().toString(),
                                        FieldType.Meta,
                                        true,true
                                )
                                .get()
                ).map(field -> new FieldDAO(field))
                .collect(Collectors.toUnmodifiableSet());
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
                this.connectorCSVService
                        .create(
                                configuration
                        )
                        .isRight();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return error if update method is called with invalid configuration")
    void configuration_service_must_return_error_if_update_method_is_called_with_invalid_configuration() {
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
                this.connectorCSVService
                        .update(
                                configuration
                        )
                        .isLeft();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return updated configuration if update method is called with valid configuration")
    void configuration_service_must_return_updated_configuration_if_update_method_is_called_with_valid_configuration() {
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
                this.connectorCSVService
                        .update(
                                configuration
                        )
                        .isRight();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return error if delete method is called with invalid configuration")
    void configuration_service_must_return_error_if_delete_method_is_called_with_invalid_configuration() {
        final var result =
                this.connectorCSVService
                        .delete(
                                UUID.randomUUID().toString()
                        )
                        .isLeft();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return deleted configuration if delete method is called with valid configuration")
    void configuration_service_must_return_deleted_configuration_if_delete_method_is_called_with_valid_configuration() {
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
                this.connectorCSVService
                        .delete(
                                configuration.id()
                        )
                        .isRight();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return a list of configurations if findAll method is called with valid configuration")
    void configuration_service_must_return_List_of_configurations_if_findAll_method_is_called_with_valid_configuration() {
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
                                                                FieldType.Text,
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

        final var result =
                this.connectorCSVService
                        .findAll().get();



        Assertions.assertTrue(result.contains(configuration)&&result.size()==1);
    }
    @Test
    @DisplayName("configuration service must return empty list  if findAll method is called with no configurations exist")
    void configuration_service_must_return_empty_list_if_findAll_method_is_called_with_no_configurations_exist() {
        final var result =
                this.connectorCSVService
                        .findAll().get();

        Assertions.assertTrue(result.isEmpty());
    }
    @Test
    @DisplayName("configuration service must return error if findOneByName method is called with invalid configuration")
    void configuration_service_must_return_error_if_findOneByName_method_is_called_with_invalid_configuration() {
        final var result =
                this.connectorCSVService
                        .findOneByName(
                                UUID.randomUUID().toString()
                        )
                        .isLeft();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return searched configuration if findOneByName method is called with valid configuration and name exist")
    void configuration_service_must_return_deleted_configuration_if_findOneByName_method_is_called_with_valid_configuration() {
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var id = UUID.randomUUID().toString();
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
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
     //   System.out.println(configuration);
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorCSVService
                        .findOneByName(
                                configuration.name()
                        )
                        .isRight();

        Assertions.assertTrue(result);
    }
    @Test
    @DisplayName("configuration service must return a list of searched configurations if findManyByNameContainsIgnoreCase method is called with valid configuration")
    void configuration_service_must_return_List_of_configurations_if_findManyByNameContainsIgnoreCase_method_is_called_with_valid_configuration() {
        final var id = UUID.randomUUID().toString();
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var configuration1 =
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
                                                                FieldType.Text,
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        ;
        final var id2 = UUID.randomUUID().toString();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id2)
                        .withName("actual bills")
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
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var id3 = UUID.randomUUID().toString();
        final var configuration3 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id3)
                        .withName("history")
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
                                                                id3,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Text,
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        ;
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration2
                )
        );
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration3
                )
        );

        final var result =
                this.connectorCSVService
                        .findManyByNameContainsIgnoreCase("BiLl").get();


        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("configuration service must return empty list  if findManyByNameContainsIgnoreCase method is called with no configurations exist that contain specified name")
    void configuration_service_must_return_empty_list_if_findManyByNameContainsIgnoreCase_method_is_called_with_no_configurations_exist_that_contain_specified_name() {
        final var id = UUID.randomUUID().toString();
        ProjectDAO projectDAO=  projectDatabaseRepository.save(new ProjectDAO("Formation2","Formation2","pppp"));

        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName("history")
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
                                                                true,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        ;
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );
        final var result =
                this.connectorCSVService
                        .findManyByNameContainsIgnoreCase("Bill").get();
        Collection collection = new ArrayList();
        Assertions.assertEquals(collection, result);
    }

}