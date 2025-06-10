package com.a301.newsseug.domain.press.service;

import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.repository.PressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PressQueryService {

    private final PressRepository pressRepository;

    public List<PressSummaryDto> getPressesByName(String name) {
        return pressRepository.findAllPressesByName(name);
    }

}
