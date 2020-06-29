package com.charlesluxinger.grpc.unary.server;

import com.charlesluxinger.grpc.unary.service.GreetServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class GreetingServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Server started");

		Server server = ServerBuilder.forPort(9090)
									 .addService(new GreetServiceImpl())
									 .addService(ProtoReflectionService.newInstance())
									 .build();

		server.start();

		Runtime.getRuntime().addShutdownHook( new Thread(
				() -> {
					System.out.println("Received Shutdown Request");
					server.shutdown();
					System.out.println("Successfully stopped the Greeting Server");
				}
		));

		server.awaitTermination();
	}
}
