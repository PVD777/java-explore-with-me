package ru.practicum.event.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT event " +
            "FROM Event event " +
            "where :initiatorIds is null or event.initiator.id in :initiatorIds AND " +
            ":states is null or event.state in :states AND " +
            ":categories is null or event.category.id in :categories AND " +
            "(event.eventDate BETWEEN COALESCE(:rangeStart, event.eventDate) " +
            "AND COALESCE(:rangeEnd, event.eventDate))")
    List<Event> searchByAdmin(List<Integer> initiatorIds, List<EventState> states, List<Integer> categories,
                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findEventsByInitiatorId(int initiatorId, Pageable pageable);

    Optional<Event> findByCategoryId(int categoryId);

    Optional<Event> findByIdAndInitiatorId(int eventId, int initiatorId);

    @Query("SELECT event FROM Event event WHERE  event.state = ru.practicum.event.model.EventState.PUBLISHED " +
            "AND ((:text is null) OR LOWER(event.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR (:text is null or LOWER(event.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "OR (:text IS NULL OR LOWER(event.title) LIKE LOWER(CONCAT('%', :text, '%')))) " +
            "AND (:categories is null or event.category.id in :categories)  " +
            "AND (:paid IS NULL OR event.paid = :paid) " +
            "AND(:onlyAvailable is NULL  OR :onlyAvailable = false " +
            "OR (:onlyAvailable = true AND event.confirmedRequest < event.participantLimit)) " +
            "AND (event.eventDate BETWEEN COALESCE(:rangeStart, event.eventDate) AND COALESCE(:rangeEnd, event.eventDate)) "
            )
    List<Event> searchPublic(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                             LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);
}


