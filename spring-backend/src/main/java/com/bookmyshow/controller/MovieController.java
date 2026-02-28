package com.bookmyshow.controller;

import com.bookmyshow.domain.Movie;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.MovieResponse;
import com.bookmyshow.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> add(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.add(movie));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getAll() {
        log.info("Received request to get all movies");
        return ResponseEntity.ok(movieService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getById(@PathVariable Long id) {
        ApiResponse<MovieResponse> res = movieService.getById(id);
        if (!res.isSuccess()) {
            return ResponseEntity.status(404).body(res);
        }
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> update(@PathVariable Long id, @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.update(id, movie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.delete(id));
    }
}
