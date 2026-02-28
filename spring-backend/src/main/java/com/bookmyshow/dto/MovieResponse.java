package com.bookmyshow.dto;

import com.bookmyshow.domain.Movie;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MovieResponse {

    @JsonProperty("_id")
    private Long id;
    private String name;
    private String description;
    private Integer duration;
    private String genre;
    private String language;
    @JsonProperty("releaseDate")
    private LocalDate releaseDate;
    /** Frontend uses movie.date for display; same as releaseDate. */
    @JsonProperty("date")
    public LocalDate getDate() { return releaseDate; }
    private String poster;

    public static MovieResponse from(Movie m) {
        if (m == null) return null;
        return MovieResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .duration(m.getDuration())
                .genre(m.getGenre())
                .language(m.getLanguage())
                .releaseDate(m.getReleaseDate())
                .poster(m.getPoster())
                .build();
    }
}
