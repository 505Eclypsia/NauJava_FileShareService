package ru.Vlad.NauJava.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.ShareLink;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class PacketRepositoryImpl implements PacketRepositoryCustom {
    private final EntityManager entityManager;

    @Autowired
    public PacketRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Packet> findByShareLinkCode (String shareLinkCode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Packet> criteriaQuery = criteriaBuilder.createQuery(Packet.class);

        Root<Packet> packetRoot = criteriaQuery.from(Packet.class);
        Join<Packet, ShareLink> shareLink = packetRoot.join("shareLinks", JoinType.INNER);
        Predicate shareLinkPredicate = criteriaBuilder.equal(shareLink.get("code"), shareLinkCode);

        criteriaQuery.select(packetRoot).where(shareLinkPredicate);
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public List<Packet> findByOwnerAndCreatedAtBetween(Long ownerId, Instant start, Instant end){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Packet> criteriaQuery = criteriaBuilder.createQuery(Packet.class);

        Root<Packet> packetRoot = criteriaQuery.from(Packet.class);
        Predicate dateTimePredicate = criteriaBuilder.between(packetRoot.get("createdAt"), start, end);
        Predicate ownerPredicate = criteriaBuilder.equal(packetRoot.get("owner").get("id"), ownerId);

        criteriaQuery.select(packetRoot).where(criteriaBuilder.and(dateTimePredicate, ownerPredicate));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
