package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.request.dao.RequestRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.model.User;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getByRequesterId(int userId) {
         if (!userRepository.existsById(userId)) {
             throw new ObjectNotFoundException("User не найден");
         }
        return requestRepository.findByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }



    @Override
    public ParticipationRequestDto create(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));
        validate(user, event);
        Request request = createNewRequest(event, user);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancel(int userId, int requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Request не найден"));
        int requesterId = request.getRequester().getId();
        if (userId != requesterId) {
            throw new ValidationException("Запрещено");
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getByUserIdAndEventId(int userId, int eventId) {
        return requestRepository.findByUserIdAndEventId(userId, eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(int userId, int eventId, EventRequestStatusUpdateRequest updatedRequest) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User не найден");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));

        if (event.getConfirmedRequest() >= event.getParticipantLimit()) {
            throw new ValidationException("Limit reached");
        }

        List<Request> requests = requestRepository.findByIdIn(updatedRequest.getRequestIds());
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        for (Request request : requests) {
            if (request.getStatus() == RequestStatus.PENDING) {
                if (event.getParticipantLimit() == 0) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequest(event.getConfirmedRequest() + 1);
                } else if (event.getParticipantLimit() > event.getConfirmedRequest()) {
                    if (!event.getRequestModeration()) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequest(event.getConfirmedRequest() + 1);
                    } else {
                        if (updatedRequest.getStatus() == RequestStatus.CONFIRMED) {
                            request.setStatus(RequestStatus.CONFIRMED);
                            event.setConfirmedRequest(event.getConfirmedRequest() + 1);
                        } else {
                            request.setStatus(RequestStatus.REJECTED);
                        }
                    }
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                }
            } else {
                throw new ValidationException("request status not waiting");
            }

            if (request.getStatus().equals((RequestStatus.CONFIRMED))) {
                confirmed.add(request);
            } else {
                rejected.add(request);
            }
        }
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmed.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()),
                rejected.stream()
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList()));
    }

    Request createNewRequest(Event event, User user) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequest(event.getConfirmedRequest() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return request;
    }

    private void validate(User user, Event event) {
        int requesterId = user.getId();
        int initiatorId = event.getInitiator().getId();
        int eventId = event.getId();
        int participantLimit = event.getParticipantLimit();

        if (!CollectionUtils.isEmpty(requestRepository.findByRequesterIdAndEventId(requesterId, eventId))) {
            throw new ValidationException("Request уже существует");
        }

        if (requesterId == initiatorId) {
            throw new ValidationException("Initiator и Requester одинаковы");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ValidationException("Event не в стадии published");
        }

        int countByEventIdAndStatus = requestRepository.countByEventIdAndStatus(eventId);

        if ((participantLimit != 0)
                && (countByEventIdAndStatus >= participantLimit)) {
            throw new ValidationException("Лимит участия");
        }
    }

}
