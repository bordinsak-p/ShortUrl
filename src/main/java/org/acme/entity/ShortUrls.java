package org.acme.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "short_urls")
public class ShortUrls {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "created_at")
    private String createdAt;
}
