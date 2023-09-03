package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Header;
import io.vavr.control.Either;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Entity
@Table(name = "connector")
public class ConnectorDAO implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    private String id;
    @Column(name = "name", nullable = false, unique = true, insertable = true, updatable = true)
    private String name;
    @Column(name = "separator", nullable = false, unique = false, insertable = true, updatable = true)
    private String separator;
    @Column(name = "encoding", nullable = false, unique = false, insertable = true, updatable = true)
    private String encoding;
    @Column(name = "folder_to_scan", nullable = false, unique = false, insertable = true, updatable = true)
    private String folderToScan;
    @Column(name = "archive_folder", nullable = false, unique = false, insertable = true, updatable = true)
    private String archiveFolder;
    @Column(name = "failed_records_folder", nullable = false, unique = false, insertable = true, updatable = true)
    private String failedRecordsFolder;
    @Column(name = "contains_headers", nullable = false, unique = false, insertable = true, updatable = true)
    private boolean containsHeaders;
    @OneToMany(mappedBy = "referenceConnector", fetch = FetchType.EAGER, targetEntity = FieldDAO.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FieldDAO> headers;

    public ConnectorDAO() {
    }

    public ConnectorDAO(
            String id,
            String name,
            String separator,
            String encoding,
            String folderToScan,
            String archiveFolder,
            String failedRecordsFolder,
            boolean containsHeaders,
            Collection<FieldDAO> headers
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

    public ConnectorDAO(
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
                ConnectorDAO.headersToHeaderDAOs(
                        connector.id(),
                        connector.headers()
                )
        );
    }

    public String id() {
        return this.id;
    }

    public String name() {
        return this.name;
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

    public Collection<FieldDAO> headers() {
        return this.headers;
    }

    public final Either<Collection<Connector.Error>, Connector> toConfiguration() {
        return
                Connector.Builder
                        .builder()
                        .withId(this.id)
                        .withName(this.name)
                        .withSeparator(this.separator)
                        .withEncoding(this.encoding)
                        .withFolderToScan(this.folderToScan)
                        .withArchiveFolder(this.archiveFolder)
                        .withFailedRecordsFolder(this.failedRecordsFolder)
                        .withContainsHeaders(this.containsHeaders)
                        .withHeaders(ConnectorDAO.headerDAOsToHeaderBuilders(this.headers))
                        .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ConnectorDAO) obj;
        return
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.separator, that.separator) &&
                Objects.equals(this.encoding, that.encoding) &&
                Objects.equals(this.folderToScan, that.folderToScan) &&
                Objects.equals(this.archiveFolder, that.archiveFolder) &&
                Objects.equals(this.failedRecordsFolder, that.failedRecordsFolder) &&
                this.containsHeaders == that.containsHeaders &&
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
                        ConfigurationDAO[
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

    private static Collection<FieldDAO> headersToHeaderDAOs(
            final String configurationId,
            final Collection<Header> headers
    ) {
        return
                headers.stream()
                        .map(FieldDAO::new)
                        .toList();
    }

    private static Supplier<Either<Collection<Header.Error>, Header>>[] headerDAOsToHeaderBuilders(
            final Collection<FieldDAO> headers
    ) {
        final Function<FieldDAO, Supplier<Either<Collection<Header.Error>, Header>>> headerBuilder =
                fieldDAO ->
                        fieldDAO::toHeader;
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
}
