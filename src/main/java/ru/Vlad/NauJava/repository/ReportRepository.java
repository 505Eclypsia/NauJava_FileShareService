package ru.Vlad.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Vlad.NauJava.entity.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {
}