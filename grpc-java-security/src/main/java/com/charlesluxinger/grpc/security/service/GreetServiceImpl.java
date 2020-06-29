package com.charlesluxinger.grpc.security.service;

import com.charlesluxinger.grpc.GreetWithSecurityRequest;
import com.charlesluxinger.grpc.GreetWithSecurityResponse;
import com.charlesluxinger.grpc.GreetWithSecurityServiceGrpc.GreetWithSecurityServiceImplBase;
import com.charlesluxinger.grpc.Greeting;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetWithSecurityServiceImplBase {

	@Override
	public void greetWithSecurity(GreetWithSecurityRequest request, StreamObserver<GreetWithSecurityResponse> responseObserver) {
		Greeting greeting = request.getGreeting();
		String firstName = greeting.getFirstName();

		String result = "Hello " + firstName;
		GreetWithSecurityResponse response = GreetWithSecurityResponse.newBuilder().setResult(result).build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}