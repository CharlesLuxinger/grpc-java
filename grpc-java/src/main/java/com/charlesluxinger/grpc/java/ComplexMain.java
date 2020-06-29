package com.charlesluxinger.grpc.java;

import example.complex.Complex.ComplexMessage;
import example.complex.Complex.DummyMessage;

import java.util.Arrays;

public class ComplexMain {
	public static void main(String[] args) {
		DummyMessage.Builder builder = DummyMessage.newBuilder();

		DummyMessage dummy = newDummyMessage(14, "Name");

		ComplexMessage.Builder complexBuilder = ComplexMessage.newBuilder();

		ComplexMessage message = complexBuilder.setOneDummy(dummy)
												.addAllMultipleDummy(Arrays.asList(newDummyMessage(15, "Name"),
																					newDummyMessage(16, "Name"))).build();

		System.out.println(message.toString());
	}

	private static DummyMessage newDummyMessage(int i, String name) {
		return DummyMessage.newBuilder().setId(i).setName(name).build();
	}

}