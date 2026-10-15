package ru.Vlad.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.ShareLink;

import java.time.Instant;
import java.util.List;

public interface ShareLinkRepository extends CrudRepository<ShareLink, Long> {
    List<ShareLink> findByPacket (Packet packet);
    List<ShareLink> findByExpiresAtBefore(Instant date);
}