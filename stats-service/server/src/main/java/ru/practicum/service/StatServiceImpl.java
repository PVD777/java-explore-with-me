package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dao.StatRepository;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.mapper.EndpointHitMapper;
import ru.practicum.model.mapper.ViewStatsMapper;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new ValidationException("Check start and end time");
        }
        List<ViewStats> viewStatsList;
        if (uris.isEmpty()) {
            if (unique) {
                viewStatsList = statRepository.getAllViewStatsUniqueIp(start, end);
            } else {
                viewStatsList = statRepository.getAllViewStats(start, end);
            }
        } else {
            if (unique) {
                viewStatsList = statRepository.getViewStatsUniqueIp(start, end, uris);
            } else {
                viewStatsList = statRepository.getViewStats(start, end, uris);
            }
        }
        return viewStatsList.stream()
                .map(ViewStatsMapper::viewStatsDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statRepository.save(EndpointHitMapper.dtoToHit(endpointHitDto));
    }
}