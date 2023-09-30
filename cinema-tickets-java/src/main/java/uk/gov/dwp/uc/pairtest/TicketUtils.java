package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

/**
 * The TicketUtils interface provides a set of utility methods that are essential
 * for the ticket purchasing process. Each method is designed to perform specific validation,
 * calculation, or action that supports the ticket service functionality.
 */
public interface TicketUtils {
    /**
     * Validates the provided account ID.
     * The method throws an InvalidPurchaseException if the account ID is not valid (i.e., it is null, zero, or negative).
     *
     * @param accountId The ID of the account to be validated.
     * @throws uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException if accountId is null, zero or negative.
     */
    void validateAccountId(Long accountId);

    /**
     * Validates the provided TicketTypeRequests.
     * This method ensures that there is at least one ticket to purchase, ticket counts are positive,
     * and there are adult tickets if there are child or infant tickets.
     * Throws InvalidPurchaseException for any violation of the above rules.
     *
     * @param ticketTypeRequests varargs of TicketTypeRequest objects representing different types of tickets to be validated.
     * @throws uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException if validation rules are violated.
     */
    void validateTicketRequests(TicketTypeRequest... ticketTypeRequests);

    /**
     * Calculates and returns the total amount to be paid based on the provided TicketTypeRequests.
     * The calculation is based on predefined ticket prices for each ticket type.
     *
     * @param ticketTypeRequests varargs of TicketTypeRequest objects for which the total amount is to be calculated.
     * @return the total amount to be paid for the provided ticket requests.
     */
    int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests);

    /**
     * Calculates and returns the total number of seats to be reserved based on the provided TicketTypeRequests.
     * Note: Infant tickets do not require a seat reservation.
     *
     * @param ticketTypeRequests varargs of TicketTypeRequest objects for which the total number of seats is to be calculated.
     * @return the total number of seats to be reserved for the provided ticket requests.
     */
    int calculateTotalSeatsToReserve(TicketTypeRequest... ticketTypeRequests);
}
