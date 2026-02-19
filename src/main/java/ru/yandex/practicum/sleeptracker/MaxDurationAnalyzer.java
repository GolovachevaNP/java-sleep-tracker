package ru.yandex.practicum.sleeptracker;

import java.util.Comparator;
import java.util.List;

// максимальная продолжительность сессии
public class MaxDurationAnalyzer implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        Long maxDurationSession = sessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .max(Comparator.comparingLong(duration -> duration))
                .orElse(0L);
        return new SleepAnalysisResult<>("Максимальная продолжительность сессии (в минутах): ", maxDurationSession);
    }
}


