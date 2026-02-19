package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;


public class SleepTrackerApp {

    public static void main(String[] args) {
        System.out.println("Введите путь к файлу с логом сна: "); // src\main\resources\sleep_log.txt
        Scanner scanner = new Scanner(System.in);
        String pathFile = scanner.nextLine();

        List<SleepingSession> sessions = readSessions(pathFile);
        List<SleepAnalyzer> analyzers = List.of(
                new TotalSessionsAnalyzer(),   // общее количество сессий сна
                new MinDurationAnalyzer(),     // минимальная продолжительность сессии сна (в минутах)
                new MaxDurationAnalyzer(),     // максимальная продолжительность сессии сна (в минутах)
                new AverageDurationAnalyzer(), // средняя продолжительность сессии сна(в минутах)
                new BadQualityCountAnalyzer(), // количество сессий с плохим качество сна
                new SleeplessNightsAnalyzer(), // количество бессонных ночей
                new ChronotypeAnalyzer()       // хронотип пользователя
        );

        analyzers.stream()
                .map(analyzer -> analyzer.analyze(sessions))
                .forEach(result -> System.out.println(result.getDescription() + result.getAnalysisResult()));
    }

    private static List<SleepingSession> readSessions(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(line -> parseLine(line, formatter))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + fileName, e);
        }
    }

    private static SleepingSession parseLine(String line, DateTimeFormatter formatter) {
        String[] parts = line.split(";");

        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), formatter);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), formatter);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());

        return new SleepingSession(start, end, quality);
    }
}