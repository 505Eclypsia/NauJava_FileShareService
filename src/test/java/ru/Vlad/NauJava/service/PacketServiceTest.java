package ru.Vlad.NauJava.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.ShareLink;
import ru.Vlad.NauJava.entity.User;
import ru.Vlad.NauJava.repository.FileRepository;
import ru.Vlad.NauJava.repository.PacketRepository;
import ru.Vlad.NauJava.repository.ShareLinkRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class PacketServiceTest {

    @Test
    void testCreatePacket() {
        PacketRepository mockPacketRepository = Mockito.mock(PacketRepository.class);
        PacketService packetService = new PacketServiceImpl(
                null,
                null,
                mockPacketRepository,
                null);

        User user = new User();
        user.setUsername("TestUser");
        packetService.createPacket("TestPacket", user);

        ArgumentCaptor<Packet> packetCaptor = ArgumentCaptor.forClass(Packet.class);
        Mockito.verify(mockPacketRepository).save(packetCaptor.capture());

        Packet packet = packetCaptor.getValue();

        Assertions.assertEquals("TestPacket", packet.getName());
        Assertions.assertEquals(user, packet.getOwner());
        Assertions.assertNotNull(packet.getStorageUUID());
        Assertions.assertNotNull(packet.getCreatedAt());
    }

    @Test
    void testDeletePacketFailsIfPacketDoesNotExist() {
        PacketRepository mockPacketRepo = Mockito.mock(PacketRepository.class);
        FileRepository mockFileRepo = Mockito.mock(FileRepository.class);
        PlatformTransactionManager mockTxManager = Mockito.mock(PlatformTransactionManager.class);
        Mockito.when(mockTxManager.getTransaction(any())).thenReturn(Mockito.mock(TransactionStatus.class));

        Mockito.when(mockPacketRepo.findById(42L)).thenReturn(Optional.empty());

        PacketService packetService = new PacketServiceImpl(null, mockFileRepo, mockPacketRepo, mockTxManager);

        Assertions.assertThrows(IllegalArgumentException.class, () -> packetService.deletePacket(42L));

        Mockito.verify(mockTxManager).rollback(any());
        Mockito.verify(mockFileRepo, Mockito.never()).delete(any());
    }

    @Test
    void testDeletePacketIfPacketExists() {
        ShareLinkRepository mockShareLinkRepo = Mockito.mock(ShareLinkRepository.class);
        PacketRepository mockPacketRepo = Mockito.mock(PacketRepository.class);
        FileRepository mockFileRepo = Mockito.mock(FileRepository.class);
        PlatformTransactionManager mockTxManager = Mockito.mock(PlatformTransactionManager.class);
        TransactionStatus mockTxStatus = Mockito.mock(TransactionStatus.class);


        Packet packet = new Packet();
        FileItem file1 = new FileItem();
        FileItem file2 = new FileItem();
        ShareLink shareLink = new ShareLink();

        Mockito.when(mockTxManager.getTransaction(any())).thenReturn(mockTxStatus);
        Mockito.when(mockPacketRepo.findById(1L)).thenReturn(Optional.of(packet));
        Mockito.when(mockFileRepo.findByPacket(packet)).thenReturn(List.of(file1, file2));
        Mockito.when(mockShareLinkRepo.findByPacket(packet)).thenReturn(List.of(shareLink));

        PacketService packetService = new PacketServiceImpl(mockShareLinkRepo, mockFileRepo, mockPacketRepo, mockTxManager);
        packetService.deletePacket(1L);

        Mockito.verify(mockFileRepo).delete(file1);
        Mockito.verify(mockFileRepo).delete(file2);
        Mockito.verify(mockShareLinkRepo).delete(shareLink);
        Mockito.verify(mockPacketRepo).delete(packet);
        Mockito.verify(mockTxManager).commit(mockTxStatus);
    }

    @Test
    void testDeletePacketRollsBackOnDbFailure() {
        ShareLinkRepository mockShareLinkRepo = Mockito.mock(ShareLinkRepository.class);
        PacketRepository mockPacketRepo = Mockito.mock(PacketRepository.class);
        FileRepository mockFileRepo = Mockito.mock(FileRepository.class);
        PlatformTransactionManager mockTxManager = Mockito.mock(PlatformTransactionManager.class);
        TransactionStatus mockTxStatus = Mockito.mock(TransactionStatus.class);

        Packet packet = new Packet();

        Mockito.when(mockTxManager.getTransaction(any())).thenReturn(mockTxStatus);
        Mockito.when(mockPacketRepo.findById(1L)).thenReturn(Optional.of(packet));
        Mockito.when(mockFileRepo.findByPacket(packet)).thenReturn(List.of());
        Mockito.when(mockShareLinkRepo.findByPacket(packet)).thenReturn(List.of());

        Mockito.doThrow(new DataAccessResourceFailureException("БД недоступна"))
                .when(mockPacketRepo).delete(packet);

        PacketService packetService = new PacketServiceImpl(mockShareLinkRepo, mockFileRepo, mockPacketRepo, mockTxManager);

        Assertions.assertThrows(DataAccessException.class, () -> packetService.deletePacket(1L));

        Mockito.verify(mockTxManager).rollback(mockTxStatus);
        Mockito.verify(mockTxManager, Mockito.never()).commit(any());
    }

    @Test
    void testFindByShareLinkCodePositive() {
        PacketRepository mockPacketRepo = Mockito.mock(PacketRepository.class);
        PacketServiceImpl packetService = new PacketServiceImpl(null, null, mockPacketRepo, null);

        Packet packet = new Packet();
        String code = "qwertyabc";

        Mockito.when(mockPacketRepo.findByShareLinkCode(code)).thenReturn(Optional.of(packet));
        Assertions.assertEquals(packet, packetService.findByShareLinkCode(code));
    }

    @Test
    void testFindByShareLinkCodeNegative() {
        PacketRepository mockPacketRepo = Mockito.mock(PacketRepository.class);
        PacketServiceImpl packetService = new PacketServiceImpl(null, null, mockPacketRepo, null);

        String code = "qwertyabc";

        Mockito.when(mockPacketRepo.findByShareLinkCode(code)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> packetService.findByShareLinkCode(code));
    }


}
