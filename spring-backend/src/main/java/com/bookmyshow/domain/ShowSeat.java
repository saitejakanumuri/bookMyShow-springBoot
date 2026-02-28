package com.bookmyshow.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "show_seats",uniqueConstraints = @UniqueConstraint(columnNames = {"show_id","seat_number"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;
    //AVIALABLE, BOOKED, BLOCKED

    @Column(name= "locked_at")
    private Instant lockedAt;

    @Column(name = "locked_by")
    private Long lockedByUserId;

    @Version
    private Long version; //for optimistic locking
}




