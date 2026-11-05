package ru.Vlad.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.Report;
import ru.Vlad.NauJava.entity.ReportStatus;
import ru.Vlad.NauJava.repository.PacketRepository;
import ru.Vlad.NauJava.repository.ReportRepository;
import ru.Vlad.NauJava.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PacketRepository packetRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             UserRepository userRepository,
                             PacketRepository packetRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.packetRepository = packetRepository;
    }

    @Override
    public Long createReport() {
        Report report = new Report(ReportStatus.CREATED);
        Report saved = reportRepository.save(report);
        return saved.getId();
    }

    @Override
    public void generateReportAsync(Long reportId) {
        CompletableFuture.runAsync(() -> {
            long totalStartTime = System.currentTimeMillis();

            AtomicLong userCount = new AtomicLong();
            AtomicLong userCalcTime = new AtomicLong();

            AtomicReference<List<Packet>> packetsRef = new AtomicReference<>(new ArrayList<>());
            AtomicLong packetsCalcTime = new AtomicLong();

            try {
                Thread userThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    userCount.set(userRepository.count());
                    userCalcTime.set(System.currentTimeMillis() - start);
                });

                Thread packetsThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    List<Packet> list = new ArrayList<>();
                    packetRepository.findAll().forEach(list::add);
                    packetsRef.set(list);
                    packetsCalcTime.set(System.currentTimeMillis() - start);
                });

                userThread.start();
                packetsThread.start();

                userThread.join();
                packetsThread.join();

                long totalTime = System.currentTimeMillis() - totalStartTime;

                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Отчет</title></head><body>");
                html.append("<h1>Отчет статистики файлообменника</h1>");
                html.append("<p><strong>Зарегистрировано пользователей:</strong> ").append(userCount.get()).append("</p>");
                html.append("<p><strong>Время подсчета пользователей:</strong> ").append(userCalcTime.get()).append(" мс</p>");
                html.append("<hr/>");
                html.append("<h2>Список пакетов в системе</h2>");
                html.append("<table border='1' cellpadding='5'><tr><th>ID</th><th>Название пакета</th><th>UUID хранилища</th><th>Дата создания</th></tr>");
                for (Packet p : packetsRef.get()) {
                    html.append("<tr>")
                            .append("<td>").append(p.getId()).append("</td>")
                            .append("<td>").append(p.getName()).append("</td>")
                            .append("<td>").append(p.getStorageUUID()).append("</td>")
                            .append("<td>").append(p.getCreatedAt()).append("</td>")
                            .append("</tr>");
                }
                html.append("</table>");
                html.append("<p><strong>Время выгрузки пакетов:</strong> ").append(packetsCalcTime.get()).append(" мс</p>");
                html.append("<hr/>");
                html.append("<p><strong>Общее время формирования отчета:</strong> ").append(totalTime).append(" мс</p>");
                html.append("</body></html>");

                Report report = reportRepository.findById(reportId).orElseThrow();
                report.setContent(html.toString());
                report.setStatus(ReportStatus.COMPLETED);
                reportRepository.save(report);

            } catch (Exception ex) {
                Report report = reportRepository.findById(reportId).orElse(null);
                if (report != null) {
                    report.setStatus(ReportStatus.ERROR);
                    report.setContent("Ошибка при генерации: " + ex.getMessage());
                    reportRepository.save(report);
                }
            }
        });
    }

    @Override
    public Report getReport(Long reportId) {
        return reportRepository.findById(reportId).orElse(null);
    }
}