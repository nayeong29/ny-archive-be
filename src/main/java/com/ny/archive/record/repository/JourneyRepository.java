package com.ny.archive.record.repository;

import com.ny.archive.record.domain.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    List<Journey> findAllByOrderByStartDateDesc();
}
