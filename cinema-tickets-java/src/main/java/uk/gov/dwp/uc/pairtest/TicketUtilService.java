package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public interface TicketUtilService {
    void validateAccountId(Long accountId);
    void validateTicketRequests(TicketTypeRequest... ticketTypeRequests);
    int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests);
    int calculateTotalSeatsToReserve(TicketTypeRequest... ticketTypeRequests);
}
