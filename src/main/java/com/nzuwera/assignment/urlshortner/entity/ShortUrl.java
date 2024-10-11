package com.nzuwera.assignment.urlshortner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expireTimestamp;
}
