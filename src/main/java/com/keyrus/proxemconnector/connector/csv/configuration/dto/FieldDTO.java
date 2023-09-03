package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Header;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public record FieldDTO(
        String id,
        String name,
        int position,
        String meta,
        boolean partOfDocumentIdentity,
        boolean canBeNullOrEmpty
) {
    public FieldDTO(
            final Header header
    ) {
        this(
                header.id(),
                header.name(),
                header.position(),
                header.meta(),
                header.partOfDocumentIdentity(),
                header.canBeNullOrEmpty()
        );
    }

    public Either<Collection<Header.Error>, Header> toHeader(
            final String referenceConnector
    ) {
        return
                Header.of(
                        FieldDTO.idNonNullOrRandomId(this.id),
                        referenceConnector,
                        this.name,
                        this.position,
                        this.meta,
                        this.partOfDocumentIdentity,
                        this.canBeNullOrEmpty
                );
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
