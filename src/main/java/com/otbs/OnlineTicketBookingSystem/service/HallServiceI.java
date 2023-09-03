package com.otbs.OnlineTicketBookingSystem.service;

import com.otbs.OnlineTicketBookingSystem.constant.ExceptionMessage;
import com.otbs.OnlineTicketBookingSystem.converter.HallConverter;
import com.otbs.OnlineTicketBookingSystem.entity.Hall;
import com.otbs.OnlineTicketBookingSystem.exception.NotFoundResponseException;
import com.otbs.OnlineTicketBookingSystem.model.HallDTO;
import com.otbs.OnlineTicketBookingSystem.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class HallServiceI implements HallService {

    private final HallRepository hallRepository;
    private final HallConverter hallConverter;


    @Override
    public HallDTO save(HallDTO hallDTO) {
        Hall hall = hallRepository.save(hallConverter.convertToEntity(hallDTO));
        return hallConverter.convert(hall);
    }

    @Override
    public void deleteById(Integer id) {
        if (!hallRepository.existsById(id)) {
            log.info("Hall not found by id");
            throw new NotFoundResponseException(ExceptionMessage.NO_SUCH_HALL, id);
        }
        hallRepository.deleteById(id);
    }
}
