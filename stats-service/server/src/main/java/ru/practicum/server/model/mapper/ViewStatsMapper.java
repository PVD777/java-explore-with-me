package ru.practicum.server.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.ViewStats;

@UtilityClass
public class ViewStatsMapper {
    public ViewStatsDto viewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
