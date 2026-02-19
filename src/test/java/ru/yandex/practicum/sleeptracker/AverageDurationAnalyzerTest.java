package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AverageDurationAnalyzerTest {
    // Проверить корректность вычисления средней продолжительности сессии сна (целое число)
    @Test
    void shouldReturnAverageDuration() {
        AverageDurationAnalyzer analyzer = new AverageDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.GOOD), // 480
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 14, 0),
                        LocalDateTime.of(2026, 2, 3, 15, 0), SleepQuality.NORMAL) // 60
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(270.0, (Double) result.getAnalysisResult());
    }

    // Проверить корректность вычисления средней продолжительности сессии сна (число с дробной частью)
    @Test
    void shouldReturnAverageDurationDouble() {
        AverageDurationAnalyzer analyzer = new AverageDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 22, 0),
                        LocalDateTime.of(2026, 2, 2, 6, 0), SleepQuality.GOOD), // 480
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 14, 0),
                        LocalDateTime.of(2026, 2, 3, 15, 17), SleepQuality.NORMAL) // 77
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(278.5, (Double) result.getAnalysisResult());
    }

    // Проверить, что возвращаемое среднее значение для пустого списка - 0 сессий
    @Test
    void shouldReturnZeroForEmptyList() {
        AverageDurationAnalyzer analyzer = new AverageDurationAnalyzer();

        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0.0, result.getAnalysisResult());
    }
}
