package com.charlesluxinger.grpc.java;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import example.simple.Simple;

import java.util.Arrays;

public class ProtoToJsonMain {
	public static void main(String[] args) throws InvalidProtocolBufferException {
		Simple.SimpleMessage.Builder builder = Simple.SimpleMessage.newBuilder();

		builder.setId(42)
				.setIsSimple(true)
				.setName("My Simple Message Name")
				.addAllSampleList(Arrays.asList(1, 2, 3));

		String print = JsonFormat.printer().includingDefaultValueFields().print(builder);

		System.out.println(print);

		Simple.SimpleMessage.Builder builder2 = Simple.SimpleMessage.newBuilder();

		JsonFormat.parser().ignoringUnknownFields().merge(print, builder2);

		System.out.println(builder2);
	}
}
