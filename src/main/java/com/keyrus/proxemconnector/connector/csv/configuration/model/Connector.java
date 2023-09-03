package com.keyrus.proxemconnector.connector.csv.configuration.model;

import io.vavr.control.Either;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Connector {
    private final String id;
    private final String name;
    private final String separator;
    private final String encoding;
    private final String folderToScan;
    private final String archiveFolder;
    private final String failedRecordsFolder;
    private final boolean containsHeaders;
    private final Collection<Header> headers;

    private Connector(
            final String id,
            final String name,
            final String separator,
            final String encoding,
            final String folderToScan,
            final String archiveFolder,
            final String failedRecordsFolder,
            final boolean containsHeaders,
            final Collection<Header> headers
    ) {
        this.id = id;
        this.name = name;
        this.separator = separator;
        this.encoding = encoding;
        this.folderToScan = folderToScan;
        this.archiveFolder = archiveFolder;
        this.failedRecordsFolder = failedRecordsFolder;
        this.containsHeaders = containsHeaders;
        this.headers = headers;
    }

    private Connector(
            final String id,
            final String name,
            final String folderToScan,
            final String archiveFolder,
            final String failedRecordsFolder,
            final boolean containsHeaders,
            final Collection<Header> headers
    ) {
        this(
                id,
                name,
                ",",
                StandardCharsets.UTF_8.name(),
                folderToScan,
                archiveFolder,
                failedRecordsFolder,
                containsHeaders,
                headers
        );
    }

    private Connector(
            final String id,
            final String name,
            final String separator,
            final String folderToScan,
            final String archiveFolder,
            final String failedRecordsFolder,
            final boolean containsHeaders,
            final Collection<Header> headers
    ) {
        this(
                id,
                name,
                separator,
                StandardCharsets.UTF_8.name(),
                folderToScan,
                archiveFolder,
                failedRecordsFolder,
                containsHeaders,
                headers
        );
    }

    public String name() {
        return this.name;
    }

    public String id() {
        return this.id;
    }

    public String separator() {
        return this.separator;
    }

    public String encoding() {
        return this.encoding;
    }

    public String folderToScan() {
        return this.folderToScan;
    }

    public String archiveFolder() {
        return this.archiveFolder;
    }

    public String failedRecordsFolder() {
        return this.failedRecordsFolder;
    }

    public boolean containsHeaders() {
        return this.containsHeaders;
    }

    public Collection<Header> headers() {
        return this.headers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Connector) obj;
        return
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.separator, that.separator) &&
                Objects.equals(this.encoding, that.encoding) &&
                Objects.equals(this.folderToScan, that.folderToScan) &&
                Objects.equals(this.archiveFolder, that.archiveFolder) &&
                Objects.equals(this.failedRecordsFolder, that.failedRecordsFolder) &&
                Objects.equals(this.containsHeaders, that.containsHeaders) &&
                (
                        Objects.nonNull(this.headers) &&
                        Objects.nonNull(that.headers)
                                ? this.headers.containsAll(that.headers)
                                : Objects.isNull(that.headers)
                );
    }

    @Override
    public int hashCode() {
        return
                Objects.hash(
                        this.id,
                        this.name,
                        this.separator,
                        this.encoding,
                        this.folderToScan,
                        this.archiveFolder,
                        this.failedRecordsFolder,
                        this.containsHeaders,
                        this.headers
                );
    }

    @Override
    public String toString() {
        return
                """
                        Configuration[
                            id=%s,
                            name=%s,
                            separator=%s,
                            encoding=%s,
                            folderToScan=%s,
                            archiveFolder=%s,
                            failedRecordsFolder=%s,
                            containsHeaders=%s,
                            headers=%s
                        ]
                        """
                        .formatted(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.folderToScan,
                                this.archiveFolder,
                                this.failedRecordsFolder,
                                this.containsHeaders,
                                this.headers
                        );
    }

    private static Either<Collection<Error>, Connector> of(
            final String id,
            final String name,
            final String separator,
            final String encoding,
            final String folderToScan,
            final String archiveFolder,
            final String failedRecordsFolder,
            final boolean containsHeaders,
            final Collection<Header> headers
    ) {
        return
                Connector.checkThenInstantiate(
                        Connector.instance(
                                id,
                                name,
                                separator,
                                encoding,
                                folderToScan,
                                archiveFolder,
                                failedRecordsFolder,
                                containsHeaders,
                                headers
                        ),
                        Connector.checkId(
                                id
                        ), Connector.checkName(
                                name
                        ),
                        Connector.checkSeparator(
                                separator
                        ),
                        Connector.checkEncoding(
                                encoding
                        ),
                        Connector.checkFolderToScan(
                                folderToScan
                        ),
                        Connector.checkArchiveFolder(
                                archiveFolder
                        ),
                        Connector.checkFailedRecordsFolder(
                                failedRecordsFolder
                        ),
                        Connector.checkHeaders(
                                headers
                        )
                );
    }

    private static Supplier<Connector> instance(
            final String id,
            final String name,
            final String separator,
            final String encoding,
            final String folderToScan,
            final String archiveFolder,
            final String failedRecordsFolder,
            final boolean containsHeaders,
            final Collection<Header> headers
    ) {
        if (
                Objects.isNull(separator) &&
                Objects.isNull(encoding)
        )
            return
                    () ->
                            new Connector(
                                    id,
                                    name,
                                    folderToScan,
                                    archiveFolder,
                                    failedRecordsFolder,
                                    containsHeaders,
                                    headers
                            );
        if (Objects.isNull(encoding))
            return
                    () ->
                            new Connector(
                                    id,
                                    name,
                                    separator,
                                    folderToScan,
                                    archiveFolder,
                                    failedRecordsFolder,
                                    containsHeaders,
                                    headers
                            );
        return

                () ->
                        new Connector(
                                id,
                                name,
                                separator,
                                encoding,
                                folderToScan,
                                archiveFolder,
                                failedRecordsFolder,
                                containsHeaders,
                                headers
                        );
    }

    private static Supplier<Optional<Error>> checkId(
            final String id
    ) {
        return
                () ->
                        Connector.checkNonNullableNonBlankStringField(
                                id,
                                Error.IdMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkName(
            final String name
    ) {
        return
                () ->
                        Connector.checkNonNullableNonBlankStringField(
                                name,
                                Error.NameMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkSeparator(
            final String separator
    ) {
        return
                () ->
                        Connector.checkNullableNonBlankStringField(
                                separator,
                                Error.SeparatorMalformed::new,
                                Connector::checkSeparatorIsNotComposedOfLettersOrDigits
                        );
    }

    private static Supplier<Optional<Error>> checkEncoding(
            final String encoding
    ) {
        return
                () ->
                        Connector.checkNullableNonBlankStringField(
                                encoding,
                                Error.EncodingMalformed::new,
                                Charset::isSupported
                        );
    }

    private static Supplier<Optional<Error>> checkFolderToScan(
            final String folderToScan
    ) {
        return
                () ->
                        Connector.checkNonNullableNonBlankStringField(
                                folderToScan,
                                Error.FolderToScanMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkArchiveFolder(
            final String archiveFolder
    ) {
        return
                () ->
                        Connector.checkNonNullableNonBlankStringField(
                                archiveFolder,
                                Error.ArchiveFolderMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkFailedRecordsFolder(
            final String failedRecordsFolder
    ) {
        return
                () ->
                        Connector.checkNonNullableNonBlankStringField(
                                failedRecordsFolder,
                                Error.FailedRecordsFolderMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkHeaders(
            final Collection<Header> headers
    ) {
        return
                () ->
                        Connector.checkHeaderFormat(headers)
                                .or(() ->
                                        Connector.checkHeadersIds(headers)
                                )
                                .or(() ->
                                        Connector.checkHeadersPositions(headers)
                                );
    }

    private static Optional<Error> checkHeadersPositions(
            final Collection<Header> headers
    ) {
        return
                Connector.checkField(
                        headers,
                        Error.HeadersSequenceMalformed::new,
                        Connector::checkHeadersPositionSequence
                );
    }

    private static Optional<Error> checkHeadersIds(
            final Collection<Header> headers
    ) {
        return
                Connector.checkField(
                        headers,
                        Error.IdHeaderMissing::new,
                        Connector::checkHeadersContainsIds
                );
    }

    private static Optional<Error> checkHeaderFormat(
            final Collection<Header> headers
    ) {
        return
                Connector.checkNonNullableField(
                        headers,
                        Error.HeaderMalformed::new
                );
    }

    private static boolean checkHeadersContainsIds(
            final Collection<Header> headers
    ) {
        return
                Objects.nonNull(headers) &&
                headers.stream()
                        .anyMatch(Header::partOfDocumentIdentity);
    }

    private static boolean checkHeadersPositionSequence(
            final Collection<Header> headers
    ) {
        return
                Objects.nonNull(headers) &&
                headers.stream()
                        .map(Header::position)
                        .max(Comparator.naturalOrder())
                        .map(max -> headers.size() == max)
                        .orElse(false);
    }

    private static boolean checkSeparatorIsNotComposedOfLettersOrDigits(
            final String separator
    ) {
        return
                separator.chars()
                        .noneMatch(
                                ((IntPredicate) Character::isDigit)
                                        .or(Character::isAlphabetic)
                        );
    }

    @SafeVarargs
    private static Optional<Error> checkNonNullableNonBlankStringField(
            final String field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<String>... extraChecks
    ) {
        return
                Connector.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        Connector.mergePredicates(
                                Predicate.not(String::isBlank),
                                extraChecks
                        )
                );
    }

    @SafeVarargs
    private static Optional<Error> checkNullableNonBlankStringField(
            final String field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<String>... extraChecks
    ) {
        return
                Objects.nonNull(field)
                        ?
                        Connector.checkNonNullableField(
                                field,
                                errorIfInvalid,
                                Connector.mergePredicates(
                                        Predicate.not(String::isBlank),
                                        extraChecks
                                )
                        )
                        :
                        Optional.empty();
    }

    @SafeVarargs
    private static <FIELD> Optional<Error> checkNonNullableField(
            final FIELD field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<FIELD>... extraChecks
    ) {
        return
                Connector.checkField(
                        field,
                        errorIfInvalid,
                        Connector.mergePredicates(
                                Objects::nonNull,
                                extraChecks
                        )
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
    private static Either<Collection<Error>, Connector> checkThenInstantiate(
            final Supplier<Connector> instance,
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
                        ? Either.right(instance.get())
                        : Either.left(errors);
    }

    @SafeVarargs
    private static <TYPE> Predicate<TYPE> mergePredicates(
            final Predicate<TYPE> predicate,
            final Predicate<TYPE>... predicates
    ) {
        return
                Stream.of(
                                Stream.of(predicate),
                                Arrays.stream(predicates)
                        )
                        .flatMap(Function.identity())
                        .filter(Objects::nonNull)
                        .reduce(
                                it -> true,
                                Predicate::and
                        );
    }

    public static final class Builder {
        private final String id;
        private final String name;
        private final String separator;
        private final String encoding;
        private final String folderToScan;
        private final String archiveFolder;
        private final String failedRecordsFolder;
        private final boolean containsHeaders;
        private final Collection<Header> headers;
        private final Collection<Supplier<Either<Collection<Header.Error>, Header>>> headerBuilders;

        private Builder(
                final String id,
                final String name,
                final String separator,
                final String encoding,
                final String folderToScan,
                final String archiveFolder,
                final String failedRecordsFolder,
                final boolean containsHeaders,
                final Collection<Header> headers,
                final Collection<Supplier<Either<Collection<Header.Error>, Header>>> headerBuilders
        ) {
            this.id = id;
            this.name = name;
            this.separator = separator;
            this.encoding = encoding;
            this.folderToScan = folderToScan;
            this.archiveFolder = archiveFolder;
            this.failedRecordsFolder = failedRecordsFolder;
            this.containsHeaders = containsHeaders;
            this.headers = headers;
            this.headerBuilders = headerBuilders;
        }

        public Builder withId(
                final String id
        ) {
            return
                    new Builder(
                            Builder.nonNullOrDefault(
                                    id,
                                    this.id
                            ),
                            this.name,
                            this.separator,
                            this.encoding,
                            this.folderToScan,
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withName(
                final String name
        ) {
            return
                    new Builder(
                            this.id,
                            Builder.nonNullOrDefault(
                                    name,
                                    this.name
                            ),
                            this.separator,
                            this.encoding,
                            this.folderToScan,
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withSeparator(
                final String separator
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            Builder.nonNullOrDefault(
                                    separator,
                                    this.separator
                            ),
                            this.encoding,
                            this.folderToScan,
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withEncoding(
                final String encoding
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            Builder.nonNullOrDefault(
                                    encoding,
                                    this.encoding
                            ),
                            this.folderToScan,
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withFolderToScan(
                final String folderToScan
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            Builder.nonNullOrDefault(
                                    folderToScan,
                                    this.folderToScan
                            ),
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withArchiveFolder(
                final String archiveFolder
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.folderToScan,
                            Builder.nonNullOrDefault(
                                    archiveFolder,
                                    this.archiveFolder
                            ),
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withFailedRecordsFolder(
                final String failedRecordsFolder
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.folderToScan,
                            this.archiveFolder,
                            Builder.nonNullOrDefault(
                                    failedRecordsFolder,
                                    this.failedRecordsFolder
                            ),
                            this.containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withContainsHeaders(
                final boolean containsHeaders
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.folderToScan,
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            containsHeaders,
                            this.headers,
                            this.headerBuilders
                    );
        }

        public Builder withHeaders(
                final Collection<Header> headers
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.folderToScan,
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            Builder.addToCollection(
                                    this.headers,
                                    headers
                            ),
                            this.headerBuilders
                    );
        }

        @SafeVarargs
        public final Builder withHeaders(
                final Supplier<Either<Collection<Header.Error>, Header>>... headerBuilders
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.folderToScan,
                            this.archiveFolder,
                            this.failedRecordsFolder,
                            this.containsHeaders,
                            this.headers,
                            Builder.addToCollection(
                                    this.headerBuilders,
                                    headerBuilders
                            )
                    );
        }

        public Either<Collection<Error>, Connector> build() {
            return
                    Builder.buildHeaders(
                                    this.headers,
                                    this.headerBuilders
                            )
                            .mapLeft(
                                    Builder.configurationInstanceForErrors(
                                            this.id,
                                            this.name,
                                            this.separator,
                                            this.encoding,
                                            this.folderToScan,
                                            this.archiveFolder,
                                            this.failedRecordsFolder,
                                            this.containsHeaders
                                    )
                            )
                            .flatMap(
                                    Builder.configurationInstance(
                                            this.id,
                                            this.name,
                                            this.separator,
                                            this.encoding,
                                            this.folderToScan,
                                            this.archiveFolder,
                                            this.failedRecordsFolder,
                                            this.containsHeaders
                                    )
                            );
        }

        public static Builder builder() {
            return
                    new Builder(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true,
                            null,
                            null
                    );
        }

        private static Function<Collection<Error>, Collection<Error>> configurationInstanceForErrors(
                final String id,
                final String name,
                final String separator,
                final String encoding,
                final String folderToScan,
                final String archiveFolder,
                final String failedRecordsFolder,
                final boolean containsHeaders
        ) {
            return
                    headersErrors ->
                            Builder.addToCollection(
                                    headersErrors,
                                    Connector.of(
                                                    id,
                                                    name,
                                                    separator,
                                                    encoding,
                                                    folderToScan,
                                                    archiveFolder,
                                                    failedRecordsFolder,
                                                    containsHeaders,
                                                    null
                                            )
                                            .fold(
                                                    Function.identity(),
                                                    __ -> Collections.emptySet()
                                            )
                            );
        }

        private static Function<Collection<Header>, Either<Collection<Error>, Connector>> configurationInstance(
                final String id,
                final String name,
                final String separator,
                final String encoding,
                final String folderToScan,
                final String archiveFolder,
                final String failedRecordsFolder,
                final boolean containsHeaders
        ) {
            return
                    headers ->
                            Connector.of(
                                    id,
                                    name,
                                    separator,
                                    encoding,
                                    folderToScan,
                                    archiveFolder,
                                    failedRecordsFolder,
                                    containsHeaders,
                                    headers
                            );
        }

        private static Either<Collection<Error>, Collection<Header>> buildHeaders(
                final Collection<Header> headers,
                final Collection<Supplier<Either<Collection<Header.Error>, Header>>> headerBuilders
        ) {
            final var errors = Builder.buildHeadersForErrors(headerBuilders);
            final Supplier<Collection<Header>> validHeaders =
                    () ->
                            Builder.mergeHeaders(
                                    headers,
                                    headerBuilders
                            );
            return
                    errors.isEmpty()
                            ? Either.right(validHeaders.get())
                            : Either.left(errors);
        }

        private static Collection<Header> mergeHeaders(
                final Collection<Header> headers,
                final Collection<Supplier<Either<Collection<Header.Error>, Header>>> headerBuilders
        ) {
            return
                    Builder.addToCollection(
                            headers,
                            Objects.nonNull(headerBuilders)
                                    ?
                                    headerBuilders.stream()
                                            .map(Supplier::get)
                                            .filter(Either::isRight)
                                            .map(Either::get)
                                            .collect(Collectors.toUnmodifiableSet())
                                    :
                                    Collections.emptySet()
                    );
        }

        private static Collection<Error> buildHeadersForErrors(
                final Collection<Supplier<Either<Collection<Header.Error>, Header>>> headerBuilders
        ) {
            return
                    Objects.nonNull(headerBuilders)
                            ?
                            headerBuilders.stream()
                                    .map(Supplier::get)
                                    .filter(Either::isLeft)
                                    .map(Either::getLeft)
                                    .flatMap(Collection::stream)
                                    .map(Builder::errorToConfigurationError)
                                    .collect(Collectors.toUnmodifiableSet())
                            :
                            Collections.emptySet();
        }

        private static Error errorToConfigurationError(
                final Header.Error headerError
        ) {
            if (headerError instanceof Header.Error.IdMalformed)
                return new Error.Header.IdMalformed();
            if (headerError instanceof Header.Error.ReferenceConnectorMalformed)
                return new Error.Header.ReferenceConnectorMalformed();
            if (headerError instanceof Header.Error.NameMalformed)
                return new Error.Header.NameMalformed();
            if (headerError instanceof Header.Error.PositionMalformed)
                return new Error.Header.PositionMalformed();
            if (headerError instanceof Header.Error.MetaMalformed)
                return new Error.Header.MetaMalformed();
            if (headerError instanceof Header.Error.NullOrEmptyRestrictionCombination)
                return new Error.Header.NullOrEmptyRestrictionCombination();
            throw new IllegalStateException("header error not mapped to connector error");
        }

        private static <FIELD> FIELD nonNullOrDefault(
                final FIELD field,
                final FIELD defaultValue
        ) {
            return
                    Objects.nonNull(field)
                            ? field
                            : defaultValue;
        }

        private static <TYPE> Collection<TYPE> addToCollection(
                final Collection<TYPE> initialCollection,
                final Collection<TYPE> elements
        ) {
            return
                    Stream.of(
                                    initialCollection,
                                    elements
                            )
                            .filter(Objects::nonNull)
                            .flatMap(Collection::stream)
                            .toList();
        }

        @SafeVarargs
        private static <TYPE> Collection<TYPE> addToCollection(
                final Collection<TYPE> initialCollection,
                final TYPE... elements
        ) {
            return
                    Objects.nonNull(initialCollection)
                            ?
                            Stream.of(
                                            initialCollection.stream(),
                                            Arrays.stream(elements)
                                    )
                                    .flatMap(Function.identity())
                                    .toList()
                            :
                            Arrays.stream(elements)
                                    .toList();
        }
    }

    public sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        final class IdMalformed implements Error {
            private IdMalformed() {
            }
        }

        final class NameMalformed implements Error {
            private NameMalformed() {
            }
        }

        final class SeparatorMalformed implements Error {
            private SeparatorMalformed() {
            }
        }

        final class EncodingMalformed implements Error {
            private EncodingMalformed() {
            }
        }

        final class FolderToScanMalformed implements Error {
            private FolderToScanMalformed() {
            }
        }

        final class ArchiveFolderMalformed implements Error {
            private ArchiveFolderMalformed() {
            }
        }

        final class FailedRecordsFolderMalformed implements Error {
            private FailedRecordsFolderMalformed() {
            }
        }

        final class HeaderMalformed implements Error {
            private HeaderMalformed() {
            }
        }

        final class IdHeaderMissing implements Error {
            private IdHeaderMissing() {
            }
        }

        final class HeadersSequenceMalformed implements Error {
            private HeadersSequenceMalformed() {
            }
        }

        sealed interface Header extends Error {

            final class IdMalformed implements Header {
                private IdMalformed() {
                }
            }

            final class ReferenceConnectorMalformed implements Header {
                private ReferenceConnectorMalformed() {
                }
            }

            final class NameMalformed implements Header {
                private NameMalformed() {
                }
            }

            final class PositionMalformed implements Header {
                private PositionMalformed() {
                }
            }

            final class MetaMalformed implements Header {
                private MetaMalformed() {
                }
            }

            final class NullOrEmptyRestrictionCombination implements Header {
                private NullOrEmptyRestrictionCombination() {
                }
            }
        }
    }
}
