package com.hotelbay.steps;

import com.hotelbay.entity.Room;
import com.hotelbay.entity.Hotel;
import com.hotelbay.service.RoomService;
import com.hotelbay.service.HotelService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RoomManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CucumberSpringConfiguration config;

    @Autowired
    private TestContext testContext;

    private Room testRoom;
    private Hotel testHotel;

    private String getFullUrl(String path) {
        return config.getBaseUrl() + path;
    }

    @Given("a hotel exists in the system")
    public void hotelExistsInTheSystem() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);
    }

    @Given("a room exists in a hotel")
    public void aRoomExistsInAHotel() {
        roomService.deleteAll();
        hotelService.deleteAll();
        
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testRoom = new Room();
        testRoom.setRoomNumber("101");
        testRoom.setRoomType("Deluxe");
        testRoom.setDescription("Test Room");
        testRoom.setCapacity(2);
        testRoom.setPricePerNight(BigDecimal.valueOf(150.00));
        testRoom.setAvailable(true);
        testRoom.setHotel(testHotel);
        testRoom = roomService.save(testRoom);
    }

    @Given("a room does not exist in a hotel")
    public void roomDoesNotExistInHotel() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);
    }

    @Given("multiple hotels and rooms exist in the system")
    public void multipleHotelsAndRoomsExist() {
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel 1");
        hotel1.setLocation("Location 1");
        hotel1.setDescription("Description 1");
        hotel1.setContactInfo("hotel1@example.com");
        hotel1.setActive(true);
        Hotel savedHotel1 = hotelService.save(hotel1);

        Room room1 = new Room();
        room1.setRoomNumber("101");
        room1.setRoomType("Deluxe");
        room1.setDescription("Room 1");
        room1.setCapacity(2);
        room1.setPricePerNight(BigDecimal.valueOf(150.00));
        room1.setAvailable(true);
        room1.setHotel(savedHotel1);
        roomService.save(room1);

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel 2");
        hotel2.setLocation("Location 2");
        hotel2.setDescription("Description 2");
        hotel2.setContactInfo("hotel2@example.com");
        hotel2.setActive(true);
        Hotel savedHotel2 = hotelService.save(hotel2);

        Room room2 = new Room();
        room2.setRoomNumber("201");
        room2.setRoomType("Suite");
        room2.setDescription("Room 2");
        room2.setCapacity(4);
        room2.setPricePerNight(BigDecimal.valueOf(250.00));
        room2.setAvailable(true);
        room2.setHotel(savedHotel2);
        roomService.save(room2);
    }

    @Given("no rooms match the search filters")
    public void noRoomsMatchSearchFilters() {
        // Ensure no rooms exist that match certain filters
        hotelService.deleteAll();
        roomService.deleteAll();
    }

    @Given("a room exists in the system")
    public void roomExistsInTheSystem() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testRoom = new Room();
        testRoom.setRoomNumber("101");
        testRoom.setRoomType("Deluxe");
        testRoom.setDescription("Test Room");
        testRoom.setCapacity(2);
        testRoom.setPricePerNight(BigDecimal.valueOf(150.00));
        testRoom.setAvailable(true);
        testRoom.setHotel(testHotel);
        testRoom = roomService.save(testRoom);
    }

    @Given("a room has been deleted")
    public void roomHasBeenDeleted() {
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setDescription("Test Description");
        testHotel.setContactInfo("test@example.com");
        testHotel.setActive(true);
        testHotel = hotelService.save(testHotel);

        testRoom = new Room();
        testRoom.setRoomNumber("101");
        testRoom.setRoomType("Deluxe");
        testRoom.setDescription("Test Room");
        testRoom.setCapacity(2);
        testRoom.setPricePerNight(BigDecimal.valueOf(150.00));
        testRoom.setAvailable(true);
        testRoom.setHotel(testHotel);
        testRoom = roomService.save(testRoom);
        
        roomService.deleteById(testRoom.getId());
    }

    @When("an administrator adds a new room with identifier, type, capacity, and price")
    public void administratorAddsNewRoom() {
        testRoom = new Room();
        testRoom.setRoomNumber("101");
        testRoom.setRoomType("Deluxe");
        testRoom.setDescription("New Room");
        testRoom.setCapacity(2);
        testRoom.setPricePerNight(BigDecimal.valueOf(150.00));
        testRoom.setAvailable(true);
        testRoom.setHotel(testHotel);
        testContext.setLastResponse(restTemplate.postForEntity("/api/rooms", testRoom, Room.class));
    }

    @When("the administrator updates the room description or price")
    public void administratorUpdatesRoomInformation() {
        testRoom.setDescription("Updated Description");
        testRoom.setPricePerNight(BigDecimal.valueOf(200.00));
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/rooms/" + testRoom.getId()), HttpMethod.PUT, new HttpEntity<>(testRoom), Room.class));
    }

    @When("the administrator deletes the room")
    public void administratorDeletesRoom() {
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/rooms/" + testRoom.getId()), HttpMethod.DELETE, null, Void.class));
    }

    @When("the administrator attempts to delete the room")
    public void administratorAttemptsToDeleteRoom() {
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/rooms/999"), HttpMethod.DELETE, null, Void.class));
    }

    @When("a user searches for rooms using filters such as location, category, price, and dates")
    public void userSearchesForRoomsWithFilters() {
        testContext.setLastResponse(restTemplate.getForEntity("/api/rooms/available", Room[].class));
    }

    @When("a user searches for rooms")
    public void userSearchesForRooms() {
        testContext.setLastResponse(restTemplate.getForEntity("/api/rooms/available", Room[].class));
    }

    @When("a user requests the room details")
    public void userRequestsRoomDetails() {
        testContext.setLastResponse(restTemplate.getForEntity("/api/rooms/" + testRoom.getId(), Room.class));
    }

    @Then("the system should store the room information")
    public void systemShouldStoreRoomInformation() {
        assertNotNull(testContext.getLastResponse().getBody());
        assertTrue(roomService.existsById(((Room) testContext.getLastResponse().getBody()).getId()));
    }

    @Then("associate the room with the hotel")
    public void associateRoomWithHotel() {
        Room room = (Room) testContext.getLastResponse().getBody();
        assertNotNull(room.getHotel());
    }

    @Then("the response status code should be {int}")
    public void responseStatusCodeShouldBe(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), testContext.getLastResponse().getStatusCode());
    }

    @Then("the system should save the updated room information")
    public void systemShouldSaveUpdatedRoomInformation() {
        assertNotNull(testContext.getLastResponse().getBody());
    }

    @Then("the room should be removed from the list of available rooms")
    public void roomShouldBeRemovedFromAvailableRooms() {
        assertFalse(roomService.existsById(testRoom.getId()));
    }

    @Then("past reservations should remain recorded")
    public void pastReservationsShouldRemainRecorded() {
        // This would verify that past reservations are not affected
        assertNotNull(testContext.getLastResponse());
        assertEquals(HttpStatus.NO_CONTENT, testContext.getLastResponse().getStatusCode());
    }

    @Then("the system should return status code {int}")
    public void systemShouldReturnStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), testContext.getLastResponse().getStatusCode());
    }

    @Then("an error message is returned")
    public void errorMessageIsReturned() {
        assertNotNull(testContext.getLastResponse());
        // Just check that response exists - body may be null for some error responses
        assertTrue(testContext.getLastResponse().getStatusCode().is4xxClientError() || 
                   testContext.getLastResponse().getStatusCode().is5xxServerError());
    }

    @Then("the system should return rooms that match the search criteria and are available")
    public void systemShouldReturnMatchingRooms() {
        Room[] rooms = (Room[]) testContext.getLastResponse().getBody();
        assertNotNull(rooms);
    }

    @Then("the system should return an empty list")
    public void systemShouldReturnEmptyList() {
        Room[] rooms = (Room[]) testContext.getLastResponse().getBody();
        assertNotNull(rooms);
        assertEquals(0, rooms.length);
    }

    @Then("the system should return the room type, capacity, price, and description")
    public void systemShouldReturnRoomDetails() {
        Room room = (Room) testContext.getLastResponse().getBody();
        assertNotNull(room);
        assertNotNull(room.getRoomType());
        assertNotNull(room.getCapacity());
        assertNotNull(room.getPricePerNight());
        assertNotNull(room.getDescription());
    }

    @Then("the room is updated in the system")
    public void roomIsUpdatedInTheSystem() {
        Room room = (Room) testContext.getLastResponse().getBody();
        assertNotNull(room);
        assertNotNull(room.getDescription());
    }

    @Then("the system should return status code {int} for deleted room")
    public void systemShouldReturnStatusCodeForDeletedRoom(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), testContext.getLastResponse().getStatusCode());
    }
}
