package ru.yandex.practicum.sleeptracker;

import java.util.Comparator;
import java.util.List;

// минимальная продолжительность сессии
public class MinDurationAnalyzer implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        Long minDurationSession = sessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .min(Comparator.comparingLong(duration -> duration))
                .orElse(0L);
        return new SleepAnalysisResult<>("Минимальная продолжительность сессии (в минутах): ", minDurationSession);
    }
}
