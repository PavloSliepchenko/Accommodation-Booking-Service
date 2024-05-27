package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
