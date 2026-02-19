package ru.yandex.practicum.sleeptracker;

import java.util.List;

@FunctionalInterface
public interface SleepAnalyzer {

    SleepAnalysisResult<?> analyze(List<SleepingSession> sessions);
}