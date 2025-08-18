package com.a301.newsseug.domain.press.repository;

import static com.a301.newsseug.domain.press.model.entity.QPress.press;

import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.model.dto.QPressSummaryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PressCustomRepositoryImpl implements PressCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PressSummaryDto> findAllPressSummaries() {
        return jpaQueryFactory
                .select(new QPressSummaryDto(
                        press.id,
                        press.name,
                        press.imageUrl,
                        press.subscribeCount
                ))
                .from(press)
                .fetch();
    }

    @Override
    public List<PressSummaryDto> findPressSummariesByName( String name) {
        return jpaQueryFactory
                .select(new QPressSummaryDto(
                        press.id,
                        press.name,
                        press.imageUrl,
                        press.subscribeCount
                ))
                .where(press.name.containsIgnoreCase(name))
                .from(press)
                .fetch();
    }

}
