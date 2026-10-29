package ru.Vlad.NauJava.dao;

import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PacketRepositoryCustom {
    Optional<Packet> findByShareLinkCode(String shareLinkCode);
    List<Packet> findByOwnerAndCreatedAtBetween(User owner, Instant start, Instant end);
}
