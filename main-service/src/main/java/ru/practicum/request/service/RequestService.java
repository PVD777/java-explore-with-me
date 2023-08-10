package ru.practicum.request.service;

import ru.practicum.request.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getByRequesterId(int userId);

    ParticipationRequestDto create(int userId, int eventId);

    ParticipationRequestDto cancel(int userId, int requestId);

    List<ParticipationRequestDto> getByUserIdAndEventId(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequests(int userId, int eventId,
                                                  EventRequestStatusUpdateRequest updatedRequest);
}
