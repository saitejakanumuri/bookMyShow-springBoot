package com.bookmyshow.controller;

import com.bookmyshow.domain.Theatre;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.TheatreResponse;
import com.bookmyshow.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<TheatreResponse>> add(@RequestBody Theatre theatre) {
        return ResponseEntity.ok(theatreService.add(theatre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TheatreResponse>> update(@PathVariable Long id, @RequestBody Theatre theatre) {
        return ResponseEntity.ok(theatreService.update(id, theatre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(theatreService.delete(id));
    }

    @GetMapping("/get-all-theatres")
    public ResponseEntity<ApiResponse<List<TheatreResponse>>> getAll() {
        return ResponseEntity.ok(theatreService.getAll());
    }

    @GetMapping("/get-all-theatres-by-owner")
    public ResponseEntity<ApiResponse<List<TheatreResponse>>> getByOwner(Authentication auth) {
        Long userId = auth != null && auth.getPrincipal() instanceof Long ? (Long) auth.getPrincipal() : null;
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }
        return ResponseEntity.ok(theatreService.getByOwnerId(userId));
    }
}
