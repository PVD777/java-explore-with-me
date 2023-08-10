package ru.practicum.request.model;

import lombok.experimental.UtilityClass;
import ru.practicum.request.model.dto.ParticipationRequestDto;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        if (request != null) {
            participationRequestDto.setId(request.getId());
            participationRequestDto.setCreated(request.getCreated());
            participationRequestDto.setEvent(request.getEvent().getId());
            participationRequestDto.setRequester(request.getRequester().getId());
            participationRequestDto.setStatus(request.getStatus());
        }
        return participationRequestDto;
    }
}
