package com.charlesluxinger.grpc.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static example.simple.Simple.SimpleMessage;

public class SimpleMain {
	public static void main(String[] args) throws IOException {
		SimpleMessage.Builder builder = SimpleMessage.newBuilder();

		builder.setId(42)
			   .setIsSimple(true)
			   .setName("My Simple Message Name")
			   .addAllSampleList(Arrays.asList(1, 2, 3));

		System.out.println(builder.toString());

		SimpleMessage message = builder.build();

		FileOutputStream outputStream = new FileOutputStream("simple_message.bin");
		message.writeTo(outputStream);
		outputStream.close();

		FileInputStream inputStream = new FileInputStream("simple_message.bin");
		SimpleMessage messageFromFile = SimpleMessage.parseFrom(inputStream);

		System.out.println(messageFromFile);
	}
}
