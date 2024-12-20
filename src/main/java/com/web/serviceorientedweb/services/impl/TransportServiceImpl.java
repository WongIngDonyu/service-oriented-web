package com.web.serviceorientedweb.services.impl;

import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.repositories.TransportRepository;
import com.web.serviceorientedweb.services.TransportService;
import org.web.transportapi.dto.TransportDto;
import org.web.transportapi.dto.TransportViewDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransportServiceImpl implements TransportService<UUID> {
    private final ModelMapper modelMapper;
    private final TransportRepository transportRepository;

    public TransportServiceImpl(ModelMapper modelMapper, TransportRepository transportRepository) {
        this.modelMapper = modelMapper;
        this.transportRepository = transportRepository;
    }

    @Override
    public List<TransportDto> getAllTransports() {
        return transportRepository.findAll().stream().map(transport -> modelMapper.map(transport, TransportDto.class)).collect(Collectors.toList());
    }

    @Override
    public TransportViewDto getTransportById(UUID id) {
        return modelMapper.map(transportRepository.findById(id).orElse(null), TransportViewDto.class);
    }

    @Override
    public TransportViewDto createTransport(TransportViewDto transportDto) {
        Transport newTransport = modelMapper.map(transportDto, Transport.class);
        return modelMapper.map(transportRepository.saveAndFlush(newTransport), TransportViewDto.class);
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
