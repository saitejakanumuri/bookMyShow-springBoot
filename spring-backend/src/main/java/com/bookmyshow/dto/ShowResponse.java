package com.bookmyshow.dto;

import com.bookmyshow.domain.Show;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import com.bookmyshow.domain.SeatStatus;
import java.util.stream.Collectors;
import com.bookmyshow.domain.ShowSeat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ShowResponse {

    @JsonProperty("_id")
    private Long id;
    private String name;
    private LocalDate date;
    private String time;
    private MovieResponse movie;
    private TheatreResponse theatre;
    private BigDecimal ticketPrice;
    private Integer totalSeats;
    private List<String> bookedSeats;

    public static ShowResponse from(Show s) {
        if (s == null) return null;
        return ShowResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .date(s.getDate())
                .time(s.getTime())
                .movie(MovieResponse.from(s.getMovie()))
                .theatre(TheatreResponse.from(s.getTheatre()))
                .ticketPrice(s.getTicketPrice())
                .totalSeats(s.getTotalSeats())
                .bookedSeats(s.getSeats() != null ?
                        s.getSeats().stream()
                        .filter(ss -> ss.getStatus() == SeatStatus.BOOKED)
                        .map(ShowSeat :: getSeatNumber)
                        .collect(Collectors.toList())
                        : List.of()
                )
                .build();
    }
}
