package com.charlesluxinger.grpc.errorhandle.client;

import com.charlesluxinger.grpc.SquareRootRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import static com.charlesluxinger.grpc.CalculatorServiceGrpc.CalculatorServiceBlockingStub;
import static com.charlesluxinger.grpc.CalculatorServiceGrpc.newBlockingStub;

public class CalculatorClient {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Client starting...");
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

		CalculatorServiceBlockingStub blockingStub = newBlockingStub(channel);

		try {
			blockingStub.squareRoot(SquareRootRequest.newBuilder().setNumber(-1).build());
		} catch (StatusRuntimeException ex){
			System.out.println("Got an exception for square root.");
			ex.printStackTrace();
		}

		System.out.println("Shutting down channel");
		channel.shutdown();
	}
}
