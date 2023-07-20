package ru.practicum.model.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit dtoToHit(EndpointHitDto dto) {
        return new EndpointHit(dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());
    }
}
