package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateAccountId(accountId);
        validateTicketRequests(ticketTypeRequests);
    }

    private void validateAccountId(Long accountId) {
        if (accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID");
        }
    }

    private void validateTicketRequests(TicketTypeRequest... ticketTypeRequests) {
        int adultTickets = 0;
        int totalTickets = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request.getTicketType() == TicketTypeRequest.Type.ADULT) {
                adultTickets += request.getNoOfTickets();
            }
            totalTickets += request.getNoOfTickets();
        }

        if (adultTickets == 0) {
            throw new InvalidPurchaseException("Cannot purchase Child or Infant tickets without purchasing Adult tickets");
        }
    }

}
