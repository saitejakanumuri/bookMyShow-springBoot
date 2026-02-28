package com.bookmyshow.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "theatres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String address;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String phone;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    /** For JSON: client sends "owner": 5 or "owner": {"_id": 5}; service resolves to User. */
    @Transient
    private Long ownerId;

    @JsonSetter("owner")
    public void setOwnerFromJson(Object o) {
        if (o instanceof Number) {
            this.ownerId = ((Number) o).longValue();
        } else if (o instanceof Map) {
            Object id = ((Map<?, ?>) o).get("_id");
            if (id == null) id = ((Map<?, ?>) o).get("id");
            if (id instanceof Number) this.ownerId = ((Number) id).longValue();
        }
    }

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = false;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void timestamps() {
        if (createdAt == null) createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void updatedAt() {
        updatedAt = Instant.now();
    }
}
