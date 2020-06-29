package com.charlesluxinger.grpc.clientstream.client;

import com.charlesluxinger.grpc.GreetServiceGrpc;
import com.charlesluxinger.grpc.Greeting;
import com.charlesluxinger.grpc.LongGreetRequest;
import com.charlesluxinger.grpc.LongGreetResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Client starting...");
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

		GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

		CountDownLatch latch = new CountDownLatch(1);

		StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
			@Override
			public void onNext(LongGreetResponse value) {
				System.out.println("Received a response from the Server");
				System.out.println(value.getResult());
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("Error on Service");
			}

			@Override
			public void onCompleted() {
				System.out.println("Server has completed sending us something");
				latch.countDown();
			}
		});

		System.out.println("Sending Message #1");
		requestObserver.onNext(LongGreetRequest.newBuilder()
				.setGreeting(Greeting.newBuilder()
									 .setFirstName("Charles")
									 .build())
				.build());

		System.out.println("Sending Message #2");
		requestObserver.onNext(LongGreetRequest.newBuilder()
				.setGreeting(Greeting.newBuilder()
									 .setFirstName("Manuel")
									 .build())
				.build());

		System.out.println("Sending Message #3");
		requestObserver.onNext(LongGreetRequest.newBuilder()
				.setGreeting(Greeting.newBuilder()
									 .setFirstName("Maria")
									 .build())
				.build());

		requestObserver.onCompleted();

		latch.await(10L, TimeUnit.SECONDS);

		System.out.println("Shutting down channel");
		channel.shutdown();
	}
}
