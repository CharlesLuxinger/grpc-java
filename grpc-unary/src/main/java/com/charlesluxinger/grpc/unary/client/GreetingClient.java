package com.charlesluxinger.grpc.unary.client;

import com.charlesluxinger.grpc.GreetRequest;
import com.charlesluxinger.grpc.GreetResponse;
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

		Greeting greeting = Greeting.newBuilder().setFirstName("Charles").setLastName("Luxinger").build();
		GreetRequest request = GreetRequest.newBuilder().setGreeting(greeting).build();

		GreetResponse greetResponse = greetClient.greet(request);

		System.out.println(greetResponse.getResult());

		System.out.println("Shutting down channel");
		channel.shutdown();
	}
}
