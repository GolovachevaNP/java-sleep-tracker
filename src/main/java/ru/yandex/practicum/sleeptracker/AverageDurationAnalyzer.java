package ru.yandex.practicum.sleeptracker;

import java.util.List;

// средняя продолжительность сессии
public class AverageDurationAnalyzer implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        Double averageDurationSession = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult<>("Средняя продолжительность сессии (в минутах): ", averageDurationSession);
    }
}