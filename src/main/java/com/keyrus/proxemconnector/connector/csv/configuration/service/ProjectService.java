package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Objects;

import static org.springframework.data.domain.PageRequest.of;

@Slf4j
public final class ProjectService {


    private static ProjectService instance = null;

        public static ProjectService instance(
                final ProjectRepository projectRepository
        ) {
            if (Objects.isNull(instance))
                instance =
                        new ProjectService(
                                projectRepository
                        );
            return instance;
        }

        private final ProjectRepository projectRepository;

        private ProjectService(
                final ProjectRepository projectRepository
        ) {
            this.projectRepository = projectRepository;
        }

        public Either<Error, Project> create(
                final Project project
        ) {
            return
                    this.projectRepository
                            .create(
                                    project
                            )
                            .mapLeft(ProjectService::repositoryErrorToServiceError);
        }

        public Either<Error, Project> update(
                final Project project
        ) {
            return
                    this.projectRepository
                            .update(
                                    project
                            )
                            .mapLeft(ProjectService::repositoryErrorToServiceError);
        }

        public Either<Error, Project> delete(
                final String id
        ) {
            return
                    this.projectRepository
                            .delete(
                                    id
                            )
                            .mapLeft(ProjectService::repositoryErrorToServiceError);
        }

        public Either<Error, Collection<Project>> findAll() {
            return this.projectRepository
                    .findAll()
                    .mapLeft(ProjectService::repositoryErrorToServiceError);

        }
    public Page<ProjectDAO> findAll(int page, int size){
        log.info("Fetching for page {} of size {}",page,size);
        return projectRepository.findAll(of(page,size));
    }
    public Either<Error, Project> findOneByName(String name) {
        return this.projectRepository
                .findOneByName(
                        name
                )
                .mapLeft(ProjectService::repositoryErrorToServiceError);
    }

        private static Error repositoryErrorToServiceError(
                final ProjectRepository.Error repositoryError
        ) {
            if (repositoryError instanceof ProjectRepository.Error.IO io)
                return new Error.IO(io.message());
            if (repositoryError instanceof ProjectRepository.Error.AlreadyExist)
                return new Error.AlreadyExist();
            if (repositoryError instanceof ProjectRepository.Error.NotFound)
                return new Error.NotFound();
            throw new IllegalStateException("repository error not mapped to service error");
        }

    public Page<ProjectDAO> getProjects(String name, int page, int size) {
        log.info("Fetching users for page {} of size {}", page, size);
        return projectRepository.findByNameContaining(name, of(page, size));
    }


    public sealed interface Error {

            default String message() {
                return this.getClass().getCanonicalName();
            }

            record IO(
                    String message
            ) implements Error {
            }

            record AlreadyExist() implements Error {
            }

            record NotFound() implements Error {
            }
        }
    }



