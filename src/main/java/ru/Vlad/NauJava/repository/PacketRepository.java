package ru.Vlad.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PacketRepository extends CrudRepository<Packet, Long> {
    @Query("SELECT p FROM Packet p JOIN p.shareLinks sl WHERE sl.code = :shareLinkCode")
    Optional<Packet> findByShareLinkCode (@Param("shareLinkCode") String shareLinkCode);

    List<Packet> findByOwnerAndCreatedAtBetween(User owner, Instant start, Instant end);

}
