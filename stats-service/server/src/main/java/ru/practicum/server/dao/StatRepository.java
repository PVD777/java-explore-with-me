package ru.practicum.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("SELECT new ru.practicum.server.model.ViewStats(h.app, h.uri, count(DISTINCT(h.ip))) " +
            "from EndpointHit as h " +
            "where ((h.timestamp between :start and :end) and :uris is null or h.uri in :uris) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) desc")
    List<ViewStats> getViewStatsUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.server.model.ViewStats(h.app, h.uri, COUNT((h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp between :start and :end and :uris is null or h.uri in :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);



}
