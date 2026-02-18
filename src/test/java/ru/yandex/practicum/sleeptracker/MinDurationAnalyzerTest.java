package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinDurationAnalyzerTest {
    // Проверить корректность вычисления минимального значения (при разной продолжительности сессий)
    @Test
    void shouldReturnMinDuration() {
        MinDurationAnalyzer analyzer = new MinDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.GOOD), // 480
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 14, 0),
                        LocalDateTime.of(2026, 2, 3, 15, 0), SleepQuality.NORMAL) // 60
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(60L, result.getAnalysisResult());
    }

    // Проверить корректность вычисления минимального значения (при одинаковой продолжительности сессий)
    @Test
    void shouldReturnMinDurationForEqualDurationSessions() {
        MinDurationAnalyzer analyzer = new MinDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.GOOD), // 480
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 14, 0),
                        LocalDateTime.of(2026, 2, 3, 15, 0), SleepQuality.NORMAL), // 60
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 23, 0),
                        LocalDateTime.of(2026, 2, 4, 0, 0), SleepQuality.BAD) // 60
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(60L, result.getAnalysisResult());
    }

    // Проверить, что возвращаемое минимальное значение для пустого списка - 0 сессий
    @Test
    void shouldReturnZeroForEmptyList() {
        MinDurationAnalyzer analyzer = new MinDurationAnalyzer();

        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }
}
