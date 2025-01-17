# Objective

This is a coding exercise which will allow you to demonstrate how you code and your approach to a given problem.

You will be assessed on:

- Your ability to write clean, well-tested and reusable code.

- How you have ensured the following business rules are correctly met.

# Business Rules

- There are 3 types of tickets i.e. Infant, Child, and Adult.

- The ticket prices are based on the type of ticket (see table below).

- The ticket purchaser declares how many and what type of tickets they want to buy.

- Multiple tickets can be purchased at any given time.

- Only a maximum of 20 tickets that can be purchased at a time.

- Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap.

- Child and Infant tickets cannot be purchased without purchasing an Adult ticket.

|   Ticket Type    |     Price   |
| ---------------- | ----------- |
|    INFANT        |    £0       |
|    CHILD         |    £10      |
|    ADULT         |    £20      |

- There is an existing `TicketPaymentService` responsible for taking payments.

- There is an existing `SeatReservationService` responsible for reserving seats.

## Constraints

- The TicketService interface CANNOT be modified. (For Java solution only)

- The code in the thirdparty.* packages CANNOT be modified.

- The `TicketTypeRequest` SHOULD be an immutable object.

## Assumptions

You can assume:

- All accounts with an id greater than zero are valid. They also have sufficient funds to pay for any no of tickets.

- The `TicketPaymentService` implementation is an external provider with no defects. You do not need to worry about how the actual payment happens.

- The payment will always go through once a payment request has been made to the `TicketPaymentService`.

- The `SeatReservationService` implementation is an external provider with no defects. You do not need to worry about how the seat reservation algorithm works.

- The seat will always be reserved once a reservation request has been made to the `SeatReservationService`.

## Your Task

Provide a working implementation of a `TicketService` that:

- Considers the above objective, business rules, constraints & assumptions.

- Calculates the correct amount for the requested tickets and makes a payment request to the `TicketPaymentService`.

- Calculates the correct no of seats to reserve and makes a seat reservation request to the `SeatReservationService`.

- Rejects any invalid ticket purchase requests. It is up to you to identify what should be deemed as an invalid purchase request.

## My Solution

I wrote the code to be easily understood even for a newbie. Below is a summary of my approach to solving the problem;

1. The `TicketServiceImpl` class is initialized with instances of `TicketUtils`, `TicketPaymentService` and `SeatReservationService`.

2. The `purchaseTickets` method first validates the input parameters using `TicketUtils` to ensure they adhere to the rules and constraints mentioned. 
If not, an InvalidPurchaseException is thrown. Some of the validations done are:
   - It validates that the `accountId` is greater than zero.
   - It iterates through the `ticketTypeRequests` to count the number of adult, child, and infant tickets requested.
   - It validates that the ticket purchase requests, has a positive number.
   - It validates that the ticket purchase requests, has an Adult ticket in the purchase. 
   - It validates that the total number of tickets does not exceed 20. 

3. The `purchaseTickets` method then;
   - Calculates the total amount to be paid and the total seats to be reserved based on the ticket requests. 
   - Makes a payment request to TicketPaymentService with the total amount to be paid.
   - Makes a seat reservation request to SeatReservationService with the total seats to be reserved.
     
## Test Coverage

17 unit tests was written for `TicketServiceImpl`, resulting in at least 80% code coverage.
- `shouldThrowExceptionForInvalidAccountId`
- `shouldThrowExceptionForChildWithNoAdultTickets`
- `shouldThrowExceptionForZeroAdultTicketsWithChild`
- `shouldThrowExceptionForMoreThan20Tickets`
- `shouldMakePaymentAndReserveSeats`
- `shouldHandleOnlyAdultTickets`
- `shouldHandleChildAndInfantWithAdult`
- `shouldHandleMaxAllowedTickets`
- `shouldThrowExceptionForNoTickets`
- `shouldThrowExceptionForNegativeNumberOfTickets`
- `shouldCalculateCorrectAmountForAdultTickets`
- `shouldCalculateCorrectAmountForMixedTickets`
- `shouldReserveCorrectNumberOfSeatsForMixedTickets`
- `shouldReserveSeatsForAdultTicketsOnly`
- `shouldNotReserveSeatsForInfantTickets`
- `shouldThrowExceptionForNullRequest`
- `shouldThrowExceptionForNullTicketType`