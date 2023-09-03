package com.otbs.OnlineTicketBookingSystem.converter;

import com.otbs.OnlineTicketBookingSystem.entity.Hall;
import com.otbs.OnlineTicketBookingSystem.model.HallDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HallConverter implements EntityConverter<Hall, HallDTO> {


    @Override
    public HallDTO convert(@NotNull Hall hall) {
        return HallDTO.builder()
                .id(hall.getId())
                .numRows(hall.getNumRows())
                .numColumns(hall.getNumColumns())
                .build();
    }

    @Override
    public Hall convertToEntity(@NotNull HallDTO dto) {
        return Hall.builder()
                .id(dto.id())
                .numRows(dto.numRows())
                .numColumns(dto.numColumns())
                .build();
    }
}
