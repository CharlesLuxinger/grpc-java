package com.charlesluxinger.grpc.security.server;

import com.charlesluxinger.grpc.security.service.GreetServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class GreetingServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Server started");

		Server server = ServerBuilder.forPort(9090)
									 .addService(new GreetServiceImpl())
									 .addService(ProtoReflectionService.newInstance())
									 .useTransportSecurity(new File("ssl/server.crt"),
											               new File("ssl/server.pem"))
									 .build();

		server.start();

		Runtime.getRuntime().addShutdownHook( new Thread(
				() -> {
					System.out.println("Received Shutdown Request");
					server.shutdown();
					System.out.println("Successfully stopped the Greeting Server");
				}
		));

		server.awaitTermination();
	}

}
