package com.web.serviceorientedweb.repositories;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.models.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransportRepository extends JpaRepository<Transport, UUID> {
    Optional<Transport> findByModel(String model);
}
