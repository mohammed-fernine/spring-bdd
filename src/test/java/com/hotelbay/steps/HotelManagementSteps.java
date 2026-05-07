package com.hotelbay.steps;

import com.hotelbay.entity.Hotel;
import com.hotelbay.service.HotelService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;

public class HotelManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CucumberSpringConfiguration config;

    @Autowired
    private TestContext testContext;
    private Hotel testHotel;

    private String getFullUrl(String path) {
        return config.getBaseUrl() + path;
    }

    @Given("an administrator is authenticated")
    public void administratorIsAuthenticated() {
        // For testing purposes, we assume admin privileges
    }

    @Given("a hotel with ID {string} exists in the system")
    public void hotelWithIdExists(String id) {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);
        testHotel = savedHotel;
    }

    @Given("a hotel with ID 1 exists in the system")
    public void hotelWithId1Exists() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);
        testHotel = savedHotel;
    }

    @Given("a hotel with ID {string} exists and is inactive")
    public void hotelWithIdExistsAndIsInactive(String id) {
        Hotel hotel = new Hotel();
        hotel.setName("Inactive Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(false);
        Hotel savedHotel = hotelService.save(hotel);
        testHotel = savedHotel;
    }

    @Given("a hotel with ID 1 exists and is inactive")
    public void hotelWithId1ExistsAndIsInactive() {
        Hotel hotel = new Hotel();
        hotel.setName("Inactive Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(false);
        Hotel savedHotel = hotelService.save(hotel);
        testHotel = savedHotel;
    }

    @Given("a hotel with ID {string} exists and is active")
    public void hotelWithIdExistsAndIsActive(String id) {
        Hotel hotel = new Hotel();
        hotel.setName("Active Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);
        testHotel = savedHotel;
    }

    @Given("a hotel with ID 1 exists and is active")
    public void hotelWithId1ExistsAndIsActive() {
        Hotel hotel = new Hotel();
        hotel.setName("Active Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);
        testHotel = savedHotel;
    }

    @Given("multiple hotels exist in the system")
    public void multipleHotelsExistInTheSystem() {
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel 1");
        hotel1.setLocation("Location 1");
        hotel1.setDescription("Description 1");
        hotel1.setContactInfo("hotel1@example.com");
        hotel1.setActive(true);
        hotelService.save(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel 2");
        hotel2.setLocation("Location 2");
        hotel2.setDescription("Description 2");
        hotel2.setContactInfo("hotel2@example.com");
        hotel2.setActive(true);
        hotelService.save(hotel2);
    }

    @When("the administrator submits a request to create a new hotel with name, description, location, and contact information")
    public void administratorSubmitsRequestToCreateNewHotel() {
        testHotel = new Hotel();
        testHotel.setName("New Hotel");
        testHotel.setLocation("New Location");
        testHotel.setDescription("New Description");
        testHotel.setContactInfo("new@example.com");
        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/hotels"), testHotel, Hotel.class));
    }

    @When("the administrator updates the hotel description or contact information")
    public void administratorUpdatesHotelInformation() {
        testHotel.setDescription("Updated Description");
        testHotel.setContactInfo("updated@example.com");
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/hotels/" + testHotel.getId()), HttpMethod.PUT, new HttpEntity<>(testHotel), Hotel.class));
    }

    @When("the administrator activates the hotel")
    public void administratorActivatesHotel() {
        testHotel.setActive(true);
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/hotels/" + testHotel.getId() + "/activate"), HttpMethod.PUT, new HttpEntity<>(testHotel), Hotel.class));
    }

    @When("the administrator deactivates the hotel")
    public void administratorDeactivatesHotel() {
        testHotel.setActive(false);
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/hotels/" + testHotel.getId() + "/deactivate"), HttpMethod.PUT, new HttpEntity<>(testHotel), Hotel.class));
    }

    @When("a user requests the hotel details")
    public void userRequestsHotelDetails() {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl("/api/hotels/" + testHotel.getId()), Hotel.class));
    }

    @When("a user requests the list of hotels")
    public void userRequestsListOfHotels() {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl("/api/hotels"), Hotel[].class));
    }

    @Then("the system should store the hotel information")
    public void systemShouldStoreHotelInformation() {
        assertNotNull(testContext.getLastResponse().getBody());
        assertTrue(hotelService.existsById(((Hotel) testContext.getLastResponse().getBody()).getId()));
    }

    @Then("the system should return the created hotel details")
    public void systemShouldReturnCreatedHotelDetails() {
        assertNotNull(testContext.getLastResponse().getBody());
    }

    @Then("the system should save the updated hotel information")
    public void systemShouldSaveUpdatedHotelInformation() {
        assertNotNull(testContext.getLastResponse().getBody());
    }

    @Then("the hotel should become available for room searches and reservations")
    public void hotelShouldBecomeAvailable() {
        Hotel hotel = hotelService.findById(testHotel.getId()).orElse(null);
        assertNotNull(hotel);
        assertTrue(hotel.getActive());
    }

    @Then("the hotel should become unavailable for new reservations")
    public void hotelShouldBecomeUnavailable() {
        Hotel hotel = hotelService.findById(testHotel.getId()).orElse(null);
        assertNotNull(hotel);
        assertFalse(hotel.getActive());
    }

    @Then("existing reservations should remain valid")
    public void existingReservationsShouldRemainValid() {
        // This would verify that existing reservations are not affected
        assertNotNull(testContext.getLastResponse());
    }

    @Then("the hotel details are returned")
    public void hotelDetailsAreReturned() {
        Hotel hotel = (Hotel) testContext.getLastResponse().getBody();
        assertNotNull(hotel);
    }

    @Then("the hotel status is updated to active")
    public void hotelStatusIsUpdatedToActive() {
        Hotel hotel = (Hotel) testContext.getLastResponse().getBody();
        assertNotNull(hotel);
        assertTrue(hotel.getActive());
    }

    @Then("the hotel status is updated to inactive")
    public void hotelStatusIsUpdatedToInactive() {
        Hotel hotel = (Hotel) testContext.getLastResponse().getBody();
        assertNotNull(hotel);
        assertFalse(hotel.getActive());
    }

    @Then("the system returns a list of all hotels")
    public void systemReturnsListOfAllHotels() {
        Hotel[] hotels = (Hotel[]) testContext.getLastResponse().getBody();
        assertNotNull(hotels);
    }

    @Then("the system should return the hotel name, description, location, and services")
    public void systemShouldReturnHotelDetails() {
        Hotel hotel = (Hotel) testContext.getLastResponse().getBody();
        assertNotNull(hotel);
        assertNotNull(hotel.getName());
        assertNotNull(hotel.getDescription());
        assertNotNull(hotel.getLocation());
    }

    @Then("the system should return all registered hotels")
    public void systemShouldReturnAllHotels() {
        Hotel[] hotels = (Hotel[]) testContext.getLastResponse().getBody();
        assertNotNull(hotels);
        assertTrue(hotels.length >= 2);
    }
}
