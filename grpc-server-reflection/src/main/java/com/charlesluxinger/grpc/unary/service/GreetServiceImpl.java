package com.charlesluxinger.grpc.unary.service;

import com.charlesluxinger.grpc.GreetRequest;
import com.charlesluxinger.grpc.GreetResponse;
import com.charlesluxinger.grpc.GreetServiceGrpc.GreetServiceImplBase;
import com.charlesluxinger.grpc.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

	@Override
	public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
		Greeting greeting = request.getGreeting();
		String firstName = greeting.getFirstName();

		String result = "Hello " + firstName;
		GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
