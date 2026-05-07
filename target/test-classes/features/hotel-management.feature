Feature: Hotel Administration
  As a HotelBay administrator
  I want to manage hotel information
  So that I can control hotel properties and availability

  Background:
    Given the system is running
    And an administrator is authenticated

  Scenario: Create hotel
    Given an administrator is authenticated
    When the administrator submits a request to create a new hotel with name, description, location, and contact information
    Then the system should store the hotel information
    And the system should return the created hotel details

  Scenario: Update hotel information
    Given a hotel with ID 1 exists in the system
    When the administrator updates the hotel description or contact information
    Then the system should save the updated hotel information
    And the client receives status code of 200

  Scenario: Activate hotel
    Given a hotel with ID 1 exists and is inactive
    When the administrator activates the hotel
    Then the hotel should become available for room searches and reservations
    And the client receives status code of 200

  Scenario: Deactivate hotel
    Given a hotel with ID 1 exists and is active
    When the administrator deactivates the hotel
    Then the hotel should become unavailable for new reservations
    And existing reservations should remain valid
    And the client receives status code of 200

  Scenario: Consult hotel details
    Given a hotel with ID 1 exists in the system
    When a user requests the hotel details
    Then the system should return the hotel name, description, location, and services
    And the client receives status code of 200

  Scenario: List all hotels
    Given multiple hotels exist in the system
    When a user requests the list of hotels
    Then the system should return all registered hotels
    And the client receives status code of 200
