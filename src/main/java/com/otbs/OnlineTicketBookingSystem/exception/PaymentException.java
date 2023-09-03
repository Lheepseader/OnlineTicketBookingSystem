package com.otbs.OnlineTicketBookingSystem.exception;

public class PaymentException extends RuntimeException {

    public PaymentException() {
        super("Ошибка оплаты");
    }
}
