package com.bookmyshow.controller;

import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.BookingResponse;
import com.bookmyshow.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/make-payment")
    public ResponseEntity<ApiResponse<String>> makePayment(@RequestBody Map<String, Object> body) {
        String paymentMethodId = body != null && body.get("paymentMethodId") != null ? body.get("paymentMethodId").toString() : null;
        Object amountObj = body != null ? body.get("amount") : null;
        if (amountObj == null) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("amount required"));
        }
        long amount = amountObj instanceof Number ? ((Number) amountObj).longValue() : Long.parseLong(amountObj.toString());
        return ResponseEntity.ok(bookingService.makePayment(paymentMethodId, amount));
    }

    @PostMapping("/book-show")
    public ResponseEntity<ApiResponse<BookingResponse>> bookShow(Authentication auth, @RequestBody Map<String, Object> body) {
        Long userId = auth != null && auth.getPrincipal() instanceof Long ? (Long) auth.getPrincipal() : null;
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }
        Object showIdObj = body != null ? body.get("show") : null;
        Object seatsObj = body != null ? body.get("seats") : null;
        Object txIdObj = body != null ? body.get("transactionId") : null;
        if (showIdObj == null || seatsObj == null || txIdObj == null) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("show, seats and transactionId required"));
        }
        Long showId = showIdObj instanceof Number ? ((Number) showIdObj).longValue() : Long.parseLong(showIdObj.toString());
        @SuppressWarnings("unchecked")
        List<String> seats = body.get("seats") instanceof List ? (List<String>) body.get("seats") : List.of();
        String transactionId = txIdObj.toString();
        return ResponseEntity.ok(bookingService.bookShow(userId, showId, seats, transactionId));
    }

    @GetMapping("/all-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings(Authentication auth) {
        Long userId = auth != null && auth.getPrincipal() instanceof Long ? (Long) auth.getPrincipal() : null;
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }
        return ResponseEntity.ok(bookingService.getAllByUser(userId));
    }
}
