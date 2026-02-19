package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChronotypeAnalyzerTest {

    // Проверить корректность определения хронотипа: "сова"
    @Test
    void shouldDetectOwl() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        // сон 23:30-09:30 - ночь не бессонная
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 30),
                        LocalDateTime.of(2026, 2, 2, 9, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.OWL.rusName, result.getAnalysisResult());
    }

    // Проверить корректность определения хронотипа: "жаворонок"
    @Test
    void shouldDetectLark() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        // сон 21:30-06:30 - ночь не бессонная
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 21, 30),
                        LocalDateTime.of(2026, 2, 2, 6, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.LARK.rusName, result.getAnalysisResult());
    }

    // Проверить корректность определения хронотипа: "голубь"
    @Test
    void shouldDetectDove_1() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        // сон 21:30-09:30 - ночь не бессонная
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 21, 30),
                        LocalDateTime.of(2026, 2, 2, 9, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.DOVE.rusName, result.getAnalysisResult());
    }

    // Проверить корректность определения хронотипа: "голубь"
    @Test
    void shouldDetectDove_2() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        // сон 23:30-06:30 - ночь не бессонная
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 30),
                        LocalDateTime.of(2026, 2, 2, 6, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.DOVE.rusName, result.getAnalysisResult());
    }

    // Проверить корректность определения хронотипа при равных значениях "сова":"жаворонок" - "голубь"
    @Test
    void shouldReturnDoveOnTie() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 23:30 01.02 – 09:30 02.02 - ночь не бессонная, "сова"
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 30),
                        LocalDateTime.of(2026, 2, 2, 9, 30), SleepQuality.GOOD),
                // сон 21:30 02.02 – 06:30 03.02 - ночь не бессонная, "жаворонок"
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 21, 30),
                        LocalDateTime.of(2026, 2, 3, 6, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.DOVE.rusName, result.getAnalysisResult());
    }

    // Проверить корректность определения хронотипа, если в одну календарную ночь было 2 ночные сессии сна
    @Test
    void shouldReturnDoveWhenOneNightHasTwoSessionsWithDifferentChronotypes() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                // ночная сессия 21:30 01.02 – 04:00 02.02 -> "жаворонок"
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 21, 30),
                        LocalDateTime.of(2026, 2, 2, 4, 0), SleepQuality.GOOD),
                // ночная сессия 05:40 02.02 – 09:30 02.02 -> "голубь"
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 5, 40),
                        LocalDateTime.of(2026, 2, 2, 9, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.DOVE.rusName, result.getAnalysisResult());
    }

    // Проверить, что бессонные ночи не учитываются при определении хронотипа
    @Test
    void shouldIgnoreSleeplessNights() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 06:00 02.02 – 08:00 02.02 - ночь бессонная
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 6, 0),
                        LocalDateTime.of(2026, 2, 2, 8, 0), SleepQuality.BAD),
                // сон 21:00 02.02 – 06:00 03.02 - ночь не бессонная, "жаворонок"
                new SleepingSession(LocalDateTime.of(2026, 2, 2, 21, 0),
                        LocalDateTime.of(2026, 2, 3, 6, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.LARK.rusName, result.getAnalysisResult());
    }

    // Проверить, что дневные сессии сна не учитываются при определении хронотипа
    @Test
    void shouldIgnoreDaySleepingSessions() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                // сон 15:00 01.02 – 17:00 01.02 - дневной сон
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 15, 0),
                        LocalDateTime.of(2026, 2, 1, 17, 0), SleepQuality.GOOD),
                // сон 23:30 01.02 – 09:30 02.02 - ночь не бессонная, "сова"
                new SleepingSession(LocalDateTime.of(2026, 2, 1, 23, 30),
                        LocalDateTime.of(2026, 2, 2, 9, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.analyze(sessions);
        assertEquals(Chronotype.OWL.rusName, result.getAnalysisResult());
    }
}
