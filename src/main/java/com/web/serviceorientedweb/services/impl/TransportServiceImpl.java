package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.repositories.RaceRepository;
import com.web.serviceorientedweb.repositories.TransportRepository;
import com.web.serviceorientedweb.services.TransportService;
import com.web.serviceorientedweb.services.dtos.TransportDto;
import com.web.serviceorientedweb.services.dtos.TransportViewDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransportServiceImpl implements TransportService<UUID> {
    private final ModelMapper modelMapper;
    private final TransportRepository transportRepository;
    public TransportServiceImpl(ModelMapper modelMapper, TransportRepository transportRepository)
    {this.modelMapper = modelMapper;this.transportRepository = transportRepository;}
    @Override
    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    @Override
    public Transport getTransportById(UUID id) {
        return transportRepository.findById(id).orElse(null);
    }

    @Override
    public TransportViewDto createTransport(TransportViewDto transportDto) {
        Transport transport = modelMapper.map(transportDto, Transport.class);
        transport = transportRepository.saveAndFlush(transport);
        return modelMapper.map(transport, TransportViewDto.class);
    }

    @Override
    public void deleteTransport(UUID id) {
        transportRepository.deleteById(id);
    }

    @Override
    public Transport getTransportByModel(String model) {
        return transportRepository.findByModel(model).orElse(null);
    }
}
