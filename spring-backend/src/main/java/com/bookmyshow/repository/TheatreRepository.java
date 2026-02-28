package com.bookmyshow.repository;

import com.bookmyshow.domain.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheatreRepository extends JpaRepository<Theatre, Long> {

    List<Theatre> findByOwner_Id(Long ownerId);
}
