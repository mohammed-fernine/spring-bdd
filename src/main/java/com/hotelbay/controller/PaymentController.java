package com.hotelbay.controller;

import com.hotelbay.entity.Payment;
import com.hotelbay.entity.Reservation;
import com.hotelbay.service.PaymentService;
import com.hotelbay.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.findById(id);
        return payment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<Payment>> getPaymentsByReservation(@PathVariable Long reservationId) {
        List<Payment> payments = paymentService.findByReservationId(reservationId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus status) {
        List<Payment> payments = paymentService.findByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/reservation/{reservationId}/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByReservationAndStatus(
            @PathVariable Long reservationId, @PathVariable Payment.PaymentStatus status) {
        List<Payment> payments = paymentService.findByReservationAndStatus(reservationId, status);
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        if (payment.getReservation() == null || 
            !reservationService.existsById(payment.getReservation().getId())) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Reservation> reservationOpt = reservationService.findById(payment.getReservation().getId());
        if (!reservationOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        payment.setReservation(reservationOpt.get());
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        Payment savedPayment = paymentService.save(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @Valid @RequestBody Payment paymentDetails) {
        Optional<Payment> optionalPayment = paymentService.findById(id);
        if (!optionalPayment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Payment payment = optionalPayment.get();
        payment.setAmount(paymentDetails.getAmount());
        payment.setPaymentDate(paymentDetails.getPaymentDate());
        payment.setStatus(paymentDetails.getStatus());
        payment.setTransactionId(paymentDetails.getTransactionId());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        payment.setNotes(paymentDetails.getNotes());

        Payment updatedPayment = paymentService.save(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Payment> completePayment(@PathVariable Long id) {
        Optional<Payment> optionalPayment = paymentService.findById(id);
        if (!optionalPayment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Payment payment = optionalPayment.get();
        if (payment.getStatus() == Payment.PaymentStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        Payment updatedPayment = paymentService.save(payment);
        
        // Update reservation status to CONFIRMED when payment is completed
        if (payment.getReservation() != null) {
            payment.getReservation().setStatus(Reservation.ReservationStatus.CONFIRMED);
            reservationService.save(payment.getReservation());
        }
        
        return ResponseEntity.ok(updatedPayment);
    }

    @PutMapping("/{id}/fail")
    public ResponseEntity<Payment> failPayment(@PathVariable Long id) {
        Optional<Payment> optionalPayment = paymentService.findById(id);
        if (!optionalPayment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Payment payment = optionalPayment.get();
        payment.setStatus(Payment.PaymentStatus.FAILED);
        Payment updatedPayment = paymentService.save(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        Optional<Payment> optionalPayment = paymentService.findById(id);
        if (!optionalPayment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Payment payment = optionalPayment.get();
        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        Payment updatedPayment = paymentService.save(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (!paymentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
