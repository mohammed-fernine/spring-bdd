Feature: Ratings
  As a HotelBay guest
  I want to rate hotels
  So that I can provide feedback on my experiences

  Background:
    Given the system is running
    And the client is authenticated as a guest

  Scenario: Add a rating
    Given a guest has completed their stay
    When the client calls POST /hotels/1/ratings with a score
    Then the client receives status code of 201
    And the rating is saved

  Scenario: View average rating
    Given a hotel has ratings
    When the client calls GET /hotels/1/ratings/average
    Then the client receives status code of 200
    And the system returns the average rating

  Scenario: Update a rating
    Given a guest has submitted a rating
    When the client calls PUT /ratings/1 with a new score
    Then the client receives status code of 200
    And the rating is updated

  Scenario: Reject invalid rating
    Given a guest has completed their stay
    When the client calls POST /hotels/1/ratings with an invalid score
    Then the client receives status code of 400
    And an error message is returned for ratings

  Scenario: Update non-existing rating
    Given no rating exists with ID 999
    When the client calls PUT /ratings/999 with a score
    Then the client receives status code of 404
    And an error message is returned for ratings

  Scenario: View average rating for hotel without ratings
    Given a hotel has no ratings
    When the client calls GET /hotels/1/ratings/average
    Then the client receives status code of 200
    And the system returns 0 as the average rating
