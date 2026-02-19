package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TotalSessionsAnalyzerTest {
    // Проверить корректность вычисления возвращаемого количества сессий - 2 сессии
    @Test
    void shouldReturnTotalSessionsCount() {
        TotalSessionsAnalyzer analyzer = new TotalSessionsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.GOOD), // 480
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 23, 0),
                        LocalDateTime.of(2026, 2, 3, 7, 0), SleepQuality.NORMAL) // 480
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(2L, result.getAnalysisResult());
    }

    // Проверить, что возвращаемое количество сессий для пустого списка - 0 сессий
    @Test
    void shouldReturnZeroForEmptyList() {
        TotalSessionsAnalyzer analyzer = new TotalSessionsAnalyzer();

        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }
}
