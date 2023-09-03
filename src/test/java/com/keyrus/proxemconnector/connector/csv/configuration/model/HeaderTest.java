package com.keyrus.proxemconnector.connector.csv.configuration.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

class HeaderTest {

    @Test
    @DisplayName("header must return error if of method is called with invalid id")
    void header_must_return_error_if_of_method_is_called_with_invalid_id() {
        final var result =
                Header.of(
                                " ",
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                false,
                                new Random().nextBoolean()
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.IdMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid reference connector")
    void header_must_return_error_if_of_method_is_called_with_invalid_reference_connector() {
        final var result =
                Header.of(
                                UUID.randomUUID().toString(),
                                " ",
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                false,
                                new Random().nextBoolean()
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.ReferenceConnectorMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid name")
    void header_must_return_error_if_of_method_is_called_with_invalid_name() {
        final var result =
                Header.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                " ",
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                false,
                                new Random().nextBoolean()
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.NameMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid position")
    void header_must_return_error_if_of_method_is_called_with_invalid_position() {
        final var result =
                Header.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                -1,
                                UUID.randomUUID().toString(),
                                false,
                                new Random().nextBoolean()
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.PositionMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid meta")
    void header_must_return_error_if_of_method_is_called_with_invalid_meta() {
        final var result =
                Header.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                " ",
                                false,
                                new Random().nextBoolean()
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.MetaMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid value restriction")
    void header_must_return_error_if_of_method_is_called_with_invalid_value_restriction() {
        final var result =
                Header.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                true,
                                true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.NullOrEmptyRestrictionCombination))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with multiple invalid parameters")
    void header_must_return_error_if_of_method_is_called_with_multiple_invalid_parameters() {
        final var result =
                Header.of(
                                null,
                                "",
                                null,
                                -1,
                                " ",
                                true,
                                true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(6, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.IdMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.ReferenceConnectorMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.NameMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.PositionMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.MetaMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Header.Error.NullOrEmptyRestrictionCombination))
        );
    }

    @Test
    @DisplayName("header must return instance if of method is called with valid parameters")
    void header_must_return_instance_if_of_method_is_called_with_valid_parameters() {
        final var id = UUID.randomUUID().toString();
        final var referenceConnector = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var position = new Random().nextInt(1, 10);
        final var meta = UUID.randomUUID().toString();
        final var partOfDocumentIdentity = false;
        final var canBeNullOrEmpty = new Random().nextBoolean();

        final var result =
                Header.of(
                                id,
                                referenceConnector,
                                name,
                                position,
                                meta,
                                partOfDocumentIdentity,
                                canBeNullOrEmpty
                        )
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(referenceConnector, result.referenceConnector()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(position, result.position()),
                () -> Assertions.assertEquals(meta, result.meta()),
                () -> Assertions.assertEquals(partOfDocumentIdentity, result.partOfDocumentIdentity()),
                () -> Assertions.assertEquals(canBeNullOrEmpty, result.canBeNullOrEmpty())
        );
    }
}