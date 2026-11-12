package ru.Vlad.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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
    private final TemplateEngine templateEngine;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             UserRepository userRepository,
                             PacketRepository packetRepository,
                             TemplateEngine templateEngine) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.packetRepository = packetRepository;
        this.templateEngine = templateEngine;
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
                Context context = new Context();
                context.setVariable("userCount", userCount.get());
                context.setVariable("userCalcTime", userCalcTime.get());
                context.setVariable("packets", packetsRef.get());
                context.setVariable("packetsCalcTime", packetsCalcTime.get());
                context.setVariable("totalTime", totalTime);

                String htmlContent = templateEngine.process("report", context);

                Report report = reportRepository.findById(reportId).orElseThrow();
                report.setStatus(ReportStatus.COMPLETED);
                report.setContent(htmlContent);
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