package com.bookmyshow.repository;

import com.bookmyshow.domain.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;


@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByTheatre_Id(Long id);

    List<Show> findByMovie_IdAndDateBetween(Long id, LocalDate start, LocalDate end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Show s WHERE s.id = :id")
    Show findByIdForUpdate(@Param("id") Long id);
}
