package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepingSessionTest {

    // Проверить корректное сохранение полей
    @Test
    void shouldCreateSessionAndReturnFieldsCorrectly() {
        LocalDateTime start = LocalDateTime.of(2026, 2, 1, 22, 15);
        LocalDateTime end = LocalDateTime.of(2026, 2, 2, 8, 0);

        SleepingSession session =
                new SleepingSession(start, end, SleepQuality.GOOD);

        assertEquals(start, session.getStart());
        assertEquals(end, session.getEnd());
        assertEquals(SleepQuality.GOOD, session.getQuality());
    }

    // Проверить подсчёт количества минут сессии сна (два разных дня)
    @Test
    void shouldCalculateDurationMinutes() {
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2026, 2, 1, 22, 0),
                LocalDateTime.of(2026, 2, 2, 6, 0),
                SleepQuality.GOOD
        );

        assertEquals(480, session.getDurationMinutes());
    }

    // Проверить подсчёт количества минут сессии сна (один и тот же день)
    @Test
    void shouldCalculateDurationMinutesWithinSameDay() {
        SleepingSession session =
                new SleepingSession(
                        LocalDateTime.of(2026, 2, 1, 14, 30),
                        LocalDateTime.of(2026, 2, 1, 15, 0),
                        SleepQuality.BAD
                );

        assertEquals(30, session.getDurationMinutes());
    }
}
