package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxDurationAnalyzerTest {

    // Проверить корректность вычисления максимального значения (при разной продолжительности сессий)
    @Test
    void shouldReturnMaxDuration() {
        MaxDurationAnalyzer analyzer = new MaxDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.GOOD), // 480
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 14, 0),
                        LocalDateTime.of(2026, 2, 3, 15, 0), SleepQuality.NORMAL) // 60
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(480L, result.getAnalysisResult());
    }

    // Проверить корректность вычисления максимального значения (при одинаковой продолжительности сессий)
    @Test
    void shouldReturnMaxDurationForEqualDurationSessions() {
        MaxDurationAnalyzer analyzer = new MaxDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.GOOD), // 480
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 14, 0),
                        LocalDateTime.of(2026, 2, 3, 15, 0), SleepQuality.NORMAL), // 60
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 23, 0),
                        LocalDateTime.of(2026, 2, 4, 7, 0), SleepQuality.GOOD) // 480
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(480L, result.getAnalysisResult());
    }

    // Проверить, что возвращаемое максимальное значение для пустого списка - 0 сессий
    @Test
    void shouldReturnZeroForEmptyList() {
        MaxDurationAnalyzer analyzer = new MaxDurationAnalyzer();

        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }
}
