package com.web.serviceorientedweb.web;

import com.web.serviceorientedweb.models.Race;
import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.services.TransportService;
import com.web.serviceorientedweb.services.dtos.TransportDto;
import com.web.serviceorientedweb.services.dtos.TransportViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/transports")
public class TransportController {

    private TransportService transportService;

    @Autowired
    public void setTransportService(TransportService transportService) {this.transportService = transportService;}

    @GetMapping("/all")
    public List<EntityModel<TransportDto>> getAllTransports() {
        List<Transport> transports = transportService.getAllTransports();
        List<EntityModel<TransportDto>> transportDtos = new ArrayList<>();
        for (Transport transport : transports) {
            TransportDto transportDto = new TransportDto(transport.getType(), transport.getModel());
            EntityModel<TransportDto> resource = EntityModel.of(transportDto,
                    linkTo(methodOn(TransportController.class).getTransportById(transport.getId())).withSelfRel());
            transportDtos.add(resource);
        }
        return transportDtos;
    }

    @GetMapping("/{id}")
    public EntityModel<TransportViewDto> getTransportById(@PathVariable UUID id) {
        Transport transport = transportService.getTransportById(id);
        List<String> racesName = new ArrayList<>();
        for (Race race : transport.getRaces()) {
            racesName.add(race.getRaceName());
        }
        TransportViewDto transportViewDto = new TransportViewDto(transport.getType(),transport.getModel(),transport.getCapacity(),racesName);
        EntityModel<TransportViewDto> resource = EntityModel.of(transportViewDto,
                linkTo(methodOn(TransportController.class).getTransportById(transport.getId())).withSelfRel(),
                linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));
        List<Transport> transports = transportService.getAllTransports();
        int transportIndex = transports.indexOf(transport);
        if (transportIndex < transports.size() - 1) {
            resource.add(linkTo(methodOn(TransportController.class).getTransportById(transports.get(transportIndex + 1).getId())).withRel("next"));
        }
        if (transportIndex > 0) {
            resource.add(linkTo(methodOn(TransportController.class).getTransportById(transports.get(transportIndex - 1).getId())).withRel("previous"));
        }
        return resource;
    }



    @PostMapping
    public EntityModel<TransportDto> createTransport(@RequestBody TransportViewDto transport) {

        TransportViewDto transportViewDto = transportService.createTransport(transport);
        TransportDto transportDto = new TransportDto(transportViewDto.getType(), transportViewDto.getModel());
        EntityModel<TransportDto> resource = EntityModel.of(transportDto,
                linkTo(methodOn(TransportController.class).getTransportById(transportViewDto.getId())).withSelfRel(),
                linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));
        return resource;
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> deleteTransport(@PathVariable UUID id) {
        transportService.deleteTransport(id);
        EntityModel<String> resource = EntityModel.of("Transport with ID " + id + " was deleted",
                linkTo(methodOn(TransportController.class).getAllTransports()).withRel("all-transports"));
        return resource;
    }
}

