package com.bookmyshow.controller;

import com.bookmyshow.domain.Show;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.ShowResponse;
import com.bookmyshow.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> add(@RequestBody Show show) {
        return ResponseEntity.ok(showService.add(show));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @RequestBody Show show) {
        return ResponseEntity.ok(showService.update(id, show));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(showService.delete(id));
    }

    @PostMapping("/get-all-shows-by-theatre")
    public ResponseEntity<ApiResponse<List<ShowResponse>>> getByTheatre(@RequestBody Map<String, Long> body) {
        Long theatreId = body != null ? body.get("theatreId") : null;
        if (theatreId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("theatreId required"));
        }
        return ResponseEntity.ok(showService.getByTheatreId(theatreId));
    }

    @PostMapping("/get-all-theatres-by-movie")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTheatresByMovie(@RequestBody Map<String, Object> body) {
        Object movieObj = body != null ? body.get("movie") : null;
        Object dateObj = body != null ? body.get("date") : null;
        if (movieObj == null || dateObj == null) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("movie and date required"));
        }
        Long movieId = movieObj instanceof Number ? ((Number) movieObj).longValue() : Long.parseLong(movieObj.toString());
        LocalDate date = dateObj instanceof String ? LocalDate.parse((String) dateObj) : LocalDate.parse(dateObj.toString());
        return ResponseEntity.ok(showService.getTheatresByMovieAndDate(movieId, date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowResponse>> getById(@PathVariable Long id) {
        ApiResponse<ShowResponse> res = showService.getById(id);
        if (!res.isSuccess()) {
            return ResponseEntity.status(404).body(res);
        }
        return ResponseEntity.ok(res);
    }
}
