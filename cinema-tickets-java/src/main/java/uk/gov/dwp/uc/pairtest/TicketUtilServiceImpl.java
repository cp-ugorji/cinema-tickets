package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketUtilServiceImpl implements TicketUtilService{
    public void validateAccountId(Long accountId) {
        if (accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID");
        }
    }

    public void validateTicketRequests(TicketTypeRequest... ticketTypeRequests) {
        int adultTickets = 0;
        int totalTickets = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
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

        if (totalTickets > 20) {
            throw new InvalidPurchaseException("Cannot purchase more than 20 tickets at a time");
        }
    }
    public int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests) {
        int totalAmount = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT -> totalAmount += 20 * request.getNoOfTickets();
                case CHILD -> totalAmount += 10 * request.getNoOfTickets();
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
