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
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User не найден");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));
        List<Request> requests = requestRepository.findByIdIn(updatedRequest.getRequestIds());
        if ((!event.getRequestModeration() || event.getParticipantLimit() == 0)
                && (updatedRequest.getStatus() == RequestStatus.CONFIRMED)) {
            result.setConfirmedRequests(requests.stream()
                    .map(RequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList())
            );
            result.setRejectedRequests(Collections.emptyList());
            return result;
        }

        final AtomicInteger vacancyLeft = new AtomicInteger(event.getParticipantLimit()
                - requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));

        if (vacancyLeft.get() <= 0) {
            throw new ValidationException("Request limit");
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        requests.forEach(request -> {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ValidationException("Проверить event status");
            }
            if (updatedRequest.getStatus().equals(RequestStatus.CONFIRMED) && vacancyLeft.get() > 0) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(request);
                vacancyLeft.decrementAndGet();
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        });

        List<Request> allRequests = Stream.of(confirmedRequests, rejectedRequests)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!allRequests.isEmpty()) {
            requestRepository.saveAll(allRequests);
        }

        result.setConfirmedRequests(confirmedRequests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));

        result.setRejectedRequests(rejectedRequests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));

        return result;
    }

    Request createNewRequest(Event event, User user) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus((!event.getRequestModeration() || event.getParticipantLimit() == 0) ?
                RequestStatus.CONFIRMED : RequestStatus.PENDING
        );
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

        Long countByEventIdAndStatus = requestRepository.countByEventIdAndStatus(eventId);

        if ((participantLimit != 0)
                && (countByEventIdAndStatus >= participantLimit)) {
            throw new ValidationException("Лимит участия");
        }
    }

}
