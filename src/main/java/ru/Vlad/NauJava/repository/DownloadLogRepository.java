package ru.Vlad.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Vlad.NauJava.entity.DownloadLog;

@RepositoryRestResource(path = "download-logs")
public interface DownloadLogRepository extends CrudRepository<DownloadLog, Long> {
}
