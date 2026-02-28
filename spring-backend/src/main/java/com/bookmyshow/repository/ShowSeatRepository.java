package com.bookmyshow.repository;

import com.bookmyshow.domain.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bookmyshow.domain.SeatStatus;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShow_IdAndStatus(Long id, SeatStatus status);

    List<ShowSeat> findByShow_Id(Long id);

    Optional<ShowSeat> findByShow_IdAndSeatNumber(Long id, String seatNumber);
}