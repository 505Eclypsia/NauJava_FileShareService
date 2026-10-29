package ru.Vlad.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.ShareLink;
import ru.Vlad.NauJava.repository.FileRepository;
import ru.Vlad.NauJava.repository.PacketRepository;
import ru.Vlad.NauJava.repository.ShareLinkRepository;

import java.util.List;

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
        catch (DataAccessException ex)
        {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
