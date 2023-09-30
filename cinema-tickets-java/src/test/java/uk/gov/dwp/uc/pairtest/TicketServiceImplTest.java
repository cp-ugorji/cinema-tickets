package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import static org.mockito.Mockito.*;

import org.junit.Test;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {
    private TicketServiceImpl ticketService;
    private TicketPaymentService paymentService;
    private SeatReservationService reservationService;

    @Before
    public void setup() {
        paymentService = mock(TicketPaymentService.class);
        reservationService = mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(paymentService, reservationService);
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

}
