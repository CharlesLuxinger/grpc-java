package com.charlesluxinger.grpc.clientstream.service;

import com.charlesluxinger.grpc.GreetServiceGrpc.GreetServiceImplBase;
import com.charlesluxinger.grpc.LongGreetRequest;
import com.charlesluxinger.grpc.LongGreetResponse;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

	@Override
	public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
		return new StreamObserver<LongGreetRequest>() {
			StringBuilder result = new StringBuilder();

			@Override
			public void onNext(LongGreetRequest value) {
				result.append("Hello " + value.getGreeting().getFirstName() + "\n");
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("Error on Service");
			}

			@Override
			public void onCompleted() {
				responseObserver.onNext(
						LongGreetResponse.newBuilder().setResult(result.toString()).build()
				);
				responseObserver.onCompleted();
			}
		};
	}
}
