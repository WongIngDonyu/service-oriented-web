package com.web.serviceorientedweb.grpc;

import com.web.serviceorientedweb.grpc.RaceValidationOuterClass.RaceTimeRequest;
import com.web.serviceorientedweb.grpc.RaceValidationOuterClass.RaceTimeResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class RaceValidationServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090).addService(new RaceValidationServiceImpl()).build();
        System.out.println("Starting gRPC server on port 9090...");
        server.start();
        System.out.println("gRPC server started.");
        server.awaitTermination();
    }

    static class RaceValidationServiceImpl extends RaceValidationGrpc.RaceValidationImplBase {

        @Override
        public void validateRaceTime(RaceTimeRequest request, StreamObserver<RaceTimeResponse> responseObserver) {
            long currentTime = request.getCurrentTime();
            long newTime = request.getNewTime();
            long twelveHoursInSeconds = 43200;
            boolean isValid = newTime > currentTime && (newTime - currentTime) <= twelveHoursInSeconds;
            String message;
            if (isValid) {
                message = "Время рейса допустимо.";
            } else if (newTime <= currentTime) {
                message = "Время рейса недопустимо: новое время должно быть больше текущего времени.";
            } else {
                message = "Время рейса недопустимо: новое время не более 12 часов от текущего времени.";
            }
            System.out.println("Validation result: " + message);
            RaceTimeResponse response = RaceTimeResponse.newBuilder().setIsValid(isValid).setMessage(message).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}


