package com.hotelbay.steps;

import com.hotelbay.entity.Payment;
import com.hotelbay.entity.Reservation;
import com.hotelbay.service.PaymentService;
import com.hotelbay.service.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CucumberSpringConfiguration config;

    @Autowired
    private TestContext testContext;

    private Reservation testReservation;
    private Payment testPayment;

    private String getFullUrl(String path) {
        return config.getBaseUrl() + path;
    }

    @Given("the guest has a pending reservation")
    public void guestHasPendingReservation() {
        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);
        testReservation.setTotalAmount(BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);
    }

    @Given("a payment attempt is successful")
    public void paymentAttemptIsSuccessful() {
        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);
        testReservation.setTotalAmount(BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);

        testPayment = new Payment();
        testPayment.setReservation(testReservation);
        testPayment.setAmount(BigDecimal.valueOf(300.00));
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment.setPaymentMethod("Credit Card");
        testPayment.setStatus(Payment.PaymentStatus.PENDING);
        // Create payment via API to get proper ID
        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/payments"), testPayment, Payment.class));
        testPayment = (Payment) testContext.getLastResponse().getBody();
    }

    @Given("the client provides invalid payment details")
    public void clientProvidesInvalidPaymentDetails() {
        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);
        testReservation.setTotalAmount(BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);

        testPayment = new Payment();
        testPayment.setReservation(testReservation);
        testPayment.setAmount(null); // Invalid: null amount
        testPayment.setStatus(Payment.PaymentStatus.PENDING);
    }

    @Given("no payment exists with ID {string}")
    public void noPaymentExistsWithId(String id) {
        // Ensure no payment exists
        paymentService.deleteAll();
    }

    @Given("a payment has already been confirmed")
    public void paymentHasBeenConfirmed() {
        testReservation = new Reservation();
        testReservation.setStatus(Reservation.ReservationStatus.PENDING);
        testReservation.setTotalAmount(BigDecimal.valueOf(300.00));
        testReservation = reservationService.save(testReservation);

        testPayment = new Payment();
        testPayment.setReservation(testReservation);
        testPayment.setAmount(BigDecimal.valueOf(300.00));
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment.setPaymentMethod("Credit Card");
        testPayment.setStatus(Payment.PaymentStatus.PENDING);
        // Create payment via API
        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/payments"), testPayment, Payment.class));
        testPayment = (Payment) testContext.getLastResponse().getBody();
        // Then complete it via API
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/payments/" + testPayment.getId() + "/complete"), HttpMethod.PUT, new HttpEntity<>(null), Payment.class));
    }

    @When("the client calls POST {string} with a payment amount")
    public void clientCallsPostWithPaymentAmount(String endpoint) {
        testPayment = new Payment();
        testPayment.setReservation(testReservation);
        testPayment.setAmount(BigDecimal.valueOf(300.00));
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment.setPaymentMethod("Credit Card");
        testPayment.setStatus(Payment.PaymentStatus.PENDING);
        testContext.setLastResponse(restTemplate.postForEntity(getFullUrl(endpoint), testPayment, Payment.class));
    }

    @When("the client calls POST \\/payments with a payment amount")
    public void clientCallsPostPaymentsWithAmount() {
        if (testReservation == null) {
            testReservation = new Reservation();
            testReservation.setStatus(Reservation.ReservationStatus.PENDING);
            testReservation.setTotalAmount(BigDecimal.valueOf(300.00));
            testReservation = reservationService.save(testReservation);
        }
        // Only create new payment if not already set (e.g., by invalid payment details step)
        if (testPayment == null || testPayment.getReservation() == null) {
            testPayment = new Payment();
            testPayment.setReservation(testReservation);
            testPayment.setAmount(BigDecimal.valueOf(300.00));
            testPayment.setPaymentDate(LocalDateTime.now());
            testPayment.setPaymentMethod("Credit Card");
            testPayment.setStatus(Payment.PaymentStatus.PENDING);
        }
        // Use String.class for error responses to avoid deserialization issues
        if (testPayment.getAmount() == null) {
            testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/payments"), testPayment, String.class));
        } else {
            testContext.setLastResponse(restTemplate.postForEntity(getFullUrl("/api/payments"), testPayment, Payment.class));
            testPayment = (Payment) testContext.getLastResponse().getBody();
        }
    }

    @When("the client calls PUT {string} confirm")
    public void clientCallsPutConfirm(String endpoint) {
        testPayment.setStatus(Payment.PaymentStatus.COMPLETED);
        testContext.setLastResponse(restTemplate.exchange(getFullUrl(endpoint), HttpMethod.PUT, new HttpEntity<>(testPayment), Payment.class));
    }

    @When("the client calls PUT \\/payments\\/1\\/confirm")
    public void clientCallsPutPaymentsConfirm() {
        // Don't set status to COMPLETED - let the endpoint do it
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/payments/" + testPayment.getId() + "/complete"), HttpMethod.PUT, new HttpEntity<>(null), Payment.class));
    }

    @When("the client calls PUT {string} for non-existing payment")
    public void clientCallsPutForNonExistingPayment(String endpoint) {
        testContext.setLastResponse(restTemplate.exchange(getFullUrl(endpoint), HttpMethod.PUT, new HttpEntity<>(null), Payment.class));
    }

    @When("the client calls PUT \\/payments\\/999\\/confirm for non-existing payment")
    public void clientCallsPutPaymentsConfirmNonExisting() {
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/payments/999/complete"), HttpMethod.PUT, new HttpEntity<>(null), String.class));
    }

    @When("the client calls PUT {string} for already confirmed payment")
    public void clientCallsPutForAlreadyConfirmedPayment(String endpoint) {
        testContext.setLastResponse(restTemplate.exchange(getFullUrl(endpoint), HttpMethod.PUT, new HttpEntity<>(testPayment), Payment.class));
    }

    @When("the client calls PUT \\/payments\\/1\\/confirm for already confirmed payment")
    public void clientCallsPutPaymentsConfirmAlreadyConfirmed() {
        testContext.setLastResponse(restTemplate.exchange(getFullUrl("/api/payments/" + testPayment.getId() + "/complete"), HttpMethod.PUT, new HttpEntity<>(null), Payment.class));
    }

    @Given("no payment exists")
    public void noPaymentExists() {
        paymentService.deleteAll();
    }

    @Then("the payment status is set to processing")
    public void paymentStatusIsSetToProcessing() {
        Payment payment = (Payment) testContext.getLastResponse().getBody();
        assertNotNull(payment);
        assertEquals(Payment.PaymentStatus.PENDING, payment.getStatus());
    }

    @Then("the reservation state changes to confirmed")
    public void reservationStateChangesToConfirmed() {
        Payment updatedPayment = paymentService.findById(testPayment.getId()).orElse(null);
        assertNotNull(updatedPayment);
        assertNotNull(updatedPayment.getReservation());
        assertEquals(Reservation.ReservationStatus.CONFIRMED, updatedPayment.getReservation().getStatus());
    }

    @Then("the reservation remains in a pending state")
    public void reservationRemainsInPendingState() {
        Reservation reservation = reservationService.findById(testReservation.getId()).orElse(null);
        assertNotNull(reservation);
        assertEquals(Reservation.ReservationStatus.PENDING, reservation.getStatus());
    }
}
