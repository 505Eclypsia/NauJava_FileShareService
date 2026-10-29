package ru.Vlad.NauJava.entity;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name="download_logs")
public class DownloadLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_adress")
    private String ipAdress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "share_link_id", nullable = false)
    private ShareLink shareLink;

    public DownloadLog(){
    }

    public DownloadLog(Long id, String ipAdress, String userAgent, Instant createdAt, ShareLink shareLink) {
        this.id = id;
        this.ipAdress = ipAdress;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
        this.shareLink = shareLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ShareLink getShareLink() {
        return shareLink;
    }

    public void setShareLink(ShareLink shareLink) {
        this.shareLink = shareLink;
    }
}
