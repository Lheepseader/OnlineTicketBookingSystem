package com.otbs.OnlineTicketBookingSystem.constant;

import java.time.format.DateTimeFormatter;

public interface DateFormat {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    DateTimeFormatter DATE_HOUR_MINUTE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
}
