package ru.Vlad.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.entity.Packet;

import java.util.List;

public interface FileRepository extends CrudRepository<FileItem, Long> {
    List<FileItem> findByPacket (Packet packet);
}
