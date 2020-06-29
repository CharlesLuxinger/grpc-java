package com.charlesluxinger.grpc.bidistream.client;

import com.charlesluxinger.grpc.GreetEveryoneRequest;
import com.charlesluxinger.grpc.GreetEveryoneResponse;
import com.charlesluxinger.grpc.GreetServiceGrpc;
import com.charlesluxinger.grpc.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Client starting...");
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

		GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

		CountDownLatch latch = new CountDownLatch(1);

		StreamObserver<GreetEveryoneRequest> requestObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
			@Override
			public void onNext(GreetEveryoneResponse value) {
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


		Arrays.asList("Charles", "Manuel", "Maria")
			  .forEach( name -> {
					System.out.println("**** Send " + name + " message ***");
					requestObserver.onNext(GreetEveryoneRequest.newBuilder()
							.setGreeting(Greeting.newBuilder()
									.setFirstName(name)
									.build())
							.build());

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			  }
		);


		requestObserver.onCompleted();

		latch.await(10L, TimeUnit.SECONDS);

		System.out.println("Shutting down channel");
		channel.shutdown();
	}
}
