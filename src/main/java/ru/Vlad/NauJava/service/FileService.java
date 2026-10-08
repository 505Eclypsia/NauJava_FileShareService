package ru.Vlad.NauJava.service;

import ru.Vlad.NauJava.entity.FileItem;

public interface FileService
{
    void uploadFile(Long id, String originalName, Long size);
    FileItem findById(Long id);
    FileItem findByDownloadCode(String code);
    void renameFile(Long id, String newName);
    void deleteById(Long id);

}