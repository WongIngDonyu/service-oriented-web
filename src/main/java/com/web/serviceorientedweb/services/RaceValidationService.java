package com.web.serviceorientedweb.services;

import com.web.serviceorientedweb.grpc.RaceValidationGrpc;
import com.web.serviceorientedweb.grpc.RaceValidationOuterClass.RaceTimeRequest;
import com.web.serviceorientedweb.grpc.RaceValidationOuterClass.RaceTimeResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RaceValidationService {

    private final RaceValidationGrpc.RaceValidationBlockingStub raceValidationStub;

    public RaceValidationService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.raceValidationStub = RaceValidationGrpc.newBlockingStub(channel);
    }

    public boolean validateRaceTime(long currentTime, long newTime) {
        RaceTimeRequest request = RaceTimeRequest.newBuilder().setCurrentTime(currentTime).setNewTime(newTime).build();
        RaceTimeResponse response = raceValidationStub.validateRaceTime(request);
        return response.getIsValid();
    }
}
