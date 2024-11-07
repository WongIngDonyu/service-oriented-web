package com.web.serviceorientedweb.repositories;

import com.web.serviceorientedweb.models.Race;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RaceRepository extends JpaRepository<Race, UUID> {
    Optional<Race> findByRaceName(String raceName);

    @EntityGraph(attributePaths = "persons")
    Optional<Race> findWithPersonsById(UUID id);
}
