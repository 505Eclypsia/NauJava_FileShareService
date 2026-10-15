package ru.Vlad.NauJava.entity;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name = "files")
public class FileItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name="size_bytes")
    private Long sizeBytes;

    @Column(name="created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name="packet_id", nullable = false)
    private Packet packet;


    public FileItem(){
    }

    public FileItem(Long id, String name, Long sizeBytes, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.sizeBytes = sizeBytes;
        this.createdAt = createdAt;
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

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    @Override
    public String toString() {
        return "Файл [ID: " + id +
                ", Имя: '" + name + '\'' +
                ", Размер: " + sizeBytes + " байт" +
                ", Дата: " + createdAt + "]";
    }
}
