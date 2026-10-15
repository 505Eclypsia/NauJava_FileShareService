package ru.Vlad.NauJava.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "share_links")
public class ShareLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "download_code", unique = true)
    private String code;

    @Column(name = "max_downloads")
    private int maxDownloads;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name="packet_id", nullable = false)
    private Packet packet;

    @OneToMany(mappedBy = "shareLink", cascade = CascadeType.ALL)
    private List<DownloadLog> downloadLogs;

    public ShareLink(){
    }

    public ShareLink(Long id, String code, int maxDownloads, Instant expiresAt, Instant createdAt, Packet packet, List<DownloadLog> downloadLogs) {
        this.id = id;
        this.code = code;
        this.maxDownloads = maxDownloads;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.packet = packet;
        this.downloadLogs = downloadLogs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMaxDownloads() {
        return maxDownloads;
    }

    public void setMaxDownloads(int maxDownloads) {
        this.maxDownloads = maxDownloads;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
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

    public List<DownloadLog> getDownloadLogs() {
        return downloadLogs;
    }

    public void setDownloadLogs(List<DownloadLog> downloadLogs) {
        this.downloadLogs = downloadLogs;
    }
}
