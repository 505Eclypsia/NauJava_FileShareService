package ru.Vlad.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.Vlad.NauJava.dao.PacketRepositoryCustom;
import ru.Vlad.NauJava.entity.Packet;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/custom/packets")
public class PacketController {
    private PacketRepositoryCustom packetRepositoryCustom;

    @Autowired
    public PacketController (PacketRepositoryCustom packetRepositoryCustom) {
        this.packetRepositoryCustom = packetRepositoryCustom;
    }

    @GetMapping("/findByShareLinkCode")
    public Packet findByShareLinkCode(@RequestParam String shareLinkCode) {
        return packetRepositoryCustom.findByShareLinkCode(shareLinkCode)
                .orElseThrow(() -> new ResourceNotFoundException("Пакет с кодом " + shareLinkCode + " не найден"));
    }

    @GetMapping("/findByOwnerAndCreatedAtBetween")
    public List<Packet> findByOwnerAndCreatedAtBetween(@RequestParam Long ownerId, @RequestParam Instant start, @RequestParam Instant end) {
        return packetRepositoryCustom.findByOwnerAndCreatedAtBetween(ownerId, start, end);
    }
}
