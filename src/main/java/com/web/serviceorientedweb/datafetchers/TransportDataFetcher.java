package com.web.serviceorientedweb.datafetchers;

import com.web.serviceorientedweb.models.Race;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.services.TransportService;
import com.web.serviceorientedweb.services.dtos.TransportDto;
import com.web.serviceorientedweb.services.dtos.TransportViewDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@DgsComponent
public class TransportDataFetcher {
    private TransportService transportService;

    @Autowired
    public void setTransportService(TransportService transportService) {
        this.transportService = transportService;
    }

    @DgsQuery
    public List<TransportDto> allTransports() {
        List<Transport> transports = transportService.getAllTransports();
        List<TransportDto> transportDtos = new ArrayList<>();
        for (Transport transport : transports) {
            TransportDto transportDto = new TransportDto(transport.getType(), transport.getModel());
            transportDtos.add(transportDto);
        }
        return transportDtos;
    }

    @DgsQuery
    public TransportViewDto transportById(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        Transport transport = transportService.getTransportById(id);
        List<String> racesName = new ArrayList<>();
        for (Race race : transport.getRaces()) {
            racesName.add(race.getRaceName());
        }

        return new TransportViewDto(transport.getType(), transport.getModel(), transport.getCapacity(), racesName);
    }

    @DgsMutation
    public TransportDto createTransport(DataFetchingEnvironment dataFetchingEnvironment) {
        Map<String, Object> transportInput = dataFetchingEnvironment.getArgument("transport");
        TransportViewDto newTransport = transportService.createTransport(new TransportViewDto(
                (Transport.Type) transportInput.get("type"),
                (String) transportInput.get("model"),
                (Integer) transportInput.get("capacity"),
                (List<String>) transportInput.get("racesName")
        ));
        return new TransportDto(newTransport.getType(), newTransport.getModel());
    }

    @DgsMutation
    public String deleteTransport(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        transportService.deleteTransport(id);
        return "Transport with ID " + id + " was deleted";
    }
}
