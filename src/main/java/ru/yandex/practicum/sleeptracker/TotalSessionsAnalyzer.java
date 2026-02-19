package ru.yandex.practicum.sleeptracker;

import java.util.List;

// общее количество сессий сна
public class TotalSessionsAnalyzer implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        long countSessions = sessions.size();
        return new SleepAnalysisResult<>("Всего сессий сна за представленный период: ", countSessions);
    }
}
