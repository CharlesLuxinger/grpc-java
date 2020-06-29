package com.charlesluxinger.grpc.java;

import static example.enumerations.EnumExample.DayOfTheWeek;
import static example.enumerations.EnumExample.EnumMessage;

public class EnumMain {
	public static void main(String[] args) {
		EnumMessage.Builder builder = EnumMessage.newBuilder();

		builder.setId(12).setDayOfTheWeek(DayOfTheWeek.TUESDAY);

		EnumMessage message = builder.build();

		System.out.println(message);
	}
}
