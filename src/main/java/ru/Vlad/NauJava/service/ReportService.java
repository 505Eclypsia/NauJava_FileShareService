package ru.Vlad.NauJava.service;

import ru.Vlad.NauJava.entity.Report;

public interface ReportService {
    Long createReport();
    void generateReportAsync(Long reportId);
    Report getReport(Long reportId);
}