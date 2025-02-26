package com.app.globenotes_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Entity
@Table(name = "moments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Moment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(length = 100, nullable = false)
    private String name;

    @Lob
    private String description;

    private Instant dateTime;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Instant createdAt;
    private Instant updatedAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "moment", cascade = CascadeType.ALL)
    private List<MomentMedia> momentMediaList;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
    }
}
