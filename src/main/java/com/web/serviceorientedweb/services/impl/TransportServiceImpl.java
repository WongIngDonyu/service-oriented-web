package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.repositories.TransportRepository;
import com.web.serviceorientedweb.services.TransportService;
import com.web.serviceorientedweb.services.dtos.TransportDto;
import org.modelmapper.ModelMapper;
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
    public TransportDto createTransport(TransportDto transportDto) {
        Transport transport = modelMapper.map(transportDto, Transport.class);
        return modelMapper.map(transportRepository.saveAndFlush(transport), TransportDto.class);
    }

    @Override
    public void deleteTransport(UUID id) {
        transportRepository.deleteById(id);
    }
}
