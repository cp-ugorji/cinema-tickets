package uk.gov.dwp.uc.pairtest;

import org.junit.Assert;
import org.junit.Before;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {
    private TicketServiceImpl ticketService;
    private TicketPaymentService paymentService;
    private SeatReservationService reservationService;
    private TicketUtilsImpl ticketUtils;

    @Before
    public void setup() {
        paymentService = mock(TicketPaymentService.class);
        reservationService = mock(SeatReservationService.class);
        ticketUtils = new TicketUtilsImpl();
        ticketService = new TicketServiceImpl(paymentService, reservationService, ticketUtils);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void shouldThrowExceptionForInvalidAccountId() {
        ticketService.purchaseTickets(0L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
    }

    @Test(expected = InvalidPurchaseException.class)
    public void shouldThrowExceptionForChildWithNoAdultTickets() {
        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
    }

    @Test(expected = InvalidPurchaseException.class)
    public void shouldThrowExceptionForZeroAdultTicketsWithChild() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
    }

    @Test(expected = InvalidPurchaseException.class)
    public void shouldThrowExceptionForMoreThan20Tickets() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 15),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 6));
    }

    @Test
    public void shouldMakePaymentAndReserveSeats() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));

        verify(paymentService).makePayment(1L, 50);
        verify(reservationService).reserveSeat(1L, 3);
    }

    @Test
    public void shouldHandleOnlyAdultTickets() {
        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3));

        verify(paymentService).makePayment(1L, 60);
        verify(reservationService).reserveSeat(1L, 3);
    }

    @Test
    public void shouldHandleChildAndInfantWithAdult() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));

        verify(paymentService).makePayment(1L, 30);
        verify(reservationService).reserveSeat(1L, 2);
    }

    @Test
    public void shouldHandleMaxAllowedTickets() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 19),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));

        verify(paymentService).makePayment(1L, 390);
        verify(reservationService).reserveSeat(1L, 20);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void shouldThrowExceptionForNoTickets() {
        ticketService.purchaseTickets(1L);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void shouldThrowExceptionForNegativeNumberOfTickets() {
        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -1));
    }

    @Test
    public void shouldCalculateCorrectAmountForAdultTickets() {
        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3));
        verify(paymentService).makePayment(1L, 60);
    }

    @Test
    public void shouldCalculateCorrectAmountForMixedTickets() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3));
        verify(paymentService).makePayment(1L, 70);
    }

    @Test
    public void shouldReserveCorrectNumberOfSeatsForMixedTickets() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));
        verify(reservationService).reserveSeat(1L, 5);
    }

    @Test
    public void shouldReserveSeatsForAdultTicketsOnly() {
        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3));
        verify(reservationService).reserveSeat(1L, 3);
    }

    @Test
    public void shouldNotReserveSeatsForInfantTickets() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));
        verify(reservationService).reserveSeat(1L, 1);
    }

    @Test
    public void shouldThrowExceptionForNullRequest() {
        // Arrange: Creating an array with a null request
        TicketTypeRequest[] requests = {null};

        // Act & Assert: Expecting an exception when passing a null request
        InvalidPurchaseException thrown = Assertions.assertThrows(
                InvalidPurchaseException.class,
                () -> ticketUtils.validateTicketRequests(requests),
                "Expected validateTicketRequests to throw, but it didn't"
        );

        // Assert: Verify the message of the thrown exception
        Assert.assertTrue(thrown.getMessage().contains("Ticket request or ticket type cannot be null"));
    }

    @Test
    public void shouldThrowExceptionForNullTicketType() {
        // Arrange: Creating a TicketTypeRequest with a null TicketType
        TicketTypeRequest requestWithNullType = new TicketTypeRequest(null, 1);

        // Act & Assert: Expecting an exception when passing a request with a null TicketType
        InvalidPurchaseException thrown = Assertions.assertThrows(
                InvalidPurchaseException.class,
                () -> ticketUtils.validateTicketRequests(requestWithNullType),
                "Expected validateTicketRequests to throw, but it didn't"
        );

        // Assert: Verify the message of the thrown exception
        Assert.assertTrue(thrown.getMessage().contains("Ticket request or ticket type cannot be null"));
    }
}
