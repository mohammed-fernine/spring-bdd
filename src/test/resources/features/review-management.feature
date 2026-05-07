Feature: Reviews
  As a HotelBay guest
  I want to manage reviews for hotels
  So that I can share my experiences and help others make informed decisions

  Background:
    Given the system is running
    And the client is authenticated as a guest

  Scenario: View hotel reviews
    Given a hotel has received reviews from past guests
    When the client calls GET /api/reviews/hotel/1 for reviews
    Then the review response status code is 200
    And the system returns a list of reviews

  Scenario: View reviews for a non-existing hotel
    Given a hotel does not exist
    When the client calls GET /api/reviews/hotel/99 for reviews
    Then the review response status code is 200
    And an error message is returned for reviews

  Scenario: Add a review
    Given a guest has a completed reservation for the hotel
    When the client calls POST /api/reviews with a textual description
    Then the review response status code is 201

  Scenario: Add a review without a completed reservation
    Given a guest does not have a completed reservation for the hotel
    When the client calls POST /api/reviews with a textual description
    Then the review response status code is 400
    And an error message is returned for reviews

  Scenario: Update a review
    Given a guest has previously submitted a review
    When the client calls PUT /api/reviews/1 with an updated description
    Then the review response status code is 200
    And the system updates the review text

  Scenario: Update a non-existing review
    Given a review does not exist
    When the client calls PUT /api/reviews/99 with an updated description for non-existing review
    Then the review response status code is 404
    And an error message is returned for reviews
