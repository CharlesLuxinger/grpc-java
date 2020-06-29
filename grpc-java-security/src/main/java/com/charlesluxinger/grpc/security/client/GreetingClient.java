package com.charlesluxinger.grpc.security.client;

import com.charlesluxinger.grpc.GreetWithSecurityRequest;
import com.charlesluxinger.grpc.GreetWithSecurityResponse;
import com.charlesluxinger.grpc.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import javax.net.ssl.SSLException;
import java.io.File;

import static com.charlesluxinger.grpc.GreetWithSecurityServiceGrpc.GreetWithSecurityServiceBlockingStub;
import static com.charlesluxinger.grpc.GreetWithSecurityServiceGrpc.newBlockingStub;

public class GreetingClient {
	public static void main(String[] args) throws SSLException {
		System.out.println("Client starting...");
		ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 9090)
													.sslContext(GrpcSslContexts.forClient()
																			    .trustManager(new File("ssl/ca.crt"))
																				.build())
													.build();

		GreetWithSecurityServiceBlockingStub blockingStub = newBlockingStub(channel);

		GreetWithSecurityResponse response = blockingStub.greetWithSecurity(GreetWithSecurityRequest.newBuilder()
																									.setGreeting(Greeting.newBuilder().setFirstName("Charles").build())
																									.build());

		System.out.println(response.getResult());

		System.out.println("Shutting down channel");
		channel.shutdown();
	}
}
