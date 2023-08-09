package ru.practicum.stat;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.Client;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final Client statsClient;

    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${application.name}")
    private final String applicationName;

    private final String path = "/stats?start={start}&end={end}&uri={uri}&unique={unique}";

    @Override
    public void saveHit(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(applicationName);
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        endpointHitDto.setUri(request.getRequestURI());
        statsClient.post("/hit", endpointHitDto);
    }

    @Override
    public Map<Integer, Integer> getViews(LocalDateTime start, LocalDateTime end, List<Integer> eventIds, Boolean unique) {
        Map<Integer, Integer> result = new HashMap<>();
        Map<String, Integer> uris = new HashMap<>();
        for (Integer id : eventIds) {
            uris.put("/events/" + id, id);
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uri", uris.keySet(),
                "unique", unique
        );
        ResponseEntity<Object> responseEntity = statsClient.get(path, parameters);
        List<ViewStatsDto> statsDtoList = toViewStatsDtoList(responseEntity);
        System.out.println("statsDtoList" + statsDtoList);

        for (ViewStatsDto statsDto : statsDtoList) {
            Integer eventId = uris.get(statsDto.getUri());
            if (eventId != null) {
                result.put(eventId, statsDto.getHits());
            }
        }
        return result;
    }

    private List<ViewStatsDto> toViewStatsDtoList(ResponseEntity<Object> responseEntity) {
        List<ViewStatsDto> statsDtoList;
        try {
            statsDtoList = Arrays.asList(
                    objectMapper.readValue(
                            objectMapper.writeValueAsString(responseEntity.getBody()), ViewStatsDto[].class)
            );
        } catch (JsonProcessingException e) {
            throw new ClassCastException(e.getMessage());
        }
        return statsDtoList;
    }


}
