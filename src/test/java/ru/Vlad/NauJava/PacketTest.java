package ru.Vlad.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Vlad.NauJava.dao.PacketRepositoryCustom;
import ru.Vlad.NauJava.entity.FileItem;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.ShareLink;
import ru.Vlad.NauJava.entity.User;
import ru.Vlad.NauJava.repository.FileRepository;
import ru.Vlad.NauJava.repository.PacketRepository;
import ru.Vlad.NauJava.repository.ShareLinkRepository;
import ru.Vlad.NauJava.repository.UserRepository;
import ru.Vlad.NauJava.service.PacketService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class PacketTest {
    private final UserRepository userRepository;
    private final PacketRepository packetRepository;
    private final PacketRepositoryCustom packetRepositoryCustom;
    private final FileRepository fileRepository;
    private final ShareLinkRepository shareLinkRepository;
    private final PacketService packetService;

    @Autowired
    PacketTest(UserRepository userRepository,
               PacketRepository packetRepository,
               PacketRepositoryCustom packetRepositoryCustom,
               FileRepository fileRepository,
               ShareLinkRepository shareLinkRepository,
               PacketService packetService){
        this.userRepository = userRepository;
        this.packetRepository = packetRepository;
        this.packetRepositoryCustom = packetRepositoryCustom;
        this.fileRepository = fileRepository;
        this.shareLinkRepository = shareLinkRepository;
        this.packetService = packetService;
    }

    @Test
    void testFindPacketByShareLinkCode() {
        Packet packet = new Packet();
        packet.setName("Тестовый пакет");
        packet.setStorageUUID(UUID.randomUUID());
        packet.setCreatedAt(Instant.now());
        packetRepository.save(packet);

        String code = UUID.randomUUID().toString();

        ShareLink shareLink = new ShareLink();
        shareLink.setCode(code);
        shareLink.setCreatedAt(Instant.now());
        shareLink.setPacket(packet);
        shareLinkRepository.save(shareLink);

        Optional<Packet> foundPacket = packetRepository.findByShareLinkCode(code);

        Assertions.assertTrue(foundPacket.isPresent());
        Assertions.assertEquals(packet.getId(), foundPacket.get().getId());

        Optional<Packet> foundPacketCustom = packetRepositoryCustom.findByShareLinkCode(code);
        Assertions.assertEquals(foundPacket.get().getId(), foundPacketCustom.get().getId());
    }

    @Test
    void testFindPacketByOwnerAndCreatedAtBetween() {
        User user = new User();
        user.setUsername("Тестовый пользователь");
        user.setCreatedAt(Instant.now());
        userRepository.save(user);

        Packet packet = new Packet();
        packet.setName("Тестовый пакет");
        packet.setStorageUUID(UUID.randomUUID());
        packet.setCreatedAt(Instant.now());
        packet.setOwner(user);
        packetRepository.save(packet);

        List<Packet> foundPackets = packetRepository.findByOwnerAndCreatedAtBetween(user,
                Instant.now().minus(Duration.ofMinutes(1)),
                Instant.now());
        Assertions.assertEquals(packet.getId(), foundPackets.getFirst().getId());

        List<Packet> foundPacketsFalse = packetRepository.findByOwnerAndCreatedAtBetween(user,
                Instant.now().minus(Duration.ofMinutes(10)),
                Instant.now().minus(Duration.ofMinutes(5)));
        Assertions.assertTrue(foundPacketsFalse.isEmpty());

        List<Packet> foundCustom = packetRepositoryCustom.findByOwnerAndCreatedAtBetween(user,
                Instant.now().minus(Duration.ofMinutes(1)),
                Instant.now());
        Assertions.assertEquals(packet.getId(), foundCustom.getFirst().getId());
    }

    @Test
    void testDeletePacketInTx() {
        Packet packet = new Packet();
        packet.setName("Тестовый пакет");
        packet.setStorageUUID(UUID.randomUUID());
        packet.setCreatedAt(Instant.now());
        packetRepository.save(packet);
        Long packetId = packet.getId();

        FileItem file1 = new FileItem();
        file1.setName("Тестовый файл №1");
        file1.setSizeBytes(5L);
        file1.setPacket(packet);
        file1.setCreatedAt(Instant.now());
        fileRepository.save(file1);

        FileItem file2 = new FileItem();
        file2.setName("Тестовый файл №2");
        file2.setSizeBytes(5L);
        file2.setPacket(packet);
        file2.setCreatedAt(Instant.now());
        fileRepository.save(file2);

        ShareLink shareLink = new ShareLink();
        shareLink.setCode(UUID.randomUUID().toString());
        shareLink.setPacket(packet);
        shareLink.setCreatedAt(Instant.now());
        shareLinkRepository.save(shareLink);

        packetService.deletePacket(packet.getId());
        Optional<Packet> foundPacket = packetRepository.findById(packetId);
        Assertions.assertTrue(foundPacket.isEmpty());

        List<FileItem> foundFiles = fileRepository.findByPacket(packet);
        Assertions.assertTrue(foundFiles.isEmpty());

        List<ShareLink> foundShareLinks = shareLinkRepository.findByPacket(packet);
        Assertions.assertTrue(foundShareLinks.isEmpty());
    }

    @Test
    void testDeletePacketRollback() {
        Long fakeId = 999999L;

        Assertions.assertThrows(Exception.class, () -> {
            packetService.deletePacket(fakeId);
        });
    }

}
