Feature: Payment Management
  As a HotelBay guest
  I want to manage payments for my reservations
  So that I can pay for my bookings

  Background:
    Given the system is running
    And the client is authenticated as a guest

  Scenario: Process payment
    Given the guest has a pending reservation
    When the client calls POST /payments with a payment amount
    Then the client receives status code of 201
    And the payment status is set to processing

  Scenario: Confirm payment
    Given a payment attempt is successful
    When the client calls PUT /payments/1/confirm
    Then the client receives status code of 200
    And the reservation state changes to confirmed

  Scenario: Handle payment failure
    Given the client provides invalid payment details
    When the client calls POST /payments with a payment amount
    Then the client receives status code of 400
    And the reservation remains in a pending state

  Scenario: Confirm non-existing payment
    Given no payment exists
    When the client calls PUT /payments/999/confirm for non-existing payment
    Then the client receives status code of 404
    And an error message is returned

  Scenario: Confirm already confirmed payment
    Given a payment has already been confirmed
    When the client calls PUT /payments/1/confirm for already confirmed payment
    Then the client receives status code of 409
    And an error message is returned
