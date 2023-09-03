package com.otbs.OnlineTicketBookingSystem.exception;

public class ChangePasswordException extends RuntimeException {


    public ChangePasswordException(int msg) {
        super("При смене пароля произошла ошибка. Ожидалась количество изменений 1, а получено " + msg);
    }
}
