package ru.Vlad.NauJava.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
//import ru.Vlad.NauJava.command.CommandProcessor;
import ru.Vlad.NauJava.entity.FileItem;


@Configuration
public class Config {
    @org.springframework.beans.factory.annotation.Value("${app.name}")
    private String appName;

    @org.springframework.beans.factory.annotation.Value("${app.version}")
    private String appVersion;

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

//    @Autowired
//    @Lazy
//    private CommandProcessor commandProcessor;

    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public List<FileItem> fileContainer() {
        return new ArrayList<>();
    }

//    @Bean
//    public CommandLineRunner commandScanner() {
//        return args ->
//        {
//            try (Scanner scanner = new Scanner(System.in)) {
//                System.out.println("Введите команду. 'exit' для выхода.");
//                while (true) {
//                    System.out.print("> ");
//                    String input = scanner.nextLine();
//
//                    if ("exit".equalsIgnoreCase(input.trim())) {
//                        System.out.println("Выход из программы...");
//                        break;
//                    }
//                    commandProcessor.processCommand(input);
//                }
//            }
//        };
//    }
}