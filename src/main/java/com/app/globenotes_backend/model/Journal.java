package com.app.globenotes_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Entity
@Table(name = "journals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255, nullable = false)
    private String name;

    @Lob
    private String shortSummary;

    @ManyToOne
    @JoinColumn(name = "trip_location_id")
    private Location tripLocation;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 500)
    private String coverPhotoUrl;

    private Boolean remindersEnabled = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL)
    private List<Moment> moments;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
