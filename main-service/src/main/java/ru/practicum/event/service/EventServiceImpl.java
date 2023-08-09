package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.model.*;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.request.dao.RequestRepository;
import ru.practicum.request.model.Pair;
import ru.practicum.stat.StatService;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final StatService statsService;

    @Override
    public List<EventFullDto> searchEvents(List<Integer> users, List<EventState> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        List<Event> events = eventRepository.searchByAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        Map<Integer, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Integer, Integer> views = getViews(events);
        return events.stream()
                .map(event -> {
                    Integer eventId = event.getId();
                    return EventMapper.eventToFullDto(event, confirmedRequests.get(eventId), views.get(eventId));
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto create(Integer userId, NewEventDto newEventDto) {
        User user = getUserById(userId);
        Category category = getCategoryById(newEventDto.getCategory());
        Event event = EventMapper.newDtoToEvent(newEventDto, user, category);
        return EventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateByAdmin(int eventId, UpdateEventRequest updatedEvent) {
        Event event = getEventById(eventId);
        validate(event, updatedEvent);
        updateEventFields(event, updatedEvent);
        return EventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> search(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                                      int from, int size, HttpServletRequest request) {
        rangeStart = (rangeStart != null ? rangeStart : LocalDateTime.now());
        validateDateRange(rangeStart, rangeEnd);
        Pageable pageable = null;
        if (sort != null) {
            switch (sort) {
                case VIEWS:
                    pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "views"));
                    break;
                case EVENT_DATE:
                    pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "eventDate"));
                    break;
            }

        }
        pageable = PageRequest.of(from,size);
        List<Event> events = eventRepository.searchPublic(text, categories, paid, rangeStart, rangeEnd, pageable);
        Map<Integer, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Integer, Integer> views = getViews(events);
        statsService.saveHit(request);
        return events.stream()
                .map(event -> {
                    Integer eventId = event.getId();
                    return EventMapper.eventToShortDto(event, confirmedRequests.get(eventId), views.get(eventId));
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getByEventId(int eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ObjectNotFoundException("Event не опубликован");
        }
        Integer views = getViews(event);
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId);
        statsService.saveHit(request);
        return EventMapper.eventToFullDto(event, confirmedRequests, views);
    }

    @Override
    public List<EventShortDto> getByUserId(int userId, Pageable pageable) {
        return eventRepository.findEventsByInitiatorId(userId, pageable).stream()
                .map(EventMapper::eventToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getByUserIdAndEventId(int eventId, int initiatorId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));
        Integer views = getViews(event);
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId);
        return EventMapper.eventToFullDto(event, confirmedRequests, views);
    }

    @Override
    public EventFullDto updateByUser(int userId, int eventId, UpdateEventRequest updatedEvent) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        validate(user, event, updatedEvent);
        updateEventFields(event, updatedEvent);
        return EventMapper.eventToFullDto(eventRepository.save(event));
    }

    private void updateEventFields(Event event, UpdateEventRequest updatedEvent) {
        LocalDateTime eventDate = updatedEvent.getEventDate();
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }

        EventStateAction stateAction = updatedEvent.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    if (event.getState() == EventState.PENDING) {
                        event.setState(EventState.PUBLISHED);
                        event.setPublishedOn(LocalDateTime.now());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Проверить state");
            }
        }

        if (updatedEvent.getAnnotation() != null && !updatedEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getCategory() != null) {
            Category eventCat = getCategoryById(updatedEvent.getCategory());
            event.setCategory(eventCat);
        }

        if (updatedEvent.getDescription() != null && !updatedEvent.getDescription().isBlank()) {
            event.setDescription(updatedEvent.getDescription());
        }

        if (updatedEvent.getLocation() != null) {
            event.setLocation(updatedEvent.getLocation());
        }

        if (updatedEvent.getPaid() != null) {
            event.setPaid(updatedEvent.getPaid());
        }

        if (updatedEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }

        if (updatedEvent.getRequestModeration() != null) {
            event.setRequestModeration(updatedEvent.getRequestModeration());
        }

        if (updatedEvent.getTitle() != null && !updatedEvent.getTitle().isBlank()) {
            event.setTitle(updatedEvent.getTitle());
        }
    }

    private void validate(User user, Event event, UpdateEventRequest updatedEvent) {
        int userId = user.getId();
        int initiatorId = event.getInitiator().getId();
        if (userId != initiatorId) {
            throw new ValidationException("User не инициатор");
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ValidationException("Проверить статус event");
        }

        LocalDateTime eventDate = updatedEvent.getEventDate();
        if ((eventDate != null) && (eventDate.isBefore(LocalDateTime.now().plusHours(2)))) {
            throw new BadRequestException("Проверить eventDate");
        }
    }

    private void validate(Event event, UpdateEventRequest updatedEvent) {
        LocalDateTime eventDate = updatedEvent.getEventDate();
        if ((eventDate != null) && (eventDate.isBefore(LocalDateTime.now().plusHours(2L)))) {
            throw new BadRequestException("Проверить дату event");
        }

        if ((updatedEvent.getStateAction() == EventStateAction.PUBLISH_EVENT)
                && (event.getState() != EventState.PENDING)) {
            throw new ValidationException("Проверить статус event");
        }

        if ((updatedEvent.getStateAction() == EventStateAction.REJECT_EVENT)
                && (event.getState() == EventState.PUBLISHED)) {
            throw new ValidationException("Проверить статус event");
        }
    }

    private Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category не найдена"));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User не найден"));
    }

    private Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event не найден"));
    }

    private void validateDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Проверить rangeDates");
        }
    }


    private Map<Integer, Long> getConfirmedRequests(List<Event> events) {
        if ((events == null) || (events.isEmpty())) return new HashMap<>();

        List<Pair> confirmedRequestsList = requestRepository.findByEventIdInAndStatusConfirmed(
                events.stream()
                        .map(Event::getId)
                        .collect(Collectors.toList())
        );

        return confirmedRequestsList.stream()
                .collect(Collectors.toMap(Pair::getId, Pair::getHit));
    }

    private Map<Integer, Integer> getViews(List<Event> events) {
        if ((events == null) || (events.isEmpty())) return new HashMap<>();

        List<Event> sortedEvents = events.stream()
                .sorted(Comparator.comparing(Event::getPublishedOn)).collect(Collectors.toList());
        LocalDateTime start = sortedEvents.get(0).getPublishedOn();

        return statsService.getViews(start, LocalDateTime.now(),
                events.stream().map(Event::getId).collect(Collectors.toList()), true);
    }

    private Integer getViews(Event event) {
        if (event == null) return 0;
        Integer eventId = event.getId();
        Map<Integer, Integer> views = statsService.getViews(event.getPublishedOn(), LocalDateTime.now(),
                List.of(eventId), true);
        return views.get(eventId);
    }




}
