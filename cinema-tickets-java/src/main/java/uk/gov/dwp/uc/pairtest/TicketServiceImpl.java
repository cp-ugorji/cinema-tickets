package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.utils.TicketUtils;

public class TicketServiceImpl implements TicketService {
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;
    private final TicketUtils ticketUtils;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService,
                             SeatReservationService seatReservationService,
                             TicketUtils ticketUtils) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
        this.ticketUtils = ticketUtils;
    }

    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        ticketUtils.validateAccountId(accountId);
        ticketUtils.validateTicketRequests(ticketTypeRequests);

        int totalAmountToPay = ticketUtils.calculateTotalAmount(ticketTypeRequests);
        int totalSeatsToReserve = ticketUtils.calculateTotalSeatsToReserve(ticketTypeRequests);

        ticketPaymentService.makePayment(accountId, totalAmountToPay);
        seatReservationService.reserveSeat(accountId, totalSeatsToReserve);
    }
}
