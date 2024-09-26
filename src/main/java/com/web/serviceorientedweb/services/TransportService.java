package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.services.dtos.TransportDto;

import java.util.List;
import java.util.UUID;

public interface TransportService <I extends UUID>{
    List<Transport> getAllTransports();
    Transport getTransportById(UUID id);
    TransportDto createTransport(TransportDto transportDto);
    void deleteTransport(UUID id);
}
