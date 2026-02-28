package com.bookmyshow.service;

import com.bookmyshow.domain.Movie;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.MovieResponse;
import com.bookmyshow.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public ApiResponse<List<MovieResponse>> getAll() {
        log.info("Fetching all movies from the database");
        List<MovieResponse> data = movieRepository.findAll().stream()
                .map(MovieResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("all movies have been retrieved", data);
    }

    @Transactional(readOnly = true)
    public ApiResponse<MovieResponse> getById(Long id) {
        return movieRepository.findById(id)
                .map(m -> ApiResponse.success("movie retrieved successfully", MovieResponse.from(m)))
                .orElse(ApiResponse.failure("Movie not found"));
    }

    @Transactional
    public ApiResponse<Void> add(Movie movie) {
        movieRepository.save(movie);
        return ApiResponse.success("new movies has been added");
    }

    @Transactional
    public ApiResponse<MovieResponse> update(Long id, Movie update) {
        return movieRepository.findById(id)
                .map(m -> {
                    if (update.getName() != null) m.setName(update.getName());
                    if (update.getDescription() != null) m.setDescription(update.getDescription());
                    if (update.getDuration() != null) m.setDuration(update.getDuration());
                    if (update.getGenre() != null) m.setGenre(update.getGenre());
                    if (update.getLanguage() != null) m.setLanguage(update.getLanguage());
                    if (update.getReleaseDate() != null) m.setReleaseDate(update.getReleaseDate());
                    if (update.getPoster() != null) m.setPoster(update.getPoster());
                    return ApiResponse.success("movie updated successfully", MovieResponse.from(movieRepository.save(m)));
                })
                .orElse(ApiResponse.failure("Movie not found"));
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        if (!movieRepository.existsById(id)) {
            return ApiResponse.failure("Movie not found");
        }
        movieRepository.deleteById(id);
        return ApiResponse.success("movie deleted successfully");
    }
}
