package ru.Vlad.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.service.FileService;


@Component
public class CommandProcessor
{
    private final FileService fileService;
    @Autowired
    public CommandProcessor(FileService fileService)
    {
        this.fileService = fileService;
    }

    public void processCommand(String input)
    {
        String[] cmd = input.split(" ");
        switch (cmd[0])
        {
            case "upload" ->
            {
                if (cmd.length < 4) {
                    System.out.println("Ошибка! Формат: upload <id> <имя_файла> <размер>");
                    return;
                }
                Long id = Long.valueOf(cmd[1]);
                String fileName = cmd[2];
                Long size = Long.valueOf(cmd[3]);

                if (fileService.findById(id) != null) {
                    System.out.println("Ошибка: файл с id " + id + " уже существует!");
                    return;
                }

                fileService.uploadFile(id, fileName, size);
                FileItem created = fileService.findById(id);
                System.out.println("Файл успешно загружен!");
                System.out.println("Ссылка/код для скачивания: " + created.getDownloadCode());
            }

            case "info" -> {
                if (cmd.length < 2) {
                    System.out.println("Ошибка! Формат: info <id>");
                    return;
                }
                FileItem file = fileService.findById(Long.valueOf(cmd[1]));
                if (file != null) {
                    System.out.println(file);
                } else {
                    System.out.println("Файл не найден!");
                }
            }

            case "download" -> {
                if (cmd.length < 2) {
                    System.out.println("Ошибка! Формат: download <код>");
                    return;
                }
                FileItem file = fileService.findByDownloadCode(cmd[1]);
                if (file != null) {
                    System.out.println("Скачивание файла: " + file.getOriginalName() + " (" + file.getSize() + " байт)");
                } else {
                    System.out.println("Неверный код для скачивания!");
                }
            }

            case "rename" -> {
                if (cmd.length < 3) {
                    System.out.println("Ошибка! Формат: rename <id> <новое_имя>");
                    return;
                }
                fileService.renameFile(Long.valueOf(cmd[1]), cmd[2]);
                System.out.println("Имя файла обновлено.");
            }

            case "delete" -> {
                if (cmd.length < 2) {
                    System.out.println("Ошибка! Формат: delete <id>");
                    return;
                }
                fileService.deleteById(Long.valueOf(cmd[1]));
                System.out.println("Файл удален.");
            }

            default -> System.out.println("Введена неизвестная команда...");
        }
    }
}
