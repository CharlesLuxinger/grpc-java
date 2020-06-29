package com.charlesluxinger.grpc.deadline.service;

import com.charlesluxinger.grpc.GreetWithDeadlineRequest;
import com.charlesluxinger.grpc.GreetWithDeadlineResponse;
import com.charlesluxinger.grpc.GreetWithDeadlineServiceGrpc.GreetWithDeadlineServiceImplBase;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetWithDeadlineServiceImplBase {

	@Override
	public void greetWithDeadline(GreetWithDeadlineRequest request, StreamObserver<GreetWithDeadlineResponse> responseObserver) {
		try {
			Context context = Context.current();
			for (int i = 0; i < 3; i++) {
				if (!context.isCancelled()){
					System.out.println("Sleeping for 100ms.");
					Thread.sleep(100);
				} else {
					return;
				}
			}

			System.out.println("Sending response.");
			responseObserver.onNext(
					GreetWithDeadlineResponse.newBuilder()
							.setResult("Hello " + request.getGreeting().getFirstName())
							.build()
			);

			responseObserver.onCompleted();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}