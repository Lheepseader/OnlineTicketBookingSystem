package com.otbs.OnlineTicketBookingSystem.service;

import com.otbs.OnlineTicketBookingSystem.model.HallDTO;

public interface HallService {

    HallDTO save(HallDTO hall);

    void deleteById(Integer id);
}

