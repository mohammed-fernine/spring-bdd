package com.hotelbay.steps;

import com.hotelbay.entity.Reservation;
import com.hotelbay.entity.User;
import com.hotelbay.entity.Room;
import com.hotelbay.entity.Hotel;
import com.hotelbay.service.ReservationService;
import com.hotelbay.service.UserService;
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
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BookingManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CucumberSpringConfiguration config;

    @Autowired
    private TestContext testContext;

    private Reservation testReservation;
    private User testUser;
    private Room testRoom;

    private String getFullUrl(String path) {
        return config.getBaseUrl() + path;
    }

    @Given("the client is authenticated as a guest")
    public void clientIsAuthenticatedAsGuest() {
        testUser = new User();
        testUser.setName("Test Guest");
        testUser.setUsername("testguest_" + System.currentTimeMillis());
        testUser.setEmail("testguest_" + System.currentTimeMillis() + "@example.com");
        testUser.setRole(User.UserRole.GUEST);
        testUser = userService.save(testUser);
    }

    @Given("the guest has a confirmed reservation with ID {string}")
    public void guestHasConfirmedReservation(String id) {
        guestHasConfirmedReservation();
    }

    @Given("the guest has a confirmed reservation with ID 1")
    public void guestHasConfirmedReservation() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);

        Room room = new Room();
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");
        room.setDescription("Test Room");
        room.setCapacity(2);
        room.setPricePerNight(java.math.BigDecimal.valueOf(150.00));
        room.setAvailable(true);
        room.setHotel(savedHotel);
        Room savedRoom = roomService.save(room);

        testReservation = new Reservation();
        testReservation.setGuest(testUser);
        testReservation.setRoom(savedRoom);
        testReservation.setCheckInDate(LocalDate.now());
        testReservation.setCheckOutDate(LocalDate.now().plusDays(3));
        testReservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        testReservation = reservationService.save(testReservation);
    }

    @Given("the guest has an existing reservation with ID {string}")
    public void guestHasExistingReservation(String id) {
        guestHasExistingReservation();
    }

    @Given("the guest has an existing reservation with ID 1")
    public void guestHasExistingReservation() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);

        Room room = new Room();
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");
        room.setDescription("Test Room");
        room.setCapacity(2);
        room.setPricePerNight(java.math.BigDecimal.valueOf(150.00));
        room.setAvailable(true);
        room.setHotel(savedHotel);
        Room savedRoom = roomService.save(room);

        testReservation = new Reservation();
        testReservation.setGuest(testUser);
        testReservation.setRoom(savedRoom);
        testReservation.setCheckInDate(LocalDate.now());
        testReservation.setCheckOutDate(LocalDate.now().plusDays(3));
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);
        testReservation = reservationService.save(testReservation);
    }

    @Given("the user provides check-in and check-out dates")
    public void userProvidesCheckInAndCheckOutDates() {
        // Set up test dates
        testReservation = new Reservation();
        testReservation.setCheckInDate(LocalDate.now());
        testReservation.setCheckOutDate(LocalDate.now().plusDays(3));
    }

    @When("the client calls POST {string} with reservation details")
    public void clientCallsPostWithReservationDetails(String endpoint) {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);

        Room room = new Room();
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");
        room.setDescription("Test Room");
        room.setCapacity(2);
        room.setPricePerNight(java.math.BigDecimal.valueOf(150.00));
        room.setAvailable(true);
        room.setHotel(savedHotel);
        Room savedRoom = roomService.save(room);

        testReservation = new Reservation();
        testReservation.setGuest(testUser);
        testReservation.setRoom(savedRoom);
        testReservation.setHotel(savedHotel);
        testReservation.setCheckInDate(LocalDate.now());
        testReservation.setCheckOutDate(LocalDate.now().plusDays(3));
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);

        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl(endpoint), testReservation, Reservation.class));
    }

    @When("the client calls POST \\/api\\/reservations with reservation details")
    public void clientCallsPostApiReservationsWithDetails() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setContactInfo("test@example.com");
        hotel.setActive(true);
        Hotel savedHotel = hotelService.save(hotel);

        Room room = new Room();
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");
        room.setDescription("Test Room");
        room.setCapacity(2);
        room.setPricePerNight(java.math.BigDecimal.valueOf(150.00));
        room.setAvailable(true);
        room.setHotel(savedHotel);
        Room savedRoom = roomService.save(room);

        testReservation = new Reservation();
        testReservation.setGuest(testUser);
        testReservation.setRoom(savedRoom);
        testReservation.setHotel(savedHotel);
        testReservation.setCheckInDate(LocalDate.now());
        testReservation.setCheckOutDate(LocalDate.now().plusDays(3));
        testReservation.setNumberOfGuests(2);
        testReservation.setTotalAmount(BigDecimal.valueOf(450.00));
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);

        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/reservations"), testReservation, Reservation.class));
    }

    @When("the client calls PUT {string} cancel")
    public void clientCallsPutCancel(String endpoint) {
        testReservation.setStatus(Reservation.ReservationStatus.CANCELED);
        testContext.setLastResponse(restTemplate.exchange(getFullUrl(endpoint), HttpMethod.PUT, new HttpEntity<>(testReservation), Reservation.class));
    }

    @When("the client calls PUT \\/api\\/reservations\\/1\\/cancel")
    public void clientCallsPutApiReservationsCancel() {
        if (testReservation == null || testReservation.getId() == null) {
            guestHasConfirmedReservation();
        }
        // Don't set status here - let the controller handle it
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/reservations/" + testReservation.getId() + "/cancel"), HttpMethod.PUT, new HttpEntity<>(testReservation), Reservation.class));
    }

    @When("the client calls GET {string} for reservation details")
    public void clientCallsGet(String endpoint) {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl(endpoint), Reservation.class));
    }

    @When("the client calls GET \\/api\\/reservations\\/1 for reservation details")
    public void clientCallsGetApiReservationsDetails() {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl("/api/reservations/" + testReservation.getId()), Reservation.class));
    }

    @When("the client calls GET {string} for user bookings")
    public void clientCallsGetUserBookings(String endpoint) {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl(endpoint), Reservation[].class));
    }

    @When("the client calls GET \\/api\\/reservations\\/guest\\/1 for user bookings")
    public void clientCallsGetApiReservationsGuestBookings() {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl("/api/reservations/guest/" + testUser.getId()), Reservation[].class));
    }

    @When("the client calls GET {string} for room availability")
    public void clientCallsGetAvailability(String endpoint) {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl(endpoint), Room[].class));
    }

    @When("the client calls GET \\/api\\/rooms for room availability")
    public void clientCallsGetApiRoomsAvailability() {
        testContext.setLastResponse(restTemplate.getForEntity(getFullUrl("/api/rooms"), Room[].class));
    }

    @Then("the client receives status code of {int}")
    public void clientReceivesStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), testContext.getLastResponse().getStatusCode());
    }

    @Then("the booking status is set to pending")
    public void bookingStatusIsSetToPending() {
        Reservation reservation = (Reservation) testContext.getLastResponse().getBody();
        assertNotNull(reservation);
        assertEquals(Reservation.ReservationStatus.PENDING, reservation.getStatus());
    }

    @Then("the reservation status is marked as canceled")
    public void reservationStatusIsMarkedCanceled() {
        Reservation reservation = (Reservation) testContext.getLastResponse().getBody();
        assertNotNull(reservation);
        assertEquals(Reservation.ReservationStatus.CANCELED, reservation.getStatus());
    }

    @Then("the system returns the reservation details")
    public void systemReturnsReservationDetails() {
        Reservation reservation = (Reservation) testContext.getLastResponse().getBody();
        assertNotNull(reservation);
    }

    @Then("the system returns a list of the user's reservations")
    public void systemReturnsUserReservations() {
        Reservation[] reservations = (Reservation[]) testContext.getLastResponse().getBody();
        assertNotNull(reservations);
    }

    @Then("the system returns a list of available rooms")
    public void systemReturnsAvailableRooms() {
        Room[] rooms = (Room[]) testContext.getLastResponse().getBody();
        assertNotNull(rooms);
    }
}
