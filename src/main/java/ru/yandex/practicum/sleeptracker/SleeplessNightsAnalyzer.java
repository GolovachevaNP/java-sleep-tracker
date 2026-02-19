package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SleeplessNightsAnalyzer implements SleepAnalyzer {

    private static final LocalTime HOUR_12 = LocalTime.of(12, 0);
    private static final LocalTime HOUR_6 = LocalTime.of(6, 0);
    private static final LocalTime HOUR_0 = LocalTime.of(0, 0);

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей: ", 0L);
        }

        long sleeplessNights = totalNights(sessions) - notSleeplessNight(sessions);
        return new SleepAnalysisResult<>("Количество бессонных ночей: ", sleeplessNights);
    }

    // общее количество ночей за время логирования
    public long totalNights(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return 0L;
        }
        SleepingSession firstSession = sessions.stream()
                .min(Comparator.comparing(SleepingSession::getStart))
                .orElseThrow();

        SleepingSession lastSession = sessions.stream()
                .max(Comparator.comparing(SleepingSession::getEnd))
                .orElseThrow();

        LocalDateTime firstSessionStart = firstSession.getStart();
        LocalDateTime lastSessionEnd = lastSession.getEnd();

        LocalDate firstNightDate = firstSessionStart.toLocalDate();
        if (firstSessionStart.toLocalTime().isAfter(HOUR_12)) {
            firstNightDate = firstNightDate.plusDays(1);
        }

        LocalDate lastNightDate = lastSessionEnd.toLocalDate();

        return lastNightDate.plusDays(1).toEpochDay() - firstNightDate.toEpochDay();
    }

    // есть ли в сессии сон в промежутке 00:00–06:00
    private boolean isSleepingSessionBetween0And6(SleepingSession session) {
        LocalDateTime start = session.getStart();
        LocalDateTime end = session.getEnd();

        LocalDate nightDate = getNightDate(session);

        LocalDateTime intervalStart = nightDate.atTime(HOUR_0);   // 00:00
        LocalDateTime intervalEnd = nightDate.atTime(HOUR_6);     // 06:00

        return start.isBefore(intervalEnd) && end.isAfter(intervalStart);
    }

    // определение даты, к которой относится сессия сна
    private LocalDate getNightDate(SleepingSession session) {
        LocalDateTime sessionStart = session.getStart();

        LocalDate nightDate = sessionStart.toLocalDate();
        if (sessionStart.toLocalTime().isAfter(HOUR_12)) {
            nightDate = nightDate.plusDays(1);
        }

        return nightDate;
    }

    // количество НЕ бессонных ночей (сон в промежутке 00:00–06:00)
    public long notSleeplessNight(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return 0L;
        }

        // границы периода логирования (по тем же правилам, что и в totalNights)
        LocalDateTime firstStart = sessions.stream()
                .map(SleepingSession::getStart)
                .min(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDateTime lastEnd = sessions.stream()
                .map(SleepingSession::getEnd)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDate startNight;

        if (firstStart.toLocalTime().isAfter(HOUR_12)) {
            startNight = firstStart.toLocalDate().plusDays(1);
        } else {
            startNight = firstStart.toLocalDate();
        }

        LocalDate endNight = lastEnd.toLocalDate();

        Set<LocalDate> nightsWithSleep = sessions.stream()
                .filter(this::isSleepingSessionBetween0And6)
                .map(this::getNightDate)
                .filter(date -> !date.isBefore(startNight) && !date.isAfter(endNight))
                .collect(Collectors.toSet());

        return nightsWithSleep.size();
    }
}
