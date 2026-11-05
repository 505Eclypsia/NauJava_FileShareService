package ru.Vlad.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Vlad.NauJava.entity.Report;
import ru.Vlad.NauJava.entity.ReportStatus;
import ru.Vlad.NauJava.service.ReportService;

import java.util.Map;

@RestController
@RequestMapping("/custom/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> startReport() {
        Long reportId = reportService.createReport();
        reportService.generateReportAsync(reportId);
        return ResponseEntity.ok(Map.of(
                "reportId", reportId,
                "message", "Отчет формируется. Проверьте статус по GET /custom/reports/" + reportId
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getReportContent(@PathVariable Long id) {
        Report report = reportService.getReport(id);

        if (report == null) {
            return ResponseEntity.status(404).body("Отчет с ID " + id + " не найден");
        }

        if (report.getStatus() == ReportStatus.CREATED) {
            return ResponseEntity.ok("<h3>Отчет еще формируется... Обновите страницу через секунду.</h3>");
        }

        if (report.getStatus() == ReportStatus.ERROR) {
            return ResponseEntity.status(500).body("<h3>Ошибка формирования отчета: " + report.getContent() + "</h3>");
        }

        return ResponseEntity.ok(report.getContent());
    }
}