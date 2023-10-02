package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.utils.TicketUtilsImpl;

public class TicketUtilServiceImplTest {

    private TicketUtilsImpl ticketUtilService;

    @Before
    public void setUp() {
        ticketUtilService = new TicketUtilsImpl();
    }

    @Test
    public void validateAccountId_InvalidAccountId_ShouldThrowException() {
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateAccountId(-1L));
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateAccountId(0L));
    }

    @Test
    public void validateTicketRequests_NullTicketRequests_ShouldThrowException() {
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateTicketRequests((TicketTypeRequest[]) null));
    }

    @Test
    public void validateTicketRequests_EmptyTicketRequests_ShouldThrowException() {
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateTicketRequests());
    }

    @Test
    public void validateTicketRequests_NullTicketType_ShouldThrowException() {
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateTicketRequests(new TicketTypeRequest(null, 1)));
    }

    @Test
    public void validateTicketRequests_NegativeTickets_ShouldThrowException() {
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateTicketRequests(new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -1)));
    }

    @Test
    public void validateTicketRequests_NoAdultTickets_ShouldThrowException() {
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateTicketRequests(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1)));
    }

    @Test
    public void validateTicketRequests_MoreThanTwentyTickets_ShouldThrowException() {
        assertThrows(InvalidPurchaseException.class, () -> ticketUtilService.validateTicketRequests(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 15),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 6)
        ));
    }

    @Test
    public void calculateTotalAmount_ShouldReturnCorrectAmount() {
        int totalAmount = ticketUtilService.calculateTotalAmount(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3)
        );
        assertEquals(70, totalAmount);
    }

    @Test
    public void calculateTotalSeatsToReserve_ShouldReturnCorrectSeats() {
        int totalSeats = ticketUtilService.calculateTotalSeatsToReserve(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        );
        assertEquals(5, totalSeats);
    }
}
