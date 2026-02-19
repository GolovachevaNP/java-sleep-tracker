package ru.yandex.practicum.sleeptracker;

// класс-обёртка для возвращаемого значения
public class SleepAnalysisResult<T> {

    private final String description; // описание сессии сна
    private final T analysisResult; // результат анализа сессии сна

    public SleepAnalysisResult(String description, T analysisResult) {
        this.description = description;
        this.analysisResult = analysisResult;
        validate();
    }

    private void validate() {
        if (description == null || analysisResult == null) {
            throw new IllegalArgumentException("Некорректные данные в описании и/или результате анализа сессии сна");
        }
    }

    public String getDescription() {
        return description;
    }

    public T getAnalysisResult() {
        return analysisResult;
    }
}