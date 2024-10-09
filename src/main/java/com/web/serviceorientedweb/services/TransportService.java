package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.services.dtos.TransportDto;
import com.web.serviceorientedweb.services.dtos.TransportViewDto;

import java.util.List;
import java.util.UUID;

public interface TransportService <I extends UUID>{
    List<TransportDto> getAllTransports();
    TransportViewDto getTransportById(UUID id);
    TransportViewDto createTransport(TransportViewDto transportViewDto);
    void deleteTransport(UUID id);
    Transport getTransportByModel(String model);
}
