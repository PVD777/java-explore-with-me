package ru.practicum.stat;



import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatService {
    void saveHit(HttpServletRequest request);

    Map<Integer, Long> getViews(LocalDateTime start, LocalDateTime end, List<Integer> eventIds, Boolean unique);
}
