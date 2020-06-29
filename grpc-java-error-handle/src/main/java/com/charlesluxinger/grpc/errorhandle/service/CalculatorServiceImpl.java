package com.charlesluxinger.grpc.errorhandle.service;

import com.charlesluxinger.grpc.CalculatorServiceGrpc.CalculatorServiceImplBase;
import com.charlesluxinger.grpc.SquareRootRequest;
import com.charlesluxinger.grpc.SquareRootResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceImplBase {
	@Override
	public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
		if (request.getNumber() >= 0) {
			double numberRoot = Math.sqrt(request.getNumber());
			responseObserver.onNext(SquareRootResponse.newBuilder()
													  .setNumberRoot(numberRoot)
													  .build());
		} else {
			responseObserver.onError(Status.INVALID_ARGUMENT
										   .withDescription("The number beign sent is not positive")
										   .augmentDescription("Number sent: " + request.getNumber())
										   .asRuntimeException());
		}
	}
}