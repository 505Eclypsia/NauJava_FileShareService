package ru.Vlad.NauJava.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column (name = "password_hash")
    private String passwordHash;

    @Column (name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Packet> packets;

    public User(){
    }

    public User(Long id, String username, String passwordHash, Instant createdAt, List<Packet> packets) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.packets = packets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<Packet> getPackets() {
        return packets;
    }

    public void setPackets(List<Packet> packets) {
        this.packets = packets;
    }
}
