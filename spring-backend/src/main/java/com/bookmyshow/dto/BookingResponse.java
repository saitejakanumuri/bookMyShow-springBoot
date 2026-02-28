package com.bookmyshow.dto;

import com.bookmyshow.domain.Booking;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingResponse {

    @JsonProperty("_id")
    private Long id;
    private UserResponse user;
    private ShowResponse show;
    private List<String> seats;
    private String transactionId;

    public static BookingResponse from(Booking b) {
        if (b == null) return null;
        return BookingResponse.builder()
                .id(b.getId())
                .user(UserResponse.from(b.getUser()))
                .show(ShowResponse.from(b.getShow()))
                .seats(b.getSeats() != null ? b.getSeats() : List.of())
                .transactionId(b.getTransactionId())
                .build();
    }
}
