package ru.Vlad.NauJava.service;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Vlad.NauJava.config.Config;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.repository.FileRepository;


@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final Config config;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, Config config) {
        this.fileRepository = fileRepository;
        this.config = config;
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("Запущено приложение: " + config.getAppName());
        System.out.println("Версия: " + config.getAppVersion());
    }

    @Override
    public void uploadFile(Long id, String originalName, Long size) {
        if (fileRepository.read(id) != null) {
            System.out.println("Ошибка! Файл с id " + id + " уже существует.");
            return;
        }

        FileItem fileItem = new FileItem();
        fileItem.setId(id);
        fileItem.setOriginalName(originalName);
        fileItem.setSize(size);
        fileItem.setDownloadCode(UUID. randomUUID().toString().substring(0, 8));
        fileItem.setUploadDate(LocalDate.now().toString());

        fileRepository.create(fileItem);
    }

    @Override
    public FileItem findById(Long id) {
        return fileRepository.read(id);
    }

    @Override
    public FileItem findByDownloadCode(String code) {
        return fileRepository.findByDownloadCode(code);
    }

    public void renameFile(Long id, String newName) {
        FileItem fileItem = fileRepository.read(id);
        if (fileItem != null) {
            fileItem.setOriginalName(newName);
            fileRepository.update(fileItem);
        } else {
            System.out.println("Файл с id " + id + " не найден.");
        }
    }

    public void deleteById(Long id) {
        fileRepository.delete(id);
    }
}
