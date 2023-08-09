package ru.practicum.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Pair;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByRequesterId(int userId);

    List<Request> findByRequesterIdAndEventId(int requesterId, int eventId);

    @Query("SELECT COUNT(request) FROM Request request WHERE " +
            "request.status = ru.practicum.request.model.RequestStatus.CONFIRMED " +
            "AND request.event.id = :eventId")
    Integer countByEventIdAndStatus(int eventId);

    List<Request> findByIdIn(List<Integer> requestIds);

    @Query(
            "SELECT request " +
            "FROM Request request " +
            "WHERE (request.event.id = :eventId) AND (request.event.initiator.id = :userId) " +
            "ORDER BY request.id"
    )
    List<Request> findByUserIdAndEventId(int userId, int eventId);

    @Query(
            "SELECT new ru.practicum.request.model.Pair(request.event.id, COUNT(request.id)) " +
                    "FROM Request request " +
                    "WHERE (:eventIds IS NULL OR :eventIds in request.event.id) " +
                    "   AND (request.status = ru.practicum.request.model.RequestStatus.CONFIRMED) " +
                    "GROUP BY request.event.id"
    )
    List<Pair> findByEventIdInAndStatusConfirmed(List<Integer> eventIds);

    Integer countByEventIdAndStatus(Integer eventId, RequestStatus status);
}
