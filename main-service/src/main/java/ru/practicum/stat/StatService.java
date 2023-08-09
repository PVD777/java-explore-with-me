package ru.practicum.stat;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatService {
    void saveHit(HttpServletRequest request);

    Map<Integer, Integer> getViews(LocalDateTime start, LocalDateTime end, List<Integer> eventIds, Boolean unique);
}
