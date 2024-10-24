package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.services.TransportService;
import com.web.serviceorientedweb.services.dtos.TransportDto;
import com.web.serviceorientedweb.services.dtos.TransportViewDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transports")
public class TransportController {

    private final TransportService transportService;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "transports-exchange";
    private final String routingKey = "transports-routing-key";

    @Autowired
    public TransportController(TransportService transportService, RabbitTemplate rabbitTemplate) {
        this.transportService = transportService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/all")
    public List<EntityModel<TransportDto>> getAllTransports() {
        List<TransportDto> transportDtos = transportService.getAllTransports();
        List<EntityModel<TransportDto>> entityModels = new ArrayList<>();
        for (TransportDto transportDto : transportDtos) {
            EntityModel<TransportDto> entityModel = EntityModel.of(transportDto,
                    linkTo(methodOn(TransportController.class).getTransportById(transportDto.getId())).withSelfRel());
            entityModels.add(entityModel);
        }
        return entityModels;
    }

    @GetMapping("/{id}")
    public EntityModel<TransportViewDto> getTransportById(@PathVariable UUID id) {
        TransportViewDto transportViewDto = transportService.getTransportById(id);
        EntityModel<TransportViewDto> entityModel = EntityModel.of(transportViewDto,
                linkTo(methodOn(TransportController.class).getTransportById(id)).withSelfRel(),
                linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));
        List<TransportDto> transports = transportService.getAllTransports();
        int transportIndex = -1;
        for (int i = 0; i < transports.size(); i++) {
            if (transports.get(i).getId().equals(id)) {
                transportIndex = i;
                break;
            }
        }
        if (transportIndex < transports.size() - 1) {
            UUID nextId = transports.get(transportIndex + 1).getId();
            entityModel.add(linkTo(methodOn(TransportController.class).getTransportById(nextId)).withRel("next"));
        }
        if (transportIndex > 0) {
            UUID previousId = transports.get(transportIndex - 1).getId();
            entityModel.add(linkTo(methodOn(TransportController.class).getTransportById(previousId)).withRel("previous"));
        }
        return entityModel;
    }

    @PostMapping
    public EntityModel<TransportDto> createTransport(@RequestBody TransportViewDto transportViewDto) {
        TransportViewDto createdTransport = transportService.createTransport(transportViewDto);
        TransportDto transportDto = new TransportDto(createdTransport.getType(), createdTransport.getModel());
        rabbitTemplate.convertAndSend(exchange, routingKey, "Transport created: " + createdTransport.getId());
        EntityModel<TransportDto> entityModel = EntityModel.of(transportDto,
                linkTo(methodOn(TransportController.class).getTransportById(createdTransport.getId())).withSelfRel(),
                linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));

        return entityModel;
    }

    @DeleteMapping("/{id}")
    public void deleteTransport(@PathVariable UUID id) {
        transportService.deleteTransport(id);
        rabbitTemplate.convertAndSend(exchange, routingKey, "Transport deleted: " + id);
    }
}

