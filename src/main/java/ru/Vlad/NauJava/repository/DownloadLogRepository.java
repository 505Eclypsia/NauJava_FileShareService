package ru.Vlad.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Vlad.NauJava.entity.DownloadLog;

public interface DownloadLogRepository extends CrudRepository<DownloadLog, Long> {
}
