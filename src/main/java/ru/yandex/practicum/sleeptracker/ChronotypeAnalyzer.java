package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

// определение хронотипа пользователя

/**
 * В данной реализации оценка хронотипа даётся каждой ночной !сессии сна! (бессонные ночи и дневные сессии сна игнорируются)
 * В ТЗ задание по определению хронотиа пользователя следует с формулировкой:
 * "2. Посчитайте количество !ночей! каждого типа и выберите, какой встречается чаще всего.",
 * но не сказано, как определять хронотип, если в ночь было 2 сессии сна, поэтому оценка хронотипа давалась каждой ночной
 * сессии сна отдельно, после чего проводилось сравнение количества каждого типа по заданным условиям.
 * Или же надо руководствоваться логикой: игнорируем время бодрствования (не зависимо от его длительности) между сессиями сна
 * в ночное время, для каждой ночи определяем время начала первой ночной сессии, время окончания второй ночной сессии,
 * и уже исходя из этих данных определяем хронотип "целой" ночи?
 **/

public class ChronotypeAnalyzer implements SleepAnalyzer {
    // для совы
    private static final LocalTime OWL_START_SLEEPS = LocalTime.of(23, 0);
    private static final LocalTime OWL_END_SLEEPS = LocalTime.of(9, 0);
    // для жаворонка
    private static final LocalTime LARK_START_SLEEPS = LocalTime.of(22, 0);
    private static final LocalTime LARK_END_SLEEPS = LocalTime.of(7, 0);

    private static final LocalTime HOUR_12 = LocalTime.of(12, 0);
    private static final LocalTime HOUR_6 = LocalTime.of(6, 0);
    private static final LocalTime HOUR_0 = LocalTime.of(0, 0);

    @Override
    public SleepAnalysisResult<?> analyze(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Хронотип пользователя: ", Chronotype.DOVE.rusName);
        }

        // берём только ночные записи (сон попадает в интервал 00:00–06:00)
        List<SleepingSession> nightSessions = sessions.stream()
                .filter(this::isSleepingSessionBetween0And6)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult<>("Хронотип пользователя: ", Chronotype.DOVE.rusName);
        }

        long owlCount = nightSessions.stream()
                .map(this::classifySession)
                .filter(type -> type == Chronotype.OWL)
                .count();

        long larkCount = nightSessions.stream()
                .map(this::classifySession)
                .filter(type -> type == Chronotype.LARK)
                .count();

        long doveCount = nightSessions.stream()
                .map(this::classifySession)
                .filter(type -> type == Chronotype.DOVE)
                .count();

        Chronotype result;
        if (owlCount > larkCount && owlCount > doveCount) {
            result = Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > doveCount) {
            result = Chronotype.LARK;
        } else {
            result = Chronotype.DOVE;
        }

        return new SleepAnalysisResult<>("Хронотип пользователя: ", result.rusName);
    }

    // проверка, что сессия сна ночная (сон попадает в интервал 00:00–06:00)
    private boolean isSleepingSessionBetween0And6(SleepingSession session) {
        LocalDateTime start = session.getStart();
        LocalDateTime end = session.getEnd();

        LocalDate nightDate = getNightDate(session);

        LocalDateTime intervalStart = nightDate.atTime(HOUR_0); // 00:00
        LocalDateTime intervalEnd = nightDate.atTime(HOUR_6);   // 06:00

        return start.isBefore(intervalEnd) && end.isAfter(intervalStart);
    }

    // определение даты ночи, к которой относится сессия
    private LocalDate getNightDate(SleepingSession session) {
        LocalDateTime sessionStart = session.getStart();

        LocalDate nightDate = sessionStart.toLocalDate();
        if (sessionStart.toLocalTime().isAfter(HOUR_12)) {
            nightDate = nightDate.plusDays(1);
        }

        return nightDate;
    }

    // классификация записи сна по хронотипу
    private Chronotype classifySession(SleepingSession session) {
        LocalTime startSleep = session.getStart().toLocalTime();
        LocalTime endSleep = session.getEnd().toLocalTime();

        boolean isOwl = startSleep.isAfter(OWL_START_SLEEPS) && endSleep.isAfter(OWL_END_SLEEPS);
        if (isOwl) {
            return Chronotype.OWL;
        }

        boolean isLark = startSleep.isBefore(LARK_START_SLEEPS) && endSleep.isBefore(LARK_END_SLEEPS);
        if (isLark) {
            return Chronotype.LARK;
        }

        return Chronotype.DOVE;
    }
}