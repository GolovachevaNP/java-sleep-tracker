package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadQualityCountAnalyzerTest {

    // Проверить корректность вычисления количества сессий с плохим качеством сна - 2 сессии
    @Test
    void shouldCountBadQualitySessions() {
        BadQualityCountAnalyzer analyzer = new BadQualityCountAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.BAD),
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 22, 0),
                        LocalDateTime.of(2026, 2, 3, 6, 0), SleepQuality.GOOD), new SleepingSession(LocalDateTime.of(2026, 2, 3, 22, 0), LocalDateTime.of(2026, 2, 4, 6, 0), SleepQuality.BAD));

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(2L, result.getAnalysisResult());
    }

    // Проверить корректность вычисления количества сессий с плохим качеством сна при отсутствии таковых в списке - 0 сессий
    @Test
    void shouldReturnZeroIfNoBadSessions() {
        BadQualityCountAnalyzer analyzer = new BadQualityCountAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 22, 0),
                        LocalDateTime.of(2026, 2, 3, 6, 0), SleepQuality.GOOD), new SleepingSession(LocalDateTime.of(2026, 2, 3, 22, 0), LocalDateTime.of(2026, 2, 4, 6, 0), SleepQuality.NORMAL));

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }

    // Проверить, что возвращаемое количество сессий с плохим качеством сна для пустого списка - 0 сессий
    @Test
    void shouldReturnZeroForEmptyList() {
        BadQualityCountAnalyzer analyzer = new BadQualityCountAnalyzer();

        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }
}
