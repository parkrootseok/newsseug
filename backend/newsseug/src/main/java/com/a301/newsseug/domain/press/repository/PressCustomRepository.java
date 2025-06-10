package com.a301.newsseug.domain.press.repository;

import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import java.util.List;

public interface PressCustomRepository {

    List<PressSummaryDto> findAllArticles();

    List<PressSummaryDto> findAllPressesByName(String name);

}
