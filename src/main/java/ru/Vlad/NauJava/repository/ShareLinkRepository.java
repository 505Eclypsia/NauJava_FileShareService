package ru.Vlad.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.ShareLink;

import java.time.Instant;
import java.util.List;

@RepositoryRestResource(path = "share-links")
public interface ShareLinkRepository extends CrudRepository<ShareLink, Long> {
    List<ShareLink> findByPacket (Packet packet);
    List<ShareLink> findByExpiresAtBefore(Instant date);
}