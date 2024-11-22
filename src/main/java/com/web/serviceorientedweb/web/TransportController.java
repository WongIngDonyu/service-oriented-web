package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.services.TransportService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.RestController;
import org.web.transportapi.controllers.TransportApi;
import org.web.transportapi.dto.TransportDto;
import org.web.transportapi.dto.TransportViewDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TransportController implements TransportApi {

    private final TransportService transportService;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;
    private final String exchange = "transports-exchange";
    private final String routingKey = "transports-routing-key";

    public TransportController(TransportService transportService, RabbitTemplate rabbitTemplate, ModelMapper modelMapper) {
        this.transportService = transportService;
        this.rabbitTemplate = rabbitTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EntityModel<TransportDto>> getAllTransports() {
        List<com.web.serviceorientedweb.services.dtos.TransportDto> serviceTransportDtos = transportService.getAllTransports();
        List<EntityModel<TransportDto>> transportModels = new ArrayList<>();
        for (com.web.serviceorientedweb.services.dtos.TransportDto serviceDto : serviceTransportDtos) {
            TransportDto transportDto = modelMapper.map(serviceDto, TransportDto.class);
            EntityModel<TransportDto> entityModel = EntityModel.of(transportDto,
                    linkTo(methodOn(TransportController.class).getTransportById(serviceDto.getId())).withSelfRel(),
                    linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));
            transportModels.add(entityModel);
        }

        return transportModels;
    }

    @Override
    public EntityModel<TransportViewDto> getTransportById(UUID id) {
        com.web.serviceorientedweb.services.dtos.TransportViewDto serviceTransportViewDto = transportService.getTransportById(id);
        TransportViewDto transportViewDto = modelMapper.map(serviceTransportViewDto, TransportViewDto.class);
        return EntityModel.of(transportViewDto,
                linkTo(methodOn(TransportController.class).getTransportById(id)).withSelfRel(),
                linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));
    }

    @Override
    public EntityModel<TransportViewDto> createTransport(TransportViewDto transport) {
        com.web.serviceorientedweb.services.dtos.TransportViewDto serviceTransportViewDto = modelMapper.map(transport, com.web.serviceorientedweb.services.dtos.TransportViewDto.class);
        com.web.serviceorientedweb.services.dtos.TransportViewDto createdServiceTransport = transportService.createTransport(serviceTransportViewDto);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Transport created: " + createdServiceTransport.getId());
        TransportViewDto createdTransport = modelMapper.map(createdServiceTransport, TransportViewDto.class);
        return EntityModel.of(createdTransport,
                linkTo(methodOn(TransportController.class).getTransportById(createdServiceTransport.getId())).withSelfRel(),
                linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));
    }

    @Override
    public void deleteTransport(UUID id) {
        transportService.deleteTransport(id);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Transport deleted: " + id);
    }
}
