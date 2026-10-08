package ru.Vlad.NauJava.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Vlad.NauJava.entity.FileItem;

@Component
public class FileRepository implements CrudRepository<FileItem, Long>
{
    private final List<FileItem> fileContainer;

    @Autowired
    public FileRepository(List<FileItem> fileContainer)
    {
        this.fileContainer = fileContainer;
    }

    @Override
    public void create(FileItem fileItem)
    {
        fileContainer.add(fileItem);
    }

    @Override
    public FileItem read(Long id)
    {
        return fileContainer.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(FileItem fileItem)
    {
        for (int i = 0; i < fileContainer.size(); i++) {
            if (fileContainer.get(i).getId().equals(fileItem.getId())) {
                fileContainer.set(i, fileItem);
                return;
            }
        }
    }

    public FileItem findByDownloadCode (String code){
        return fileContainer.stream()
                .filter(item -> item.getDownloadCode() != null && item.getDownloadCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(Long id)
    {
        fileContainer.removeIf(item -> item.getId().equals(id));
    }
}