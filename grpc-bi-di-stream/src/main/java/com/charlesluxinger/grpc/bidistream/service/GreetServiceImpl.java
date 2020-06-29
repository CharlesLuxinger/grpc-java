package com.charlesluxinger.grpc.bidistream.service;

import com.charlesluxinger.grpc.GreetEveryoneRequest;
import com.charlesluxinger.grpc.GreetEveryoneResponse;
import com.charlesluxinger.grpc.GreetServiceGrpc.GreetServiceImplBase;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

	@Override
	public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
		return new StreamObserver<GreetEveryoneRequest>() {
			@Override
			public void onNext(GreetEveryoneRequest value) {
				responseObserver.onNext(
						GreetEveryoneResponse.newBuilder().setResult("Hello " + value.getGreeting().getFirstName() + "\n").build()
				);
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("Error on Service");
			}

			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}
}