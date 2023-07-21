package ru.practicum.server.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;

@UtilityClass
public class EndpointHitMapper {
    public EndpointHit dtoToHit(EndpointHitDto dto) {
        return new EndpointHit(dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());
    }
}
