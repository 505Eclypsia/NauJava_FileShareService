package ru.Vlad.NauJava.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "packets")
public class Packet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "storage_uuid", unique = true)
    private UUID storageUUID;

    @Column(name = "changed_at")
    private Instant changedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = true)
    private User owner;

    @OneToMany(mappedBy = "packet", cascade = CascadeType.ALL)
    private List<FileItem> files;

    @OneToMany(mappedBy = "packet", cascade = CascadeType.ALL)
    private List<ShareLink> shareLinks;

    public Packet(){
    }

    public Packet(Long id, String name, UUID storageUUID, Instant changedAt, Instant createdAt, User owner, List<FileItem> files, List<ShareLink> shareLinks) {
        this.id = id;
        this.name = name;
        this.storageUUID = storageUUID;
        this.changedAt = changedAt;
        this.createdAt = createdAt;
        this.owner = owner;
        this.files = files;
        this.shareLinks = shareLinks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getStorageUUID() {
        return storageUUID;
    }

    public void setStorageUUID(UUID storageUUID) {
        this.storageUUID = storageUUID;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<FileItem> getFiles() {
        return files;
    }

    public void setFiles(List<FileItem> files) {
        this.files = files;
    }

    public List<ShareLink> getShareLinks() {
        return shareLinks;
    }

    public void setShareLinks(List<ShareLink> shareLinks) {
        this.shareLinks = shareLinks;
    }
}
