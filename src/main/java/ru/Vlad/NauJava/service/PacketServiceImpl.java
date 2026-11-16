package ru.Vlad.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.ShareLink;
import ru.Vlad.NauJava.entity.User;
import ru.Vlad.NauJava.repository.FileRepository;
import ru.Vlad.NauJava.repository.PacketRepository;
import ru.Vlad.NauJava.repository.ShareLinkRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PacketServiceImpl implements PacketService{
    private final ShareLinkRepository shareLinkRepository;
    private final FileRepository fileRepository;
    private final PacketRepository packetRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public PacketServiceImpl(ShareLinkRepository shareLinkRepository,
                             FileRepository fileRepository,
                             PacketRepository packetRepository,
                             PlatformTransactionManager transactionManager){
        this.shareLinkRepository = shareLinkRepository;
        this.fileRepository = fileRepository;
        this.packetRepository = packetRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Packet createPacket(String name, User user) {
        Packet packet =  new Packet();
        packet.setName(name);
        packet.setOwner(user);
        packet.setStorageUUID(UUID.randomUUID());
        packet.setCreatedAt(Instant.now());
        packet.setChangedAt(Instant.now());
        return packetRepository.save(packet);
    }

    @Override
    public void deletePacket(Long id) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try
        {
            Packet packet = packetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пакет с id " + id + " не найден"));

            List<FileItem> files = fileRepository.findByPacket(packet);
            for (FileItem file : files){
                fileRepository.delete(file);
            }

            List<ShareLink> shareLinks = shareLinkRepository.findByPacket(packet);
            for (ShareLink shareLink : shareLinks){
                shareLinkRepository.delete(shareLink);
            }

            packetRepository.delete(packet);
            transactionManager.commit(status);
        }
        catch (RuntimeException ex)
        {
            transactionManager.rollback(status);
            throw ex;
        }
    }

    @Override
    public Packet findByShareLinkCode(String code) {
        Optional<Packet> packet = packetRepository.findByShareLinkCode(code);
        return packet.orElseThrow(() -> new ResourceNotFoundException("Пакет с кодом ссылки " + code + " не найден."));
    }
}
