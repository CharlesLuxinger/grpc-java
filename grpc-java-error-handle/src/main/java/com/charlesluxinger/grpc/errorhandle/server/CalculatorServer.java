package com.charlesluxinger.grpc.errorhandle.server;

import com.charlesluxinger.grpc.errorhandle.service.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class CalculatorServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Server started");

		Server server = ServerBuilder.forPort(9090)
									 .addService(new CalculatorServiceImpl())
									 .addService(ProtoReflectionService.newInstance())
									 .build();

		server.start();

		Runtime.getRuntime().addShutdownHook( new Thread(
				() -> {
					System.out.println("Received Shutdown Request");
					server.shutdown();
					System.out.println("Successfully stopped the Calculator Server");
				}
		));

		server.awaitTermination();
	}
}
