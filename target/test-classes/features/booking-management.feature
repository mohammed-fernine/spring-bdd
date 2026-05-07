Feature: Booking Management
  As a HotelBay guest
  I want to manage my bookings
  So that I can book rooms and manage my reservations

  Background:
    Given the system is running
    And the client is authenticated as a guest

  Scenario: Create booking
    Given the client is authenticated as a guest
    When the client calls POST /api/reservations with reservation details
    Then the client receives status code of 201
    And the booking status is set to pending

  Scenario: Cancel booking
    Given the guest has a confirmed reservation with ID 1
    When the client calls PUT /api/reservations/1/cancel
    Then the client receives status code of 200
    And the reservation status is marked as canceled

  Scenario: View booking details
    Given the guest has an existing reservation with ID 1
    When the client calls GET /api/reservations/1 for reservation details
    Then the client receives status code of 200
    And the system returns the reservation details

  Scenario: List user bookings
    Given the client is authenticated as a guest
    When the client calls GET /api/reservations/guest/1 for user bookings
    Then the client receives status code of 200
    And the system returns a list of the user's reservations

  Scenario: Check room availability
    Given the user provides check-in and check-out dates
    When the client calls GET /api/rooms for room availability
    Then the client receives status code of 200
    And the system returns a list of available rooms
