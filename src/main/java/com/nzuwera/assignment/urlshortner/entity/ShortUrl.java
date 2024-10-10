package com.nzuwera.assignment.urlshortner.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "SHORT_URLS", indexes = @Index(name = "URL_ID_IDX", columnList = "URL_ID", unique = true))
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @URL
    @Column(name = "URL", nullable = false, unique = true)
    private String url;
    @Column(name = "URL_ID", nullable = false)
    private String urlId;
    @Column(name = "EXPIRE_TIMESTAMP")
    private LocalDateTime expireTimestamp;
}
