package com.charlesluxinger.grpc.serverstream.client;

import com.charlesluxinger.grpc.GreetRequest;
import com.charlesluxinger.grpc.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static com.charlesluxinger.grpc.GreetServiceGrpc.GreetServiceBlockingStub;
import static com.charlesluxinger.grpc.GreetServiceGrpc.newBlockingStub;

public class GreetingClient {
	public static void main(String[] args) {
		System.out.println("Client starting...");
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

		GreetServiceBlockingStub greetClient = newBlockingStub(channel);

		GreetRequest request = GreetRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("Charles").build()).build();

		greetClient.greetManyTimes(request)
				   .forEachRemaining(response -> System.out.println(response.getResult()));

		System.out.println("Shutting down channel");
		channel.shutdown();
	}
}
