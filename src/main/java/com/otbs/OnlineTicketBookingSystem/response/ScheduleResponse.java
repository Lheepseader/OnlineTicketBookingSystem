package com.otbs.OnlineTicketBookingSystem.response;

import com.otbs.OnlineTicketBookingSystem.entity.Movie;
import com.otbs.OnlineTicketBookingSystem.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse {

    private Map<Movie, List<Session>> movieSessionsMap;

    private TreeSet<String> futureSessionDates;
}
