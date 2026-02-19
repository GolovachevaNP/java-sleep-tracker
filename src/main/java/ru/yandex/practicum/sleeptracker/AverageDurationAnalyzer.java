package ru.yandex.practicum.sleeptracker;

import java.util.List;

// средняя продолжительность сессии
public class AverageDurationAnalyzer implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        double averageDurationSession = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0.0);

        averageDurationSession = Math.round(averageDurationSession * 100.0)/100.0; // округление значения до сотых
        return new SleepAnalysisResult<>("Средняя продолжительность сессии (в минутах): ", averageDurationSession);
    }
}