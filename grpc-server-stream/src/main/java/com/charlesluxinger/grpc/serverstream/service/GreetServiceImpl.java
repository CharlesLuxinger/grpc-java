package com.charlesluxinger.grpc.serverstream.service;

import com.charlesluxinger.grpc.GreetManyTimesResponse;
import com.charlesluxinger.grpc.GreetRequest;
import com.charlesluxinger.grpc.GreetServiceGrpc.GreetServiceImplBase;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

	@Override
	public void greetManyTimes(GreetRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
		String firstName = request.getGreeting().getFirstName();
		try {
			for (int i = 1; i <= 10; i++) {
				String result = "Hello " + firstName + ", response number: " + i;
				GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(result).build();
				responseObserver.onNext(response);

				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			responseObserver.onCompleted();
		}

	}
}
