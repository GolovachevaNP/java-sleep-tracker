package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleeplessNightsAnalyzerTest {

    // Проверить корректность вычисления бессонных ночей при отсутствии таковых в списке - 0 ночей
    @Test
    void shouldReturn0LWhen0SleeplessNights() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 23:00 01.02 – 03:00 02.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 0),
                        LocalDateTime.of(2026, 2, 2, 3, 0), SleepQuality.GOOD),
                // сон 02:00 03.02 – 07:00 03.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 2, 0),
                        LocalDateTime.of(2026, 2, 3, 7, 0), SleepQuality.NORMAL),
                // сон 23:30 03.02 – 06:20 04.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 23, 30),
                        LocalDateTime.of(2026, 2, 4, 6, 20), SleepQuality.BAD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }

    // Проверить корректность вычисления бессонных ночей при наличии 1 бессонной ночи
    @Test
    void shouldReturn1LWhenSleepStartsAt6Hour() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 23:00 01.02 – 05:00 02.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 0),
                        LocalDateTime.of(2026, 2, 2, 5, 0), SleepQuality.GOOD),
                // сон 06:00 03.02 – 08:00 03.02 - бессонная ночь
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 6, 0),
                        LocalDateTime.of(2026, 2, 3, 8, 0), SleepQuality.BAD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(1L, result.getAnalysisResult());
    }

    // Проверить корректность правила: если первая сессия началась после 12 дня, потенциальной ночью для сна
    // считается следующая ночь
    @Test
    void shouldReturn1LWhenFirstSessionStartsAfter12Hour() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 23:00 31.01 – 15:30 01.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 1, 31, 23, 0),
                        LocalDateTime.of(2026, 2, 1, 15, 30), SleepQuality.GOOD),
                // сон 06:00 02.02 – 11:30 02.02 - бессонная ночь (00:00-06:00 не пересекается)
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 6, 0),
                        LocalDateTime.of(2026, 2, 2, 11, 30), SleepQuality.BAD),
                // сон 00:30 03.02 – 08:00 03.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 0, 30),
                        LocalDateTime.of(2026, 2, 3, 8, 0), SleepQuality.NORMAL)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(1L, result.getAnalysisResult());
    }

    // Проверить корректность правила: если первая сессия началась до 12 дня, потенциальной ночью для сна
    // считается предыдущая ночь
    @Test
    void shouldReturn0LWhenFirstSessionStartsBefore12Hour() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 01:00 01.02 – 05:00 01.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 1, 0),
                        LocalDateTime.of(2026, 2, 1, 5, 0), SleepQuality.GOOD),
                // сон 23:00 01.02 – 04:00 02.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 0),
                        LocalDateTime.of(2026, 2, 2, 4, 0), SleepQuality.NORMAL)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }

    // Проверить корректность вычисления бессонных ночей при переходе в следующий месяц
    @Test
    void shouldWorkCorrectlyWhenMonthChanges() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 23:00 31.01 – 05:00 01.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 1, 31, 23, 0),
                        LocalDateTime.of(2026, 2, 1, 5, 0), SleepQuality.GOOD),
                // сон 23:00 01.02 – 05:00 02.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 0),
                        LocalDateTime.of(2026, 2, 2, 5, 0), SleepQuality.NORMAL),
                // сон 23:00 02.02 – 05:00 03.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 23, 0),
                        LocalDateTime.of(2026, 2, 3, 5, 0), SleepQuality.BAD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(0L, result.getAnalysisResult());
    }

    // Проверить корректность вычисления бессонных ночей при наличии сна в дневное время и отсутствии в промежуток 00:00-06:00
    @Test
    void shouldReturn1LWhenOnlyDaySleepBetweenNights() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 23:00 01.02 – 05:00 02.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 0),
                        LocalDateTime.of(2026, 2, 2, 5, 0), SleepQuality.GOOD),
                // дневной сон 14:00 02.02 – 15:00 02.02 - ночь бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 14, 0),
                        LocalDateTime.of(2026, 2, 2, 15, 0), SleepQuality.NORMAL),
                // сон 23:00 03.02 – 05:00 04.02 - ночь не бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 3, 23, 0),
                        LocalDateTime.of(2026, 2, 4, 5, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(1L, result.getAnalysisResult());
    }
}

