package ru.yandex.practicum.sleeptracker;

import java.util.List;

// количество сессий с плохим качеством сна
public class BadQualityCountAnalyzer implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        Long badCount = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна: ", badCount);
    }
}