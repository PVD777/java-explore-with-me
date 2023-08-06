package ru.practicum.stat;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StatService {
    ResponseEntity<Object> saveHit(HttpServletRequest request);

    ResponseEntity<Object> getStatsWithUris(String start, String end, List<String> uris, boolean unique);
}
