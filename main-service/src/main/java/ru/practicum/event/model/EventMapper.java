package ru.practicum.event.model;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
        public Event newDtoToEvent(NewEventDto newEventDto, User user, Category category) {
            Event event = new Event();
            event.setAnnotation(newEventDto.getAnnotation());
            event.setCategory(category);
            event.setCreatedOn(LocalDateTime.now());
            event.setDescription(newEventDto.getDescription());
            event.setEventDate(newEventDto.getEventDate());
            event.setPaid(newEventDto.isPaid());
            event.setParticipantLimit(newEventDto.getParticipantLimit());
            event.setPublishedOn(LocalDateTime.now());
            event.setInitiator(user);
            event.setState(EventState.PENDING);
            event.setLocation(newEventDto.getLocation());
            event.setRequestModeration(newEventDto.isRequestModeration());
            event.setTitle(newEventDto.getTitle());
            return event;
        }

        public EventFullDto eventToFullDto(Event event) {
            EventFullDto eventFullDto = new EventFullDto();
            eventFullDto.setId(event.getId());
            eventFullDto.setTitle(event.getTitle());
            eventFullDto.setAnnotation(event.getAnnotation());
            eventFullDto.setCategory(CategoryMapper.categoryToDto(event.getCategory()));
            eventFullDto.setPaid(event.getPaid());
            eventFullDto.setEventDate(event.getEventDate());
            eventFullDto.setInitiator(UserMapper.userToShortDto(event.getInitiator()));
            eventFullDto.setDescription(event.getDescription());
            eventFullDto.setParticipantLimit(event.getParticipantLimit());
            eventFullDto.setState(event.getState());
            eventFullDto.setCreatedOn(event.getCreatedOn());
            eventFullDto.setPublishedOn(event.getPublishedOn());
            eventFullDto.setLocation(event.getLocation());
            eventFullDto.setRequestModeration(event.getRequestModeration());
            eventFullDto.setConfirmedRequests(0L);
            eventFullDto.setViews(0);
            return eventFullDto;
        }

    public EventFullDto eventToFullDto(Event event, Long confirmedRequests, Integer views) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.categoryToDto(event.getCategory()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.userToShortDto(event.getInitiator()));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setState(event.getState());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        if (confirmedRequests != null) {
            eventFullDto.setConfirmedRequests(confirmedRequests);
        }
        if (views != null) {
            eventFullDto.setViews(views);
        }
        return eventFullDto;
    }

        public EventShortDto eventToShortDto(Event event) {
            EventShortDto eventShortDto = new EventShortDto();
            eventShortDto.setId(event.getId());
            eventShortDto.setAnnotation(event.getAnnotation());
            eventShortDto.setCategory(CategoryMapper.categoryToDto(event.getCategory()));
            eventShortDto.setEventDate(event.getEventDate());
            eventShortDto.setInitiator(UserMapper.userToShortDto(event.getInitiator()));
            eventShortDto.setPaid(event.getPaid());
            eventShortDto.setTitle(event.getTitle());
            eventShortDto.setConfirmedRequests(0L);
            eventShortDto.setViews(0);
            return eventShortDto;
        }

    public EventShortDto eventToShortDto(Event event, Long confirmedRequests, Integer views) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.categoryToDto(event.getCategory()));
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UserMapper.userToShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        if (confirmedRequests != null) {
            eventShortDto.setConfirmedRequests(confirmedRequests);
        }
        if (views != null) {
            eventShortDto.setViews(views);
        }
        return eventShortDto;
    }

}
