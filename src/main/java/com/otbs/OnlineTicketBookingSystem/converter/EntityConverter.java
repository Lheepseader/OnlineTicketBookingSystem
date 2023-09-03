package com.otbs.OnlineTicketBookingSystem.converter;

import org.springframework.core.convert.converter.Converter;

public interface EntityConverter<E, D> extends Converter<E, D> {

    E convertToEntity(D dto);

}
