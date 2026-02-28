package com.bookmyshow.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "shows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long movieId;
    @Transient
    private Long theatreId;

    @JsonSetter("movie")
    public void setMovieFromJson(Object o) {
        if (o instanceof Number) this.movieId = ((Number) o).longValue();
        else if (o instanceof Map) {
            Object id = ((Map<?, ?>) o).get("_id");
            if (id == null) id = ((Map<?, ?>) o).get("id");
            if (id instanceof Number) this.movieId = ((Number) id).longValue();
        }
    }

    @JsonSetter("theatre")
    public void setTheatreFromJson(Object o) {
        if (o instanceof Number) this.theatreId = ((Number) o).longValue();
        else if (o instanceof Map) {
            Object id = ((Map<?, ?>) o).get("_id");
            if (id == null) id = ((Map<?, ?>) o).get("id");
            if (id instanceof Number) this.theatreId = ((Number) id).longValue();
        }
    }

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @NotNull
    @Column(name = "ticket_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    @NotNull
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    // @Column(name = "booked_seats", columnDefinition = "jsonb")
    // @Convert(converter = StringListConverter.class)
    // @Builder.Default
    // private List<String> bookedSeats = new ArrayList<>();

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShowSeat> seats = new ArrayList<>();

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
