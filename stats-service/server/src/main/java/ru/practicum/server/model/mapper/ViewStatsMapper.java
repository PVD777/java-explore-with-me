package ru.practicum.server.model.mapper;

import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.ViewStats;

public class ViewStatsMapper {
    public static ViewStatsDto viewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
