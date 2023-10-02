package uk.gov.dwp.uc.pairtest.utils;

import uk.gov.dwp.uc.pairtest.domain.TicketConstants;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.logging.Logger;

public class TicketUtilsImpl implements TicketUtils {
    private static final Logger LOGGER = Logger.getLogger(TicketUtilsImpl.class.getName());
    public void validateAccountId(Long accountId) {
        if (accountId == null || accountId <= 0) {
            LOGGER.severe("Invalid account ID: " + accountId);
            throw new InvalidPurchaseException("Invalid account ID");
        }
    }

    public void validateTicketRequests(TicketTypeRequest... ticketTypeRequests) {
        LOGGER.info("Validating ticket requests...");
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            LOGGER.severe("At least one ticket should be purchased");
            throw new InvalidPurchaseException("At least one ticket should be purchased");
        }

        int adultTickets = 0;
        int totalTickets = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request == null || request.getTicketType() == null) {
                LOGGER.severe("Ticket request or ticket type cannot be null");
                throw new InvalidPurchaseException("Ticket request or ticket type cannot be null");
            }

            if (request.getNoOfTickets() <= 0) {
                LOGGER.severe("Number of tickets should be positive");
                throw new InvalidPurchaseException("Number of tickets should be positive");
            }

            if (request.getTicketType() == TicketTypeRequest.Type.ADULT) {
                adultTickets += request.getNoOfTickets();
            }
            totalTickets += request.getNoOfTickets();
        }

        if (adultTickets == 0) {
            LOGGER.severe("Cannot purchase Child or Infant tickets without purchasing Adult tickets");
            throw new InvalidPurchaseException("Cannot purchase Child or Infant tickets without purchasing Adult tickets");
        }

        if (totalTickets > TicketConstants.MAX_TICKETS_PURCHASABLE) {
            LOGGER.severe("Cannot purchase more than " + TicketConstants.MAX_TICKETS_PURCHASABLE + " tickets at a time");
            throw new InvalidPurchaseException("Cannot purchase more than " + TicketConstants.MAX_TICKETS_PURCHASABLE + " tickets at a time");
        }
        LOGGER.info("Ticket requests validated successfully");
    }
    public int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests) {
        LOGGER.info("Calculating total amount for ticket requests...");
        int totalAmount = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT -> totalAmount += TicketConstants.ADULT_TICKET_PRICE * request.getNoOfTickets();
                case CHILD -> totalAmount += TicketConstants.CHILD_TICKET_PRICE * request.getNoOfTickets();
                case INFANT -> {} // Child/Infant tickets are free
            }
        }
        LOGGER.info("Total amount calculated: " + totalAmount);
        return totalAmount;
    }

    public int calculateTotalSeatsToReserve(TicketTypeRequest... ticketTypeRequests) {
        LOGGER.info("Calculating total seats to reserve for ticket requests...");
        int totalSeats = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request.getTicketType() != TicketTypeRequest.Type.INFANT) {
                totalSeats += request.getNoOfTickets();
            }
        }
        LOGGER.info("Total seats to reserve: " + totalSeats);
        return totalSeats;
    }
}
