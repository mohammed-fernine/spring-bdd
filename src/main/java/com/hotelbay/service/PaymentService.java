package com.hotelbay.service;

import com.hotelbay.entity.Payment;
import com.hotelbay.entity.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final List<Payment> payments = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }

    public Optional<Payment> findById(Long id) {
        return payments.stream()
                .filter(payment -> payment.getId().equals(id))
                .findFirst();
    }

    public List<Payment> findByReservationId(Long reservationId) {
        return payments.stream()
                .filter(payment -> payment.getReservation() != null && payment.getReservation().getId().equals(reservationId))
                .collect(Collectors.toList());
    }

    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        return payments.stream()
                .filter(payment -> payment.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Payment> findByReservationAndStatus(Long reservationId, Payment.PaymentStatus status) {
        return payments.stream()
                .filter(payment -> payment.getReservation() != null && 
                        payment.getReservation().getId().equals(reservationId) && 
                        payment.getStatus() == status)
                .collect(Collectors.toList());
    }

    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(idGenerator.getAndIncrement());
        } else {
            payments.removeIf(p -> p.getId().equals(payment.getId()));
        }
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }
        payment.setUpdatedAt(LocalDateTime.now());
        payments.add(payment);
        return payment;
    }

    public void deleteById(Long id) {
        payments.removeIf(payment -> payment.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return payments.stream().anyMatch(payment -> payment.getId().equals(id));
    }

    public void deleteAll() {
        payments.clear();
    }
}
