package com.bookmyshow.service;

import com.bookmyshow.domain.Movie;
import com.bookmyshow.domain.Show;
import com.bookmyshow.domain.Theatre;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.ShowResponse;
import com.bookmyshow.repository.MovieRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;

    @Transactional(readOnly = true)
    public ApiResponse<ShowResponse> getById(Long id) {
        return showRepository.findById(id)
                .map(ShowResponse::from)
                .map(s -> ApiResponse.success("All shows fetched", s))
                .orElse(ApiResponse.failure("Show not found"));
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<ShowResponse>> getByTheatreId(Long theatreId) {
        List<ShowResponse> data = showRepository.findByTheatre_Id(theatreId).stream()
                .map(ShowResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("All shows fetched", data);
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<Map<String, Object>>> getTheatresByMovieAndDate(Long movieId, LocalDate date) {
        LocalDate start = date;
        LocalDate end = date;
        List<Show> shows = showRepository.findByMovie_IdAndDateBetween(movieId, start, end);
        Map<Long, Map<String, Object>> theatreMap = new LinkedHashMap<>();
        for (Show show : shows) {
            Theatre t = show.getTheatre();
            if (t == null) continue;
            theatreMap.computeIfAbsent(t.getId(), k -> {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("_id", t.getId());
                entry.put("name", t.getName());
                entry.put("address", t.getAddress());
                entry.put("phone", t.getPhone());
                entry.put("email", t.getEmail());
                entry.put("isActive", t.getIsActive());
                entry.put("shows", new ArrayList<ShowResponse>());
                return entry;
            });
            @SuppressWarnings("unchecked")
            List<ShowResponse> showList = (List<ShowResponse>) theatreMap.get(t.getId()).get("shows");
            showList.add(ShowResponse.from(show));
        }
        return ApiResponse.success("All theatres fetched", new ArrayList<>(theatreMap.values()));
    }

    @Transactional
    public ApiResponse<Void> add(Show show) {
        if (show.getMovieId() != null) {
            movieRepository.findById(show.getMovieId()).ifPresent(show::setMovie);
        }
        if (show.getTheatreId() != null) {
            theatreRepository.findById(show.getTheatreId()).ifPresent(show::setTheatre);
        }
        showRepository.save(show);
        return ApiResponse.success("new show has been added");
    }

    @Transactional
    public ApiResponse<Void> update(Long id, Show update) {
        return showRepository.findById(id)
                .map(s -> {
                    if (update.getName() != null) s.setName(update.getName());
                    if (update.getDate() != null) s.setDate(update.getDate());
                    if (update.getTime() != null) s.setTime(update.getTime());
                    if (update.getTicketPrice() != null) s.setTicketPrice(update.getTicketPrice());
                    if (update.getTotalSeats() != null) s.setTotalSeats(update.getTotalSeats());
                    if (update.getMovieId() != null) {
                        movieRepository.findById(update.getMovieId()).ifPresent(s::setMovie);
                    }
                    if (update.getTheatreId() != null) {
                        theatreRepository.findById(update.getTheatreId()).ifPresent(s::setTheatre);
                    }
                    showRepository.save(s);
                    return ApiResponse.<Void>success("show has been updated");
                })
                .orElse(ApiResponse.failure("Show not found"));
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        if (!showRepository.existsById(id)) {
            return ApiResponse.failure("Show not found");
        }
        showRepository.deleteById(id);
        return ApiResponse.success("show has been deleted");
    }
}
