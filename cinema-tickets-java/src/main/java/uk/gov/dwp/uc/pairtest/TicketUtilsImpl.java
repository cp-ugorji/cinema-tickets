package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketConstants;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketUtilsImpl implements TicketUtils {
    public void validateAccountId(Long accountId) {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID");
        }
    }

    public void validateTicketRequests(TicketTypeRequest... ticketTypeRequests) {
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("At least one ticket should be purchased");
        }

        int adultTickets = 0;
        int totalTickets = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request == null || request.getTicketType() == null) {
                throw new InvalidPurchaseException("Ticket request or ticket type cannot be null");
            }

            if (request.getNoOfTickets() <= 0) {
                throw new InvalidPurchaseException("Number of tickets should be positive");
            }

            if (request.getTicketType() == TicketTypeRequest.Type.ADULT) {
                adultTickets += request.getNoOfTickets();
            }
            totalTickets += request.getNoOfTickets();
        }

        if (adultTickets == 0) {
            throw new InvalidPurchaseException("Cannot purchase Child or Infant tickets without purchasing Adult tickets");
        }

        if (totalTickets > TicketConstants.MAX_TICKETS_PURCHASABLE) {
            throw new InvalidPurchaseException("Cannot purchase more than " + TicketConstants.MAX_TICKETS_PURCHASABLE + " tickets at a time");
        }
    }
    public int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests) {
        int totalAmount = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT -> totalAmount += TicketConstants.ADULT_TICKET_PRICE * request.getNoOfTickets();
                case CHILD -> totalAmount += TicketConstants.CHILD_TICKET_PRICE * request.getNoOfTickets();
                case INFANT -> {} // Child/Infant tickets are free
            }
        }
        return totalAmount;
    }

    public int calculateTotalSeatsToReserve(TicketTypeRequest... ticketTypeRequests) {
        int totalSeats = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request.getTicketType() != TicketTypeRequest.Type.INFANT) {
                totalSeats += request.getNoOfTickets();
            }
        }
        return totalSeats;
    }
}
