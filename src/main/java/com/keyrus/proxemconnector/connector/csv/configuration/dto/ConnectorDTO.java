package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Header;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public record ConnectorDTO(
        String id,
        String name,
        String separator,
        String encoding,
        String folderToScan,
        String archiveFolder,
        String failedRecordsFolder,
        boolean containsHeaders,
        Collection<FieldDTO> headers
) {

    public ConnectorDTO(
            final Connector connector
    ) {
        this(
                connector.id(),
                connector.name(),
                connector.separator(),
                connector.encoding(),
                connector.folderToScan(),
                connector.archiveFolder(),
                connector.failedRecordsFolder(),
                connector.containsHeaders(),
                ConnectorDTO.headersToHeaderDTOs(connector.headers())
        );
    }

    public Either<Collection<Connector.Error>, Connector> toConfiguration() {
        return
                Connector.Builder
                        .builder()
                        .withId(ConnectorDTO.idNonNullOrRandomId(this.id))
                        .withName(this.name)
                        .withSeparator(this.separator)
                        .withEncoding(this.encoding)
                        .withFolderToScan(this.folderToScan)
                        .withArchiveFolder(this.archiveFolder)
                        .withFailedRecordsFolder(this.failedRecordsFolder)
                        .withContainsHeaders(this.containsHeaders)
                        .withHeaders(
                                ConnectorDTO.headerDTOsToHeaderBuilders(
                                        this.id,
                                        this.headers
                                )
                        )
                        .build();
    }

    private static Collection<FieldDTO> headersToHeaderDTOs(
            final Collection<Header> headers
    ) {
        return
                headers.stream()
                        .map(FieldDTO::new)
                        .toList();
    }

    private static Supplier<Either<Collection<Header.Error>, Header>>[] headerDTOsToHeaderBuilders(
            final String connectorId,
            final Collection<FieldDTO> headers
    ) {
        final Function<FieldDTO, Supplier<Either<Collection<Header.Error>, Header>>> headerBuilder =
                fieldDTO ->
                        () ->
                                fieldDTO.toHeader(
                                        connectorId
                                );
        return
                Objects.nonNull(headers)
                        ?
                        headers.stream()
                                .filter(Objects::nonNull)
                                .map(headerBuilder)
                                .toArray(Supplier[]::new)
                        :
                        Collections.emptySet()
                                .toArray(Supplier[]::new);
    }

    private static String idNonNullOrRandomId(
            final String id
    ) {
        return
                Objects.nonNull(id)
                        ? id
                        : UUID.randomUUID().toString();
    }
}
