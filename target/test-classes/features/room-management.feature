Feature: Room Management
  As a HotelBay administrator
  I want to manage rooms
  So that I can control room availability and pricing

  Background:
    Given the system is running
    And an administrator is authenticated

  Scenario: Create room
    Given a hotel exists in the system
    When an administrator adds a new room with identifier, type, capacity, and price
    Then the system should store the room information
    And associate the room with the hotel
    And the response status code should be 201

  Scenario: Update room information
    Given a room exists in a hotel
    When the administrator updates the room description or price
    Then the system should save the updated room information
    And the response status code should be 200

  Scenario: Delete room
    Given a room exists in a hotel
    When the administrator deletes the room
    Then the room should be removed from the list of available rooms
    And past reservations should remain recorded
    And the response status code should be 204

  Scenario: Delete non-existing room
    Given a room does not exist in a hotel
    When the administrator attempts to delete the room
    Then the system should return status code 404 for deleted room
    And an error message is returned

  Scenario: Search rooms
    Given multiple hotels and rooms exist in the system
    When a user searches for rooms using filters such as location, category, price, and dates
    Then the system should return rooms that match the search criteria and are available

  Scenario: Search with no results
    Given no rooms match the search filters
    When a user searches for rooms
    Then the system should return an empty list

  Scenario: View room details
    Given a room exists in the system
    When a user requests the room details
    Then the system should return the room type, capacity, price, and description

  Scenario: View deleted room
    Given a room has been deleted
    When a user requests the room details
    Then the system should return status code 404
    And an error message is returned
