package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class Test10ArgumentCaptors {

	private BookingService bookingService;
	private PaymentService paymentServiceMock;
	private RoomService roomServiceMock;
	private BookingDAO bookingDAOMock;
	private MailSender mailSenderMock;
	private ArgumentCaptor<Double> doubleCaptor;

	@BeforeEach
	void setup(){

		this.paymentServiceMock = mock(PaymentService.class);
		this.roomServiceMock = mock(RoomService.class);
		this.bookingDAOMock = mock(BookingDAO.class);
		this.mailSenderMock = mock(MailSender.class);

		this.bookingService = new BookingService(paymentServiceMock, roomServiceMock, bookingDAOMock, mailSenderMock);

		this.doubleCaptor = ArgumentCaptor.forClass(Double.class);
	}

	@Test
	void should_payCorrectPrice_When_InputOK(){
//        given
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 1, 01),LocalDate.of(2020, 01, 05), 2, true);

//        when
		bookingService.makeBooking(bookingRequest);

//        then
		verify(paymentServiceMock, times(1)).pay(eq(bookingRequest), doubleCaptor.capture());
		double capturedArgument = doubleCaptor.getValue();

		assertEquals(400.0, capturedArgument);
	}

	@Test
	void should_payCorrectPrices_When_MultipleCalls(){
//        given
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2020, 1, 01),LocalDate.of(2020, 01, 05), 2, true);
		BookingRequest bookingRequest2 = new BookingRequest("1", LocalDate.of(2020, 1, 01),LocalDate.of(2020, 01, 03), 2, true);
		List<Double> expectedValues = Arrays.asList(400.0, 200.0);

//        when
		bookingService.makeBooking(bookingRequest);
		bookingService.makeBooking(bookingRequest2);

//        then
		verify(paymentServiceMock, times(2)).pay(any(), doubleCaptor.capture());
		List<Double> capturedArgument = doubleCaptor.getAllValues();

		assertEquals(expectedValues, capturedArgument);
	}
}