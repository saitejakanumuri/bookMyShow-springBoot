package com.bookmyshow.service;

import com.bookmyshow.domain.Booking;
import com.bookmyshow.domain.Show;
import com.bookmyshow.domain.ShowSeat;
import com.bookmyshow.domain.SeatStatus;
import com.bookmyshow.domain.User;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.BookingResponse;
import com.bookmyshow.repository.BookingRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.ShowSeatRepository;
import com.bookmyshow.repository.UserRepository;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;
    private final StripeService stripeService;
    private final EmailService emailService;

    public ApiResponse<String> makePayment(String paymentMethodId, Long amount) {
        log.info("makePayment called with - paymentMethodId: {}, amount: {}", paymentMethodId, amount);
        if (paymentMethodId == null || paymentMethodId.isBlank()) {
            return ApiResponse.failure("payment method required");
        }
        try {
            log.info("Creating payment intent with paymentMethodId: {}", paymentMethodId);
            String transactionId = stripeService.createPaymentIntent(paymentMethodId, amount);
            log.info("Payment successful with transactionId: {}", transactionId);
            return ApiResponse.success("payment successfull! ticket(s) booked !", transactionId);
        } catch (StripeException e) {
            log.error("StripeException during payment: {}", e.getMessage(), e);
            return ApiResponse.failure(e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<BookingResponse> bookShow(Long userId, Long showId, List<?> seats, String transactionId) {
        if (userId == null || showId == null || seats == null || seats.isEmpty() || transactionId == null) {
            return ApiResponse.failure("missing required fields");
        }
        // Convert seats to strings in case they come as integers
        List<String> seatStrings = seats.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        log.info("bookShow called with - userId: {}, showId: {}, seats: {}, transactionId: {}", userId, showId, seatStrings, transactionId);
        User user = userRepository.findById(userId).orElse(null);
        Show show = showRepository.findByIdForUpdate(showId);
        if (user == null || show == null) {
            return ApiResponse.failure("user or show not found");
        }

        // Re-check availability of requested seats under the lock
        List<ShowSeat> requestedShowSeats = new ArrayList<>();
        for (String seatNumber : seatStrings) {
            ShowSeat showSeat = showSeatRepository.findByShow_IdAndSeatNumber(showId, seatNumber).orElse(null);
            if (showSeat == null) {
                ShowSeat seat = ShowSeat.builder()
                        .show(show)
                        .seatNumber(seatNumber)
                        .status(SeatStatus.AVAILABLE)
                        .build();
                seat = showSeatRepository.save(seat);
                log.info("Created new ShowSeat with id: {}, seatNumber: {}", seat.getId(), seat.getSeatNumber());
                showSeat = seat;
               // return ApiResponse.failure("seat " + seatNumber + " not found");
            }
            if (showSeat.getStatus() != SeatStatus.AVAILABLE) {
                return ApiResponse.failure("seat " + seatNumber + " is not available");
            }
            requestedShowSeats.add(showSeat);
        }

        // Update seat statuses to BOOKED
        Instant now = Instant.now();
        for (ShowSeat showSeat : requestedShowSeats) {
            showSeat.setStatus(SeatStatus.BOOKED);
            showSeat.setLockedAt(now);
            showSeat.setLockedByUserId(userId);
            showSeatRepository.save(showSeat);
        }

        // Create and save booking
        Booking booking = Booking.builder()
                .user(user)
                .show(show)
                .seats(seatStrings)
                .transactionId(transactionId)
                .build();
        booking = bookingRepository.save(booking);

        Booking populated = bookingRepository.findById(booking.getId()).orElse(booking);
        BigDecimal amount = show.getTicketPrice().multiply(BigDecimal.valueOf(seatStrings.size()));
        emailService.sendTicketEmail(
                user.getEmail(),
                user.getName(),
                show.getMovie() != null ? show.getMovie().getName() : "",
                show.getTheatre() != null ? show.getTheatre().getName() : "",
                show.getDate(),
                show.getTime(),
                seatStrings,
                amount,
                transactionId
        );
        return ApiResponse.success("new booking done !", BookingResponse.from(populated));
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<BookingResponse>> getAllByUser(Long userId) {
        List<BookingResponse> data = bookingRepository.findByUser_IdOrderByCreatedAtDesc(userId).stream()
                .map(BookingResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success("booking fetched !", data);
    }
}
