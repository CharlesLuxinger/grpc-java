package com.charlesluxinger.grpc.deadline.client;

import com.charlesluxinger.grpc.GreetWithDeadlineRequest;
import com.charlesluxinger.grpc.GreetWithDeadlineResponse;
import com.charlesluxinger.grpc.GreetWithDeadlineServiceGrpc;
import com.charlesluxinger.grpc.Greeting;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

import static com.charlesluxinger.grpc.GreetWithDeadlineServiceGrpc.*;

public class GreetingClient {
	public static void main(String[] args) {
		System.out.println("Client starting...");
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

		GreetWithDeadlineServiceBlockingStub blockingStub = newBlockingStub(channel);

		try {
			System.out.println("Sending request with a deadline of 5000ms...");
			GreetWithDeadlineResponse response = blockingStub.withDeadline(Deadline.after(5000, TimeUnit.MILLISECONDS))
															 .greetWithDeadline(GreetWithDeadlineRequest.newBuilder()
																	.setGreeting(Greeting.newBuilder()
																			.setFirstName("Charles")
																			.build())
															.build());
			System.out.println(response.getResult());
		} catch (StatusRuntimeException ex){
			if (ex.getStatus().getCode() == Status.DEADLINE_EXCEEDED.getCode()){
				System.out.println("Deadline has been exceeded, we do not want the response.");
			} else {
				ex.printStackTrace();
			}
		}

		try {
			System.out.println("Sending request with a deadline of 4000ms...");
			GreetWithDeadlineResponse response = blockingStub.withDeadline(Deadline.after(4000, TimeUnit.MILLISECONDS))
					.greetWithDeadline(GreetWithDeadlineRequest.newBuilder()
							.setGreeting(Greeting.newBuilder()
									.setFirstName("Charles")
									.build())
							.build());
			System.out.println(response.getResult());
		} catch (StatusRuntimeException ex){
			if (ex.getStatus().getCode() == Status.DEADLINE_EXCEEDED.getCode()){
				System.out.println("Deadline has been exceeded, we do not want the response.");
			} else {
				ex.printStackTrace();
			}
		}

		System.out.println("Shutting down channel");
		channel.shutdown();
	}
}
