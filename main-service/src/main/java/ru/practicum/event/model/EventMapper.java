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
            event.setViews(0);
            event.setConfirmedRequest(0);
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
            eventFullDto.setConfirmedRequests(event.getConfirmedRequest() == null ? 0 : event.getConfirmedRequest());
            eventFullDto.setViews((event.getViews() == null ? 0 : event.getViews()));
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
            eventShortDto.setConfirmedRequests(event.getConfirmedRequest() == null ? 0 : event.getConfirmedRequest());
            eventShortDto.setViews((event.getViews() == null ? 0 : event.getViews()));
            return eventShortDto;
        }

}