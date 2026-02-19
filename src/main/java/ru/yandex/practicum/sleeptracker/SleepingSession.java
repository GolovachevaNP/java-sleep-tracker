package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;

public class SleepingSession {

    private final LocalDateTime start;  // начало сессии сна
    private final LocalDateTime end;    // окончание сессии сна
    private final SleepQuality quality; // оценка качества сна

    public SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        this.start = start;
        this.end = end;
        this.quality = quality;
        validate();
    }

    private void validate() {
        if (start == null || end == null || quality == null) {
            throw new IllegalArgumentException("Некорректные данные о сессии сна");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Некорректное время сессии сна");
        }
    }

    public long getDurationMinutes() {
        return Duration.between(start, end).toMinutes();
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }
}
