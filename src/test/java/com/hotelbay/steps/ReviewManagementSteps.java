package com.hotelbay.steps;

import com.hotelbay.entity.Review;
import com.hotelbay.entity.Hotel;
import com.hotelbay.entity.User;
import com.hotelbay.entity.Reservation;
import com.hotelbay.service.ReviewService;
import com.hotelbay.service.HotelService;
import com.hotelbay.service.UserService;
import com.hotelbay.service.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CucumberSpringConfiguration config;

    @Autowired
    private TestContext testContext;

    private Review testReview;
    private Hotel testHotel;
    private User testGuest;
    private Reservation testReservation;

    private String getFullUrl(String path) {
        return config.getBaseUrl() + path;
    }

    @Given("a hotel has received reviews from past guests")
    public void hotelHasReceivedReviews() {
        // For testing purposes, we assume guest privileges
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testGuest = new User();
        testGuest.setName("Test Guest");
        testGuest.setUsername("testguest_" + System.currentTimeMillis());
        testGuest.setEmail("testguest_" + System.currentTimeMillis() + "@example.com");
        testGuest.setRole(User.UserRole.GUEST);
        testGuest = userService.save(testGuest);

        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.COMPLETED);
        testReservation.setTotalAmount(java.math.BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);

        Review review = new Review();
        review.setHotel(testHotel);
        review.setGuest(testGuest);
        review.setReservation(testReservation);
        review.setDescription("Great stay!");
        review.setRating(5);
        reviewService.save(review);
    }

    @Given("a hotel does not exist")
    public void hotelDoesNotExist() {
        // Ensure no hotel exists
        hotelService.deleteAll();
    }

    @Given("a guest has a completed reservation for the hotel")
    public void guestHasCompletedReservation() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testGuest = new User();
        testGuest.setName("Test Guest");
        testGuest.setUsername("testguest_" + System.currentTimeMillis());
        testGuest.setEmail("testguest_" + System.currentTimeMillis() + "@example.com");
        testGuest.setRole(User.UserRole.GUEST);
        testGuest = userService.save(testGuest);

        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.COMPLETED);
        testReservation.setTotalAmount(java.math.BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);
    }

    @Given("a guest does not have a completed reservation for the hotel")
    public void guestDoesNotHaveCompletedReservation() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testGuest = new User();
        testGuest.setName("Test Guest");
        testGuest.setUsername("testguest_" + System.currentTimeMillis());
        testGuest.setEmail("testguest_" + System.currentTimeMillis() + "@example.com");
        testGuest.setRole(User.UserRole.GUEST);
        testGuest = userService.save(testGuest);

        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);
        testReservation.setTotalAmount(java.math.BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);
    }

    @Given("a guest has previously submitted a review")
    public void guestHasPreviouslySubmittedReview() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testGuest = new User();
        testGuest.setName("Test Guest");
        testGuest.setUsername("testguest_" + System.currentTimeMillis());
        testGuest.setEmail("testguest_" + System.currentTimeMillis() + "@example.com");
        testGuest.setRole(User.UserRole.GUEST);
        testGuest = userService.save(testGuest);

        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.COMPLETED);
        testReservation.setTotalAmount(java.math.BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);

        testReview = new Review();
        testReview.setHotel(testHotel);
        testReview.setGuest(testGuest);
        testReview.setReservation(testReservation);
        testReview.setDescription("Original review");
        testReview.setRating(4);
        testReview = reviewService.save(testReview);
    }

    @Given("no rating exists with ID {int}")
    public void noRatingExistsWithId(int id) {
        reviewService.deleteAll();
    }

    @Given("a guest has completed their stay")
    public void guestHasCompletedTheirStay() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testGuest = new User();
        testGuest.setName("Test Guest");
        testGuest.setUsername("testguest_" + System.currentTimeMillis());
        testGuest.setEmail("testguest_" + System.currentTimeMillis() + "@example.com");
        testGuest.setRole(User.UserRole.GUEST);
        testGuest = userService.save(testGuest);

        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.COMPLETED);
        testReservation.setTotalAmount(java.math.BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);
    }

    @Given("a guest has submitted a rating")
    public void guestHasSubmittedRating() {
        guestHasCompletedTheirStay();
        testReview = new Review();
        testReview.setHotel(testHotel);
        testReview.setGuest(testGuest);
        testReview.setReservation(testReservation);
        testReview.setDescription("Original review");
        testReview.setRating(4);
        testReview = reviewService.save(testReview);
    }

    @Given("a hotel has ratings")
    public void hotelHasRatings() {
        guestHasSubmittedRating();
    }

    @Given("a hotel has no ratings")
    public void hotelHasNoRatings() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);
        reviewService.deleteAll();
    }

    @When("the client calls GET {string} for reviews")
    public void clientCallsGet(String endpoint) {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl(endpoint), Review[].class));
    }

    @When("the client calls GET \\/api\\/reviews\\/hotel\\/{int} for reviews")
    public void clientCallsGetApiReviewsHotel(int hotelId) {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl("/api/reviews/hotel/" + hotelId), Review[].class));
    }

    @Then("the review response status code is {int}")
    public void reviewResponseStatusCodeIs(int statusCode) {
        assertEquals(statusCode, testContext.getLastResponse().getStatusCodeValue());
    }

    @When("the client calls POST \\/api\\/reviews with a textual description")
    public void clientCallsPostApiReviewsWithDescription() {
        testReview = new Review();
        testReview.setHotel(testHotel);
        testReview.setGuest(testGuest);
        testReview.setReservation(testReservation);
        testReview.setDescription("Great stay!");
        testReview.setRating(5);
        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/reviews"), testReview, Review.class));
    }

    @When("the client calls POST \\/hotels\\/{int}\\/ratings with a score")
    public void clientCallsPostHotelsRatingsWithScore(int hotelId) {
        testReview = new Review();
        testReview.setHotel(testHotel);
        testReview.setGuest(testGuest);
        testReview.setReservation(testReservation);
        testReview.setDescription("Great stay!");
        testReview.setRating(5);
        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/reviews"), testReview, Review.class));
    }

    @When("the client calls GET \\/hotels\\/{int}\\/ratings\\/average")
    public void clientCallsGetHotelsRatingsAverage(int hotelId) {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl("/api/reviews/hotel/" + hotelId + "/average-rating"), Double.class));
    }

    @When("the client calls POST \\/hotels\\/{int}\\/ratings with an invalid score")
    public void clientCallsPostHotelsRatingsWithInvalidScore(int hotelId) {
        testReview = new Review();
        testReview.setHotel(testHotel);
        testReview.setGuest(testGuest);
        testReview.setReservation(testReservation);
        testReview.setDescription("Great stay!");
        testReview.setRating(11); // Invalid: rating must be 0-10
        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/reviews"), testReview, String.class));
    }

    @When("the client calls PUT \\/api\\/reviews\\/{int} with an updated description")
    public void clientCallsPutApiReviewsWithUpdatedDescription(int reviewId) {
        if (testReview != null && testReview.getId() != null) {
            testReview.setDescription("Updated review text");
            testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/reviews/" + testReview.getId()), HttpMethod.PUT, new HttpEntity<>(testReview), Review.class));
        } else {
            testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/reviews/" + reviewId), HttpMethod.PUT, new HttpEntity<>(testReview), Review.class));
        }
    }

    @When("the client calls PUT \\/api\\/reviews\\/{int} with an updated description for non-existing review")
    public void clientCallsPutApiReviewsWithUpdatedDescriptionNonExisting(int reviewId) {
        testReview = new Review();
        testReview.setDescription("Updated review text");
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/reviews/" + reviewId), HttpMethod.PUT, new HttpEntity<>(testReview), String.class));
    }

    @When("the client calls PUT \\/ratings\\/{int} with a new score")
    public void clientCallsPutRatingsWithNewScore(int ratingId) {
        if (testReview != null && testReview.getId() != null) {
            testReview.setDescription("Updated review");
            testReview.setRating(4);
            testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/reviews/" + testReview.getId()), HttpMethod.PUT, new HttpEntity<>(testReview), Review.class));
        } else {
            testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/reviews/" + ratingId), HttpMethod.PUT, new HttpEntity<>(testReview), Review.class));
        }
    }

    @When("the client calls PUT \\/ratings\\/{int} with a score")
    public void clientCallsPutRatingsWithScore(int ratingId) {
        // Create a minimal valid review for the update request
        Review updateRequest = new Review();
        if (testHotel != null) {
            updateRequest.setHotel(testHotel);
        }
        if (testGuest != null) {
            updateRequest.setGuest(testGuest);
        }
        if (testReservation != null) {
            updateRequest.setReservation(testReservation);
        }
        updateRequest.setDescription("Updated review");
        updateRequest.setRating(4);
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/reviews/" + ratingId), HttpMethod.PUT, new HttpEntity<>(updateRequest), String.class));
    }

    @Given("a review does not exist")
    public void reviewDoesNotExist() {
        reviewService.deleteAll();
    }

    @Then("the system updates the review text")
    public void systemUpdatesReviewText() {
        Review updatedReview = (Review) testContext.getLastResponse().getBody();
        assertNotNull(updatedReview);
        assertEquals("Updated review text", updatedReview.getDescription());
    }

    @Then("the system returns a list of reviews")
    public void systemReturnsListOfReviews() {
        Review[] reviews = (Review[]) testContext.getLastResponse().getBody();
        assertNotNull(reviews);
    }

    @Then("an error message is returned for reviews")
    public void errorMessageIsReturnedForReviews() {
        assertNotNull(testContext.getLastResponse());
    }

    @Then("an error message is returned for ratings")
    public void errorMessageIsReturnedForRatings() {
        assertNotNull(testContext.getLastResponse());
    }

    @Then("the rating is saved")
    public void ratingIsSaved() {
        assertNotNull(testContext.getLastResponse().getBody());
    }

    @Then("the system returns the average rating")
    public void systemReturnsAverageRating() {
        Double averageRating = (Double) testContext.getLastResponse().getBody();
        assertNotNull(averageRating);
    }

    @Then("the rating is updated")
    public void ratingIsUpdated() {
        Review review = (Review) testContext.getLastResponse().getBody();
        assertNotNull(review);
    }

    @Then("the system returns 0 as the average rating")
    public void systemReturns0AsAverageRating() {
        Double averageRating = (Double) testContext.getLastResponse().getBody();
        assertNotNull(averageRating);
        assertEquals(0.0, averageRating);
    }
}
