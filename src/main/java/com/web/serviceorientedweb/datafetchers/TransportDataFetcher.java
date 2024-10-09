package com.web.serviceorientedweb.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.web.serviceorientedweb.models.Transport;
import com.web.serviceorientedweb.services.TransportService;
import com.web.serviceorientedweb.services.dtos.TransportDto;
import com.web.serviceorientedweb.services.dtos.TransportViewDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@DgsComponent
public class TransportDataFetcher {
    private final TransportService transportService;

    @Autowired
    public TransportDataFetcher(TransportService transportService) {
        this.transportService = transportService;
    }

    @DgsQuery
    public List<TransportDto> allTransports() {
        return transportService.getAllTransports();
    }

    @DgsQuery
    public TransportViewDto transportById(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        TransportViewDto transportViewDto = transportService.getTransportById(id);
        return transportViewDto;
    }

    @DgsMutation
    public TransportDto createTransport(DataFetchingEnvironment dataFetchingEnvironment) {
        Map<String, Object> transportInput = dataFetchingEnvironment.getArgument("transport");
        TransportViewDto newTransport = new TransportViewDto(
                Transport.Type.valueOf((String) transportInput.get("type")),
                (String) transportInput.get("model"),
                (Integer) transportInput.get("capacity")
        );
        TransportViewDto createdTransport = transportService.createTransport(newTransport);
        return new TransportDto(createdTransport.getType(), createdTransport.getModel());
    }

    @DgsMutation
    public String deleteTransport(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        transportService.deleteTransport(id);
        return "Transport with ID " + id + " was deleted";
    }
}

