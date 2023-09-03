package com.keyrus.proxemconnector.connector.csv.configuration.model;

import io.vavr.control.Either;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Header {
    private final String id;
    private final String referenceConnector;
    private final String name;
    private final int position;
    private final String meta;
    private final boolean partOfDocumentIdentity;
    private final boolean canBeNullOrEmpty;

    private Header(
            final String id,
            final String referenceConnector,
            final String name,
            final int position,
            final String meta,
            final boolean partOfDocumentIdentity,
            final boolean canBeNullOrEmpty
    ) {
        this.id = id;
        this.referenceConnector = referenceConnector;
        this.name = name;
        this.position = position;
        this.meta = meta;
        this.partOfDocumentIdentity = partOfDocumentIdentity;
        this.canBeNullOrEmpty = canBeNullOrEmpty;
    }

    public String id() {
        return this.id;
    }

    public String referenceConnector() {
        return this.referenceConnector;
    }

    public String name() {
        return this.name;
    }

    public int position() {
        return this.position;
    }

    public String meta() {
        return this.meta;
    }

    public boolean partOfDocumentIdentity() {
        return this.partOfDocumentIdentity;
    }

    public boolean canBeNullOrEmpty() {
        return this.canBeNullOrEmpty;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Header) obj;
        return
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.referenceConnector, that.referenceConnector) &&
                Objects.equals(this.name, that.name) &&
                this.position == that.position &&
                Objects.equals(this.meta, that.meta) &&
                this.partOfDocumentIdentity == that.partOfDocumentIdentity &&
                this.canBeNullOrEmpty == that.canBeNullOrEmpty;
    }

    @Override
    public int hashCode() {
        return
                Objects.hash(
                        this.id,
                        this.referenceConnector,
                        this.name,
                        this.position,
                        this.meta,
                        this.partOfDocumentIdentity,
                        this.canBeNullOrEmpty
                );
    }

    @Override
    public String toString() {
        return
                """
                        Header[
                            id=%s,
                            referenceConnector=%s,
                            name=%s,
                            position=%d,
                            meta=%s,
                            partOfDocumentIdentity=%s,
                            canBeNullOrEmpty=%s
                        ]
                        """
                        .formatted(
                                this.id,
                                this.referenceConnector,
                                this.name,
                                this.position,
                                this.meta,
                                this.partOfDocumentIdentity,
                                this.canBeNullOrEmpty
                        );
    }

    public static Either<Collection<Error>, Header> of(
            final String id,
            final String referenceConnector,
            final String name,
            final int position,
            final String meta,
            final boolean partOfDocumentIdentity,
            final boolean canBeNullOrEmpty
    ) {
        return
                Header.checkThenInstantiate(
                        Header.instance(
                                id,
                                referenceConnector,
                                name,
                                position,
                                meta,
                                partOfDocumentIdentity,
                                canBeNullOrEmpty
                        ),
                        Header.checkId(
                                id
                        ),
                        Header.checkReferenceConnector(
                                referenceConnector
                        ),
                        Header.checkName(
                                name
                        ),
                        Header.checkPosition(
                                position
                        ),
                        Header.checkMeta(
                                meta
                        ),
                        Header.checkNullOrEmptyValueRestriction(
                                partOfDocumentIdentity,
                                canBeNullOrEmpty
                        )
                );
    }

    private static Supplier<Optional<Error>> checkNullOrEmptyValueRestriction(
            final boolean partOfDocumentIdentity,
            final boolean canBeNullOrEmpty
    ) {
        return
                () ->
                        partOfDocumentIdentity &&
                        canBeNullOrEmpty
                                ? Optional.of(new Error.NullOrEmptyRestrictionCombination())
                                : Optional.empty();
    }

    private static Supplier<Header> instance(
            final String id,
            final String referenceConnector,
            final String name,
            final int position,
            final String meta,
            final boolean partOfDocumentIdentity,
            final boolean canBeNullOrEmpty
    ) {
        return
                () ->
                        new Header(
                                id,
                                referenceConnector,
                                name,
                                position,
                                meta,
                                partOfDocumentIdentity,
                                canBeNullOrEmpty
                        );
    }

    private static Supplier<Optional<Error>> checkId(
            final String id
    ) {
        return
                () ->
                        Header.checkNonNullableAndNonBlankString(
                                id,
                                Error.IdMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkReferenceConnector(
            final String referenceConnector
    ) {
        return
                () ->
                        Header.checkNonNullableAndNonBlankString(
                                referenceConnector,
                                Error.ReferenceConnectorMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkName(
            final String name
    ) {
        return
                () ->
                        Header.checkNonNullableAndNonBlankString(
                                name,
                                Error.NameMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkPosition(
            final int position
    ) {
        return
                () ->
                        Header.checkNonNullableAndPositiveInt(
                                position,
                                Error.PositionMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkMeta(
            final String meta
    ) {
        return
                () ->
                        Header.checkNonNullableAndNonBlankString(
                                meta,
                                Error.MetaMalformed::new
                        );
    }

    @SafeVarargs
    private static <FIELD> Optional<Error> checkNonNullableField(
            final FIELD field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<FIELD>... extraValidityChecks
    ) {
        final Predicate<FIELD> nonNullCheck = Objects::nonNull;
        final Predicate<FIELD>[] checks =
                Stream.of(
                                Stream.of(nonNullCheck),
                                Arrays.stream(extraValidityChecks)
                        )
                        .flatMap(Function.identity())
                        .filter(Objects::nonNull)
                        .toArray(Predicate[]::new);
        return
                Header.checkField(
                        field,
                        errorIfInvalid,
                        checks
                );
    }

    private static Optional<Error> checkNonNullableAndPositiveInt(
            final Integer field,
            final Supplier<Error> errorIfInvalid
    ) {
        return
                Header.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        it -> it > 0
                );
    }

    private static Optional<Error> checkNonNullableAndNonBlankString(
            final String field,
            final Supplier<Error> errorIfInvalid
    ) {
        return
                Header.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        Predicate.not(String::isBlank)
                );
    }

    @SafeVarargs
    private static <FIELD> Optional<Error> checkField(
            final FIELD field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<FIELD>... validityChecks
    ) {
        return
                Arrays.stream(validityChecks)
                        .anyMatch(Predicate.not(it -> it.test(field)))
                        ? Optional.ofNullable(errorIfInvalid.get())
                        : Optional.empty();
    }

    @SafeVarargs
    private static Either<Collection<Error>, Header> checkThenInstantiate(
            final Supplier<Header> headerInstance,
            final Supplier<Optional<Error>>... checks
    ) {
        final var errors =
                Arrays.stream(checks)
                        .map(Supplier::get)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toUnmodifiableSet());
        return
                errors.isEmpty()
                        ? Either.right(headerInstance.get())
                        : Either.left(errors);
    }

    public sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        final class IdMalformed implements Error {
            private IdMalformed() {
            }
        }

        final class ReferenceConnectorMalformed implements Error {
            private ReferenceConnectorMalformed() {
            }
        }

        final class NameMalformed implements Error {
            private NameMalformed() {
            }
        }

        final class PositionMalformed implements Error {
            private PositionMalformed() {
            }
        }

        final class MetaMalformed implements Error {
            private MetaMalformed() {
            }
        }

        final class NullOrEmptyRestrictionCombination implements Error {
            private NullOrEmptyRestrictionCombination() {
            }
        }
    }
}
